package com.pi.stepup.domain.user.util;

import com.pi.stepup.domain.user.constant.UserRole;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String username() default "testId";

    String password() default "testPassword";

    UserRole role() default UserRole.ROLE_USER;
}
