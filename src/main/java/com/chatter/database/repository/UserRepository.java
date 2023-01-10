package com.chatter.database.repository;

import com.chatter.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByDisplayName(String displayName);

    User findUserByEmail(String email);
}
