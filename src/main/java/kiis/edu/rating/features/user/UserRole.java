package kiis.edu.rating.features.user;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static kiis.edu.rating.features.user.UserRole.Permission.*;

@Getter
public enum UserRole {
    ADMIN(Permission.values()),
    MANAGER(
            SUBJECT_GET_ALL, SUBJECT_CREATE, SUBJECT_UPDATE,
            SUBJECT_RATING_GET_ALL,
            TEACHER_GET_ALL, TEACHER_CREATE, TEACHER_UPDATE,
            TEACHER_RATING_GET_ALL,
            COMMENT_GET_ALL,
            COMMENT_REACT_GET_ALL
    ),
    USER(
            SUBJECT_RATING_CREATE, SUBJECT_RATING_UPDATE, SUBJECT_RATING_DELETE,
            TEACHER_RATING_CREATE, TEACHER_RATING_UPDATE, TEACHER_RATING_DELETE,
            COMMENT_GET_ALL, COMMENT_CREATE, COMMENT_UPDATE, COMMENT_DELETE,
            COMMENT_REACT_GET_ALL, COMMENT_REACT_CREATE, COMMENT_REACT_UPDATE, COMMENT_REACT_DELETE
    );

    private final Set<Permission> authorities;

    UserRole(Permission... authorities) {
        Set<Permission> userAuthorities = new HashSet<>();
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
    public Set<String> getPermissions() {
        return authorities.stream().map(Enum::name).collect(Collectors.toSet());
    }

    public enum Permission {
        SUBJECT_GET_ALL, SUBJECT_CREATE, SUBJECT_UPDATE, SUBJECT_DELETE,
        SUBJECT_RATING_GET_ALL, SUBJECT_RATING_CREATE, SUBJECT_RATING_UPDATE, SUBJECT_RATING_DELETE,

        TEACHER_GET_ALL, TEACHER_CREATE, TEACHER_UPDATE, TEACHER_DELETE,
        TEACHER_RATING_GET_ALL, TEACHER_RATING_CREATE, TEACHER_RATING_UPDATE, TEACHER_RATING_DELETE,

        COMMENT_GET_ALL, COMMENT_CREATE, COMMENT_UPDATE, COMMENT_DELETE,
        COMMENT_REACT_GET_ALL, COMMENT_REACT_CREATE, COMMENT_REACT_UPDATE, COMMENT_REACT_DELETE,

        FILE_GET_ALL, FILE_CREATE, FILE_UPDATE, FILE_DELETE,
        OTHER_GET_ALL, OTHER_CREATE, OTHER_UPDATE, OTHER_DELETE
    }
}
