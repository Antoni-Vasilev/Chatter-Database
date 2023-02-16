package com.chatter.database.service.Impl;

import com.chatter.database.model.FriendRequest;
import com.chatter.database.repository.FriendRequestRepository;
import com.chatter.database.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    @Autowired
    public FriendRequestServiceImpl(FriendRequestRepository friendRequestRepository) {
        this.friendRequestRepository = friendRequestRepository;
    }

    @Override
    public FriendRequest registerRequest(FriendRequest friendRequest) {
        return friendRequestRepository.save(friendRequest);
    }

    @Override
    public void deleteRequest(long fromID, long toID) {
        friendRequestRepository.delete(friendRequestRepository.findByFrom_IdAndTo_Id(fromID, toID));
    }

    @Override
    public boolean checkByIDs(long fromID, long toID) {
        return friendRequestRepository.findByFrom_IdAndTo_Id(fromID, toID) == null;
    }

    @Override
    public List<FriendRequest> all(String email) {
        return friendRequestRepository.findAllByTo_Email(email);
    }
}
