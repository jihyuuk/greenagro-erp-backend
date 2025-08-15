package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.customer.*;
import erp.greenagro.greenagro_erp_backend.model.entity.Customer;
import erp.greenagro.greenagro_erp_backend.model.enums.*;
import erp.greenagro.greenagro_erp_backend.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class CustomerServiceTest {


    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    EntityManager em;

    private Long dummyId;


    @Test
    void 고객_생성_정상() {
        //given
        CreateCustomerRequest request = generateCreateCustomerRequest();

        //when
        CreateCustomerResponse response = customerService.createCustomer(request); //고객 생성 서비스 수행

        //저장된 고객 조회
        Customer customer = customerRepository.findById(dummyId).orElseThrow(() -> new IllegalArgumentException("더미 고객 찾기 실패 Id:" + dummyId));

        //then
        assertNotNull(response.getCustomerId()); //생성된 고객 아이디 반환 확인

        assertEquals(request.getCustomerType(), customer.getCustomerType());
        assertEquals(request.getSalesGroup(), customer.getSalesGroup());
        assertEquals(request.getCorpNo(), customer.getCorpNo());
        assertEquals(request.getBizNo(), customer.getBizNo());
        assertEquals(request.getRrn(), customer.getRrn());
        assertEquals(request.getBizName(), customer.getBizName());
        assertEquals(request.getCeoName(), customer.getCeoName());
        assertEquals(request.getBizType(), customer.getBizType());
        assertEquals(request.getBizItem(), customer.getBizItem());
        assertEquals(request.getTel(), customer.getTel());
        assertEquals(request.getPhone(), customer.getPhone());
        assertEquals(request.getAddressMain(), customer.getAddressMain());
        assertEquals(request.getAddressSub(), customer.getAddressSub());
        assertEquals(request.getFax(), customer.getFax());
        assertEquals(request.getEmail(), customer.getEmail());
        assertEquals(request.getOurManager(), customer.getOurManager());
        assertEquals(request.getCustomerManager(), customer.getCustomerManager());
        assertEquals(request.getMemo(), customer.getMemo());
    }


    //상세조회
    @Test
    void 고객_상세_조회() {
        //given
        Customer customer = customerRepository.findById(dummyId).orElseThrow(() -> new IllegalArgumentException("더미 고객 찾기 실패 Id:" + dummyId));

        //when
        CustomerDetailResponse response = customerService.getCustomerDetail(customer.getId());

        //then
        assertEquals(customer.getId(), response.getId());
        assertEquals(customer.getCustomerType(), response.getCustomerType());
        assertEquals(customer.getSalesGroup(), response.getSalesGroup());
        assertEquals(customer.getCorpNo(), response.getCorpNo());
        assertEquals(customer.getBizNo(), response.getBizNo());
        assertEquals(customer.getRrn(), response.getRrn());
        assertEquals(customer.getBizName(), response.getBizName());
        assertEquals(customer.getCeoName(), response.getCeoName());
        assertEquals(customer.getBizType(), response.getBizType());
        assertEquals(customer.getBizItem(), response.getBizItem());
        assertEquals(customer.getTel(), response.getTel());
        assertEquals(customer.getPhone(), response.getPhone());
        assertEquals(customer.getAddressMain(), response.getAddressMain());
        assertEquals(customer.getAddressSub(), response.getAddressSub());
        assertEquals(customer.getFax(), response.getFax());
        assertEquals(customer.getEmail(), response.getEmail());
        assertEquals(customer.getOurManager(), response.getOurManager());
        assertEquals(customer.getCustomerManager(), response.getCustomerManager());
        assertEquals(customer.getMemo(), response.getMemo());
    }


    //전체조회
    @Test
    void 고객_전체_조회() {
        //given
        Customer customer = customerRepository.findById(dummyId).orElseThrow(() -> new IllegalArgumentException("더미 고객 찾기 실패 Id:" + dummyId));


        //when
        List<CustomerSummaryResponse> allCustomers = customerService.getAllCustomers();

        //id = dummyid인 고객 찾기
        CustomerSummaryResponse summaryDto = allCustomers.stream().filter(dto -> dto.getId() == customer.getId()).findFirst().orElseThrow();

        //then
        assertEquals(customerRepository.findAll().size(), allCustomers.size());
        assertEquals(customer.getId(), summaryDto.getId());
        assertEquals(customer.getCustomerType(), summaryDto.getCustomerType());
        assertEquals(customer.getSalesGroup(), summaryDto.getSalesGroup());
        assertEquals(customer.getBizNo(), summaryDto.getBizNo());
        assertEquals(customer.getRrn(), summaryDto.getRrn());
        assertEquals(customer.getBizName(), summaryDto.getBizName());
        assertEquals(customer.getCeoName(), summaryDto.getCeoName());
        assertEquals(customer.getTel(), summaryDto.getTel());
        assertEquals(customer.getFax(), summaryDto.getFax());
        assertEquals(customer.getEmail(), summaryDto.getEmail());
        assertEquals(customer.getOurManager(), summaryDto.getOurManager());
        assertEquals(customer.getCustomerManager(), summaryDto.getCustomerManager());
        assertEquals(customer.getMemo(), summaryDto.getMemo());
    }


    @Test
    void 고객_수정하기() {
        //given
        Customer customer = customerRepository.findById(dummyId).orElseThrow(() -> new IllegalArgumentException("더미 고객 찾기 실패 Id:" + dummyId));
        UpdateCustomerRequest request = generateUpdateCustomerRequest();

        //when
        customerService.updateCustomer(customer.getId(), request);

        //then
        Customer afterCustomer = customerRepository.findById(dummyId).orElseThrow(() -> new IllegalArgumentException("더미 고객 찾기 실패 Id:" + dummyId));


        assertEquals(customer.getId(), afterCustomer.getId());
        assertEquals(request.getCustomerType(), afterCustomer.getCustomerType(), "customerType 불일치");
        assertEquals(request.getSalesGroup(), afterCustomer.getSalesGroup(), "salesGroup 불일치");
        assertEquals(request.getCorpNo(), afterCustomer.getCorpNo(), "corpNo 불일치");
        assertEquals(request.getBizNo(), afterCustomer.getBizNo(), "bizNo 불일치");
        assertEquals(request.getRrn(), afterCustomer.getRrn(), "rrn 불일치");
        assertEquals(request.getBizName(), afterCustomer.getBizName(), "bizName 불일치");
        assertEquals(request.getCeoName(), afterCustomer.getCeoName(), "ceoName 불일치");
        assertEquals(request.getBizType(), afterCustomer.getBizType(), "bizType 불일치");
        assertEquals(request.getBizItem(), afterCustomer.getBizItem(), "bizItem 불일치");
        assertEquals(request.getTel(), afterCustomer.getTel(), "tel 불일치");
        assertEquals(request.getPhone(), afterCustomer.getPhone(), "phone 불일치");
        assertEquals(request.getAddressMain(), afterCustomer.getAddressMain(), "addressMain 불일치");
        assertEquals(request.getAddressSub(), afterCustomer.getAddressSub(), "addressSub 불일치");
        assertEquals(request.getFax(), afterCustomer.getFax(), "fax 불일치");
        assertEquals(request.getEmail(), afterCustomer.getEmail(), "email 불일치");
        assertEquals(request.getOurManager(), afterCustomer.getOurManager(), "ourManager 불일치");
        assertEquals(request.getCustomerManager(), afterCustomer.getCustomerManager(), "customerManager 불일치");
        assertEquals(request.getMemo(), afterCustomer.getMemo(), "memo 불일치");
    }


    @Test
    void 고객_수정_데이터_조회() {
        //given
        Customer customer = customerRepository.findById(dummyId).orElseThrow(() -> new IllegalArgumentException("더미 고객 찾기 실패 Id:" + dummyId));

        //when
        CustomerEditResponse response = customerService.getCustomerEditData(dummyId);

        //then
        assertTrue(Arrays.stream(CustomerType.values()).allMatch(response.getCustomerTypeOptions()::contains));//모든 사업자 분류 enum이 옵션에 포함되었는지
        assertTrue(Arrays.stream(SalesGroup.values()).allMatch(response.getSalesGroupOptions()::contains));//모든 영업 분류 enum이 옵션에 포함되었는지
        assertEquals(customer.getId(), response.getCustomer().getId());//CustomerDetailResponse 확인 (서비스 내부에서 getCustomerDetail()호출함 따라서 자세한 검증 패스)
    }


    @Test
    void 고객_논리_삭제() {
        //given
        Customer customer = customerRepository.findById(dummyId).orElseThrow(() -> new IllegalArgumentException("더미 고객 찾기 실패 Id:" + dummyId));

        //when
        customerService.deleteCustomer(customer.getId()); //삭제 수행

        //then
        assertNull(customerRepository.findById(customer.getId()).orElse(null)); //findById 논리 삭제 테스트
        assertFalse(customerRepository.existsById(customer.getId())); //existsById 논리 삭제 테스트
    }




    @BeforeEach
    void initData() {
        //더미 고객 생성
        Customer customer = new Customer(
                CustomerType.INDIVIDUAL,            // customerType
                SalesGroup.NONGHYUP,               // salesGroup
                "1234567890123",                   // corpNo
                "9876543210",                      // bizNo
                "900101-1234567",                  // rrn
                "테스트 상호명",                     // bizName
                "홍길동",                           // ceoName
                "도소매",                           // bizType
                "농자재",                           // bizItem
                "02-123-4567",                     // tel
                "010-1234-5678",                   // phone
                "서울특별시 강남구 테헤란로 123",       // addressMain
                "101호",                           // addressSub
                "02-987-6543",                     // fax
                "test@example.com",                // email
                "김우리",                           // ourManager
                "이거래",                           // customerManager
                "테스트 메모입니다."                 // memo
        );

        //더미 직원 저장
        customerRepository.save(customer);

        dummyId = customer.getId();
    }


    private CreateCustomerRequest generateCreateCustomerRequest() {
        return new CreateCustomerRequest(
                CustomerType.INDIVIDUAL,           // customerType
                SalesGroup.NONGHYUP,               // salesGroup
                "1234567890123",                   // corpNo
                "9876543210",                      // bizNo
                "900101-1234567",                  // rrn
                "테스트 상호명",                      // bizName
                "홍길동",                           // ceoName
                "도소매",                           // bizType
                "농자재",                           // bizItem
                "02-123-4567",                     // tel
                "010-1234-5678",                   // phone
                "서울특별시 강남구 테헤란로 123",        // addressMain
                "101호",                           // addressSub
                "02-987-6543",                     // fax
                "test@example.com",                // email
                "김우리",                           // ourManager
                "이거래",                           // customerManager
                "테스트 메모입니다."                  // memo
        );
    }


    private UpdateCustomerRequest generateUpdateCustomerRequest() {
        return new UpdateCustomerRequest(
                CustomerType.CORPORATION,              // customerType
                SalesGroup.ETC,                        // salesGroup
                "1234567890123 업데이트",                 // corpNo
                "9876543210 업데이트",                    // bizNo
                "900101-1234567 업데이트",                // rrn
                "테스트 상호명 업데이트",                    // bizName
                "홍길동 업데이트",                          // ceoName
                "도소매 업데이트",                          // bizType
                "농자재 업데이트",                          // bizItem
                "02-123-4567 업데이트",                    // tel
                "010-1234-5678 업데이트",                  // phone
                "서울특별시 강남구 테헤란로 123 업데이트",       // addressMain
                "101호 업데이트",                           // addressSub
                "02-987-6543 업데이트",                     // fax
                "test@example.com 업데이트",                // email
                "김우리 업데이트",                           // ourManager
                "이거래 업데이트",                           // customerManager
                "테스트 메모입니다. 업데이트"                  // memo
        );
    }
}