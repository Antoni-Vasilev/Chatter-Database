package com.chatter.database.repository;

import com.chatter.database.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Friend> findAllByFirstUser_IdOrSecondUser_Id(long firstUserId, long secondUserId);
}
