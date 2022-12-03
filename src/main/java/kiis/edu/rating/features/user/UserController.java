package kiis.edu.rating.features.user;

import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
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
    private String login(@RequestBody LoginRequest loginRequest) {
        Optional<UserEntity> userEntity =
                userRepository.findByEmailAndPassword(loginRequest.username, loginRequest.password);
        if (!userEntity.isPresent()) throw new IllegalArgumentException("Email or Password is incorrect");

        String token = Jwts.builder()
                .setSubject(userEntity.get().email)
                .claim(CLAIM_AUTHORITY, userEntity.get().role)
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                .signWith(ENCODED_SECRET_KEY)
                .compact();
        return BEARER + token;
    }

    @GetMapping("/{id}")
    private UserEntity getById(@PathVariable long id) {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent())
            throw new IllegalArgumentException("No user with id : " + id);
        return optionalUser.get();
    }

    @PostMapping("")
    private UserEntity registry(@RequestBody registerRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email))
            throw new IllegalArgumentException("Email have already be using");
        return userRepository.save(registerRequest.mapToUserEntity());
    }
//    {
//        "displayName": "Admin",
//        "dob": "2022-12-03T11:59:39.818Z",
//        "email": "Admin@gmail.com",
//        "gender": "male",
//        "password": "AdminPassword",
//        "role": "ADMIN"
//    }

    @AllArgsConstructor
    private static class LoginRequest {
        public final String username, password;
    }

    @AllArgsConstructor
    private static class registerRequest {
        public String email;
        public String password, displayName, gender;
        public Instant dob;
        public UserRole role;

        public UserEntity mapToUserEntity() {
            return new UserEntity(email, password, displayName, gender, dob, role);
        }
    }
}
