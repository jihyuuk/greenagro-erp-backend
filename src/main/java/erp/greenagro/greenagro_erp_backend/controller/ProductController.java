package erp.greenagro.greenagro_erp_backend.controller;

import erp.greenagro.greenagro_erp_backend.dto.productgroup.CreateProductGroupRequest;
import erp.greenagro.greenagro_erp_backend.dto.productgroup.CreateProductGroupResponse;
import erp.greenagro.greenagro_erp_backend.dto.productgroup.ProductGroupDTO;
import erp.greenagro.greenagro_erp_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //품목 그룹 생성
    @PostMapping("/product-groups")
    public ResponseEntity<CreateProductGroupResponse> createProductGroup(@RequestBody CreateProductGroupRequest request){
        CreateProductGroupResponse response = productService.createGroup(request);
        return ResponseEntity.ok(response);
    }


    //품목 그룹 전체 조회
    @GetMapping("/product-groups")
    public ResponseEntity<List<ProductGroupDTO>> getAllProductGroup(){
        List<ProductGroupDTO> responses = productService.getAllGroups();
        return ResponseEntity.ok(responses);
    }


    //품목 그룹 수정
    @PutMapping("/product-groups/{id}")
    public ResponseEntity<Void> updateProductGroup(@PathVariable Long id, @RequestBody ProductGroupDTO request){
        productService.updateGroup(id,request);
        return ResponseEntity.noContent().build();
    }


    //품목 그룹 삭제
    @DeleteMapping("/product-groups/{id}")
    public ResponseEntity<Void> deleteProductGroup(@PathVariable Long id){
        productService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

}
