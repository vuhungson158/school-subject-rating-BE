package kiis.edu.rating.features.user;

import io.jsonwebtoken.Jwts;
import kiis.edu.rating.enums.Gender;
import kiis.edu.rating.exception.LoginException;
import kiis.edu.rating.features.common.BaseEntity;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kiis.edu.rating.helper.Constant.*;

@SuppressWarnings("unused")
@AllArgsConstructor
@RestController
@RequestMapping(path = "/user")
public class UserController {
    private final UserRepository userRepository;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        UserEntity userEntity = userRepository.findByEmail(loginRequest.email)
                .orElseThrow(() -> new LoginException("Email is not correct"));
        if (!userEntity.password.equals(loginRequest.password))
            throw new LoginException("Password is not correct");

        String token = Jwts.builder()
                .setSubject(userEntity.email)
                .claim(CLAIM_AUTHORITY, userEntity.role)
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                .signWith(ENCODED_SECRET_KEY)
                .compact();
        return new LoginResponse(userEntity, BEARER + token);
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
        return all.stream()
                .map(userEntity -> Util.mapping(userEntity, SimpleUserInfo.class))
                .collect(Collectors.toList());
    }

    @PostMapping("")
    public boolean createNewAcc(@RequestBody Request request) {
        if (userRepository.existsByEmail(request.email))
            throw new IllegalArgumentException("Email have already be using");
        userRepository.save(request.toEntity());
        return true;
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable long id, @RequestBody Request request) {
        if (!userRepository.existsById(id))
            throw new IllegalArgumentException("No User with Id: " + id);
        UserEntity userEntity = request.toEntity();
        userEntity.id = id;
        userRepository.save(userEntity);
        return true;
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable long id) {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent())
            throw new IllegalArgumentException("No User with Id: " + id);
        UserEntity userEntity = optionalUser.get();
        userEntity.disable = true;
        userRepository.save(userEntity);
        return true;
    }

    @AllArgsConstructor
    private static class LoginRequest {
        public final String email, password;
    }

    @AllArgsConstructor
    private static class Request {
        public String email;
        public String password, displayName;
        public Gender gender;
        public Instant dob;
        public UserRole role;

        public UserEntity toEntity() {
            return Util.mapping(this, UserEntity.class);
        }
    }

    @AllArgsConstructor
    private static class LoginResponse {
        public UserEntity user;
        public String token;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    private static class SimpleUserInfo extends BaseEntity {
        public String displayName;
        public Gender gender;
        public UserRole role;
    }
}
