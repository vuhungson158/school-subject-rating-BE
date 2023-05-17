//package kiis.edu.rating.features.user;
//
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//@AllArgsConstructor
//public class UserService implements UserDetailsService {
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<UserEntity> user = userRepository.findByEmail(username);
//        if (!user.isPresent())
//            throw new UsernameNotFoundException(String.format("UserName %s not found", username));
//        return new LoginDetail(user.get(), passwordEncoder);
//    }
//}
