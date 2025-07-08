package team.incube.gwangjutalentfestivalserver.domain.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import team.incube.gwangjutalentfestivalserver.domain.user.enums.Role;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VerifyCount {
	@Id
	@Column(nullable = false)
	private String phoneNumber;

	@Column(nullable = false)
	private Integer count;

	public void incrementCount(){
		count++;
	}
}
