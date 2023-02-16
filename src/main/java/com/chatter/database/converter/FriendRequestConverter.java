package com.chatter.database.converter;

import com.chatter.database.dto.friendRequest.FriendRequestAll;
import com.chatter.database.dto.friendRequest.FriendRequestRegisterOutDto;
import com.chatter.database.model.FriendRequest;
import com.chatter.database.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class FriendRequestConverter {

    private final UserConverter userConverter;

    @Autowired
    public FriendRequestConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    public FriendRequest FriendRequestRegisterInDtoToFriendRequest(User from, User to) {
        return FriendRequest.builder()
                .from(from)
                .to(to)
                .sendDate(new Date())
                .build();
    }

    public FriendRequestRegisterOutDto FriendRequestToFriendRequestRegisterOutDto(FriendRequest friendRequest) {
        return FriendRequestRegisterOutDto.builder()
                .id(friendRequest.getId())
                .from(userConverter.UserToUserInfoDto(friendRequest.getFrom()))
                .to(userConverter.UserToUserInfoDto(friendRequest.getTo()))
                .sendDate(friendRequest.getSendDate())
                .build();
    }

    public FriendRequestAll FriendRequestToFriendRequestAll(FriendRequest friendRequest) {
        return FriendRequestAll.builder()
                .id(friendRequest.getId())
                .from(userConverter.UserToUserInfoDto(friendRequest.getFrom()))
                .to(userConverter.UserToUserInfoDto(friendRequest.getTo()))
                .sendDate(friendRequest.getSendDate())
                .build();
    }
}
