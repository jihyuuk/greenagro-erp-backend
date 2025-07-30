package erp.greenagro.greenagro_erp_backend.helper;

import erp.greenagro.greenagro_erp_backend.helper.PasswordHelper.PasswordBundle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PasswordHelperTest {

    @Autowired
    PasswordHelper passwordHelper;

    //임시 비밀번호 테스트
    @Test
    void 임시_비밀번호() {
        //given
        PasswordBundle passwordBundle = passwordHelper.generateTempPassword(); //임시 비밀번호 생성

        //when
        String rawPwd = passwordBundle.getRaw();
        String hashedPwd = passwordBundle.getHashed();

        //then
        assertEquals(8, rawPwd.length()); //8자리인지
        assertNotEquals(rawPwd, hashedPwd); // 해시화 되었는지
        assertTrue(passwordHelper.matches(rawPwd, hashedPwd));// 매칭 되는지
    }


    //해싱과 매칭 테스트
    @Test
    void 비밀번호_해싱_매칭() {
        //given
        String rawPwd = "이것은 테스트 비밀번호지롱";

        //when
        String hashedPwd = passwordHelper.encode(rawPwd); //해싱

        //then
        assertNotEquals(rawPwd, hashedPwd); // 해시화 되었는지
        assertTrue(passwordHelper.matches(rawPwd, hashedPwd));//평문과 해시된거 매칭되는지 테스트
    }

}