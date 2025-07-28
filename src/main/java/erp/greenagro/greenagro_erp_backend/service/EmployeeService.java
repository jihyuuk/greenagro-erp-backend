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
import erp.greenagro.greenagro_erp_backend.repository.BranchRepository;
import erp.greenagro.greenagro_erp_backend.repository.EmployeeRepository;
import erp.greenagro.greenagro_erp_backend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new IllegalArgumentException("지점이 존재하지 않습니다."));

        //급여정보 객체 생성
        PayInfo payInfo = payInfoMapper.toEntity(request.getPayInfo());

        //임시 비밀번호 생성
        String tempPwd = SecurityUtil.generateTempPassword(4); //4자리 랜덤 숫자
        //비밀번호 해시화
        String hashedPwd = new BCryptPasswordEncoder().encode(tempPwd);

        //주민번호 암호화
        String encryptedRrn = SecurityUtil.encryptRrn(request.getRrn());

        //employee 객체 생성
        Employee employee = employeeMapper.fromCreate(request, branch, payInfo, hashedPwd, encryptedRrn);

        //저장
        employeeRepository.save(employee);

        //response 반환
        return employeeMapper.toCreate(employee, tempPwd);
    }


    //직원 상세 조회
    @Transactional
    public EmployeeDetailResponse getEmployeeDetail(Long id){
        //직원 조회
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원번호 입니다."));

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
    @Transactional
    public List<EmployeeSummaryResponse> getAllEmployees(){
        //모든 직원 조회
        List<Employee> employeeList = employeeRepository.findAll();

        //dto 변환
        return employeeList.stream().map(employee -> {
            //지점 요약 dto
            BranchSummaryResponse branchSummaryResponse = branchMapper.toResponse(employee.getBranch());
            return employeeMapper.toSummary(employee, branchSummaryResponse);
        }).toList();
    }


    //직원 퇴사 처리
    @Transactional
    public void resignEmployee(Long id, ResignEmployeeRequest request){
        //직원 조회
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원번호 입니다."));

        //직원 퇴사 처리
        employee.resign(request.getResignDate());
    }

}
