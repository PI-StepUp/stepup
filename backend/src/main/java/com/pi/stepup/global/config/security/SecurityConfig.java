package com.pi.stepup.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pi.stepup.domain.user.constant.UserRole;
import com.pi.stepup.domain.user.service.UserRedisService;
import com.pi.stepup.global.util.jwt.JwtTokenProvider;
import com.pi.stepup.global.util.jwt.filter.JwtAccessDeniedHandler;
import com.pi.stepup.global.util.jwt.filter.JwtAuthenticationEntryPoint;
import com.pi.stepup.global.util.jwt.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRedisService userRedisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().configurationSource(corsConfigurationSource())
            .and()

            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

            .authorizeRequests()

            .regexMatchers(HttpMethod.POST, Constants.PostPermitArray).permitAll()
            .regexMatchers(HttpMethod.GET, Constants.GetPermitArray).permitAll()

            .regexMatchers(Constants.AdminPermitArray)
            .hasAuthority(UserRole.ROLE_ADMIN.name())

            .antMatchers("/swagger-ui/**").permitAll()
            .antMatchers("/v3/api-docs/**").permitAll()
            .antMatchers("/**/*.html").permitAll()
            .antMatchers("/**/*.css").permitAll()
            .antMatchers("/**/*.js").permitAll()
            .antMatchers("/**/*.png").permitAll()
            .antMatchers("/images/**").permitAll()

            .anyRequest().authenticated()

            .and()
            .exceptionHandling()
            .accessDeniedHandler(new JwtAccessDeniedHandler(objectMapper))
            .authenticationEntryPoint(new JwtAuthenticationEntryPoint(objectMapper))
            .and()
            .addFilterBefore(
                new JwtAuthenticationFilter(userRedisService, jwtTokenProvider, objectMapper),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
