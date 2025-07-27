package team.incube.gwangjutalentfestivalserver.domain.auth.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {
	@Pattern(
		regexp = "^01[0-9]{8,9}$",
		message = "유효한 휴대폰 번호 형식이 아닙니다."
	)
	private String phoneNumber;

	@Pattern(
		regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$",
		message = "특수문자, 영문과 숫자를 포함한 8자리 이상의 비밀번호를 만들어주세요."
	)
	private String password;

	private String code;
}