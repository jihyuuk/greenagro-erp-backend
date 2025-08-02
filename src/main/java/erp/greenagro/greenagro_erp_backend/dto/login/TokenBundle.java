package erp.greenagro.greenagro_erp_backend.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenBundle {

    private String accessToken;
    private String refreshToken;

}
