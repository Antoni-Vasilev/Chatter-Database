package com.chatter.database.service;

import com.chatter.database.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<User> findAllByDisplayName(String displayName);

    User save(User user);

    User findByEmail(String email);

    void uploadProfileImage(MultipartFile file);

    List<User> findAllUsers();
}
