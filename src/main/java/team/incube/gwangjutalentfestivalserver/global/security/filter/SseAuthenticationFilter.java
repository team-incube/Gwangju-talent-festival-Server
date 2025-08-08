package team.incube.gwangjutalentfestivalserver.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import team.incube.gwangjutalentfestivalserver.global.security.jwt.JwtProvider;

import java.io.IOException;

public class SseAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private static final String SSE_URI = "/seat/changes";

    public SseAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (isSseConnectionRequest(request)) {
            String authHeader = request.getHeader("Authorization");
            String token = jwtProvider.resolveToken(authHeader);

            if (token == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            try {
                UsernamePasswordAuthenticationToken authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
        }

        filterChain.doFilter(request, response);
    }

    private boolean isSseConnectionRequest(HttpServletRequest request) {
        return "GET".equalsIgnoreCase(request.getMethod()) && request.getRequestURI().equals(SSE_URI);
    }
}
