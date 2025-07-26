package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.employee.CreateEmployeeRequest;
import erp.greenagro.greenagro_erp_backend.dto.employee.CreateEmployeeResponse;
import erp.greenagro.greenagro_erp_backend.dto.employee.EmployeeResponse;
import erp.greenagro.greenagro_erp_backend.dto.employee.ResignEmployeeRequest;
import erp.greenagro.greenagro_erp_backend.mapper.EmployeeMapper;
import erp.greenagro.greenagro_erp_backend.model.entity.Branch;
import erp.greenagro.greenagro_erp_backend.model.entity.Employee;
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


    //직원 등록
    @Transactional
    public CreateEmployeeResponse createEmployee(CreateEmployeeRequest request) {
        //지점 조회하기
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new IllegalArgumentException("지점이 존재하지 않습니다."));

        //임시 비밀번호 생성
        String tempPwd = SecurityUtil.generateTempPassword(4); //4자리 랜덤 숫자
        //비밀번호 해시화
        String hashedPwd = new BCryptPasswordEncoder().encode(tempPwd);

        //주민번호 암호화
        String encryptedRrn = SecurityUtil.encryptRrn(request.getRrn());

        //employee 객체 생성
        Employee employee = employeeMapper.toEntity(request, branch, hashedPwd, encryptedRrn);

        //저장
        employeeRepository.save(employee);

        //response 반환
        return employeeMapper.toResponse(employee, tempPwd);
    }


    //직원 단건 조회
    @Transactional
    public EmployeeResponse getEmployeeById(Long id){
        //직원 단건 조회
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원번호 입니다."));

        //DTO 변환
        return employeeMapper.toResponse(employee);
    }


    //모든 직원 조회
    @Transactional
    public List<EmployeeResponse> getAllEmployees(){
        //모든 직원 조회
        List<Employee> employeeList = employeeRepository.findAll();
        //dto 변환하여 반환
        return employeeList.stream().map(employeeMapper::toResponse).toList();
    }


    //직원 퇴사 처리
    @Transactional
    public void resignEmployee(Long id, ResignEmployeeRequest request){
        //직원 조회
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원번호 입니다."));

        //직원 계정 상태 변경
        employee.resign(request.getResignDate());
    }

}
