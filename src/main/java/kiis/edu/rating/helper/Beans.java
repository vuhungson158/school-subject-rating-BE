package kiis.edu.rating.helper;

import kiis.edu.rating.security.JwtTokenVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Beans {
    @Bean()
    public static JwtTokenVerifier jwtTokenVerifier(){
        return new JwtTokenVerifier();
    }

    @Bean()
    public PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder(10);
    }

}
