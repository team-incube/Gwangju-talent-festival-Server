package team.incube.gwangjutalentfestivalserver.global.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomUtil {
    public String generateRandomCode(int length){
        String characters = "0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for(int i = 0; i < length; i++){
            code.append(characters.charAt(random.nextInt(characters.length())));
        }

        return code.toString();
    }
}
