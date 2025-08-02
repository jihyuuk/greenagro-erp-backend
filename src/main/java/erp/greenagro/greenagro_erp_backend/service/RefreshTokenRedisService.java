package erp.greenagro.greenagro_erp_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenRedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PREFIX = "refreshToken:";
    private static final Duration REFRESH_TOKEN_EXP = Duration.ofDays(7); // 7일 따로 고려


    //저장
    public void save(Long id, String refreshToken){
        redisTemplate.opsForValue().set(
                buildKey(id),
                refreshToken,
                REFRESH_TOKEN_EXP
        );
    }


    //조회
    public Optional<String> get(Long id){
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(buildKey(id))
        );
    }


    //삭제
    public void delete(Long id){
        redisTemplate.delete(buildKey(id));
    }


    //재사용 확인
    public boolean isReused(Long id, String token){
        return get(id).map(stored ->
                !stored.equals(token)) //저장된 토큰과 다르면 재사용 되었다 판단
                .orElse(true); //저장된 토큰이 없으면 일단 true
    }



    private String buildKey(Long id){
        return PREFIX + id; // ex) refreshToken:3
    }
}
