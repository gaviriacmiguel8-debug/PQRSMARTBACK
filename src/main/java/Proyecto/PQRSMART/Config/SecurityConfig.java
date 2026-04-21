package Proyecto.PQRSMART.Config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
    @EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
    public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf ->csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Habilitar CORS
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicEndpoinds()).permitAll()
                        .requestMatchers(userEndpoinds()).hasRole("USER")
                        .requestMatchers(adminEndpoinds()).hasRole("ADMIN")
                        .requestMatchers(secreEndpoinds()).hasRole("SECRE")
                        .requestMatchers(allEnpoinds()).hasAnyRole("USER", "ADMIN", "SECRE")
                        .requestMatchers("/api/request/report/**").hasAnyRole("ADMIN","SECRE")

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess ->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();

    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"
                // Flutter emulador
                 )); // Reemplaza con la URL de tu frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true); // 🔥 importante

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    private RequestMatcher publicEndpoinds(){
        return new OrRequestMatcher(
          new AntPathRequestMatcher("/api/greeting/sayHelloPublic"),
                new AntPathRequestMatcher("/api/auth/registerUser"),
                new AntPathRequestMatcher("/api/auth/registerUserApp"),
                new AntPathRequestMatcher("/api/auth/authenticate"),
                new AntPathRequestMatcher("/api/auth/activate"),
                new AntPathRequestMatcher("/api/auth/verify-email"),
                new AntPathRequestMatcher("/api/auth/verifyEmail/**"),
                new AntPathRequestMatcher("/api/identification_type/get"),
                new AntPathRequestMatcher("/api/person_type/get"),
                new AntPathRequestMatcher("/api/chat/**"),
                new AntPathRequestMatcher("/api/auth/forgot-password/**")

        );
    }

    private RequestMatcher userEndpoinds(){
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/api/request/save"),
                new AntPathRequestMatcher("/api/request/report/**"),
        new AntPathRequestMatcher("/api/chat/**")
        );
    }

    private RequestMatcher adminEndpoinds(){
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/api/Usuario/report"),
                new AntPathRequestMatcher("/api/Usuario/cancel/**"),
                new AntPathRequestMatcher("/api/Usuario/activate/**"),
                new AntPathRequestMatcher("/api/Usuario/get"),
                new AntPathRequestMatcher("/api/Usuario/update"),
                new AntPathRequestMatcher("/api/request_type/save"),
                new AntPathRequestMatcher("/api/request_type/update"),
                new AntPathRequestMatcher("/api/request_state/update"),
                new AntPathRequestMatcher("/api/request_state/save"),
                new AntPathRequestMatcher("/api/person_type/save"),
                new AntPathRequestMatcher("/api/person_type/update"),
                new AntPathRequestMatcher("/api/identification_type/save"),
                new AntPathRequestMatcher("/api/identification_type/update"),
                new AntPathRequestMatcher("/api/dependence/save"),
                new AntPathRequestMatcher("/api/dependence/update"),
                new AntPathRequestMatcher("/api/dependence/cancel/**"),
                new AntPathRequestMatcher("/api/dependence/activate/**"),
                new AntPathRequestMatcher("/api/category/cancel/**"),
                new AntPathRequestMatcher("/api/category/activate/**"),
                new AntPathRequestMatcher("/api/category/save"),
                new AntPathRequestMatcher("/api/category/update"),
                new AntPathRequestMatcher("/api/auth/register")
        );
    }

    private RequestMatcher secreEndpoinds(){
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/api/request/update/**")
        );
    }

    private RequestMatcher allEnpoinds(){
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/api/request/get"),
                new AntPathRequestMatcher("/api/dependence/get"),
                new AntPathRequestMatcher("/api/request/cancel/**"),
                new AntPathRequestMatcher("/api/Usuario/profile"),
                new AntPathRequestMatcher("/api/request_type/get"),
                new AntPathRequestMatcher("/api/request_state/get"),
                new AntPathRequestMatcher("/api/Usuario/update/**"),
                new AntPathRequestMatcher("/api/request/get"),
                new AntPathRequestMatcher("/api/identification_type/get"),
                new AntPathRequestMatcher("/api/dependence/get"),
                new AntPathRequestMatcher("/api/category/get"),
                new AntPathRequestMatcher("/api/person_type/get"),
                new AntPathRequestMatcher("/api/request/getForDependence"),
                new AntPathRequestMatcher("/api/request/get/pqrs"),
                new AntPathRequestMatcher("/api/Usuario/initial")
        );
    }
    }

