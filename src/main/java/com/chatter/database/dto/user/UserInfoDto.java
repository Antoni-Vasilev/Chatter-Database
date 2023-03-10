package com.chatter.database.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserInfoDto {
    private Long id;
    private String fullName;
    private String displayName;
    private String displayNameCode;
    private String email;
    private Date lastOpen;
}
