package team.incube.gwangjutalentfestivalserver.global.redis.config;

import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
class RedisConfig {
	@Bean
	LettuceConnectionFactory connectionFactory(
		@Value("${spring.data.redis.host}") String host,
		@Value("${spring.data.redis.port}") int port,
		@Value("${spring.data.redis.password:}") String password
	){
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
		if (!password.isBlank()) config.setPassword(password);
		return new LettuceConnectionFactory(config);
	}

	@Bean
	RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		val serializer = new StringRedisSerializer();
		template.setKeySerializer(serializer);
		template.setValueSerializer(serializer);
		template.setHashKeySerializer(serializer);
		template.setHashValueSerializer(serializer);

		return template;
	}
}
