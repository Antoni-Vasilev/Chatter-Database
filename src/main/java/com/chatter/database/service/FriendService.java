package com.chatter.database.service;

import com.chatter.database.model.Friend;
import com.chatter.database.model.User;

import java.util.List;

public interface FriendService {

    void registerFriend(Friend friend);

    List<Friend> friends(long UserID);

    Friend findByFirstUserAndSecondUser(User firstUser, User secondUser);

    void delete(Friend friend);
}
