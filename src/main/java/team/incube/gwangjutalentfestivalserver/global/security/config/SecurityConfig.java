package team.incube.gwangjutalentfestivalserver.global.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import team.incube.gwangjutalentfestivalserver.domain.user.enums.Role;
import team.incube.gwangjutalentfestivalserver.global.security.filter.JwtFilter;
import team.incube.gwangjutalentfestivalserver.global.security.filter.SseAuthenticationFilter;
import team.incube.gwangjutalentfestivalserver.global.security.jwt.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtProvider jwtProvider;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		JwtFilter jwtFilter = new JwtFilter(jwtProvider);
		SseAuthenticationFilter sseAuthenticationFilter = new SseAuthenticationFilter(jwtProvider);

		return http
			.authorizeHttpRequests(it -> it
				// 인증
				.requestMatchers("/auth/**").permitAll()
				// 예매
				.requestMatchers(HttpMethod.POST, "/seat").hasAnyAuthority(Role.ROLE_ADMIN.name(), Role.ROLE_USER.name())
				.requestMatchers(HttpMethod.DELETE, "/seat").hasAnyAuthority(Role.ROLE_ADMIN.name(), Role.ROLE_USER.name())
				.requestMatchers(HttpMethod.POST, "/seat/ban").hasAuthority(Role.ROLE_ADMIN.name())
				.requestMatchers(HttpMethod.DELETE, "/seat/ban").hasAuthority(Role.ROLE_ADMIN.name())
				.requestMatchers(HttpMethod.GET, "/seat/myself").hasAnyAuthority(Role.ROLE_ADMIN.name(), Role.ROLE_USER.name())
				.requestMatchers(HttpMethod.GET, "/seat").hasAnyAuthority(Role.ROLE_ADMIN.name(), Role.ROLE_USER.name())
				.requestMatchers(HttpMethod.GET, "/seat/all").hasAnyAuthority(Role.ROLE_ADMIN.name(), Role.ROLE_USER.name())
				.requestMatchers(HttpMethod.GET, "/seat/changes").hasAnyAuthority(Role.ROLE_ADMIN.name(), Role.ROLE_USER.name())
				// 상태 확인
				.requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
				// 공연 팀
				.requestMatchers(HttpMethod.GET, "/team").permitAll()
			)
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement (it ->
				it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.addFilterBefore(sseAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}
}
