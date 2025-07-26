package erp.greenagro.greenagro_erp_backend.controller;

import erp.greenagro.greenagro_erp_backend.dto.employee.CreateEmployeeRequest;
import erp.greenagro.greenagro_erp_backend.dto.employee.CreateEmployeeResponse;
import erp.greenagro.greenagro_erp_backend.dto.employee.EmployeeResponse;
import erp.greenagro.greenagro_erp_backend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}