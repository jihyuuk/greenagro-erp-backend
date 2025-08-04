package erp.greenagro.greenagro_erp_backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class RrnCryptoUtil {

    private final SecretKey AES_KEY;

    public RrnCryptoUtil(@Value("${spring.rrn.aes-key}") String aseKey) {
        this.AES_KEY = new SecretKeySpec(aseKey.getBytes(), "AES");
    }

    /**
     * 주민등록번호 암호화
     * @param rrn 평문 주민등록번호
     * @return AES 암호화된 주민등록번호
     */
    public String encryptRrn(String rrn){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, AES_KEY);
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
    public String decryptRrn(String encryptedRrn){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, AES_KEY);
            byte[] decoded = Base64.getDecoder().decode(encryptedRrn);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("주민등록번호 복호화 실패", e);
        }
    }

    /**
     * 주문등록번호 평문과 암호문이 일치하는지 확인
     * @param rawRrn 평문 주민등록번호
     * @param encryptedRrn 암호화된 주민등록 번호
     * @return
     */
    public boolean matches(String rawRrn, String encryptedRrn){
        return decryptRrn(encryptedRrn).equals(rawRrn);
    }

}
