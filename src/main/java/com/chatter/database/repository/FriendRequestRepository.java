package com.chatter.database.repository;

import com.chatter.database.model.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    FriendRequest findByFrom_IdAndTo_Id(long fromID, long toID);

    List<FriendRequest> findAllByTo_Email(String email);
}
