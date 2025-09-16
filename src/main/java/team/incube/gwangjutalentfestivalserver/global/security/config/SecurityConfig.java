package team.incube.gwangjutalentfestivalserver.global.security.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import team.incube.gwangjutalentfestivalserver.domain.user.enums.Role;
import team.incube.gwangjutalentfestivalserver.global.security.filter.JwtFilter;
import team.incube.gwangjutalentfestivalserver.global.security.jwt.JwtProvider;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtProvider jwtProvider;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		JwtFilter jwtFilter = new JwtFilter(jwtProvider);

		return http
			.authorizeHttpRequests(it -> it
                // SSE 인증
                .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll()
				// 인증
				.requestMatchers("/auth/**").permitAll()
				// 예매
				.requestMatchers(HttpMethod.POST, "/seat").hasAnyAuthority(Role.ROLE_ADMIN.name(), Role.ROLE_USER.name(), Role.ROLE_PERFORMER.name())
				.requestMatchers(HttpMethod.DELETE, "/seat").hasAnyAuthority(Role.ROLE_ADMIN.name(), Role.ROLE_USER.name(), Role.ROLE_PERFORMER.name())
				.requestMatchers(HttpMethod.POST, "/seat/ban").hasAuthority(Role.ROLE_ADMIN.name())
				.requestMatchers(HttpMethod.DELETE, "/seat/ban").hasAuthority(Role.ROLE_ADMIN.name())
				.requestMatchers(HttpMethod.GET, "/seat/myself").hasAnyAuthority(Role.ROLE_ADMIN.name(), Role.ROLE_USER.name())
				.requestMatchers(HttpMethod.GET, "/seat").hasAnyAuthority(Role.ROLE_ADMIN.name(), Role.ROLE_USER.name(), Role.ROLE_PERFORMER.name())
				.requestMatchers(HttpMethod.GET, "/seat/all").hasAnyAuthority(Role.ROLE_ADMIN.name(), Role.ROLE_USER.name(), Role.ROLE_PERFORMER.name())
				.requestMatchers(HttpMethod.GET, "/seat/changes").hasAnyAuthority(Role.ROLE_ADMIN.name(), Role.ROLE_USER.name(), Role.ROLE_PERFORMER.name())
                .requestMatchers(HttpMethod.GET, "/seat/myself/performer").hasAnyAuthority(Role.ROLE_PERFORMER.name())
                .requestMatchers(HttpMethod.DELETE, "/seat/performer").hasAnyAuthority(Role.ROLE_PERFORMER.name())
				// 상태 확인
				.requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
				// 공연 팀
				.requestMatchers(HttpMethod.GET, "/team").hasAnyAuthority(Role.ROLE_ADMIN.name(), Role.ROLE_USER.name())
                .requestMatchers(HttpMethod.GET, "/team/ranking").hasAuthority(Role.ROLE_ADMIN.name())
                .requestMatchers(HttpMethod.POST, "/team").hasAuthority(Role.ROLE_USER.name())
                .requestMatchers(HttpMethod.PATCH, "/team/{teamId}").hasAuthority(Role.ROLE_ADMIN.name())
				// 현장 투표
				.requestMatchers(HttpMethod.POST, "/vote").hasAnyAuthority(Role.ROLE_ADMIN.name(), Role.ROLE_USER.name())
				.requestMatchers(HttpMethod.GET, "/vote/{teamId}").hasAnyAuthority(Role.ROLE_ADMIN.name(), Role.ROLE_USER.name())
				.requestMatchers(HttpMethod.GET, "/vote/{teamId}/current").hasAuthority(Role.ROLE_ADMIN.name())
				.requestMatchers(HttpMethod.POST, "/vote/{teamId}").hasAuthority(Role.ROLE_ADMIN.name())
				.requestMatchers(HttpMethod.DELETE, "/vote/{teamId}").hasAuthority(Role.ROLE_ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/vote/{teamId}/extract").hasAuthority(Role.ROLE_ADMIN.name())
                // 전문가 심사
                .requestMatchers(HttpMethod.PATCH, "/judge/{team_id}").hasAuthority(Role.ROLE_ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/judge").hasAuthority(Role.ROLE_ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/judge/{team_id}").hasAuthority(Role.ROLE_ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/judge/changes").hasAuthority(Role.ROLE_ADMIN.name())
			)
            .cors(Customizer.withDefaults())
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
