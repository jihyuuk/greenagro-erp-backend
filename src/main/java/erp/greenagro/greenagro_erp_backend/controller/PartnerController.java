package erp.greenagro.greenagro_erp_backend.controller;

import erp.greenagro.greenagro_erp_backend.dto.partner.*;
import erp.greenagro.greenagro_erp_backend.service.PartnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;


    //거래처 생성
    @PostMapping("/partners")
    public ResponseEntity<CreatePartnerResponse> createPartner(@Valid @RequestBody CreatePartnerRequest request){
        CreatePartnerResponse response = partnerService.createPartner(request);
        return ResponseEntity.ok(response);
    }


    //전체 거래처 조회
    @GetMapping("/partners")
    public ResponseEntity<List<PartnerSummaryResponse>> getAllPartners(){
        List<PartnerSummaryResponse> response = partnerService.getAllPartners();
        return ResponseEntity.ok(response);
    }


    //거래처 상세 조회
    @GetMapping("/partners/{id}")
    public ResponseEntity<PartnerDetailResponse> getPartnerDetail(@PathVariable Long id){
        PartnerDetailResponse response = partnerService.getPartnerDetail(id);
        return ResponseEntity.ok(response);
    }


    //거래처 수정 데이터 조회
    @GetMapping("/partners/{id}/edit-data")
    public ResponseEntity<PartnerEditResponse> getEditData(@PathVariable Long id){
        PartnerEditResponse response = partnerService.getPartnerEditData(id);
        return ResponseEntity.ok(response);
    }


    //거래처 수정
    @PutMapping("/partners/{id}")
    public ResponseEntity<Void> updatePartner(@PathVariable Long id, @Valid @RequestBody UpdatePartnerRequest request){
        partnerService.updatePartner(id, request);
        return ResponseEntity.noContent().build();
    }


    //거래처 삭제
    @DeleteMapping("/partners/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable Long id){
        partnerService.deletePartner(id);
        return ResponseEntity.noContent().build();
    }

}
