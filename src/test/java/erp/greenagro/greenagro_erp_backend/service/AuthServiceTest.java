package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.auth.LoginRequest;
import erp.greenagro.greenagro_erp_backend.dto.auth.TokenBundle;
import erp.greenagro.greenagro_erp_backend.exception.CustomException;
import erp.greenagro.greenagro_erp_backend.model.entity.Employee;
import erp.greenagro.greenagro_erp_backend.model.enums.AccountStatus;
import erp.greenagro.greenagro_erp_backend.model.enums.Role;
import erp.greenagro.greenagro_erp_backend.repository.EmployeeRepository;
import erp.greenagro.greenagro_erp_backend.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static erp.greenagro.greenagro_erp_backend.model.enums.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired AuthService authService;
    @Autowired RefreshTokenRedisService refreshTokenRedisService;
    @Autowired EmployeeRepository employeeRepository;
    @Autowired JwtUtil jwtUtil;
    @Autowired PasswordEncoder passwordEncoder;

    //로그인테스트
    //  1.정상로그인
    //  2.아이디 or 비번 틀린 경우
    //  3.정지당한 경우
    @Test
    void 로그인_정상() {
        //given
        Employee employee = getMockEmployee("홍길동", "1234", Role.MANAGER, AccountStatus.ACTIVE);
        LoginRequest request = new LoginRequest("홍길동", "1234");


        //when
        TokenBundle tokenBundle = authService.login(request);
        String accessToken = tokenBundle.getAccessToken();
        String refreshToken = tokenBundle.getRefreshToken();

        //then
        assertNotNull(accessToken); //토큰 반환 null 판별
        assertNotNull(refreshToken);
        assertTrue(jwtUtil.validateToken(accessToken)); //토큰 유효성 검사
        assertTrue(jwtUtil.validateToken(refreshToken));
        //Redis 에 refresh 토큰 들어갔나 확인
        assertEquals(refreshToken, refreshTokenRedisService.get(jwtUtil.getUserId(refreshToken), jwtUtil.getDeviceId(refreshToken)).get());
    }


    @Test
    void 로그인_아이디_또는_비번_틀림() {
        //given
        Employee employee = getMockEmployee("홍길동", "1234", Role.MANAGER, AccountStatus.ACTIVE);
        LoginRequest userNameRequest = new LoginRequest("김길동", "1234");
        LoginRequest passwordRequest = new LoginRequest("홍길동", "12345");

        //when
        CustomException nameEx = assertThrows(CustomException.class, () -> authService.login(userNameRequest));
        CustomException pwdEx = assertThrows(CustomException.class, () -> authService.login(passwordRequest));

        //then
        assertEquals(INVALID_CREDENTIALS, nameEx.getErrorCode());
        assertEquals(INVALID_CREDENTIALS, pwdEx.getErrorCode());
    }


    @Test
    void 로그인_재직중_아닐때() {
        //given
        Employee employee = getMockEmployee("홍길동", "1234", Role.MANAGER, AccountStatus.ON_LEAVE);
        LoginRequest loginRequest = new LoginRequest("홍길동", "1234");

        //when
        CustomException ex = assertThrows(CustomException.class, () -> authService.login(loginRequest));

        //then
        assertEquals(INVALID_CREDENTIALS, ex.getErrorCode());
    }


    //토큰갱신
    //  1.정상갱신
    //  2.직원상태가 재직중이 아니면 갱신 x
    //  3.중간에 이름과 권한이 바뀌면 갱신되는지
    //  4.rtr 위험감지시에 redis에서 지워지는지
    @Test
    void 토큰갱신_정상(){
        //given
        Employee employee = getMockEmployee("홍길동", "123", Role.MANAGER, AccountStatus.ACTIVE);
        String deviceId = UUID.randomUUID().toString();
        String refreshToken = jwtUtil.generateRefreshToken(employee.getId(), deviceId);
        //Redis에 저장
        refreshTokenRedisService.save(employee.getId(), deviceId, refreshToken);

        //when
        TokenBundle tokenBundle = authService.refresh(refreshToken);
        String newAccessToken = tokenBundle.getAccessToken();
        String newRefreshToken = tokenBundle.getRefreshToken();

        //then
        assertNotNull(newAccessToken); //토큰 반환 null 판별
        assertNotNull(newRefreshToken);
        assertTrue(jwtUtil.validateToken(newAccessToken)); //토큰 유효성 검사
        assertTrue(jwtUtil.validateToken(newRefreshToken));
        //Redis 에 refresh 토큰 들어갔나 확인
        assertEquals(newRefreshToken, refreshTokenRedisService.get(employee.getId(), deviceId).get());
    }


    @Test
    void 토큰갱신_직원_재직중x(){
        //given
        Employee employee = getMockEmployee("홍길동", "123", Role.MANAGER, AccountStatus.ACTIVE);
        String deviceId = UUID.randomUUID().toString();
        String refreshToken = jwtUtil.generateRefreshToken(employee.getId(), deviceId);
        //Redis에 저장
        refreshTokenRedisService.save(employee.getId(), deviceId, refreshToken);

        //when - 계정 상태 변경
        updateMockEmployee(employee, employee.getName(), employee.getRole(), AccountStatus.RESIGNED);

        //then
        CustomException ex = assertThrows(CustomException.class, () -> authService.refresh(refreshToken));
        assertEquals(TOKEN_REFRESH_FAILED, ex.getErrorCode());
    }


    @Test
    void 토큰갱신_이름_권한_변경시(){
        //given
        Employee employee = getMockEmployee("홍길동", "123", Role.MANAGER, AccountStatus.ACTIVE);
        String deviceId = UUID.randomUUID().toString();
        String refreshToken = jwtUtil.generateRefreshToken(employee.getId(), deviceId);

        //Redis에 저장
        refreshTokenRedisService.save(employee.getId(), deviceId, refreshToken);

        //when
        String newName = "홍길동2";
        Role newRole = Role.ADMIN;

        //계정 상태 변경
        updateMockEmployee(employee, newName, newRole, AccountStatus.ACTIVE);
        TokenBundle tokenBundle = authService.refresh(refreshToken);

        String newAccessToken = tokenBundle.getAccessToken();
        String newRefreshToken = tokenBundle.getRefreshToken();

        //then
        assertEquals(newName, jwtUtil.getUserName(newAccessToken));
        assertEquals(newRole, jwtUtil.getRole(newAccessToken));
        assertEquals(employee.getId(), jwtUtil.getUserId(newRefreshToken));
    }


    @Test
    void 토큰갱신_위험감지() throws InterruptedException {
        //given
        Employee employee = getMockEmployee("홍길동", "123", Role.MANAGER, AccountStatus.ACTIVE);
        String deviceId = UUID.randomUUID().toString();
        String firstRefreshToken = jwtUtil.generateRefreshToken(employee.getId(), deviceId);

        //Redis에 저장
        refreshTokenRedisService.save(employee.getId(), deviceId, firstRefreshToken);

        Thread.sleep(1000);

        //토큰 갱신
        String secondRefreshToken = authService.refresh(firstRefreshToken).getRefreshToken();


        //when - 이전 토큰으로 갱신 요청시 위험감지
        CustomException ex = assertThrows(CustomException.class, () -> authService.refresh(firstRefreshToken));


        //then
        assertEquals(TOKEN_REFRESH_FAILED, ex.getErrorCode());
        assertTrue(refreshTokenRedisService.get(employee.getId(), deviceId).isEmpty()); //Redis에서 리프레시 토큰 삭제
    }


    //로그아웃
    // 1.로그인시에 Redis에 refreshToken 존재
    // 2.로그아웃 후 Redis에 refreshToken 삭제
    @Test
    void 로그아웃() {
        //given
        //리프레시 토큰 생성
        Long userId = 1L;
        String deviceId = UUID.randomUUID().toString();
        String refreshToken = jwtUtil.generateRefreshToken(userId, deviceId);

        //Redis에 저장
        refreshTokenRedisService.save(userId, deviceId, refreshToken);


        //when - 로그아웃
        authService.logout(refreshToken);


        //then - Redis 에서 지워졌는지
        assertTrue(refreshTokenRedisService.get(userId, deviceId).isEmpty());
    }




    //mock 직원 생성
    Employee getMockEmployee(String name, String password, Role role, AccountStatus status) {
        //직원생성
        Employee employee = new Employee(
                null,
                name,
                passwordEncoder.encode(password),
                "주민번호",
                "과장",
                "010-1111-2222",
                "hong@example.com",
                "서울시 강남구",
                LocalDate.of(2020, 1, 15),
                role,
                status,
                null
        );

        return employeeRepository.save(employee);
    }

    //mock 직원 (이름, 권한, 상태) 업데이트
    void updateMockEmployee(Employee employee, String newName, Role newRole, AccountStatus newSate){
        employee.update(
                employee.getBranch(),
                newName, //이름
                employee.getRrn(),
                employee.getPosition(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getAddress(),
                employee.getHireDate(),
                employee.getResignDate(),
                newRole, //권한
                newSate //상태
        );
    }
}