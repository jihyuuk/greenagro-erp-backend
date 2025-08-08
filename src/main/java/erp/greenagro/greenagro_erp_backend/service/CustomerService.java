package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.customer.*;
import erp.greenagro.greenagro_erp_backend.mapper.CustomerMapper;
import erp.greenagro.greenagro_erp_backend.model.entity.Customer;
import erp.greenagro.greenagro_erp_backend.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    //거래처(고객) 생성
    @Transactional
    public CreateCustomerResponse createCustomer(CreateCustomerRequest request) {
        //1. 각종 검증

        //2. 중복확인 - 중복에 관해서는 열어두자! 추후 고려

        //3. 객체 생성 (주민번호의 경우 암호화 필요)
        Customer customer = customerMapper.fromCreate(request);

        //3. 저장
        customerRepository.save(customer);

        //4. id 반환
        return customerMapper.toCreate(customer);
    }


    //전체 조회
    @Transactional(readOnly = true)
    public List<CustomerSummaryResponse> getAllCustomers() {

        return customerRepository.findAllByDeletedFalse().stream().map(customer ->
                customerMapper.toSummary(customer)
        ).toList();
    }


    //상세 조회
    @Transactional(readOnly = true)
    public CustomerDetailResponse getCustomerDetail(Long id) {
        //1. 해당 고객 조회 (없으면 에러)
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객입니다. id:" + id));

        //2. dto 변환 및 반환 (주민번호의 경우 앞자리만 복호화)
        return customerMapper.toDetail(customer);
    }

    //수정 데이터 조회
    @Transactional(readOnly = true)
    public CustomerEditResponse getCustomerEditData(Long id) {
        //1. 해당 고객 조회 (없으면 에러)
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객입니다. id:" + id));

        //2. DTO 생성 반환
        return customerMapper.toEdit(customer);
    }

    //수정
    @Transactional
    public void updateCustomer(Long id, UpdateCustomerRequest request) {
        //1. 해당 고객 조회 (없으면 에러)
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객입니다. id:" + id));

        //검증 추후 구현

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
        customer.delete(); //JPA 더티 체킹
    }


}
