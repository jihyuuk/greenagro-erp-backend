package erp.greenagro.greenagro_erp_backend.controller;

import erp.greenagro.greenagro_erp_backend.dto.partner.PartnerEditResponse;
import erp.greenagro.greenagro_erp_backend.dto.partner.PartnerSummaryResponse;
import erp.greenagro.greenagro_erp_backend.dto.partner.create.CreatePartnerBase;
import erp.greenagro.greenagro_erp_backend.dto.partner.create.CreatePartnerResponse;
import erp.greenagro.greenagro_erp_backend.dto.partner.detail.PartnerDetailBase;
import erp.greenagro.greenagro_erp_backend.dto.partner.update.UpdatePartnerBase;
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


    //거래처 생성 - (사업자, 개인)
    @PostMapping("/partners")
    public ResponseEntity<CreatePartnerResponse> createPartner(@Valid @RequestBody CreatePartnerBase request){
        CreatePartnerResponse response = partnerService.createPartner(request);
        return ResponseEntity.ok(response);
    }


    //거래처 조회 - 전체
    @GetMapping("/partners")
    public ResponseEntity<List<PartnerSummaryResponse>> getAllPartners(){
        List<PartnerSummaryResponse> response = partnerService.getAllPartners();
        return ResponseEntity.ok(response);
    }


    //거래처 조회 - 단건 상세
    @GetMapping("/partners/{id}")
    public ResponseEntity<PartnerDetailBase> getPartnerDetail(@PathVariable Long id){
        PartnerDetailBase response = partnerService.getPartnerDetail(id);
        return ResponseEntity.ok(response);
    }


    //거래처 수정 데이터 조회
    @GetMapping("/partners/{id}/edit-data")
    public ResponseEntity<PartnerEditResponse> getEditData(@PathVariable Long id){
        PartnerEditResponse response = partnerService.getPartnerEditData(id);
        return ResponseEntity.ok(response);
    }


    //거래처 수정 - (사업자, 개인)
    @PutMapping("/partners/{id}")
    public ResponseEntity<Void> updatePartner(@PathVariable Long id, @Valid @RequestBody UpdatePartnerBase request){
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
