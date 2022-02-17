package com.tutorial.jwt.service;

import com.tutorial.jwt.dto.SignupRequestDto;
import com.tutorial.jwt.entity.Authority;
import com.tutorial.jwt.entity.User;
import com.tutorial.jwt.entity.UserAuthority;
import com.tutorial.jwt.repository.UserAuthorityRepository;
import com.tutorial.jwt.repository.UserRepository;
import com.tutorial.jwt.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    public final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequestDto requestDto) throws Exception {
        if (userRepository.findByUserIdWithAuthority(requestDto.getEmail()).orElse(null) != null) {
            throw new RuntimeException("User Not Found");
        }
        User user = User.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .name(requestDto.getName())
                .build();
        userRepository.save(user);

        UserAuthority userAuthority = UserAuthority.builder()
                                                .user(user)
                                                .authority(Authority.ROLE_USER)
                                                .build();
        userAuthorityRepository.save(userAuthority);
    }

    public User getUser() {
        return SecurityUtil.getCurrentUserId().flatMap(userRepository::findByUserIdWithAuthority)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
    }
}
