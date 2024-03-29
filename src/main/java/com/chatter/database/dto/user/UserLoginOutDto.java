package com.chatter.database.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserLoginOutDto {

    private String fullName;
    private String displayName;
    private String displayNameCode;
    private String phone;
    private Date birthdayDate;
    private String country;
    private String email;
}
