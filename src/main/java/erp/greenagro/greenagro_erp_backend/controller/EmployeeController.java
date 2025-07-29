package erp.greenagro.greenagro_erp_backend.controller;

import erp.greenagro.greenagro_erp_backend.dto.employee.*;
import erp.greenagro.greenagro_erp_backend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    //직원 등록
    @PostMapping("/employees/new")
    public ResponseEntity<CreateEmployeeResponse> createEmployee(@RequestBody CreateEmployeeRequest request){
        CreateEmployeeResponse response = employeeService.createEmployee(request);
        return ResponseEntity.status(CREATED).body(response);
    }


    //모든 직원 조회(퇴사자 포함)
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeSummaryResponse>> getAllEmployee(){
        List<EmployeeSummaryResponse> allEmployees = employeeService.getAllEmployees();
        return ResponseEntity.ok(allEmployees);
    }


    //직원 상세 조회
    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeDetailResponse> getEmployeeDetail(@PathVariable Long id){
        EmployeeDetailResponse response = employeeService.getEmployeeDetail(id);
        return ResponseEntity.ok(response);
    }

    //직원 수정 폼 데이터
    @GetMapping("/employees/{id}/edit")
    public ResponseEntity<EmployeeEditResponse> getEditData(@PathVariable Long id){
        EmployeeEditResponse response = employeeService.getEmployeeEdit(id);
        return ResponseEntity.ok(response);
    }

    //직원 업데이트
    @PutMapping("/employees/{id}")
    public ResponseEntity<Void> updateEmployee(@PathVariable Long id, @RequestBody UpdateEmployeeRequest request){
        employeeService.updateEmployee(id, request);
        return ResponseEntity.noContent().build();
    }


    //직원 퇴사 처리
    @PatchMapping("/employees/{id}/resign")
    public ResponseEntity<Void> resignEmployee(@PathVariable Long id, @RequestBody ResignEmployeeRequest request){
        employeeService.resignEmployee(id, request);
        return ResponseEntity.noContent().build();
    }

    //직원 비밀번호 초기화
    @PatchMapping("/employees/{id}/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@PathVariable Long id){
        ResetPasswordResponse response = employeeService.resetPassword(id);
        return ResponseEntity.ok(response);
    }

}