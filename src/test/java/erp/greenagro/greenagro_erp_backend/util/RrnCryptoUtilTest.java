package erp.greenagro.greenagro_erp_backend.util;

import org.junit.jupiter.api.Test;

import static erp.greenagro.greenagro_erp_backend.util.RrnCryptoUtil.*;
import static org.junit.jupiter.api.Assertions.*;


class RrnCryptoUtilTest {

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