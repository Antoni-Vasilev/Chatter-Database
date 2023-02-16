package com.chatter.database.converter;

import com.chatter.database.dto.user.UserInfoDto;
import com.chatter.database.dto.user.UserLoginOutDto;
import com.chatter.database.dto.user.UserRegisterInDto;
import com.chatter.database.dto.user.UserRegisterOutDto;
import com.chatter.database.model.User;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class UserConverter {

    @SneakyThrows
    public User UserRegisterInDto_User(UserRegisterInDto userRegisterInDto, String displayNameCode) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            md.update(userRegisterInDto.getPassword().getBytes());

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

        return User.builder()
                .fullName(userRegisterInDto.getFullName())
                .displayName(userRegisterInDto.getDisplayName())
                .displayNameCode(displayNameCode)
                .phone(userRegisterInDto.getPhone())
                .country(userRegisterInDto.getCountry())
                .birthdayDate(userRegisterInDto.getBirthdayDate())
                .email(userRegisterInDto.getEmail())
                .password(generatedPassword)
                .build();
    }

    public UserRegisterOutDto User_UserRegisterOutDto(User user) {
        return UserRegisterOutDto.builder()
                .fullName(user.getFullName())
                .displayName(user.getDisplayName())
                .displayNameCode(user.getDisplayNameCode())
                .phone(user.getPhone())
                .country(user.getCountry())
                .birthdayDate(user.getBirthdayDate())
                .email(user.getEmail())
                .build();
    }

    public UserLoginOutDto User_UserLoginOutDto(User user) {
        return UserLoginOutDto.builder()
                .fullName(user.getFullName())
                .displayName(user.getDisplayName())
                .displayNameCode(user.getDisplayNameCode())
                .phone(user.getPhone())
                .country(user.getCountry())
                .birthdayDate(user.getBirthdayDate())
                .email(user.getEmail())
                .build();
    }

    public UserInfoDto UserToUserInfoDto(User user) {
        return UserInfoDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .displayName(user.getDisplayName())
                .displayNameCode(user.getDisplayNameCode())
                .email(user.getEmail())
                .lastOpen(user.getLastOpen())
                .build();
    }
}
