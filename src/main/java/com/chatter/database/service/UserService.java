package com.chatter.database.service;

import com.chatter.database.model.User;

import java.util.List;

public interface UserService {

    List<User> findAllByDisplayName(String displayName);

    User save(User user);

    void update(User user);

    User findByEmail(String email);

    List<User> findAllUsers();
}
