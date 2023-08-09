package kiis.edu.rating.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<String> {
//    @Autowired
//    private CommonService commonService;
//
//    @Override
//    public Optional<String> getCurrentAuditor() {
//        // Login した user
//        ServiceUser serviceUser = commonService.getUser();
//        return Optional.of(serviceUser.getUsername);
//    }

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        final String username = ((String) authentication.getPrincipal());
        return Optional.of(username);
    }
}
