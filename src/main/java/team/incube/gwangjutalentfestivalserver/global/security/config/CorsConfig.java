package team.incube.gwangjutalentfestivalserver.global.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.incube.gwangjutalentfestivalserver.global.properties.CorsProperties;

@Configuration
@RequiredArgsConstructor
public class CorsConfig implements WebMvcConfigurer {
    private final CorsProperties corsProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowedOrigins(corsProperties.getAllowedOrigins().toArray(new String[0]))
            .allowedHeaders("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .exposedHeaders("Authorization", "Refresh-Token")
            .allowCredentials(true);
    }
}