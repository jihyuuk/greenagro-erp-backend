package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.productgroup.CreateProductGroupRequest;
import erp.greenagro.greenagro_erp_backend.dto.productgroup.CreateProductGroupResponse;
import erp.greenagro.greenagro_erp_backend.dto.productgroup.ProductGroupDTO;
import erp.greenagro.greenagro_erp_backend.model.entity.ProductGroup;
import erp.greenagro.greenagro_erp_backend.repository.ProductGroupRepository;
import erp.greenagro.greenagro_erp_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductGroupRepository productGroupRepository;


    //품목 생성

    //품목 수정

    //품목 전체 조회

    //품목 단건 조회

    //품목 삭제 <- 논리 삭제



    //그룹 생성
    @Transactional
    public CreateProductGroupResponse createGroup(CreateProductGroupRequest request){
        //1. 검증 및 중복검사
        if(productGroupRepository.existsByNameAndDeletedFalse(request.getName()))
            throw new IllegalArgumentException("중복된 품목그룹명 입니다. 이름:"+request.getName());

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
        ProductGroup productGroup = productGroupRepository.findByIdAndDeletedFalse(id).orElseThrow(()->new IllegalArgumentException("존재하지 않는 품목그룹입니다. id:" + request.getId()));

        //2. 검증 및 중복검사
        if(!productGroup.getName().equals(request.getName()) && productGroupRepository.existsByNameAndDeletedFalse(request.getName()))
            throw new IllegalArgumentException("중복된 품목그룹명 입니다. 이름:"+request.getName());

        //3. 업데이트
        productGroup.update(request.getName());
    }


    //전체 그룹 조회
    @Transactional(readOnly = true)
    public List<ProductGroupDTO> getAllGroups(){
        //전체 조회
        List<ProductGroup> groups = productGroupRepository.findAllByDeletedFalse();

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
        ProductGroup productGroup = productGroupRepository.findByIdAndDeletedFalse(id).orElseThrow(()->new IllegalArgumentException("존재하지 않는 품목그룹입니다. id:" + id));

        //2. 상품 있으면 삭제 불가 <- 나중에 자세히 리스트업 하기
        if(productRepository.existsByProductGroupAndDeletedFalse(productGroup))
            throw new IllegalArgumentException("해당 품목 그룹에 해당하는 품목이 있어서 삭제가 불가능합니다. ");

        //3. 삭제
        productGroup.delete();
    }


}
