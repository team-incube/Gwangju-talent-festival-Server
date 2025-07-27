package team.incube.gwangjutalentfestivalserver.domain.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.auth.dto.request.LoginRequest;
import team.incube.gwangjutalentfestivalserver.domain.auth.dto.response.LoginResponse;
import team.incube.gwangjutalentfestivalserver.domain.auth.entity.RefreshToken;
import team.incube.gwangjutalentfestivalserver.domain.auth.repository.RefreshTokenRepository;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import team.incube.gwangjutalentfestivalserver.domain.user.repository.UserRepository;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;
import team.incube.gwangjutalentfestivalserver.global.security.jwt.JwtProvider;
import team.incube.gwangjutalentfestivalserver.global.security.jwt.JwtType;
import team.incube.gwangjutalentfestivalserver.global.security.jwt.dto.JwtDetails;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginUsecase {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	public LoginResponse execute(LoginRequest loginRequest) {
		String phoneNumber = loginRequest.getPhoneNumber();
		User user = userRepository.findByPhoneNumber(phoneNumber)
				.orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."));

		String rawPassword = loginRequest.getPassword();
		String encodedPassword = user.getEncodedPassword();

		if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new HttpException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
		}

		JwtDetails accessToken = jwtProvider.generateToken(user.getId().toString(), JwtType.ACCESS_TOKEN);
		JwtDetails refreshToken = initialRefreshToken(user.getId());

		return new LoginResponse(
				accessToken.getToken(),
				accessToken.getExpiredAt(),
				refreshToken.getToken(),
				refreshToken.getExpiredAt()
		);
	}

	public JwtDetails initialRefreshToken(UUID id) {
		refreshTokenRepository.deleteById(id);

		JwtDetails newRefreshToken = jwtProvider.generateToken(id.toString(), JwtType.REFRESH_TOKEN);
		RefreshToken tokenEntity = new RefreshToken(id, newRefreshToken.getToken(), jwtProvider.getRefreshTokenExpires());
		refreshTokenRepository.save(tokenEntity);

		return newRefreshToken;
	}
}
