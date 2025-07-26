package erp.greenagro.greenagro_erp_backend.controller;

import erp.greenagro.greenagro_erp_backend.dto.employee.CreateEmployeeRequest;
import erp.greenagro.greenagro_erp_backend.dto.employee.CreateEmployeeResponse;
import erp.greenagro.greenagro_erp_backend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/employees/new")
    public ResponseEntity<CreateEmployeeResponse> createEmployee(@RequestBody CreateEmployeeRequest request){
        CreateEmployeeResponse response = employeeService.createEmployee(request);
        return ResponseEntity.status(CREATED).body(response);
    }

}