package erp.greenagro.greenagro_erp_backend.filter;

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

        //2.JWT 토큰 존재 여부 확인
        if(headerAuth != null && headerAuth.startsWith("Bearer ")){
            String token = headerAuth.substring(7);

            if(jwtUtil.validateToken(token)){
                //2-1.토큰에서 claims 꺼내기
                Long userId = jwtUtil.getUserId(token);
                String userName = jwtUtil.getUserName(token);
                Role role = jwtUtil.getRole(token);

                //2-2.인증 객체 생성
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, null,
                        List.of(new SimpleGrantedAuthority(role.name())));

                //2-3.SecurityContext 등록
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }

        //3.필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }
}
