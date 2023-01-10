package com.chatter.database.controller;

import com.chatter.database.converter.UserConverter;
import com.chatter.database.dto.User.UserLoginInDto;
import com.chatter.database.dto.User.UserLoginOutDto;
import com.chatter.database.dto.User.UserRegisterInDto;
import com.chatter.database.dto.User.UserRegisterOutDto;
import com.chatter.database.exception.DuplicateRecordException;
import com.chatter.database.exception.NotFoundRecordException;
import com.chatter.database.model.User;
import com.chatter.database.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;

    @Autowired
    public UserController(UserService userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterOutDto> register(@RequestBody @Valid UserRegisterInDto userRegisterInDto) {
        List<User> users = userService.findAllByDisplayName(userRegisterInDto.getDisplayName());
        String displayNameCode = "#0000";

        if (!users.isEmpty()) {
            boolean isFinished = false;
            for (int i = 1; i < 10000; i++) {
                String code = new String(new char[]{'0', '0', '0', '0'}, 0, 4 - String.valueOf(i).toString().length()) + i;

                if (isFinished) break;

                String dnc = "#" + code;
                for (User user : users) {
                    if (!user.getDisplayNameCode().equals("dnc")) {
                        displayNameCode = dnc;
                        isFinished = true;
                        break;
                    }
                }
            }
        }

        User user = userConverter.UserRegisterInDto_User(userRegisterInDto, displayNameCode);
        User savedUser = userService.save(user);

        return ResponseEntity.ok(userConverter.User_UserRegisterOutDto(savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginOutDto> login(@RequestBody @Valid UserLoginInDto userLoginInDto) {
        User findUser = userService.findByEmail(userLoginInDto.getEmail());
        if (findUser == null) throw new NotFoundRecordException("User is not found");

        if (!findUser.getPassword().equals(userLoginInDto.getPassword()))
            throw new DuplicateRecordException("Email or password is incorrect");

        return ResponseEntity.ok(userConverter.User_UserLoginOutDto(findUser));
    }
}
