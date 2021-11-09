package uz.pdp.loanbook.security;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.loanbook.entity.role.RoleDatabase;
import uz.pdp.loanbook.entity.user.UserDatabase;
import uz.pdp.loanbook.model.receive.user.UserSignInReceiveModel;
import uz.pdp.loanbook.service.auth.AuthService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ApplicationUsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;


    String secretKey = "Z9ALVpXw5W0Ifx2sHcBwmTKNKaqTgwD4";
    private final long expireTime = 7 * 86_400_000L;


    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        Authentication authentication = null;
        try {
            UserSignInReceiveModel userSignInReceiveModel = objectMapper.readValue(
                    request.getInputStream(),
                    UserSignInReceiveModel.class
            );
            authentication = new UsernamePasswordAuthenticationToken(
                    userSignInReceiveModel.getUsername(),
                    userSignInReceiveModel.getPassword()
            );
            return authenticationManager.authenticate(authentication);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return authentication;
    }


    public String successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, ServletException {

        ObjectMapper objectMapper = new ObjectMapper();
        UserSignInReceiveModel userSignInReceiveModel
                = objectMapper.readValue(request.getInputStream(), UserSignInReceiveModel.class);
        UserDetails userDetails = authService.loadUserByUsername(userSignInReceiveModel.getUsername());
        if (!passwordEncoder.matches(userSignInReceiveModel.getPassword(), userDetails.getPassword()))
            return "Username or password incorrect!";

        String token = generateToken(userDetails.getUsername(), (UserDatabase) userDetails);
        token = "Bearer " + token;

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        return token;
    }

    public String generateToken(String username, UserDatabase user) {
        return Jwts
                .builder()
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expireTime)) // 7 days
                .setSubject(username)
                .claim("userDetails", user)
                .compact();
    }
}

