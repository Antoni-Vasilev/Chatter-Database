package com.chatter.database.service;

import com.chatter.database.model.Friend;

import java.util.List;

public interface FriendService {

    void registerFriend(Friend friend);

    List<Friend> friends(long UserID);
}
