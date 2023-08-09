package kiis.edu.rating.features.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static kiis.edu.rating.features.user.UserRole.Feature.SUBJECT;
import static kiis.edu.rating.features.user.UserRole.Feature.SUBJECT_COMMENT;
import static kiis.edu.rating.features.user.UserRole.Feature.SUBJECT_RATING;
import static kiis.edu.rating.features.user.UserRole.Feature.TEACHER;
import static kiis.edu.rating.features.user.UserRole.Feature.TEACHER_COMMENT;
import static kiis.edu.rating.features.user.UserRole.Method.CREATE;
import static kiis.edu.rating.features.user.UserRole.Method.DELETE;
import static kiis.edu.rating.features.user.UserRole.Method.FIND_ALL;
import static kiis.edu.rating.features.user.UserRole.Method.UPDATE;

@Getter
public enum UserRole {
    ADMIN(Feature.altogether()),
    MANAGER(
            SUBJECT.all(),
            SUBJECT_RATING.all(),
            SUBJECT_COMMENT.all()
    ),
    USER(
            TEACHER.methods(FIND_ALL, CREATE),
            TEACHER_COMMENT.methods(UPDATE, DELETE)
    );

    private final Set<String> authorities;

    @SafeVarargs
    UserRole(Set<Combinator>... sets) {
        final Set<String> userAuthorities = new HashSet<>();
        Arrays.stream(sets).forEach(combinators -> userAuthorities.addAll(
                combinators.stream()
                        .map(Combinator::concat)
                        .collect(Collectors.toSet())
        ));
        this.authorities = userAuthorities;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        final Set<SimpleGrantedAuthority> permissions = this.authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }

    public enum Method {
        FIND_ALL, FIND_ENABLED, FIND_BY_ID, FIND_BY_PAGEABLE, FIND_BY_FILTER, CREATE, UPDATE, DELETE, FORCE_DELETE;
    }

    public enum Feature {
        SUBJECT, SUBJECT_RATING, SUBJECT_COMMENT, SUBJECT_COMMENT_REACT,
        TEACHER, TEACHER_RATING, TEACHER_COMMENT, TEACHER_COMMENT_REACT,
        FILE, OTHER;

        private Combinator combinator(Method method) {
            return new Combinator(this, method);
        }

        public String concat(Method method) {
            return combinator(method).concat();
        }

        public Set<Combinator> methods(Method... methods) {
            return Arrays.stream(methods)
                    .map(method -> new Combinator(this, method))
                    .collect(Collectors.toSet());
        }

        public Set<Combinator> all() {
            return methods(Method.values());
        }

        public static Set<Combinator> altogether() {
            final Set<Combinator> result = new HashSet<>();
            Arrays.stream(Feature.values()).forEach(feature -> result.addAll(feature.all()));
            return result;
        }
    }

}

@AllArgsConstructor
class Combinator {
    private final UserRole.Feature feature;
    private final UserRole.Method method;

    public String concat() {
        return feature.name() + "__" + method.name();
    }
}
