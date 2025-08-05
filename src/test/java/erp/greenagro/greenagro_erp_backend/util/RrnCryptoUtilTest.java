package erp.greenagro.greenagro_erp_backend.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class RrnCryptoUtilTest {

    @Autowired
    RrnCryptoUtil rrnCryptoUtil;


    //주민등록번호 암호화 복호화 테스트
    @Test
    void 주민등록번호_암호화_복호화() {
        //given
        String rawRrn = "123456-1234567"; //평문 주민번호

        //when
        String encryptRrn = rrnCryptoUtil.encryptRrn(rawRrn); //주민번호 암호화
        String decryptRrn = rrnCryptoUtil.decryptRrn(encryptRrn); //주민번호 복호화

        //then
        assertNotEquals(rawRrn, encryptRrn);//암호화 되었는지 판별
        assertEquals(rawRrn, decryptRrn);//복호화 되었는지 판별
    }

    //매칭 메소드 테스트
    @Test
    void 주민등록번호_매칭() {
        //given
        String rawRrn = "123456-1234567"; //평문 주민번호

        //when
        String encryptRrn = rrnCryptoUtil.encryptRrn(rawRrn); //주민번호 암호화

        //then
        assertTrue(rrnCryptoUtil.matches(rawRrn, encryptRrn)); //매칭확인
    }

}