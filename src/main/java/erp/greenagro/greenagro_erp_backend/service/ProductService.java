package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.partner.PartnerDTO;
import erp.greenagro.greenagro_erp_backend.dto.exception.LookupDTO;
import erp.greenagro.greenagro_erp_backend.dto.product.*;
import erp.greenagro.greenagro_erp_backend.dto.productgroup.CreateProductGroupRequest;
import erp.greenagro.greenagro_erp_backend.dto.productgroup.CreateProductGroupResponse;
import erp.greenagro.greenagro_erp_backend.dto.productgroup.ProductGroupDTO;
import erp.greenagro.greenagro_erp_backend.exception.CustomException;
import erp.greenagro.greenagro_erp_backend.model.entity.Partner;
import erp.greenagro.greenagro_erp_backend.model.entity.Product;
import erp.greenagro.greenagro_erp_backend.model.entity.ProductGroup;
import erp.greenagro.greenagro_erp_backend.repository.PartnerRepository;
import erp.greenagro.greenagro_erp_backend.repository.ProductGroupRepository;
import erp.greenagro.greenagro_erp_backend.repository.ProductRepository;
import erp.greenagro.greenagro_erp_backend.validator.DuplicationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static erp.greenagro.greenagro_erp_backend.model.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductGroupRepository productGroupRepository;
    private final PartnerRepository partnerRepository;


    //품목 생성
    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request){

        //1. 중복 체크 - code, name
        DuplicationValidator.validate(dv -> dv
                .check(productRepository.existsByCode(request.getCode()), "code", request.getCode())
                .check(productRepository.existsByName(request.getName()), "name", request.getName())
        );


        //2. 품목 그룹 조회
        ProductGroup productGroup = null;
        if(request.getProductGroupId() != null)
            productGroup = productGroupRepository.findById(request.getProductGroupId()).orElseThrow(() -> new CustomException(PRODUCT_GROUP_NOT_FOUND, request.getProductGroupId()));

        //3. 회사 조회
        Partner partner = null;
        if(request.getPartnerId() != null)
            partner = partnerRepository.findById(request.getPartnerId()).orElseThrow(() -> new CustomException(EMPLOYEE_NOT_FOUND, request.getPartnerId()));

        //4. 엔티티 생성
        Product product = new Product(
                request.getImgUrl(),
                request.getCode(),
                request.getName(),
                request.getSpec(),
                request.getBoxQuantity(),
                productGroup,
                partner,
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
        Product product = productRepository.findById(id).orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND, id));

        //2. 중복 체크 - 품목코드, 품목명
        DuplicationValidator.validate(dv -> dv
                .check(!product.getCode().equals(request.getCode()) && productRepository.existsByCode(request.getCode()), "code", request.getCode())
                .check(!product.getName().equals(request.getName()) && productRepository.existsByName(request.getName()), "name", request.getName())
        );

        //3. 품목 그룹 조회
        ProductGroup productGroup = null;
        if(request.getProductGroupId() != null)
            productGroup = productGroupRepository.findById(request.getProductGroupId()).orElseThrow(() -> new CustomException(PRODUCT_GROUP_NOT_FOUND, request.getProductGroupId()));

        //4. 회사 조회
        Partner partner = null;
        if(request.getPartnerId() != null)
            partner = partnerRepository.findById(request.getPartnerId()).orElseThrow(() -> new CustomException(PARTNER_NOT_FOUND, request.getPartnerId()));


        //5. 수정하기
        product.update(
                request.getImgUrl(),
                request.getCode(),
                request.getName(),
                request.getSpec(),
                request.getBoxQuantity(),
                productGroup,
                partner,
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
            Partner partner = product.getPartner();

            return new ProductSummaryResponse(
                    product.getId(),
                    product.getImgUrl(),
                    product.getCode(),
                    product.getName(),
                    product.getSpec(),
                    product.getBoxQuantity(),
                    new ProductGroupDTO(group.getId(), group.getName()),
                    new PartnerDTO(partner.getId(), partner.getPartnerName()),
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
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND, id));

        //2. dto 변환
        return new ProductDetailResponse(
                product.getId(),
                product.getImgUrl(),
                product.getCode(),
                product.getName(),
                product.getSpec(),
                product.getBoxQuantity(),
                new ProductGroupDTO(product.getProductGroup().getId(), product.getProductGroup().getName()),
                new PartnerDTO(product.getPartner().getId(), product.getPartner().getPartnerName()),
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
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new CustomException(PRODUCT_NOT_FOUND, id));

        //2. 논리 삭제
        productRepository.delete(product);
    }



    //그룹 생성
    @Transactional
    public CreateProductGroupResponse createGroup(CreateProductGroupRequest request){

        //1. 중복 체크 - 그룹명
        DuplicationValidator.validate(dv -> dv
                .check(productGroupRepository.existsByName(request.getName()), "name", request.getName())
        );

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
        ProductGroup productGroup = productGroupRepository.findById(id).orElseThrow(() -> new CustomException(PRODUCT_GROUP_NOT_FOUND, id));

        //2. 중복 체크하기 - 그룹명
        DuplicationValidator.validate(dv -> dv
                .check(!productGroup.getName().equals(request.getName()) && productGroupRepository.existsByName(request.getName()), "name", request.getName())
        );

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
        ProductGroup productGroup = productGroupRepository.findById(id).orElseThrow(() -> new CustomException(PRODUCT_GROUP_NOT_FOUND, id));

        //2. 그룹에 속한 상품 있으면 삭제 불가
        if(productRepository.existsByProductGroup(productGroup)){

            List<LookupDTO> errors = productRepository.findAllByProductGroup(productGroup).stream().map(p ->
                    new LookupDTO(p.getId(), p.getCode(), p.getName())
            ).toList();

            throw new CustomException(RESOURCE_IN_USE, errors);
        }

        //3. 삭제
        productGroupRepository.delete(productGroup);
    }


}
