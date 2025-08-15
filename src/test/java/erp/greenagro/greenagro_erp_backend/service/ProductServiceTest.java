package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.product.*;
import erp.greenagro.greenagro_erp_backend.dto.productgroup.CreateProductGroupRequest;
import erp.greenagro.greenagro_erp_backend.dto.productgroup.CreateProductGroupResponse;
import erp.greenagro.greenagro_erp_backend.dto.productgroup.ProductGroupDTO;
import erp.greenagro.greenagro_erp_backend.model.entity.Customer;
import erp.greenagro.greenagro_erp_backend.model.entity.Product;
import erp.greenagro.greenagro_erp_backend.model.entity.ProductGroup;
import erp.greenagro.greenagro_erp_backend.model.enums.CustomerType;
import erp.greenagro.greenagro_erp_backend.model.enums.DistChannel;
import erp.greenagro.greenagro_erp_backend.model.enums.SalesGroup;
import erp.greenagro.greenagro_erp_backend.model.enums.TaxType;
import erp.greenagro.greenagro_erp_backend.repository.CustomerRepository;
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
    ProductGroupRepository productGroupRepository;
    @Autowired
    ProductService productService;
    @Autowired
    CustomerRepository customerRepository;



    /**
        품목 생성 createProduct()
        1. 이름, 코드 중복 불가
        2. deleted = true 인 항목과는 중복 허용
        3. customer, productGroup 요청 없을때도 가능
        4. customer, productGroup 요청은 있는데 조회시 없는경우 -> 오류

        품목 수정 updateProduct()
         1. 이름, 코드 중복 불가 (자기 스스로는 가능)
         2. deleted = true 인 항목과는 중복 허용
         3. customer, productGroup 요청 없을때도 가능
         4. customer, productGroup 요청은 있는데 조회시 없는경우 -> 오류

        품목 전체 조회 getAllProducts()
        1.전체 조회 가능

        품목 상세 조회 getProduct()
        1. 품목 상세 조회
        2. 품목 존재 x -> 오류

        품목 삭제 deleteProduct()
        1. 논리 삭제 (deleted = true 처리)
     **/
    @Test
    void 품목_생성_정상(){
        //given
        Customer customer = customerRepository.save(getCustomer());
        ProductGroup group = productGroupRepository.save(new ProductGroup("테스트 그룹"));
        CreateProductRequest request = getCreateProductRequest("001","테스트품목", group, customer);


        //when
        CreateProductResponse response = productService.createProduct(request);


        //then
        Product product = productRepository.findById(response.getProductId()).orElseThrow();
        assertEquals(product.getId(), response.getProductId());
        assertEquals(request.getImgUrl(), product.getImgUrl());
        assertEquals(request.getCode(), product.getCode());
        assertEquals(request.getName(), product.getName());
        assertEquals(request.getSpec(), product.getSpec());
        assertEquals(request.getBoxQuantity(), product.getBoxQuantity());
        assertEquals(group, product.getProductGroup());//jpa 영속성
        assertEquals(request.getProductGroupId(), product.getProductGroup().getId());
        assertEquals(customer, product.getCustomer()); //jpa 영속성
        assertEquals(request.getCustomerId(), product.getCustomer().getId());
        assertEquals(request.getTaxType(), product.getTaxType());
        assertEquals(request.getDistChannel(), product.getDistChannel());
        assertEquals(request.getPurchasePrice(), product.getPurchasePrice());
        assertEquals(request.getSalePrice(), product.getSalePrice());
        assertEquals(request.getMemo(), product.getMemo());
    }


    @Test
    void 품목_생성_이름_코드_중복(){
        //given
        Customer customer = customerRepository.save(getCustomer());
        ProductGroup group = productGroupRepository.save(new ProductGroup("테스트 그룹"));

        CreateProductRequest request = getCreateProductRequest("P001","상품A", group, customer);
        CreateProductRequest requestDuplicateCode = getCreateProductRequest("P001","상품B", group, customer);
        CreateProductRequest requestDuplicateName = getCreateProductRequest("P002","상품A", group, customer);
        CreateProductRequest requestDuplicateAll = getCreateProductRequest("P001","상품A", group, customer);


        //when
        productService.createProduct(request);

        //then
        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(requestDuplicateCode));
        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(requestDuplicateName));
        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(requestDuplicateAll));
    }


    @Test
    void 품목_생성_논리삭제된_이름_코드_중복허용(){
        //given
        Customer customer = customerRepository.save(getCustomer());
        ProductGroup group = productGroupRepository.save(new ProductGroup("테스트 그룹"));

        CreateProductRequest request = getCreateProductRequest("P001","상품A", group, customer);
        CreateProductRequest requestDuplicateAll = getCreateProductRequest("P001","상품A", group, customer);


        //when
        CreateProductResponse product = productService.createProduct(request); //저장
        productService.deleteProduct(product.getProductId()); //논리삭제

        //then
        productService.createProduct(requestDuplicateAll);
    }


    @Test
    void 품목_생성_회사_그룹_null_가능(){
        //given
        Customer customer = customerRepository.save(getCustomer());
        ProductGroup group = productGroupRepository.save(new ProductGroup("테스트 그룹"));
        CreateProductRequest request = getCreateProductRequest("P001","상품A", group, customer);

        //when-then 정상 저장 (Customer, ProductGroup 필수 아님)
        productService.createProduct(request);
    }


    @Test
    void 품목_수정_정상(){
        //given
        Customer customer = customerRepository.save(getCustomer());
        ProductGroup group = productGroupRepository.save(new ProductGroup("테스트 그룹"));
        Product beforeProduct = getProductAndSave("P001", "품목A", group, customer); //나중에 null일때도 테스트

        Customer customer2 = customerRepository.save(getCustomer2());
        ProductGroup group2 = productGroupRepository.save(new ProductGroup("테스트 그룹2"));
        UpdateProductRequest request = getUpdateProductRequest("P002", "품목B", group2, customer2);


        //when
        productService.updateProduct(beforeProduct.getId(), request);


        //then
        Product afterProduct = productRepository.findById(beforeProduct.getId()).orElseThrow();
        assertEquals(request.getImgUrl(), afterProduct.getImgUrl());
        assertEquals(request.getCode(), afterProduct.getCode());
        assertEquals(request.getName(), afterProduct.getName());
        assertEquals(request.getSpec(), afterProduct.getSpec());
        assertEquals(request.getBoxQuantity(), afterProduct.getBoxQuantity());
        assertEquals(request.getProductGroupId(), afterProduct.getProductGroup() != null ? afterProduct.getProductGroup().getId() : null);
        assertEquals(request.getCustomerId(), afterProduct.getCustomer() != null ? afterProduct.getCustomer().getId() : null);
        assertEquals(request.getTaxType(), afterProduct.getTaxType());
        assertEquals(request.getDistChannel(), afterProduct.getDistChannel());
        assertEquals(request.getPurchasePrice(), afterProduct.getPurchasePrice());
        assertEquals(request.getSalePrice(), afterProduct.getSalePrice());
        assertEquals(request.getMemo(), afterProduct.getMemo());
    }


    @Test
    void 품목_수정_코드_이름_중복(){
        //given
        Customer customer = customerRepository.save(getCustomer());
        ProductGroup group = productGroupRepository.save(new ProductGroup("테스트 그룹"));

        Product existingProduct = getProductAndSave("P001", "품목A", group, customer); //나중에 null일때도 테스트

        Product targetProduct = getProductAndSave("P002", "품목B", group, customer); //나중에 null일때도 테스트

        UpdateProductRequest requestDuplicateCode = getUpdateProductRequest("P001", "품목B", group, customer);
        UpdateProductRequest requestDuplicateName = getUpdateProductRequest("P002", "품목A", group, customer);
        UpdateProductRequest requestDuplicateAll = getUpdateProductRequest("P001", "품목A", group, customer);


        //when-then
        assertThrows(IllegalArgumentException.class, () ->  productService.updateProduct(targetProduct.getId(), requestDuplicateCode));
        assertThrows(IllegalArgumentException.class, () ->  productService.updateProduct(targetProduct.getId(), requestDuplicateName));
        assertThrows(IllegalArgumentException.class, () ->  productService.updateProduct(targetProduct.getId(), requestDuplicateAll));
    }


    @Test
    void 품목_수정_회사_그룹_null_가능(){
        //given
        Customer customer = customerRepository.save(getCustomer());
        ProductGroup group = productGroupRepository.save(new ProductGroup("테스트 그룹"));

        Product beforeProduct = getProductAndSave("P001", "품목A", group, customer); //나중에 null일때도 테스트

        UpdateProductRequest request = getUpdateProductRequest("P001", "품목A", null, null);


        //when
        productService.updateProduct(beforeProduct.getId(), request);


        //then
        Product afterProduct = productRepository.findById(beforeProduct.getId()).orElseThrow();
        assertNull(afterProduct.getProductGroup());
        assertNull(afterProduct.getCustomer());
    }


    @Test
    void 품목_전체_조회(){
        // given
        Customer customer = customerRepository.save(getCustomer());
        ProductGroup group = productGroupRepository.save(new ProductGroup("테스트 그룹"));
        List<Product> savedProducts = List.of(
                productRepository.save(getProductAndSave("001","품목1", group, customer)),
                productRepository.save(getProductAndSave("002","품목2", group, customer)),
                productRepository.save(getProductAndSave("003","품목3", group, customer))
        );

        // when
        List<ProductSummaryResponse> products = productService.getAllProducts();

        // then
        assertEquals(savedProducts.size(), products.size());

        for (int i = 0; i < savedProducts.size(); i++) {
            Product expected = savedProducts.get(i);
            ProductSummaryResponse actual = products.get(i);

            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getImgUrl(), actual.getImgUrl());
            assertEquals(expected.getCode(), actual.getCode());
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getSpec(), actual.getSpec());
            assertEquals(expected.getBoxQuantity(), actual.getBoxQuantity());
            assertEquals(expected.getProductGroup().getId(), actual.getProductGroup().getId());
            assertEquals(expected.getProductGroup().getName(), actual.getProductGroup().getName());
            assertEquals(expected.getCustomer().getId(), actual.getCustomer().getId());
            assertEquals(expected.getCustomer().getBizName(), actual.getCustomer().getName());
            assertEquals(expected.getTaxType().toString(), actual.getTaxType());
            assertEquals(expected.getDistChannel().toString(), actual.getDistChannel());
            assertEquals(expected.getSalePrice(), actual.getSalePrice());
        }
    }


    @Test
    void 품목_상세_조회(){
        //given
        Customer customer = customerRepository.save(getCustomer());
        ProductGroup group = productGroupRepository.save(new ProductGroup("테스트 그룹"));
        Product product = getProductAndSave("001", "name", group, customer); //나중에 null일때도 테스트

        //when
        ProductDetailResponse response = productService.getProduct(product.getId());

        //then
        assertEquals(product.getId(), response.getId());
        assertEquals(product.getImgUrl(), response.getImgUrl());
        assertEquals(product.getCode(), response.getCode());
        assertEquals(product.getName(), response.getName());
        assertEquals(product.getSpec(), response.getSpec());
        assertEquals(product.getBoxQuantity(), response.getBoxQuantity());
        assertEquals(product.getProductGroup().getId(), response.getProductGroup().getId());
        assertEquals(product.getProductGroup().getName(), response.getProductGroup().getName());
        assertEquals(product.getCustomer().getId(), response.getCustomer().getId());
        assertEquals(product.getCustomer().getBizName(), response.getCustomer().getName());
        assertEquals(product.getTaxType(), response.getTaxType());
        assertEquals(product.getDistChannel(), response.getDistChannel());
        assertEquals(product.getPurchasePrice(), response.getPurchasePrice());
        assertEquals(product.getSalePrice(), response.getSalePrice());
        assertEquals(product.getMemo(), response.getMemo());
    }


    @Test
    void 품목_논리_삭제(){
        //given
        Product product = getProductAndSave("001", "품목1", null, null);

        //when
        productService.deleteProduct(product.getId());

        //then
        assertNull(productRepository.findById(product.getId()).orElse(null)); //findById 논리 삭제 테스트
        assertFalse(productRepository.existsById(product.getId())); //existsById 논리 삭제 테스트
    }



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
    void 그룹_생성_논리삭제된_이름_중복허용(){
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
        assertNull(productGroupRepository.findById(group.getId()).orElse(null)); //findById 논리 삭제 테스트
        assertFalse(productGroupRepository.existsById(group.getId())); //existsById 논리 삭제 테스트
    }


    @Test
    void 그룹_삭제_품목존재시_불가(){
        //given
        ProductGroup group = productGroupRepository.save(new ProductGroup("농약"));
        Product product = productRepository.save(new Product("http://img/1", "123", "테스트 품목", "10L", 10L, group, null, null, null, null, null, null));

        //when - then - 품목이 있어서 그룹 삭제 불가
        assertThrows(IllegalArgumentException.class, () -> productService.deleteGroup(group.getId()));

        //when - 품목 지우기
        productRepository.delete(product);
        //then - 그룹에 속한 품목이 없어서 삭제 가능!
        productService.deleteGroup(group.getId());
    }


    /**
     * 기타 유틸 메소드
     */

    //Customer 생성
    private Customer getCustomer() {
        return new Customer(
                CustomerType.CORPORATION, // 사업자 유형
                SalesGroup.NONGHYUP,     // 영업 분류
                "123456-0000000",         // corpNo
                "123-45-67890",           // bizNo
                null,                     // rrn (개인만)
                "그린아그로",               // bizName
                "홍길동",                   // ceoName
                "도소매",                   // bizType
                "농자재",                   // bizItem
                "02-123-4567",             // tel
                "010-1234-5678",           // phone
                "서울시 강남구 테헤란로 1",  // addressMain
                "서울시 강남구 봉은사로 2",  // addressSub
                "02-987-6543",             // fax
                "test@example.com",        // email
                "김우리",                   // ourManager
                "이거래",                   // customerManager
                "테스트 메모"
        );
    }

    //Customer 생성
    private Customer getCustomer2() {
        return new Customer(
                CustomerType.SOLE_PROPRIETOR, // 사업자 유형
                SalesGroup.LANDSCAPE,     // 영업 분류
                "123456-0000000-2",         // corpNo
                "123-45-67890-2",           // bizNo
                null,                     // rrn (개인만)
                "그린아그로-2",               // bizName
                "홍길동-2",                   // ceoName
                "도소매-2",                   // bizType
                "농자재-2",                   // bizItem
                "02-123-4567-2",             // tel
                "010-1234-5678-2",           // phone
                "서울시 강남구 테헤란로 1-2",  // addressMain
                "서울시 강남구 봉은사로 2-2",  // addressSub
                "02-987-6543-2",             // fax
                "test@example.com-2",        // email
                "김우리-2",                   // ourManager
                "이거래-2",                   // customerManager
                "테스트 메모-2"
        );
    }


    private CreateProductRequest getCreateProductRequest(String code, String name,ProductGroup group, Customer customer) {
        return new CreateProductRequest(
                "https://example.com/image.jpg", // imgUrl
                code,                          // code
                name,                           // name
                "spec",                         // spec
                10L,                             // boxQuantity
                group != null ? group.getId() : null,                   // productGroupId
                customer != null ? customer.getId() : null,                // customerId
                TaxType.TAXED,                   // taxType
                DistChannel.DIRECT,              // distChannel
                5000L,                           // purchasePrice
                8000L,                           // salePrice
                "테스트 메모"                      // memo
        );
    }


    private UpdateProductRequest getUpdateProductRequest(String code, String name, ProductGroup group, Customer customer) {
        return new UpdateProductRequest(
                "https://example.com/image.jpg-2", // imgUrl
                code,                          // code
                name,                     // name
                "spec-2",                         // spec
                20L,                             // boxQuantity
                group != null ? group.getId() : null,                   // productGroupId
                customer != null ? customer.getId() : null,                // customerId
                TaxType.NON_TAX,                   // taxType
                DistChannel.NON_COOP,              // distChannel
                9000L,                           // purchasePrice
                10000L,                           // salePrice
                "테스트 메모-2"                      // memo
        );
    }


    private Product getProductAndSave(String code, String name, ProductGroup group, Customer customer) {

        Product product = new Product(
                "https://example.com/image.jpg",
                code,
                name,
                "10L",
                10L,
                group,
                customer,
                TaxType.TAXED,
                DistChannel.DIRECT,
                5000L,
                8000L,
                "테스트 메모"
        );

        return productRepository.save(product);
    }

}