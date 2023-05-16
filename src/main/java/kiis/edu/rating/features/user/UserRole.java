package kiis.edu.rating.features.user;

import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum UserRole {
    ADMIN(
            Subject.values(),
            SubjectRating.values(),
            SubjectComment.values(),
            SubjectCommentReact.values(),
            Teacher.values(),
            TeacherRating.values(),
            TeacherComment.values(),
            TeacherCommentReact.values(),
            File.values(),
            Other.values()
    ),
    MANAGER(
            Subject.GET_BY_ID, Subject.GET_ALL, Subject.CREATE, Subject.UPDATE,
            SubjectRating.GET_BY_ID, SubjectRating.GET_ALL,
            SubjectComment.GET_BY_ID, SubjectComment.GET_ALL,
            SubjectCommentReact.GET_BY_ID, SubjectCommentReact.GET_ALL,
            Teacher.GET_BY_ID, Teacher.GET_ALL, Teacher.CREATE, Teacher.UPDATE,
            TeacherRating.GET_BY_ID, TeacherRating.GET_ALL,
            TeacherComment.GET_BY_ID, TeacherComment.GET_ALL,
            TeacherCommentReact.GET_BY_ID, TeacherCommentReact.GET_ALL
    ),
    USER(
            Subject.GET_BY_ID, Subject.GET_ALL, Subject.CREATE, Subject.UPDATE,
            SubjectRating.GET_BY_ID, SubjectRating.GET_ALL,
            SubjectComment.GET_BY_ID, SubjectComment.GET_ALL,
            SubjectCommentReact.GET_BY_ID, SubjectCommentReact.GET_ALL,
            Teacher.GET_BY_ID, Teacher.GET_ALL, Teacher.CREATE, Teacher.UPDATE,
            TeacherRating.GET_BY_ID, TeacherRating.GET_ALL,
            TeacherComment.GET_BY_ID, TeacherComment.GET_ALL,
            TeacherCommentReact.GET_BY_ID, TeacherCommentReact.GET_ALL
    );

    private final Set<String> permissions;

    UserRole(Permission... permissions) {
        this.permissions = Arrays.stream(permissions)
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }

    UserRole(Permission[]... permissionsArray) {
        Set<String> result = new HashSet<>();
        for (Permission[] permissions : permissionsArray) {
            for (Permission permission : permissions) {
                result.add(permission.getName());
            }
        }
        this.permissions = result;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = this.permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }

    public static void requirePermission(Permission permission) {
        String permissionName = permission.getName();
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (permissionName.equals(authority.getAuthority())) {
                return;
            }
        }
        throw new BadCredentialsException("You have no permission to call this API, permission require: " + permissionName);
    }

    //----------------------------------------------------------------------------------------
    //
    //----------------------------------------------------------------------------------------

    public enum Subject implements Permission {
        GET_BY_ID, GET_ALL, CREATE, UPDATE, DELETE, DISABLE;

        @Override
        public String getName() {
            return "SUBJECT_" + this.name();
        }
    }

    public enum SubjectRating implements Permission {
        GET_BY_ID, GET_ALL, CREATE, UPDATE, DELETE, DISABLE;

        @Override
        public String getName() {
            return "SUBJECT_RATING_" + this.name();
        }
    }

    public enum SubjectComment implements Permission {
        GET_BY_ID, GET_ALL, CREATE, UPDATE, DELETE, DISABLE;

        @Override
        public String getName() {
            return "SUBJECT_COMMENT_" + this.name();
        }
    }

    public enum SubjectCommentReact implements Permission {
        GET_BY_ID, GET_ALL, CREATE, UPDATE, DELETE, DISABLE;

        @Override
        public String getName() {
            return "SUBJECT_COMMENT_REACT_" + this.name();
        }
    }

    public enum Teacher implements Permission {
        GET_BY_ID, GET_ALL, CREATE, UPDATE, DELETE, DISABLE;

        @Override
        public String getName() {
            return "TEACHER_" + this.name();
        }
    }

    public enum TeacherRating implements Permission {
        GET_BY_ID, GET_ALL, CREATE, UPDATE, DELETE, DISABLE;

        @Override
        public String getName() {
            return "TEACHER_RATING_" + this.name();
        }
    }

    public enum TeacherComment implements Permission {
        GET_BY_ID, GET_ALL, CREATE, UPDATE, DELETE, DISABLE;

        @Override
        public String getName() {
            return "TEACHER_COMMENT_" + this.name();
        }
    }

    public enum TeacherCommentReact implements Permission {
        GET_BY_ID, GET_ALL, CREATE, UPDATE, DELETE, DISABLE;

        @Override
        public String getName() {
            return "TEACHER_COMMENT_REACT_" + this.name();
        }
    }

    public enum File implements Permission {
        GET_BY_ID;

        @Override
        public String getName() {
            return "FILE_" + this.name();
        }
    }

    public enum Other implements Permission {
        GET_BY_ID;

        @Override
        public String getName() {
            return "OTHER_" + this.name();
        }
    }

    private interface Permission {
        String getName();
    }
}
