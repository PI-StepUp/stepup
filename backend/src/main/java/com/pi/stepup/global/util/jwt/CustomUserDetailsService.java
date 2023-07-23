package com.pi.stepup.global.util.jwt;

import com.pi.stepup.domain.user.constant.UserResponseMessage;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO : UsernameNotFountException 예외처리 확인
        User user = userRepository.findById(username).orElseThrow(
            () -> new UsernameNotFoundException(UserResponseMessage.LOGIN_FAIL.getMessage()));
        logger.debug("loadUserByUsername user : {}", user);

        return new CustomUserDetails(user);
    }

}
