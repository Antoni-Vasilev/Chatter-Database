package com.chatter.database.dto.friendRequest;

import com.chatter.database.dto.user.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FriendRequestRegisterOutDto {
    private Long id;
    private UserInfoDto from;
    private UserInfoDto to;
    private Date sendDate;
}
