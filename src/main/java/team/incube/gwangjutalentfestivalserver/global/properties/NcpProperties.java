package team.incube.gwangjutalentfestivalserver.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "thirdparty.ncp")
public class NcpProperties {
    private String serviceId;
    private String accessKey;
    private String secretKey;
    private String phoneNumber;
}


