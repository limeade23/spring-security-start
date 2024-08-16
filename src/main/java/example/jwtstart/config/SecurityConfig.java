package example.jwtstart.config;

import example.jwtstart.jwt.JwtFilter;
import example.jwtstart.jwt.JwtProvider;
import example.jwtstart.jwt.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtProvider jwtProvider;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
        AuthenticationConfiguration authenticationConfiguration) throws Exception {

        http
            .cors((cors) -> cors.configurationSource(new CorsConfigurationSource() {
                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of("http://localhost:8080/"));
                    configuration.setAllowedMethods(List.of("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(List.of("*"));
                    configuration.setMaxAge(3600L);

                    configuration.setExposedHeaders(List.of("Authorization"));

                    return null;
                }
            }));

        http
            .csrf(AbstractHttpConfigurer::disable) // csrf disable
            .formLogin(AbstractHttpConfigurer::disable) // Form 로그인 방식 disable
            .httpBasic(AbstractHttpConfigurer::disable) // http basic 인증 방식 disable
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션 설정

        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/api/login", "/api/join").permitAll()
                .requestMatchers("/api/main/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            );

        LoginFilter loginFilter = new LoginFilter(
            authenticationManager(authenticationConfiguration), jwtProvider);
        loginFilter.setFilterProcessesUrl("/api/login");

        http
            .addFilterBefore(new JwtFilter(jwtProvider), LoginFilter.class);
        http
            .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
