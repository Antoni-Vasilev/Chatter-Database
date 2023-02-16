package com.chatter.database.service;

import com.chatter.database.model.FriendRequest;

import java.util.List;

public interface FriendRequestService {

    FriendRequest registerRequest(FriendRequest friendRequest);

    void deleteRequest(long fromID, long toID);

    boolean checkByIDs(long fromID, long toID);

    List<FriendRequest> all(String email);
}
