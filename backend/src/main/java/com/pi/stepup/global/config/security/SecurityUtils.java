package com.pi.stepup.global.config.security;

import com.pi.stepup.global.error.exception.ForbiddenException;
import java.util.Objects;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static String getLoggedInUserId() {
        try {
            Authentication authentication = Objects.requireNonNull(SecurityContextHolder
                .getContext()
                .getAuthentication());

            if (authentication instanceof AnonymousAuthenticationToken) {
                authentication = null;
            }

            return authentication.getName();
        } catch (NullPointerException e) {
            throw new ForbiddenException();
        }
    }
}
