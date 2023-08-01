package com.pi.stepup.domain.user.util;

import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.global.config.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        CustomUserDetails principal = new CustomUserDetails(User.builder()
            .id(customUser.username())
            .password(customUser.password())
            .role(customUser.role())
            .build());

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal,
            principal.getPassword(), principal.getAuthorities());

        context.setAuthentication(authentication);
        return context;
    }
}
