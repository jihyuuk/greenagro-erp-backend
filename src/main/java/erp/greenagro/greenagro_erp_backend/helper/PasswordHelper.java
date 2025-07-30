package erp.greenagro.greenagro_erp_backend.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PasswordHelper {

    private final PasswordEncoder passwordEncoder;

    /**
     * 8자리 랜덤 임시비밀번호 생성
     *
     * @return (평문, 해시화) 비밀번호 번들
     */
    public PasswordBundle generateTempPassword() {
        //임시 비밀번호 8자리 생성
        String rawPwd = UUID.randomUUID().toString().substring(0, 8);
        //해시화
        String hashedPwd = passwordEncoder.encode(rawPwd);

        return new PasswordBundle(rawPwd, hashedPwd);
    }


    public boolean matches(String raw, String hashed) {
        return passwordEncoder.matches(raw, hashed);
    }


    //비밀번호 원문 + 해시 번들 (응집도 up)
    @Getter
    @AllArgsConstructor
    public static class PasswordBundle {

        private final String raw;
        private final String hashed;

    }
}
