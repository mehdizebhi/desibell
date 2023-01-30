package ir.desibell.notificationService.jwt.filter;

import com.google.common.hash.Hashing;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.jwt.jwtUtil.JwtUtil;
import ir.desibell.notificationService.services.user.UserService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserService usersService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();

        final String authorizationHeader = extractJwtCookie(cookies);
        final String credentialHeader = extractCredentialCookie(cookies);

        String number = null;
        String jwt = null;
        String crt = null;

        if (authorizationHeader != null) {
            jwt = authorizationHeader;
            number = this.jwtUtil.extractUsername(jwt);
            crt = credentialHeader;
        }

        if (number != null && crt != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = this.usersService.findByNumber(number);

            if (user != null && this.jwtUtil.validateToken(jwt, user) && validationCredential(crt,jwt)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(user, null, this.usersService.findAllRolesOfUser(user));
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractJwtCookie(Cookie[] cookies) {
        String jwtCookie = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    jwtCookie = cookie.getValue();
                    break;
                }
            }
        }
        return jwtCookie;
    }

    private String extractCredentialCookie(Cookie[] cookies) {
        String credentialCookie = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("crt")) {
                    credentialCookie = cookie.getValue();
                    break;
                }
            }
        }
        return credentialCookie;
    }
    
    private boolean validationCredential(String crt, String jwt){
        final String credential = jwt + "TRUE";
        final String credentialToken = Hashing.sha256()
                .hashString(credential, StandardCharsets.UTF_8)
                .toString();
        
        return crt.equals(credentialToken);
    }
}
