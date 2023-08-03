package com.pi.stepup.global.config.security;

import com.pi.stepup.domain.user.constant.UserRole;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

            .authorizeRequests()
            //로그인하지 않아도 접근 가능
            .regexMatchers(HttpMethod.POST, Constants.PostPermitArray).permitAll()
            .regexMatchers(HttpMethod.GET, Constants.GetPermitArray).permitAll()

            //관리자 권한
            .regexMatchers(HttpMethod.POST, Constants.AdminPermitArray)
            .hasAuthority(UserRole.ROLE_ADMIN.name())
            .regexMatchers(HttpMethod.DELETE, Constants.AdminPermitArray)
            .hasAuthority(UserRole.ROLE_ADMIN.name())
            .regexMatchers(HttpMethod.PUT, Constants.AdminPermitArray)
            .hasAuthority(String.valueOf(UserRole.ROLE_ADMIN))

            //그외는 모두 로그인해야 접근 가능
            .anyRequest().authenticated()

            .and()
            .exceptionHandling()
            .accessDeniedHandler(new JwtAccessDeniedHandler(objectMapper))
            .authenticationEntryPoint(new JwtAuthenticationEntryPoint(objectMapper))
            .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, objectMapper),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
