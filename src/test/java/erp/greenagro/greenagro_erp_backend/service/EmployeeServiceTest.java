package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.employee.*;
import erp.greenagro.greenagro_erp_backend.dto.payinfo.PayInfoDTO;
import erp.greenagro.greenagro_erp_backend.model.entity.Branch;
import erp.greenagro.greenagro_erp_backend.model.entity.Employee;
import erp.greenagro.greenagro_erp_backend.model.entity.PayInfo;
import erp.greenagro.greenagro_erp_backend.model.enums.AccountStatus;
import erp.greenagro.greenagro_erp_backend.model.enums.BranchType;
import erp.greenagro.greenagro_erp_backend.model.enums.Role;
import erp.greenagro.greenagro_erp_backend.repository.BranchRepository;
import erp.greenagro.greenagro_erp_backend.repository.EmployeeRepository;
import erp.greenagro.greenagro_erp_backend.util.RrnCryptoUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class EmployeeServiceTest {


    @Autowired EmployeeService employeeService;
    @Autowired EmployeeRepository employeeRepository;
    @Autowired BranchRepository branchRepository;
    @Autowired PasswordEncoder passwordEncoder;


    /*
        createEmployee() - 직원 생성
        getEmployeeDetail() - 직원 상세 조회
        getAllEmployees() - 전체 직원 조회
        getEmployeeEditData() - 직원 수정 폼 데이터
        updateEmployee() - 직원 수정
        resignEmployee() - 직원 퇴사처리
        ResetPassword() - 직원 비밀번호 초기화
     */

    @Test
    void 직원_생성() {
        //직원 생성 후 저장
        //반환 값으로 id, 임시비번 오는지
        //반환된 id로 db에 저장되어있는지 찾기
        //주민번호, 비밀번호 암호화 되어있는지

        //given
        CreateEmployeeRequest request = generateCreateEmployeeRequest();

        //when
        CreateEmployeeResponse response = employeeService.createEmployee(request);

        //저장된 직원 조회
        Employee findEmployee = employeeRepository.findById(response.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("직원 저장 실패"));


        //then
        assertNotNull(response.getEmployeeId()); //직원 아이디 반환 되는지 확인
        assertEquals(8, response.getTempPassword().length()); //임시 비밀번호 8자리 반환 되는지 확인

        assertEquals(request.getName(), findEmployee.getName()); //이름 같나 확인
        assertEquals(request.getPayInfo().getBankName(), findEmployee.getPayInfo().getBankName()); //payInfo 은행명 일치 확인

        assertNotEquals(findEmployee.getPassword(), response.getTempPassword()); //비밀번호 해시화 되었는지 확인
        assertTrue(passwordEncoder.matches(response.getTempPassword(), findEmployee.getPassword()));//비밀번호 매칭 확인

        assertNotEquals(findEmployee.getRrn(), request.getRrn());
        assertEquals(RrnCryptoUtil.encryptRrn((request.getRrn())), findEmployee.getRrn()); //주민번호 암호화 확인
        assertEquals(RrnCryptoUtil.decryptRrn(findEmployee.getRrn()), request.getRrn()); //주민번호 복호화 확인
        assertTrue(RrnCryptoUtil.matches(request.getRrn(), findEmployee.getRrn())); //주민등록번호 매칭확인
    }


    @Test
    void 직원_상세_조회() {
        //given
        Employee employee = employeeRepository.findByName("홍길동").orElseThrow(() -> new IllegalArgumentException("직원 없음 이름 = 홍길동"));


        //when
        EmployeeDetailResponse response = employeeService.getEmployeeDetail(employee.getId());


        //then
        //employee 확인
        assertEquals(employee.getId(), response.getId());
        assertEquals(employee.getName(), response.getName());
        assertTrue(RrnCryptoUtil.matches(response.getRrn(), employee.getRrn())); //주민번호 복호화 확인
        assertEquals(employee.getPhone(), response.getPhone());
        assertEquals(employee.getEmail(), response.getEmail());
        assertEquals(employee.getAddress(), response.getAddress());
        assertEquals(employee.getHireDate(), response.getHireDate());
        assertEquals(employee.getResignDate(), response.getResignDate());
        assertEquals(employee.getRole(), response.getRole());
        assertEquals(employee.getStatus(), response.getStatus());
        //branch 확인
        assertEquals(employee.getBranch().getId(), response.getBranch().getId());
        assertEquals(employee.getBranch().getName(), response.getBranch().getName());
        //PayInfo 확인
        assertEquals(employee.getPayInfo().getBankName(), response.getPayInfo().getBankName());
        assertEquals(employee.getPayInfo().getAccountNumber(), response.getPayInfo().getAccountNumber());
        assertEquals(employee.getPayInfo().getDepositorName(), response.getPayInfo().getDepositorName());
        assertEquals(employee.getPayInfo().getBaseSalary(), response.getPayInfo().getBaseSalary());
    }


    @Test
    void 직원_전체_조회() {
        //given
        Employee activeEmployee = employeeRepository.findByName("홍길동").orElseThrow(() -> new IllegalArgumentException("직원 없음 이름 = 홍길동"));


        //when
        Map<AccountStatus, List<EmployeeSummaryResponse>> response = employeeService.getAllEmployees();


        //then
        //모든 AccountStatus enum 값이 포함됐는지 확인
        assertTrue(Arrays.stream(AccountStatus.values()).allMatch(response::containsKey));

        //상태별 직원 수 확인
        assertEquals(1, response.get(AccountStatus.ACTIVE).size()); //지금은 하드코딩 findAllByState 고려
        assertEquals(1, response.get(AccountStatus.ON_LEAVE).size());
        assertEquals(1, response.get(AccountStatus.RESIGNED).size());

        //ACTIVE 직원 확인
        EmployeeSummaryResponse activeDto = response.get(AccountStatus.ACTIVE).stream()
                .filter(dto -> dto.getName().equals(activeEmployee.getName()))
                .findFirst()
                .orElseThrow();

        //DTO 확인
        assertEquals(activeEmployee.getId(), activeDto.getId());
        assertEquals(activeEmployee.getPosition(), activeDto.getPosition());
        assertEquals(activeEmployee.getPhone(), activeDto.getPhone());
        assertEquals(activeEmployee.getEmail(), activeDto.getEmail());
        assertEquals(activeEmployee.getBranch().getId(), activeDto.getBranch().getId());
        assertEquals(activeEmployee.getBranch().getName(), activeDto.getBranch().getName());
    }


    @Test
    void 직원_수정_데이터_조회() {
        //given
        Employee employee = employeeRepository.findByName("홍길동").orElseThrow(() -> new IllegalArgumentException("직원 없음 이름 = 홍길동"));


        //when
        EmployeeEditResponse response = employeeService.getEmployeeEditData(employee.getId());


        //then
        assertEquals(branchRepository.findAll().size(), response.getBranchOptions().size()); //모든 지점이 옵션에 포함되었는지
        assertTrue(Arrays.stream(Role.values()).allMatch(response.getRoleOptions()::contains)); //모든 권한 enum이 옵션에 포함되었는지
        assertTrue(Arrays.stream(AccountStatus.values()).allMatch(response.getStatusOptions()::contains)); //모든 계정상태 enum이 옵션에 포함되었느지
        assertEquals(employee.getId(), response.getEmployee().getId()); //EmployeeDetailResponse 확인 (서비스 내부에서 getEmployeeDetail()호출함 따라서 자세한 검증 패스)
    }


    @Test
    void 직원_수정하기() {
        //given
        Employee employee = employeeRepository.findByName("홍길동").orElseThrow(() -> new IllegalArgumentException("직원 없음 이름 = 홍길동"));
        UpdateEmployeeRequest request = generateUpdateEmployeeRequest();


        //when
        employeeService.updateEmployee(employee.getId(), request);


        //then
        Employee afterEmployee = employeeRepository.findByName(request.getName()).orElseThrow(() -> new IllegalArgumentException("직원 없음 이름 = "+request.getName()));

        //직원 업데이트 확인
        assertEquals(request.getBranchId(), afterEmployee.getBranch().getId());
        assertEquals(request.getName(), afterEmployee.getName());
        assertTrue(RrnCryptoUtil.matches(request.getRrn(), afterEmployee.getRrn()));
        assertEquals(request.getPosition(), afterEmployee.getPosition());
        assertEquals(request.getPhone(), afterEmployee.getPhone());
        assertEquals(request.getEmail(), afterEmployee.getEmail());
        assertEquals(request.getAddress(), afterEmployee.getAddress());
        assertEquals(request.getHireDate(), afterEmployee.getHireDate());
        assertEquals(request.getResignDate(), afterEmployee.getResignDate());
        assertEquals(request.getRole(), afterEmployee.getRole());
        assertEquals(request.getStatus(), afterEmployee.getStatus());
        //급여정보 업데이트확인
        assertEquals(request.getPayInfo().getBankName(), afterEmployee.getPayInfo().getBankName());
        assertEquals(request.getPayInfo().getAccountNumber(), afterEmployee.getPayInfo().getAccountNumber());
        assertEquals(request.getPayInfo().getDepositorName(), afterEmployee.getPayInfo().getDepositorName());
        assertEquals(request.getPayInfo().getBaseSalary(), afterEmployee.getPayInfo().getBaseSalary());
    }


    @Test
    void 직원_퇴사처리() {
        //given
        Employee employee = employeeRepository.findByName("홍길동").orElseThrow(() -> new IllegalArgumentException("직원 없음 이름 = 홍길동"));
        LocalDate resignDate = LocalDate.of(1997, 2, 23);
        ResignEmployeeRequest request = new ResignEmployeeRequest(resignDate);

        //when
        employeeService.resignEmployee(employee.getId(), request);


        //then
        Employee afterEmployee = employeeRepository.findByName("홍길동").orElseThrow(() -> new IllegalArgumentException("직원 없음 이름 = 홍길동"));

        assertEquals(AccountStatus.RESIGNED, afterEmployee.getStatus()); //계정 상태 퇴사 적용 여부
        assertEquals(resignDate, afterEmployee.getResignDate());; //퇴사일 적용 여부
    }


    @Test
    void 직원_비밀번호_초기화() {
        //given
        Employee employee = employeeRepository.findByName("홍길동").orElseThrow(() -> new IllegalArgumentException("직원 없음 이름 = 홍길동"));
        String beforePassword = employee.getPassword(); //초기화 전 비밀번호

        //when
        ResetPasswordResponse response = employeeService.resetPassword(employee.getId());//초기화 수행

        //then
        Employee afterEmployee = employeeRepository.findByName("홍길동").orElseThrow(() -> new IllegalArgumentException("직원 없음 이름 = 홍길동"));
        String afterPassword = afterEmployee.getPassword();

        assertEquals(response.getTempPassword().length(), 8); //응답으로 8자리 랜덤 임시비밀번호 잘 오는지 확인
        assertNotEquals(beforePassword, afterPassword); //비밀번호 변경되었는지
        assertTrue(passwordEncoder.matches(response.getTempPassword(), afterPassword)); //임시 비밀번호 매칭여부
    }




    @BeforeEach
    void initData(){
        //브랜치 생성
        Branch incheon = new Branch("인천지점", "황용순", "032-544-8228", "032-544-1015", "green8228@naver.com", "인천광역시 계양구 벌말로 596-3", "122-81-85031", "도매 및 소매업", "비료 및 동약 도매업", LocalDate.of(2005, 1, 1), "https://www.aaa.aaa", BranchType.BRANCH, "120111-0362963");
        Branch kimpo = new Branch("김포지점", "황용순", "031-985-1530", "032-544-1015", "green8228@naver.com", "경기도 김포시 하성면 봉성로 153", "122-81-85031", "도매 및 소매업", "비료 및 동약 도매업", LocalDate.of(2024, 7, 1), "https://www.bbb.bbb", BranchType.HEADQUARTER, "120111-0362963");

        //브랜치 저장
        branchRepository.save(incheon);
        branchRepository.save(kimpo);

        //급여정보 생성
        PayInfo payInfo1 = new PayInfo("국민은행", "123-456-789012", "홍길동", 3_000_000L);
        PayInfo payInfo2 = new PayInfo("신한은행", "110-234-567890", "김철수", 4_200_000L);
        PayInfo payInfo3 = new PayInfo("하나은행", "356-789-123456", "이영희", 2_850_000L);

        //직원생성
        Employee employee1 = new Employee(incheon, "홍길동", passwordEncoder.encode("encodedPassword1"), RrnCryptoUtil.encryptRrn("encryptedRrn1"), "과장", "010-1111-2222", "hong@example.com", "서울시 강남구", LocalDate.of(2020, 1, 15), Role.ADMIN, AccountStatus.ACTIVE, payInfo1);
        Employee employee2 = new Employee(kimpo, "김철수", passwordEncoder.encode("encodedPassword2"), RrnCryptoUtil.encryptRrn("encryptedRrn2"), "대리", "010-2222-3333", "kim@example.com", "부산시 해운대구", LocalDate.of(2021, 6, 3), Role.MANAGER, AccountStatus.ON_LEAVE, payInfo2);
        Employee employee3 = new Employee(incheon, "이영희", passwordEncoder.encode("encodedPassword3"), RrnCryptoUtil.encryptRrn("encryptedRrn3"), "사원", "010-4444-5555", "lee@example.com", "대전시 유성구", LocalDate.of(2022, 11, 20), Role.STAFF, AccountStatus.RESIGNED, payInfo3);

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);
    }



    private CreateEmployeeRequest generateCreateEmployeeRequest(){
        PayInfoDTO payInfoDTO = new PayInfoDTO(
                "신한은행",
                "979-24-332122",
                "홍길동",
                35000000L
        );

        return  new CreateEmployeeRequest(
                1L,
                "홍길동",
                "123456-3123456",
                "과장",
                "010-1111-2222",
                "abc@gmail.com",
                "경기도 김포시 하성면",
                LocalDate.now(),
                Role.ADMIN,
                payInfoDTO
        );
    }

    private UpdateEmployeeRequest generateUpdateEmployeeRequest(){
        PayInfoDTO payInfoDTO = new PayInfoDTO(
                "신한은행",
                "979-24-332122",
                "홍길동",
                35000000L
        );

        return new UpdateEmployeeRequest(
          1L,
                "홍길동2",
                "113213-1233123",
                "부장",
                "010-2222-333",
                "hong2@example.com",
                "경기도 고양시",
                LocalDate.of(2000, 2, 3),
                LocalDate.of(2000, 2, 5),
                Role.MANAGER,
                AccountStatus.RESIGNED,
                payInfoDTO
        );
    }

}