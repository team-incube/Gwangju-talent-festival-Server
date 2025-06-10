package team.incube.gwangjutalentfestivalserver.global.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import team.incube.gwangjutalentfestivalserver.domain.user.enums.Role;
import team.incube.gwangjutalentfestivalserver.global.properties.CorsProperties;
import team.incube.gwangjutalentfestivalserver.global.security.filter.JwtFilter;
import team.incube.gwangjutalentfestivalserver.global.security.jwt.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtProvider jwtProvider;
	private final CorsProperties corsProperties;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		JwtFilter jwtFilter = new JwtFilter(jwtProvider);

		return http
			.authorizeHttpRequests(it -> it
				// 인증
				.requestMatchers("/auth/**").permitAll()
				// 사용자
				.requestMatchers(HttpMethod.GET, "/user/**").hasAuthority(Role.ROLE_USER.name())
				.requestMatchers(HttpMethod.DELETE, "/user/withdraw").hasAuthority(Role.ROLE_USER.name())
				// 상태 확인
				.requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
			)
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement (it ->
				it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}
}
