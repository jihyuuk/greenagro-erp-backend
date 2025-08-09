package erp.greenagro.greenagro_erp_backend.controller;

import erp.greenagro.greenagro_erp_backend.dto.warehouse.CreateWarehouseSiteRequest;
import erp.greenagro.greenagro_erp_backend.dto.warehouse.CreateWarehouseSiteResponse;
import erp.greenagro.greenagro_erp_backend.dto.warehouse.UpdateWarehouseSiteRequest;
import erp.greenagro.greenagro_erp_backend.dto.warehouse.WarehouseSiteResponse;
import erp.greenagro.greenagro_erp_backend.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    //창고지점 생성
    @PostMapping("/warehouse-sites")
    public ResponseEntity<CreateWarehouseSiteResponse> createSite(@RequestBody CreateWarehouseSiteRequest request){
        CreateWarehouseSiteResponse response = warehouseService.createSite(request);
        return ResponseEntity.ok(response);
    }


    //창고지점 수정
    @PutMapping("/warehouse-sites/{id}")
    public ResponseEntity<Void> updateSite(@PathVariable Long id, @RequestBody UpdateWarehouseSiteRequest request){
        warehouseService.updateSite(id, request);
        return ResponseEntity.noContent().build();
    }


    //창고지점 전체 조회
    @GetMapping("/warehouse-sites")
    public ResponseEntity<List<WarehouseSiteResponse>> getAllSites(){
        List<WarehouseSiteResponse> response = warehouseService.getAllSites();
        return ResponseEntity.ok(response);
    }


    //창고지점 단건 조회
    @GetMapping("/warehouse-sites/{id}")
    public ResponseEntity<WarehouseSiteResponse> getSite(@PathVariable Long id){
        WarehouseSiteResponse response = warehouseService.getSite(id);
        return ResponseEntity.ok(response);
    }
}
