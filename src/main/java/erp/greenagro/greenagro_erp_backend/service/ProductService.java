package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.customer.CustomerDTO;
import erp.greenagro.greenagro_erp_backend.dto.exception.DuplicatedField;
import erp.greenagro.greenagro_erp_backend.dto.product.*;
import erp.greenagro.greenagro_erp_backend.dto.productgroup.CreateProductGroupRequest;
import erp.greenagro.greenagro_erp_backend.dto.productgroup.CreateProductGroupResponse;
import erp.greenagro.greenagro_erp_backend.dto.productgroup.ProductGroupDTO;
import erp.greenagro.greenagro_erp_backend.exception.DuplicateValueException;
import erp.greenagro.greenagro_erp_backend.model.entity.Customer;
import erp.greenagro.greenagro_erp_backend.model.entity.Product;
import erp.greenagro.greenagro_erp_backend.model.entity.ProductGroup;
import erp.greenagro.greenagro_erp_backend.repository.CustomerRepository;
import erp.greenagro.greenagro_erp_backend.repository.ProductGroupRepository;
import erp.greenagro.greenagro_erp_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductGroupRepository productGroupRepository;
    private final CustomerRepository customerRepository;


    //품목 생성
    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request){

        //1. 중복 체크
        List<DuplicatedField> conflicts = new ArrayList<>();

        //1-1. code 중복 체크
        if(productRepository.existsByCode(request.getCode()))
            conflicts.add(new DuplicatedField("code", request.getCode()));

        //1-2. name 중복 체크
        if(productRepository.existsByName(request.getName()))
            conflicts.add(new DuplicatedField("name", request.getName()));

        //1-3. 중복된 값이 하나라도 있으면 예외
        if(!conflicts.isEmpty())
            throw new DuplicateValueException(conflicts);


        //2. 품목 그룹 조회
        ProductGroup productGroup = null;
        if(request.getProductGroupId() != null)
            productGroup = productGroupRepository.findById(request.getProductGroupId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 품목그룹입니다. id:" + request.getProductGroupId()));

        //3. 회사 조회
        Customer customer = null;
        if(request.getCustomerId() != null)
            customer = customerRepository.findById(request.getCustomerId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객입니다. id:" + request.getCustomerId()));

        //4. 엔티티 생성
        Product product = new Product(
                request.getImgUrl(),
                request.getCode(),
                request.getName(),
                request.getSpec(),
                request.getBoxQuantity(),
                productGroup,
                customer,
                request.getTaxType(),
                request.getDistChannel(),
                request.getPurchasePrice(),
                request.getSalePrice(),
                request.getMemo()
        );

        //5. 저장
        productRepository.save(product);

        //6. id 반한
        return new CreateProductResponse(product.getId());
    }


    //품목 수정
    @Transactional
    public void updateProduct(Long id, UpdateProductRequest request){

        //1. 해당 품목 조회
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 품목입니다. id:" + id));

        //2. 중복 체크하기
        List<DuplicatedField> conflicts = new ArrayList<>();

        //2-1. code 중복 체크
        if(!product.getCode().equals(request.getCode()) && productRepository.existsByCode(request.getCode()))
            conflicts.add(new DuplicatedField("code", request.getCode()));

        //2-2. name 중복 체크
        if(!product.getName().equals(request.getName()) && productRepository.existsByName(request.getName()))
            conflicts.add(new DuplicatedField("name", request.getName()));

        //2-3. 중복된 값이 하나라도 있으면 예외
        if(!conflicts.isEmpty())
            throw new DuplicateValueException(conflicts);

        //3. 품목 그룹 조회
        ProductGroup productGroup = null;
        if(request.getProductGroupId() != null)
            productGroup = productGroupRepository.findById(request.getProductGroupId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 품목그룹입니다. id:" + request.getProductGroupId()));

        //4. 회사 조회
        Customer customer = null;
        if(request.getCustomerId() != null)
            customer = customerRepository.findById(request.getCustomerId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객입니다. id:" + request.getCustomerId()));


        //5. 수정하기
        product.update(
                request.getImgUrl(),
                request.getCode(),
                request.getName(),
                request.getSpec(),
                request.getBoxQuantity(),
                productGroup,
                customer,
                request.getTaxType(),
                request.getDistChannel(),
                request.getPurchasePrice(),
                request.getSalePrice(),
                request.getMemo()
        );
    }


    //품목 전체 조회
    @Transactional(readOnly = true)
    public List<ProductSummaryResponse> getAllProducts(){
        //1. 전체 조회 (deleted = false)
        List<Product> products = productRepository.findAll();

        //2. DTO 변환
        return products.stream().map(product -> {

            ProductGroup group = product.getProductGroup();
            Customer customer = product.getCustomer();

            return new ProductSummaryResponse(
                    product.getId(),
                    product.getImgUrl(),
                    product.getCode(),
                    product.getName(),
                    product.getSpec(),
                    product.getBoxQuantity(),
                    new ProductGroupDTO(group.getId(), group.getName()),
                    new CustomerDTO(customer.getId(), customer.getBizName()),
                    product.getTaxType().toString(),
                    product.getDistChannel().toString(),
                    product.getSalePrice()
            );
        }).toList();
    }


    //품목 상세 조회
    @Transactional(readOnly = true)
    public ProductDetailResponse getProduct(Long id){
        //1. 해당 품목 조회
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 품목입니다. id:" + id));

        //2. dto 변환
        return new ProductDetailResponse(
                product.getId(),
                product.getImgUrl(),
                product.getCode(),
                product.getName(),
                product.getSpec(),
                product.getBoxQuantity(),
                new ProductGroupDTO(product.getProductGroup().getId(), product.getProductGroup().getName()),
                new CustomerDTO(product.getCustomer().getId(), product.getCustomer().getBizName()),
                product.getTaxType(),
                product.getDistChannel(),
                product.getPurchasePrice(),
                product.getSalePrice(),
                product.getMemo()
        );
    }


    //품목 삭제 <- 논리 삭제
    @Transactional
    public void deleteProduct(Long id){
        //1. 해당 품목 조회
        Product product = productRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 품목을 찾을 수 없습니다. id:"+id));

        //2. 논리 삭제
        productRepository.delete(product);
    }



    //그룹 생성
    @Transactional
    public CreateProductGroupResponse createGroup(CreateProductGroupRequest request){
        //1. 중복 체크
        List<DuplicatedField> conflicts = new ArrayList<>();

        //1-1. name 중복 체크
        if(productGroupRepository.existsByName(request.getName()))
            conflicts.add(new DuplicatedField("name", request.getName()));

        //1-3. 중복된 값이 하나라도 있으면 예외
        if(!conflicts.isEmpty())
            throw new DuplicateValueException(conflicts);


        //2. 엔티티 생성
        ProductGroup productGroup = new ProductGroup(request.getName());

        //3. 저장
        productGroupRepository.save(productGroup);

        //4. id 반환
        return new CreateProductGroupResponse(productGroup.getId());
    }


    //그룹 수정
    @Transactional
    public void updateGroup(Long id, ProductGroupDTO request){
        //1. 해당 품목그룹 조회
        ProductGroup productGroup = productGroupRepository.findById(id).orElseThrow(()->new IllegalArgumentException("존재하지 않는 품목그룹입니다. id:" + request.getId()));

        //2. 중복 체크하기
        List<DuplicatedField> conflicts = new ArrayList<>();

        //2-1. code 중복 체크
        if(!productGroup.getName().equals(request.getName()) && productGroupRepository.existsByName(request.getName()))
            conflicts.add(new DuplicatedField("name", request.getName()));

        //2-3. 중복된 값이 하나라도 있으면 예외
        if(!conflicts.isEmpty())
            throw new DuplicateValueException(conflicts);


        //3. 업데이트
        productGroup.update(request.getName());
    }


    //전체 그룹 조회
    @Transactional(readOnly = true)
    public List<ProductGroupDTO> getAllGroups(){
        //전체 조회(deleted = false)
        List<ProductGroup> groups = productGroupRepository.findAll();

        //dto 변환
        return groups.stream().map(group ->
                new ProductGroupDTO(
                        group.getId(),
                        group.getName()
                )
        ).toList();
    }


    //그룹 삭제
    @Transactional
    public void deleteGroup(Long id){
        //1. 해당 품목그룹 조회
        ProductGroup productGroup = productGroupRepository.findById(id).orElseThrow(()->new IllegalArgumentException("존재하지 않는 품목그룹입니다. id:" + id));

        //2. 상품 있으면 삭제 불가 <- 나중에 자세히 리스트업 하기
        if(productRepository.existsByProductGroup(productGroup))
            //throw new ResourceInUseException(Map.of());
            throw new IllegalArgumentException("해당 품목 그룹에 해당하는 품목이 있어서 삭제가 불가능합니다. ");

        //3. 삭제
        productGroupRepository.delete(productGroup);
    }


}
