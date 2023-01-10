package com.chatter.database.converter;

import com.chatter.database.dto.User.UserLoginOutDto;
import com.chatter.database.dto.User.UserRegisterInDto;
import com.chatter.database.dto.User.UserRegisterOutDto;
import com.chatter.database.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public User UserRegisterInDto_User(UserRegisterInDto userRegisterInDto, String displayNameCode) {
        return User.builder()
                .fullName(userRegisterInDto.getFullName())
                .displayName(userRegisterInDto.getDisplayName())
                .displayNameCode(displayNameCode)
                .phone(userRegisterInDto.getPhone())
                .country(userRegisterInDto.getCountry())
                .email(userRegisterInDto.getEmail())
                .password(userRegisterInDto.getPassword())
                .build();
    }

    public UserRegisterOutDto User_UserRegisterOutDto(User user) {
        return UserRegisterOutDto.builder()
                .fullName(user.getFullName())
                .displayName(user.getDisplayName())
                .displayNameCode(user.getDisplayNameCode())
                .phone(user.getPhone())
                .country(user.getCountry())
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
                .email(user.getEmail())
                .build();
    }
}
