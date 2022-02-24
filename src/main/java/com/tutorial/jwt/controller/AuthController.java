package com.tutorial.jwt.controller;

import com.tutorial.jwt.dto.LoginRequestDto;
import com.tutorial.jwt.dto.SignupRequestDto;
import com.tutorial.jwt.dto.TokenDto;
import com.tutorial.jwt.dto.UserDto;
import com.tutorial.jwt.entity.User;
import com.tutorial.jwt.jwt.JwtFilter;
import com.tutorial.jwt.jwt.TokenProvider;
import com.tutorial.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> authorize(@RequestBody LoginRequestDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/login/fail")
    public void loginFail(@RequestParam(value = "error", required = false) String error,
                                       @RequestParam(value = "exception", required = false) String exception) {
        throw new RuntimeException(exception);
    }

    @PostMapping(value = "/signup", produces = "application/json; charset=utf8")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequestDto
    ) throws Exception {
        userService.signup(signupRequestDto);
        return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> myInfo() {
        User user = userService.getUser();
        UserDto userDto = new UserDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
