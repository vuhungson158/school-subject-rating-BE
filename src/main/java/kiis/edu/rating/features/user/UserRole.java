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
            SUBJECT__GET_ALL, SUBJECT__CREATE, SUBJECT__UPDATE, SUBJECT__DELETE,
            SUBJECT_RATING__GET_ALL, SUBJECT_RATING__CREATE, SUBJECT_RATING__UPDATE, SUBJECT_RATING__DELETE,
            SUBJECT_COMMENT__GET_ALL, SUBJECT_COMMENT__CREATE, SUBJECT_COMMENT__UPDATE, SUBJECT_COMMENT__DELETE
    ),
    USER(
            TEACHER__GET_ALL, TEACHER__CREATE, TEACHER__UPDATE, TEACHER__DELETE,
            TEACHER_RATING__GET_ALL, TEACHER_RATING__CREATE, TEACHER_RATING__UPDATE, TEACHER_RATING__DELETE,
            TEACHER_COMMENT__GET_ALL, TEACHER_COMMENT__CREATE, TEACHER_COMMENT__UPDATE, TEACHER_COMMENT__DELETE,
            TEACHER_COMMENT_REACT__GET_ALL, TEACHER_COMMENT_REACT__CREATE
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

//    public Set<String> getPermissions() {
//        return authorities.stream().map(Enum::name).collect(Collectors.toSet());
//    }

    public enum Permission {
        SUBJECT__GET_ALL, SUBJECT__CREATE, SUBJECT__UPDATE, SUBJECT__DELETE,
        SUBJECT_RATING__GET_ALL, SUBJECT_RATING__CREATE, SUBJECT_RATING__UPDATE, SUBJECT_RATING__DELETE,
        SUBJECT_COMMENT__GET_ALL, SUBJECT_COMMENT__CREATE, SUBJECT_COMMENT__UPDATE, SUBJECT_COMMENT__DELETE,
        SUBJECT_COMMENT_REACT__GET_ALL, SUBJECT_COMMENT_REACT__CREATE, SUBJECT_COMMENT_REACT__UPDATE, SUBJECT_COMMENT_REACT__DELETE,

        TEACHER__GET_ALL, TEACHER__CREATE, TEACHER__UPDATE, TEACHER__DELETE,
        TEACHER_RATING__GET_ALL, TEACHER_RATING__CREATE, TEACHER_RATING__UPDATE, TEACHER_RATING__DELETE,
        TEACHER_COMMENT__GET_ALL, TEACHER_COMMENT__CREATE, TEACHER_COMMENT__UPDATE, TEACHER_COMMENT__DELETE,
        TEACHER_COMMENT_REACT__GET_ALL, TEACHER_COMMENT_REACT__CREATE, TEACHER_COMMENT_REACT__UPDATE, TEACHER_COMMENT_REACT__DELETE,

        FILE__GET_ALL, FILE__CREATE, FILE__UPDATE, FILE__DELETE,
        OTHER__GET_ALL, OTHER__CREATE, OTHER__UPDATE, OTHER__DELETE
    }
}
