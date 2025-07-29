package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.branch.BranchSummaryResponse;
import erp.greenagro.greenagro_erp_backend.dto.employee.*;
import erp.greenagro.greenagro_erp_backend.dto.payinfo.PayInfoDTO;
import erp.greenagro.greenagro_erp_backend.mapper.BranchMapper;
import erp.greenagro.greenagro_erp_backend.mapper.EmployeeMapper;
import erp.greenagro.greenagro_erp_backend.mapper.PayInfoMapper;
import erp.greenagro.greenagro_erp_backend.model.entity.Branch;
import erp.greenagro.greenagro_erp_backend.model.entity.Employee;
import erp.greenagro.greenagro_erp_backend.model.entity.PayInfo;
import erp.greenagro.greenagro_erp_backend.model.enums.AccountStatus;
import erp.greenagro.greenagro_erp_backend.model.enums.Role;
import erp.greenagro.greenagro_erp_backend.repository.BranchRepository;
import erp.greenagro.greenagro_erp_backend.repository.EmployeeRepository;
import erp.greenagro.greenagro_erp_backend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;
    private final EmployeeMapper employeeMapper;
    private final BranchMapper branchMapper;
    private final PayInfoMapper payInfoMapper;


    //직원 등록
    @Transactional
    public CreateEmployeeResponse createEmployee(CreateEmployeeRequest request) {
        //지점 조회하기
        Branch branch = getBranchOrThrow(request.getBranchId());

        //급여정보 객체 생성
        PayInfo payInfo = payInfoMapper.toEntity(request.getPayInfo());

        //임시 비밀번호 생성
        SecurityUtil.PasswordBundle passwordBundle = SecurityUtil.generateTempPassword();

        //주민번호 암호화
        String encryptedRrn = SecurityUtil.encryptRrn(request.getRrn());

        //employee 객체 생성
        Employee employee = employeeMapper.fromCreate(request, branch, payInfo, passwordBundle.getHashed(), encryptedRrn);

        //저장
        employeeRepository.save(employee);

        //response 반환
        return employeeMapper.toCreate(employee, passwordBundle.getRaw());
    }


    //직원 상세 조회
    @Transactional(readOnly = true)
    public EmployeeDetailResponse getEmployeeDetail(Long id){
        //직원 조회
        Employee employee = getEmployeeOrThrow(id);

        //주민등록번호 복호화
        String decryptRrn = SecurityUtil.decryptRrn(employee.getRrn());

        //지점정보 DTO
        BranchSummaryResponse branchSummaryResponse = branchMapper.toResponse(employee.getBranch());

        //급여정보 DTO
        PayInfoDTO payInfoDTO = payInfoMapper.toResponse(employee.getPayInfo());

        //response 반환
        return employeeMapper.toDetail(employee, decryptRrn, branchSummaryResponse, payInfoDTO);
    }


    //모든 직원 조회
    @Transactional(readOnly = true)
    public Map<AccountStatus, List<EmployeeSummaryResponse>> getAllEmployees(){

        //계정 상태별로 그룹화
        Map<AccountStatus, List<EmployeeSummaryResponse>> employeesMap = new HashMap<>();

        //employeesMap 초기화
        for (AccountStatus status : AccountStatus.values()) {
            employeesMap.put(status, new ArrayList<>());
        }

        //모든 직원 조회
        List<Employee> employeeList = employeeRepository.findAll();

        //dto 변환 및 상태별로 그룹화
        for (Employee employee : employeeList) {
            //지점 요약 dto
            BranchSummaryResponse branchSummaryResponse = branchMapper.toResponse(employee.getBranch());
            //직원 요약 dto
            EmployeeSummaryResponse employeeSummaryResponse = employeeMapper.toSummary(employee, branchSummaryResponse);

            //계정상태 그룹별로 map 에 추가
            employeesMap.get(employee.getStatus()).add(employeeSummaryResponse);
        }

        //응답
        return employeesMap;
    }


    //직원 수정 관련 필요 데이터
    @Transactional(readOnly = true)
    public EmployeeEditResponse getEmployeeEditData(Long id){

        //직원 상세 dto
        EmployeeDetailResponse employeeDetail = getEmployeeDetail(id);

        //지점 옵션
        List<BranchSummaryResponse> branchSummaryResponses = branchRepository.findAll().stream().map(branchMapper::toResponse).toList();

        //권한 옵션
        List<Role> roles = Arrays.asList(Role.values());

        //계정상태 옵션
        List<AccountStatus> accountStatuses = Arrays.asList(AccountStatus.values());

        return employeeMapper.toEdit(employeeDetail, branchSummaryResponses, roles, accountStatuses);
    }


    //직원 업데이트
    @Transactional
    public void updateEmployee(Long id, UpdateEmployeeRequest request) {
        //직원 조회
        Employee employee = getEmployeeOrThrow(id);

        //지점 조회하기
        Branch branch = getBranchOrThrow(request.getBranchId());

        //급여정보 조회하기
        PayInfo payInfo = employee.getPayInfo();

        //주민번호 암호화
        String encryptedRrn = SecurityUtil.encryptRrn(request.getRrn());

        //직원 업데이트
        employee.update(branch, request.getName(), encryptedRrn, request.getPosition(), request.getPhone(), request.getEmail(), request.getAddress(), request.getHireDate(), request.getResignDate(), request.getRole(), request.getStatus());
        //급여정보 업데이트
        PayInfoDTO updatePayInfo = request.getPayInfo();
        payInfo.update(updatePayInfo.getBankName(), updatePayInfo.getAccountNumber(), updatePayInfo.getDepositorName(), updatePayInfo.getBaseSalary());
    }


    //직원 퇴사 처리
    @Transactional
    public void resignEmployee(Long id, ResignEmployeeRequest request){
        //직원 조회
        Employee employee = getEmployeeOrThrow(id);

        //직원 퇴사 처리
        employee.resign(request.getResignDate());
    }


    //직원 비밀번호 초기화
    @Transactional
    public ResetPasswordResponse resetPassword(Long id) {
        //직원 조회
        Employee employee = getEmployeeOrThrow(id);

        //임시 비밀번호 생성
        SecurityUtil.PasswordBundle passwordBundle = SecurityUtil.generateTempPassword();

        //직원 비밀번호 초기화
        employee.resetPassword(passwordBundle.getHashed());

        //임시비밀번호 반환
        return new ResetPasswordResponse(passwordBundle.getRaw());
    }




    //직원 조회 공용 메서드
    private Employee getEmployeeOrThrow(Long id){
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원 입니다. id="+id));
    }

    //브랜치 조회 공용 메서드
    private Branch getBranchOrThrow(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지점 입니다. id="+id));
    }
}
