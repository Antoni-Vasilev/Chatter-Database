package com.chatter.database.service.Impl;

import com.chatter.database.model.Friend;
import com.chatter.database.model.User;
import com.chatter.database.repository.FriendRepository;
import com.chatter.database.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;

    @Autowired
    public FriendServiceImpl(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    @Override
    public void registerFriend(Friend friend) {
        friendRepository.save(friend);
    }

    @Override
    public List<Friend> friends(long UserID) {
        return friendRepository.findAllByFirstUser_IdOrSecondUser_Id(UserID, UserID);
    }
}
