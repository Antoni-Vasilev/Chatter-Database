package com.chatter.database.dto.friend;

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
public class FriendUserInfo {

    private Long id;
    private UserInfoDto firstUser;
    private UserInfoDto secondUser;
    private Date startDate;
}
