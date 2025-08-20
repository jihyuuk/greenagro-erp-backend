package erp.greenagro.greenagro_erp_backend.dto.partner.create;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateIndPartnerRequest extends CreatePartnerBase{

    @NotBlank
    private String rrn;   // 주민등록번호

}
