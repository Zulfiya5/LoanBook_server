package uz.pdp.loanbook.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.loanbook.service.auth.AuthService;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class ApplicationJwtTokenFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private String secretKey = "Z9ALVpXw5W0Ifx2sHcBwmTKNKaqTgwD4";




    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String bearerToken = httpServletRequest.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        String token = bearerToken.replace("Bearer ", "");

        if (!isValidToken(token)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String username = getClaims(token).getSubject();
        UserDetails userDetails = authService.loadUserByUsername(username);

        Collection<? extends GrantedAuthority> userRoles = userDetails.getAuthorities();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        userRoles
                );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isValidToken(String token) {
        Claims claims = getClaims(token);
        Date expiryDate = claims.getExpiration();
        return expiryDate.getTime() > new Date().getTime();
    }
}
//SignatureAlgorithm.HS512, secretKey

//secretKey.getBytes(StandardCharsets.UTF_8)