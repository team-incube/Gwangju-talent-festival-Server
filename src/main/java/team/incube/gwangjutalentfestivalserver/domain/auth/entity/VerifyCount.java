package team.incube.gwangjutalentfestivalserver.domain.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCount {
	@Id
	private String phoneNumber;

	@Column(nullable = false)
	private Integer count;

	public void incrementCount(){
		count++;
	}
}
