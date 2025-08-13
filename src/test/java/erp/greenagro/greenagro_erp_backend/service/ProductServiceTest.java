package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.productgroup.CreateProductGroupRequest;
import erp.greenagro.greenagro_erp_backend.dto.productgroup.CreateProductGroupResponse;
import erp.greenagro.greenagro_erp_backend.dto.productgroup.ProductGroupDTO;
import erp.greenagro.greenagro_erp_backend.model.entity.Product;
import erp.greenagro.greenagro_erp_backend.model.entity.ProductGroup;
import erp.greenagro.greenagro_erp_backend.repository.ProductGroupRepository;
import erp.greenagro.greenagro_erp_backend.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductService productService;
    @Autowired
    ProductGroupRepository productGroupRepository;

    /**
         그룹 생성 createGroup()
         1.이름 중복 불가
         2.deleted = true 인 항목과는 중복 허용

         그룹 수정 updateGroup()
         1.이름 중복 불가
         2.deleted = true 인 항목과는 중복 허용

         전체 그룹 조회 getAllGroups()
         1.전체 조회 가능
         2.deleted = true 인 항목은 제외

         그룹 삭제 deleteGroup()
         1.논리 삭제 (deleted = true 처리)
     **/

    @Test
    void 그룹_생성_정상(){
        //given
        CreateProductGroupRequest request = new CreateProductGroupRequest("농약");

        //when
        CreateProductGroupResponse response = productService.createGroup(request);

        //then
        ProductGroup group = productGroupRepository.findById(response.getProductId()).orElseThrow();
        assertNotNull(response.getProductId());
        assertEquals(request.getName(), group.getName());
        assertFalse(group.isDeleted());
    }


    @Test
    void 그룹_생성_이름_중복(){
        //given
        CreateProductGroupRequest request = new CreateProductGroupRequest("농약");
        CreateProductGroupRequest requestDuplicateName = new CreateProductGroupRequest("농약");

        //when
        CreateProductGroupResponse response = productService.createGroup(request);

        //then
        assertThrows(IllegalArgumentException.class, () -> productService.createGroup(requestDuplicateName));
    }


    @Test
    void 그룹_생성_삭제된_이름_중복허용(){
        //given
        CreateProductGroupRequest request = new CreateProductGroupRequest("농약");
        CreateProductGroupRequest requestDuplicateName = new CreateProductGroupRequest("농약");

        //when
        CreateProductGroupResponse response = productService.createGroup(request); //생성
        productService.deleteGroup(response.getProductId()); //삭제

        //then
        productService.createGroup(requestDuplicateName); // 중복 가능
    }


    @Test
    void 그룹_조회_전체(){
        //given
        Map<Long, ProductGroup> map = new HashMap<>();

        ProductGroup group1 = productGroupRepository.save(new ProductGroup("농약"));
        ProductGroup group2 = productGroupRepository.save(new ProductGroup("씨앗"));
        ProductGroup deletedGroup = productGroupRepository.save(new ProductGroup("자재"));

        map.put(group1.getId(), group1);
        map.put(group2.getId(), group2);

        productService.deleteGroup(deletedGroup.getId());

        //when
        List<ProductGroupDTO> responses = productService.getAllGroups();


        //then
        assertEquals(2, responses.size());

        responses.stream().forEach(res -> {
            ProductGroup group = map.get(res.getId());
            assertEquals(group.getId(), res.getId());
            assertEquals(group.getName(), res.getName());
        });
    }


    @Test
    void 그룹_삭제(){
        //given
        ProductGroup group = productGroupRepository.save(new ProductGroup("농약"));

        //when
        productService.deleteGroup(group.getId());

        //then
        assertTrue(group.isDeleted());
    }

    @Test
    void 그룹_삭제_품목존재시_불가(){
        //given
        ProductGroup group = productGroupRepository.save(new ProductGroup("농약"));
        Product product = productRepository.save(new Product("http://img/1", "123", "테스트 품목", "10L", 10L, group, null, null, null, null, null, null));

        //when - then - 품목이 있어서 그룹 삭제 불가
        assertThrows(IllegalArgumentException.class, () -> productService.deleteGroup(group.getId()));

        //when - 품목 지우기
        product.delete();
        //then - 그룹에 속한 품목이 없어서 삭제 가능!
        productService.deleteGroup(group.getId());
    }

}