package kiis.edu.rating.aop;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AOPHandler {

    @Before(value = "@within(allowFeature) && @annotation(allowMethod)", argNames = "allowFeature,allowMethod")
    public void featureMethodPointcut(AllowFeature allowFeature, AllowMethod allowMethod) {
        permissionCheck(allowFeature.value().concat(allowMethod.value()));

    }

    @Before("@annotation(allowPermission)")
    public void permissionPointcut(AllowPermission allowPermission) {
        permissionCheck(allowPermission.feature().concat(allowPermission.method()));
    }

    private void permissionCheck(String authority) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final SecurityExpressionRoot securityExpressionRoot = new SecurityExpressionRoot(authentication) {
        };
        if (!securityExpressionRoot.hasAuthority(authority)) {
            throw new AccessDeniedException("You need authority: " + authority + " to call this api");
        }
    }
}
