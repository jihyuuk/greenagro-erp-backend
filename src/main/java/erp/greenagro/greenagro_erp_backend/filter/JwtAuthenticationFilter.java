package erp.greenagro.greenagro_erp_backend.filter;

import ch.qos.logback.core.util.StringUtil;
import erp.greenagro.greenagro_erp_backend.model.enums.Role;
import erp.greenagro.greenagro_erp_backend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1.요청헤더에서 Authorization 추출
        String headerAuth = request.getHeader("Authorization");

        //2.유효한 JWT 토큰 존재 여부 확인
        if (hasValidToken(headerAuth)) {
            //2-1.'Bearer ' 접두사 제거
            String token = headerAuth.substring(7);

            //2-2.토큰에서 claims 꺼내기
            Long userId = jwtUtil.getUserId(token);
            String userName = jwtUtil.getUserName(token); //나중에 userDetail
            Role role = jwtUtil.getRole(token);

            //2-3.인증 객체 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority(role.name())));

            //2-4.SecurityContext 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        //3.필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }




    //요청헤더에 유효한 jwt 토큰이 있는지 검증
    private boolean hasValidToken(String headerAuth) {
        //1.요청헤더에 Authorization 있어야함
        //2.값이 'Bearer '로 시작해야함
        //3.jwtUtil 의 검증을 통과해야함
        return StringUtils.hasText(headerAuth)
                && headerAuth.startsWith("Bearer ")
                && jwtUtil.validateToken(headerAuth.substring(7));
    }
}
