package team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;
import team.incube.gwangjutalentfestivalserver.global.properties.NcpProperties;
import team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.dto.SendSmsRequest;
import team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.dto.SendSmsResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class SmsAdapter {
    private final NcpProperties ncpProperties;

    public SendSmsResponse sendSms(SendSmsRequest request) {
        WebClient webClient = WebClient.builder().build();
        String time = String.valueOf(System.currentTimeMillis());

        try {
            String signature = makeSignature();
            SendSmsResponse response = webClient.post()
                .uri("https://sens.apigw.ntruss.com/sms/v2/services/" + ncpProperties.getServiceId() + "/messages")
                .header("Content-Type", "application/json; charset=utf-8")
                .header("x-ncp-apigw-timestamp", time)
                .header("x-ncp-iam-access-key", ncpProperties.getAccessKey())
                .header("x-ncp-apigw-signature-v2", signature)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(SendSmsResponse.class)
                .block();

            return response;
        } catch (Exception e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "문자 전송 프로세스에서 문제가 발생했습니다.");
        }
    }

    public String makeSignature() throws NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/" + ncpProperties.getServiceId() + "/messages";
        String timestamp = String.valueOf(System.currentTimeMillis());

        String message = method +
            space +
            url +
            newLine +
            timestamp +
            newLine +
            ncpProperties.getAccessKey();

        SecretKeySpec signingKey = new SecretKeySpec(ncpProperties.getSecretKey().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(rawHmac);
    }
}
