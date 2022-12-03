package kiis.edu.rating.features.user;

import io.jsonwebtoken.Jwts;
import kiis.edu.rating.features.common.BaseResponse;
import kiis.edu.rating.helper.Constant;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static kiis.edu.rating.helper.Constant.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = PATH + "/User")
public class UserController {
    private final UserRepository userRepository;

    @PostMapping("/login")
    String login(@RequestBody LoginDTO loginDTO) {
        Optional<UserEntity> userEntity =
                userRepository.findByEmailAndPassword(loginDTO.username, loginDTO.password);
        if (!userEntity.isPresent()) throw new IllegalArgumentException("Email or Password is incorrect");

        String token = Jwts.builder()
                .setSubject(userEntity.get().email)
                .claim(CLAIM_AUTHORITY, userEntity.get().role.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                .signWith(ENCODED_SECRET_KEY)
                .compact();

        return BEARER + token;
    }

    @AllArgsConstructor
    private static class LoginDTO {
        public final String username, password;
    }
}
