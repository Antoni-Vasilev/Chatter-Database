package com.chatter.database.controller;

import com.chatter.database.converter.FriendConverter;
import com.chatter.database.dto.friend.FriendUserInfo;
import com.chatter.database.model.User;
import com.chatter.database.service.FriendService;
import com.chatter.database.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;
    private final FriendConverter friendConverter;

    @Autowired
    public FriendController(FriendService friendService, UserService userService, FriendConverter friendConverter) {
        this.friendService = friendService;
        this.userService = userService;
        this.friendConverter = friendConverter;
    }

    @PostMapping("/getAllByEmail")
    public ResponseEntity<List<FriendUserInfo>> getAllByEmail(@RequestParam(name = "email") String email) {
        User findUser = userService.findByEmail(email);
        return ResponseEntity.ok(friendService.friends(findUser.getId()).stream().map(friendConverter::FriendToFriendUserInfo).toList());
    }
}
