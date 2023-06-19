package kiis.edu.rating.aop;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Aspect
@Component
public class AOPHandler {

    @Before("@annotation(allowPermission)")
    public void beforePermission(AllowPermission allowPermission) {

        permissionCheck(allowPermission.feature().concat(allowPermission.method()));
    }

    @Before(value = "allowFeatureHandler(allowFeature) && allowMethodHandler(allowMethod)", argNames = "allowFeature,allowMethod")
    public void beforeFeatureMethodHandler(AllowFeature allowFeature, AllowMethod allowMethod) {

        permissionCheck(allowFeature.value().concat(allowMethod.value()));
    }

    @Pointcut("@within(allowFeature)")
    private void allowFeatureHandler(AllowFeature allowFeature) {
    }

    @Pointcut("@annotation(allowMethod)")
    private void allowMethodHandler(AllowMethod allowMethod) {
    }

    private void permissionCheck(String permissionName) {
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
