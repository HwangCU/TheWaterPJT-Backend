package fishermanjoeandchildren.thewater.config;

import fishermanjoeandchildren.thewater.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/signup", "/api/users/check-id",
                                "/api/users/check-email", "/api/users/check-nickname",
                                "/api/users/request-verification", "/api/users/verify-code").permitAll()
                        .requestMatchers("/api/users/login").permitAll()
                        .requestMatchers("/api/env-info/**").permitAll()

                        // 낚시 포인트 관련 API 추가
                        .requestMatchers("/api/fishing-points/**").permitAll()
                        .requestMatchers("/api/fishing-points/**").authenticated()

                        // aquarium 관련 경로
                        .requestMatchers("/api/aquarium/stats/**", "/api/aquarium/info/**").authenticated()

                        // guest book 관련 경로
                        .requestMatchers("api/guest-book/read/").permitAll()
                        .requestMatchers("api/guest-book/write/").authenticated()

                        // member 관련 정보
                        .requestMatchers("/api/users/me").authenticated()

                        // collection 관련 정보
                        .requestMatchers("/api/collection/myfish/add", "/api/collection/myfish/all", "/api/collection/myfish/delete/**").authenticated()

                        // Swagger UI 관련 경로 허용
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}