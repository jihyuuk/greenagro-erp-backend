package erp.greenagro.greenagro_erp_backend.controller;

import erp.greenagro.greenagro_erp_backend.dto.employee.CreateEmployeeRequest;
import erp.greenagro.greenagro_erp_backend.dto.employee.CreateEmployeeResponse;
import erp.greenagro.greenagro_erp_backend.dto.employee.EmployeeResponse;
import erp.greenagro.greenagro_erp_backend.dto.employee.ResignEmployeeRequest;
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
    public ResponseEntity<List<EmployeeResponse>> getAllEmployee(){
        List<EmployeeResponse> allEmployees = employeeService.getAllEmployees();
        return ResponseEntity.ok(allEmployees);
    }


    //직원 단건 조회
    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponse> getEmployee(@PathVariable Long id){
        EmployeeResponse response = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(response);
    }


    //직원 퇴사 처리
    @PatchMapping("/employees/{id}/resign")
    public ResponseEntity<Void> resignEmployee(@PathVariable Long id, @RequestBody ResignEmployeeRequest request){
        employeeService.resignEmployee(id, request);
        return ResponseEntity.noContent().build();
    }

}