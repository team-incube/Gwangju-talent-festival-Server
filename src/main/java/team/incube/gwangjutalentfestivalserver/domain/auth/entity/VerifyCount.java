package team.incube.gwangjutalentfestivalserver.domain.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("verify-count")
public class VerifyCount {
	@Id
	private String phoneNumber;

	private Integer count;

	public void incrementCount(){
		if(count == null) count = 0;
		count++;
	}
}
