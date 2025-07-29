package erp.greenagro.greenagro_erp_backend.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static erp.greenagro.greenagro_erp_backend.util.SecurityUtil.*;
import static org.junit.jupiter.api.Assertions.*;


class SecurityUtilTest {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    //비밀번호 8자리인지
    @Test
    void 비밀번호_8자리() {
        //given
        PasswordBundle passwordBundle = generateTempPassword(); //임시 비밀번호 생성

        //when
        String rawPwd = passwordBundle.getRaw();
        String hashedPwd = passwordBundle.getHashed();

        //then
        assertEquals(8, rawPwd.length()); //8자리인지
        assertNotEquals(rawPwd, hashedPwd); // 해시화 되었는지
    }


    //해시된 비밀번호가 임시 비밀번호와 매치되는지
    @Test
    void 비밀번호_매칭() {
        //given
        PasswordBundle passwordBundle = generateTempPassword(); //임시 비밀번호 생성

        //when
        String rawPwd = passwordBundle.getRaw();
        String hashedPwd = passwordBundle.getHashed();

        //then
        assertTrue(encoder.matches(rawPwd, hashedPwd));//평문과 해시된거 매칭되는지 테스트
    }


    //주민등록번호 암호화 복호화 테스트
    @Test
    void 주민등록번호_암호화_복호화() {
        //given
        String rawRrn = "123456-1234567"; //평문 주민번호

        //when
        String encryptRrn = encryptRrn(rawRrn); //주민번호 암호화
        String decryptRrn = decryptRrn(encryptRrn); //주민번호 복호화

        //then
        assertNotEquals(rawRrn, encryptRrn);//암호화 되었는지 판별
        assertEquals(rawRrn, decryptRrn);//복호화 되었는지 판별
    }

}