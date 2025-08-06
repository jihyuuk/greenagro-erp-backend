package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.customer.*;
import erp.greenagro.greenagro_erp_backend.model.entity.Customer;
import erp.greenagro.greenagro_erp_backend.model.enums.CustomerType;
import erp.greenagro.greenagro_erp_backend.model.enums.SalesGroup;
import erp.greenagro.greenagro_erp_backend.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    //거래처(고객) 생성
    @Transactional
    public CreateCustomerResponse createCustomer(CreateCustomerRequest request) {
        //1. 각종 검증

        //2. 중복확인

        //3. 객체 생성 (주민번호의 경우 암호화 필요)
        Customer customer = new Customer(
                request.getCustomerType(),
                request.getSalesGroup(),
                request.getCorpNo(),
                request.getBizNo(),
                request.getRrn(),
                request.getBizName(),
                request.getCeoName(),
                request.getBizType(),
                request.getBizItem(),
                request.getTel(),
                request.getPhone(),
                request.getAddressMain(),
                request.getAddressSub(),
                request.getFax(),
                request.getEmail(),
                request.getOurManager(),
                request.getCustomerManager(),
                request.getMemo()
        );

        //3. 저장
        customerRepository.save(customer);

        return new CreateCustomerResponse(customer.getId());
    }


    //전체 조회
    @Transactional(readOnly = true)
    public List<CustomerSummaryResponse> getAllCustomers() {

        return customerRepository.findAllByDeletedFalse().stream().map(customer ->
                new CustomerSummaryResponse(
                        customer.getCustomerType(),
                        customer.getSalesGroup(),
                        customer.getBizNo(),
                        customer.getRrn(),
                        customer.getBizName(),
                        customer.getCeoName(),
                        customer.getTel(),
                        customer.getFax(),
                        customer.getEmail(),
                        customer.getOurManager(),
                        customer.getCustomerManager(),
                        customer.getMemo()
                )).toList();
    }


    //상세 조회
    @Transactional(readOnly = true)
    public CustomerDetailResponse getCustomerDetail(Long id) {
        //1. 해당 고객 조회 (없으면 에러)
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객입니다. id:" + id));

        //2. dto 변환 및 반환 (주민번호의 경우 앞자리만 복호화)
        return new CustomerDetailResponse(
                customer.getId(),
                customer.getCustomerType(),
                customer.getSalesGroup(),
                customer.getCorpNo(),
                customer.getBizNo(),
                customer.getRrn(),
                customer.getBizName(),
                customer.getCeoName(),
                customer.getBizType(),
                customer.getBizItem(),
                customer.getTel(),
                customer.getPhone(),
                customer.getAddressMain(),
                customer.getAddressSub(),
                customer.getFax(),
                customer.getEmail(),
                customer.getOurManager(),
                customer.getCustomerManager(),
                customer.getMemo()
        );
    }

    //수정 데이터 조회
    @Transactional(readOnly = true)
    public CustomerEditResponse getCustomerEditData(Long id) {
        //1. 해당 고객 조회 (없으면 에러)
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객입니다. id:" + id));

        //2. DTO 생성 반환
        return new CustomerEditResponse(
                new CustomerDetailResponse(
                        customer.getId(),
                        customer.getCustomerType(),
                        customer.getSalesGroup(),
                        customer.getCorpNo(),
                        customer.getBizNo(),
                        customer.getRrn(),
                        customer.getBizName(),
                        customer.getCeoName(),
                        customer.getBizType(),
                        customer.getBizItem(),
                        customer.getTel(),
                        customer.getPhone(),
                        customer.getAddressMain(),
                        customer.getAddressSub(),
                        customer.getFax(),
                        customer.getEmail(),
                        customer.getOurManager(),
                        customer.getCustomerManager(),
                        customer.getMemo()
                ),
                Arrays.stream(CustomerType.values()).toList(),
                Arrays.stream(SalesGroup.values()).toList()
        );
    }

    //수정
    @Transactional
    public void updateCustomer(Long id, UpdateCustomerRequest request) {
        //1. 해당 고객 조회 (없으면 에러)
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객입니다. id:" + id));

        //2. 업데이트
        customer.update(
                request.getCustomerType(),
                request.getSalesGroup(),
                request.getCorpNo(),
                request.getBizNo(),
                request.getRrn(),
                request.getBizName(),
                request.getCeoName(),
                request.getBizType(),
                request.getBizItem(),
                request.getTel(),
                request.getPhone(),
                request.getAddressMain(),
                request.getAddressSub(),
                request.getFax(),
                request.getEmail(),
                request.getOurManager(),
                request.getCustomerManager(),
                request.getMemo()
        );
    }


    //삭제
    @Transactional
    public void deleteCustomer(Long id) {
        //1. 해당 고객 조회 (없으면 에러)
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객입니다. id:" + id));

        //2. 논리 삭제
        customer.delete();
    }


}
