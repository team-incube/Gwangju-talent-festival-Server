package team.incube.gwangjutalentfestivalserver.domain.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.auth.dto.request.JoinRequest;
import team.incube.gwangjutalentfestivalserver.domain.auth.entity.VerifyCode;
import team.incube.gwangjutalentfestivalserver.domain.auth.repository.VerifyCodeRepository;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import team.incube.gwangjutalentfestivalserver.domain.user.enums.Role;
import team.incube.gwangjutalentfestivalserver.domain.user.repository.UserRepository;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;

@Service
@RequiredArgsConstructor
public class JoinUsecase {
	private final UserRepository userRepository;
	private final VerifyCodeRepository verifyCodeRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void execute(JoinRequest request) {
		String phoneNumber = request.getPhoneNumber();

		if (userRepository.existsByPhoneNumber(phoneNumber)) {
			throw new HttpException(HttpStatus.BAD_REQUEST, "이미 등록된 전화번호입니다.");
		}

		String encodedPassword = passwordEncoder.encode(request.getPassword());

		VerifyCode verifyCode = verifyCodeRepository.findByPhoneNumber(phoneNumber)
			.orElseThrow(() ->
				new HttpException(HttpStatus.BAD_REQUEST, "전송된 인증번호를 찾을 수 없습니다.")
			);

		if(!request.getCode().equals(verifyCode.getCode())){
			throw new HttpException(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다.");
		}

		verifyCodeRepository.delete(verifyCode);

		User user = User.builder()
				.phoneNumber(request.getPhoneNumber())
				.encodedPassword(encodedPassword)
				.role(Role.ROLE_USER)
				.build();

		userRepository.save(user);
	}
}
