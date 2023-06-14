package kiis.edu.rating.aop;


import kiis.edu.rating.features.user.UserRole.Permission;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Aspect
@Component
public class AllowPermissionHandler {

    @Before("@annotation(allowPermission)")
    public void before(JoinPoint joinPoint, AllowPermission allowPermission) {

        String permissionName = allowPermission.value().name();
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        for (GrantedAuthority authority : authorities) {
            if (permissionName.equals(authority.getAuthority())) {
                return;
            }
        }
        throw new BadCredentialsException("You have no permission to call this API, permission require: " + permissionName);
    }
}
