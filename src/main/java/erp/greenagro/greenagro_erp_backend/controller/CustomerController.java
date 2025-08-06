package erp.greenagro.greenagro_erp_backend.controller;

import erp.greenagro.greenagro_erp_backend.dto.customer.*;
import erp.greenagro.greenagro_erp_backend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    //고객 생성
    @PostMapping("/customers")
    public ResponseEntity<CreateCustomerResponse> createCustomer(@RequestBody CreateCustomerRequest request){
        CreateCustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity.ok(response);
    }


    //전체 고객 조회
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerSummaryResponse>> getAllCustomers(){
        List<CustomerSummaryResponse> response = customerService.getAllCustomers();
        return ResponseEntity.ok(response);
    }


    //고객 상세 조회
    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDetailResponse> getCustomerDetail(@PathVariable Long id){
        CustomerDetailResponse response = customerService.getCustomerDetail(id);
        return ResponseEntity.ok(response);
    }


    //고객 수정 데이터 조회
    @GetMapping("/customers/{id}/edit-data")
    public ResponseEntity<CustomerEditResponse> getEditData(@PathVariable Long id){
        CustomerEditResponse response = customerService.getCustomerEditData(id);
        return ResponseEntity.ok(response);
    }


    //고객 수정
    @PutMapping("/customers/{id}")
    public ResponseEntity<Void> updateCustomer(@PathVariable Long id, @RequestBody UpdateCustomerRequest request){
        customerService.updateCustomer(id, request);
        return ResponseEntity.noContent().build();
    }


    //고객 삭제
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

}
