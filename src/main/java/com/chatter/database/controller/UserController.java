package com.chatter.database.controller;

import com.chatter.database.converter.UserConverter;
import com.chatter.database.dto.user.*;
import com.chatter.database.exception.DuplicateRecordException;
import com.chatter.database.exception.NotFoundRecordException;
import com.chatter.database.model.Friend;
import com.chatter.database.model.User;
import com.chatter.database.service.FriendService;
import com.chatter.database.service.UserService;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;
    private final FriendService friendService;

    @Autowired
    public UserController(UserService userService, UserConverter userConverter, FriendService friendService) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.friendService = friendService;
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

                if (i == 9999) throw new DuplicateRecordException("All codes for this display name are used.");
            }
        }

        User user = userConverter.UserRegisterInDto_User(userRegisterInDto, displayNameCode);
        User savedUser = userService.save(user);

        return ResponseEntity.ok(userConverter.User_UserRegisterOutDto(savedUser));
    }

    @SneakyThrows
    @PostMapping("/uploadProfileImage")
    public ResponseEntity<Boolean> uploadProfileImage(@RequestBody Image image) {
        Path path = Paths.get("files/profileImages/", image.getName() + ".gif");

        byte[] data = Base64.getDecoder().decode(image.getData());
        Files.write(path, data);

        return ResponseEntity.ok(true);
    }

    @RequestMapping("/getProfileImage/{email}")
    @ResponseBody
    public HttpEntity<byte[]> getProfileImage(@PathVariable String email) {
        File file = new File("files/profileImages", email + ".gif");
        try {
            byte[] imageBytes = Files.readAllBytes(file.toPath());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_GIF);
            headers.setContentLength(imageBytes.length);

            return new HttpEntity<>(imageBytes, headers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginOutDto> login(@RequestBody @Valid UserLoginInDto userLoginInDto) {
        User findUser = userService.findByEmail(userLoginInDto.getEmail());
        if (findUser == null) throw new NotFoundRecordException("User is not found");

        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            md.update(userLoginInDto.getPassword().getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // This bytes[] has bytes in decimal format. Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            // Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (!findUser.getPassword().equals(generatedPassword))
            throw new DuplicateRecordException("Email or password is incorrect");

        return ResponseEntity.ok(userConverter.User_UserLoginOutDto(findUser));
    }

    @GetMapping("/allUser")
    public ResponseEntity<List<UserInfoDto>> allUsers(@RequestParam(name = "search") String search, @RequestParam(name = "email") String email) {
        User findUser = userService.findByEmail(email);
        List<Friend> friends = friendService.friends(findUser.getId());

        List<User> allUsers = userService.findAllUsers();
        List<UserInfoDto> userInfoDtoList =
                new java.util.ArrayList<>(allUsers.stream()
                        .map(userConverter::UserToUserInfoDto)
                        .filter(item -> item.getEmail().toLowerCase().contains(search.toLowerCase())
                                || (item.getDisplayName() + item.getDisplayNameCode()).toLowerCase().contains(search.toLowerCase())
                                || item.getFullName().toLowerCase().contains(search.toLowerCase()))
                        .filter(item -> !item.getEmail().equals(email))
                        .toList());

        for (Friend item : friends) {
            userInfoDtoList.removeIf(e -> e.getId().equals(item.getFirstUser().getId()) || e.getId().equals(item.getSecondUser().getId()));
        }

        return ResponseEntity.ok(userInfoDtoList);
    }
}
