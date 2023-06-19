package kiis.edu.rating.aop;

import kiis.edu.rating.features.user.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowPermission {
    UserRole.Feature feature();

    UserRole.Method method();
}
