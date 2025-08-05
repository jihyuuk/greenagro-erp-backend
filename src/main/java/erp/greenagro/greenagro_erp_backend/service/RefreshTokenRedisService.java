package erp.greenagro.greenagro_erp_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class RefreshTokenRedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final Duration REFRESH_TOKEN_EXP; // 7일

    public RefreshTokenRedisService(RedisTemplate<String, String> redisTemplate, @Value("${spring.jwt.refresh-exp}") Duration refreshTokenExp) {
        this.redisTemplate = redisTemplate;
        this.REFRESH_TOKEN_EXP = refreshTokenExp;
    }


    //저장
    public void save(Long userId, String deviceId, String refreshToken){
        redisTemplate.opsForValue().set(
                buildKey(userId, deviceId),
                refreshToken,
                REFRESH_TOKEN_EXP
        );
    }


    //조회
    public Optional<String> get(Long userId, String deviceId){
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(buildKey(userId, deviceId))
        );
    }


    //삭제
    public void delete(Long userId, String deviceId){
        redisTemplate.delete(buildKey(userId, deviceId));
    }


    //재사용 확인
    public boolean isReused(Long userId, String deviceId, String refreshToken){
        return get(userId, deviceId).map(stored ->
                !stored.equals(refreshToken)) //저장된 토큰과 다르면 재사용 되었다 판단
                .orElse(true); //저장된 토큰이 없으면 일단 true
    }



    private String buildKey(Long userId, String deviceId){
        //Redis 키 구조 "refresh:{userId}:{deviceId}"
        return String.format("refreshToken:%s:%s", userId, deviceId);
    }
}
