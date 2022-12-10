package kiis.edu.rating.features.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Getter
public class LoginDetail implements UserDetails {
    private final String username;
    private final String password;
    private final Set<? extends GrantedAuthority> authorities;
    private final boolean isAccountNonExpired = true;
    private final boolean isAccountNonLocked = true;
    private final boolean isCredentialsNonExpired = true;
    private final boolean isEnabled = true;

    public LoginDetail(String username, String password, Set<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public LoginDetail(UserEntity userEntity, PasswordEncoder passwordEncoder) {
        this(userEntity.email, passwordEncoder.encode(userEntity.password), userEntity.role.getGrantedAuthorities());
    }
}
