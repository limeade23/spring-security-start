package example.jwtstart.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.jwtstart.model.CustomUserDetails;
import example.jwtstart.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public LoginFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        // JSON 요청을 처리하도록 설정
        setPostOnly(true);
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        // mutipart/from-login 일 경우
        // 클라이언트 요청에서 username, password 추출
        //String username = obtainUsername(request);
        //String password = obtainPassword(request);

        String username = null;
        String password = null;

        try {
            // JSON 데이터를 읽어옴
            Map<String, String> jsonRequest = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            username = jsonRequest.get("username");
            password = jsonRequest.get("password");
        } catch (IOException e) {
            throw new AuthenticationServiceException("Failed to parse authentication request body", e);
        }

        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }

        username = username.trim();

        System.out.println(username);

        // 스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            username, password, null);

        // token에 담은 검증을 위한 AuthenticationManager
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        Authentication authentication
    ) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        String role = authentication.getAuthorities().stream()
            .findFirst()
            .map(GrantedAuthority::getAuthority)
            .orElse(null);

        String token = jwtProvider.createToken(username, role, 60*60*1000L);

        response.addHeader("Authorization", "Bearer " + token);

    }

    @Override
    protected void unsuccessfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException failed
    ) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
