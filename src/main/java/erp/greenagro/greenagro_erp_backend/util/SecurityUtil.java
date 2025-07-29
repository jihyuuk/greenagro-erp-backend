package erp.greenagro.greenagro_erp_backend.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.UUID;

public class SecurityUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 8자리 랜덤 임시비밀번호 생성
     * @return (평문, 해시화) 비밀번호 번들
     */
    public static PasswordBundle generateTempPassword(){
        //임시 비밀번호 8자리 생성
        String rawPwd = UUID.randomUUID().toString().substring(0, 8);
        //해시화
        String hashedPwd = encoder.encode(rawPwd);

        return new PasswordBundle(rawPwd, hashedPwd);
    }


    /**
     * 주민등록번호 암호화
     * @param rrn 평문 주민등록번호
     * @return AES 암호화된 주민등록번호
     */
    public static String encryptRrn(String rrn){
        try {
            String AES_KEY_RRN = System.getenv("AES_KEY_RRN");
            SecretKeySpec key = new SecretKeySpec(AES_KEY_RRN.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(rrn.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("주민등록번호 암호화 실패", e);
        }
    }

    /**
     * 주민등록번호 복호화
     * @param encryptedRrn AES 암호화된 주민등록번호
     * @return 평문 주민등록번호
     */
    public static String decryptRrn(String encryptedRrn){
        try {
            String AES_KEY_RRN = System.getenv("AES_KEY_RRN");
            SecretKeySpec key = new SecretKeySpec(AES_KEY_RRN.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = Base64.getDecoder().decode(encryptedRrn);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("주민등록번호 복호화 실패", e);
        }
    }


    //비밀번호 원문 + 해시 번들
    @Getter
    @AllArgsConstructor
    public static class PasswordBundle{
        private final String raw;
        private final String hashed;

    }
}
