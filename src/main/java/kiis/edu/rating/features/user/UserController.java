package kiis.edu.rating.features.user;

import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kiis.edu.rating.helper.Constant.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = PATH + "/User")
public class UserController {
    private final UserRepository userRepository;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
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
    public UserEntity getById(@PathVariable long id) {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent())
            throw new IllegalArgumentException("No user with id : " + id);
        return optionalUser.get();
    }

    @GetMapping("")
    public List<SimpleUserInfo> getSimpleList() {
        List<UserEntity> all = userRepository.findAll();
        return all.stream().map(SimpleUserInfo::new).collect(Collectors.toList());
    }

    @PostMapping("")
    public boolean createNewAcc(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email))
            throw new IllegalArgumentException("Email have already be using");
        userRepository.save(registerRequest.mapToUserEntity());
        return true;
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable long id) {
        userRepository.deleteById(id);
        return true;
    }

    @AllArgsConstructor
    private static class LoginRequest {
        public final String username, password;
    }
    @AllArgsConstructor
    private static class RegisterRequest {
        public String email;
        public String password, displayName, gender;
        public Instant dob;
        public UserRole role;
        public UserEntity mapToUserEntity() {
            return new UserEntity(email, password, displayName, gender, dob, role);
        }
    }
    @AllArgsConstructor
    private static class SimpleUserInfo {
        public long id;
        public String displayName, gender;
        public UserRole role;
        public SimpleUserInfo(UserEntity userEntity) {
            this.id = userEntity.id;
            this.displayName = userEntity.displayName;
            this.gender = userEntity.gender;
            this.role = userEntity.role;
        }
    }
}
