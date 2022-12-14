package kiis.edu.rating.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import kiis.edu.rating.features.user.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static kiis.edu.rating.helper.Constant.*;

public class JwtTokenVerifier extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(TOKEN_HEADER);
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authorizationHeader.replace(BEARER, "");
            Claims claimsJwsBody = Jwts.parserBuilder()
                    .setSigningKey(ENCODED_SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String username = claimsJwsBody.getSubject();
            UserRole role = UserRole.valueOf((String) claimsJwsBody.get(CLAIM_AUTHORITY));
            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = role.getGrantedAuthorities();

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException e) {
            LOGGER.error("Token cannot be trust");
        }

        filterChain.doFilter(request, response);
    }
}
