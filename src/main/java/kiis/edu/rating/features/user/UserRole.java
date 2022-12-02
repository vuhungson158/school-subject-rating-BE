package kiis.edu.rating.features.user;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static kiis.edu.rating.features.user.UserRole.UserAuthority.*;

@Getter
public enum UserRole {
    ADMIN(UserAuthority.values()),
    MANAGER(
            SUBJECT_GET, SUBJECT_INSERT, SUBJECT_UPDATE, SUBJECT_DELETE,
            TEACHER_GET, TEACHER_INSERT, TEACHER_UPDATE, TEACHER_DELETE,
            RATING_GET, RATING_INSERT, RATING_UPDATE, RATING_DELETE,
            FILE_GET, FILE_INSERT, FILE_UPDATE, FILE_DELETE,
            OTHER_GET
    ),
    TEACHER(
            SUBJECT_GET,
            TEACHER_GET,
            RATING_GET,
            FILE_GET
    ),
    STUDENT(
            SUBJECT_GET, SUBJECT_UPDATE,
            TEACHER_GET,
            RATING_GET, RATING_INSERT, RATING_UPDATE, RATING_DELETE,
            FILE_GET, FILE_INSERT, FILE_DELETE
    );

    private final Set<UserAuthority> authorities;

    UserRole(UserAuthority... authorities) {
        Set<UserAuthority> userAuthorities = new HashSet<>();
        Collections.addAll(userAuthorities, authorities);
        this.authorities = userAuthorities;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = this.authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.name()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }

    public enum UserAuthority {
        SUBJECT_GET, SUBJECT_INSERT, SUBJECT_UPDATE, SUBJECT_DELETE,
        TEACHER_GET, TEACHER_INSERT, TEACHER_UPDATE, TEACHER_DELETE,
        RATING_GET, RATING_INSERT, RATING_UPDATE, RATING_DELETE,
        FILE_GET, FILE_INSERT, FILE_UPDATE, FILE_DELETE,
        OTHER_GET, OTHER_INSERT, OTHER_UPDATE, OTHER_DELETE
    }
}
