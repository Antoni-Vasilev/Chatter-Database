package com.chatter.database.dto.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserRegisterOutDto {

    private String fullName;
    private String displayName;
    private String displayNameCode;
    private String phone;
    private String country;
    private String email;
}