package erp.greenagro.greenagro_erp_backend.dto.partner.update;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonTypeName("INDIVIDUAL")
public class UpdateIndPartnerRequest extends UpdatePartnerBase{

    @NotBlank
    private String rrn;   // 주민등록번호

}
