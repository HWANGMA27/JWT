package com.tutorial.jwt.controller;

import com.tutorial.jwt.dto.*;
import com.tutorial.jwt.entity.RefreshToken;
import com.tutorial.jwt.jwt.JwtFilter;
import com.tutorial.jwt.jwt.RefreshTokenProvider;
import com.tutorial.jwt.jwt.TokenProvider;
import com.tutorial.jwt.service.RefreshTokenService;
import com.tutorial.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenProvider refreshTokenProvider;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtDto> authorize(@RequestBody LoginRequestDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        RefreshToken refreshToken = refreshTokenProvider.createRefreshToken(userService.getUser());

        JwtDto response = JwtDto.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .build();
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/login/fail")
    public void loginFail(@RequestParam(value = "error", required = false) String error,
                                       @RequestParam(value = "exception", required = false) String exception) {
        throw new RuntimeException(exception);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<JwtDto> refreshToken(@RequestBody JwtDto request) {
        String requestRefreshToken = request.getRefreshToken();
        RefreshToken refreshToken = refreshTokenService.findByRefreshToken(requestRefreshToken);
        refreshTokenProvider.verifyExpiration(refreshToken);

        String accessToken = request.getAccessToken();
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        String newAccessToken = tokenProvider.createToken(authentication);

        return ResponseEntity.ok(new JwtDto(newAccessToken, requestRefreshToken));
    }
}
