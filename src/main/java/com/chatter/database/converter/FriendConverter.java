package com.chatter.database.converter;

import com.chatter.database.dto.friend.FriendUserInfo;
import com.chatter.database.model.Friend;
import com.chatter.database.model.User;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class FriendConverter {

    private final UserConverter userConverter;

    public FriendConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    public Friend BuildFriend(User firstUser, User secondUser) {
        return Friend.builder()
                .firstUser(firstUser)
                .secondUser(secondUser)
                .startDate(new Date())
                .build();
    }

    public FriendUserInfo FriendToFriendUserInfo(Friend friend) {
        return FriendUserInfo.builder()
                .id(friend.getId())
                .firstUser(userConverter.UserToUserInfoDto(friend.getFirstUser()))
                .secondUser(userConverter.UserToUserInfoDto(friend.getSecondUser()))
                .startDate(friend.getStartDate())
                .build();
    }
}
