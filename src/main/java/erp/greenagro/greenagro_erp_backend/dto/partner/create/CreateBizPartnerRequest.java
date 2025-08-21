package erp.greenagro.greenagro_erp_backend.dto.partner.create;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonTypeName("BUSINESS")
public class CreateBizPartnerRequest extends CreatePartnerBase{

    @NotBlank
    private String bizNo;              // 사업자등록번호 (필수, 유니크 권장)

    private String bizType;            // 업태 (선택)

    private String bizItem;            // 종목 (선택)

}
