package team.incube.gwangjutalentfestivalserver.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.incube.gwangjutalentfestivalserver.domain.user.enums.Role;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
	private String accessToken;
	private LocalDateTime accessTokenExpiredAt;
	private String refreshToken;
	private LocalDateTime refreshTokenExpiredAt;
    private Role role;
}