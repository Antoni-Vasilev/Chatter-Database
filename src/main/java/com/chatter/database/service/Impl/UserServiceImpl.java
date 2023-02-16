package com.chatter.database.service.Impl;

import com.chatter.database.exception.DuplicateRecordException;
import com.chatter.database.model.User;
import com.chatter.database.repository.UserRepository;
import com.chatter.database.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAllByDisplayName(String displayName) {
        return userRepository.findAllByDisplayName(displayName);
    }

    @Override
    public User save(User user) {
        User findUser = userRepository.findUserByEmail(user.getEmail());
        if (findUser != null) throw new DuplicateRecordException("This email is already used");

        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void uploadProfileImage(MultipartFile file) {
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
