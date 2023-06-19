package kiis.edu.rating.features.user;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static kiis.edu.rating.features.user.UserRole.Feature.*;
import static kiis.edu.rating.features.user.UserRole.Method.*;

@Getter
public enum UserRole {
    ADMIN(Feature.altogether()),
    MANAGER(
            SUBJECT.all(),
            SUBJECT_RATING.all(),
            SUBJECT_COMMENT.all()
    ),
    USER(
            TEACHER.methods(GET_ALL, CREATE),
            TEACHER_COMMENT.methods(UPDATE, DELETE)
    );

    private final Set<String> authorities;

    @SafeVarargs
    UserRole(Set<String>... sets) {
        Set<String> userAuthorities = new HashSet<>();
        Arrays.stream(sets).forEach(userAuthorities::addAll);
        this.authorities = userAuthorities;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = this.authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }

    public enum Method {
        GET_ALL, GET_ENABLED, GET_BY_ID, CREATE, UPDATE, DELETE,
    }

    public enum Feature {
        SUBJECT, SUBJECT_RATING, SUBJECT_COMMENT, SUBJECT_COMMENT_REACT,
        TEACHER, TEACHER_RATING, TEACHER_COMMENT, TEACHER_COMMENT_REACT,
        FILE, OTHER;

        public String concat(Method method) {
            return this.name() + "_" + method.name();
        }

        public Set<String> methods(Method... methods) {
            return Arrays.stream(methods).map(this::concat).collect(Collectors.toSet());
        }

        public Set<String> all() {
            return methods(Method.values());
        }

        public static Set<String> altogether() {
            Set<String> result = new HashSet<>();
            Arrays.stream(Feature.values()).forEach(feature -> {
                result.addAll(feature.all());
            });
            return result;
        }
    }
}
