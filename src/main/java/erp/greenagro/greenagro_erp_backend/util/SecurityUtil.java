package erp.greenagro.greenagro_erp_backend.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class SecurityUtil {

    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * 임시 비밀번호 랜덤 생성
     * @param length 생성할 자리수 (1 ~ 10자리)
     * @return 숫자로만 구성된 임시 비밀번호
     */
    public static String generateTempPassword(int length){
        //범위 판별
        if(length <= 0 || length >10){
            throw new IllegalArgumentException("임시 비밀번호는 1 ~ 10 자리여야 합니다.");
        }

        //생성
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(secureRandom.nextInt(10)); //0~9까지
        }

        return sb.toString();
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
}
