package com.tutorial.jwt.controller;

import com.tutorial.jwt.dto.SignupRequestDto;
import com.tutorial.jwt.dto.UserDto;
import com.tutorial.jwt.entity.User;
import com.tutorial.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

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
