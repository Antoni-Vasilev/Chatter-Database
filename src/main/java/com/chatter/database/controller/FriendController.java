package com.chatter.database.controller;

import com.chatter.database.converter.FriendConverter;
import com.chatter.database.dto.friend.FriendUserInfo;
import com.chatter.database.model.Chat;
import com.chatter.database.model.Friend;
import com.chatter.database.model.User;
import com.chatter.database.service.ChatService;
import com.chatter.database.service.FriendService;
import com.chatter.database.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;
    private final FriendConverter friendConverter;
    private final ChatController chatController;
    private final ChatService chatService;

    @Autowired
    public FriendController(FriendService friendService, UserService userService, FriendConverter friendConverter, ChatController chatController, ChatService chatService) {
        this.friendService = friendService;
        this.userService = userService;
        this.friendConverter = friendConverter;
        this.chatController = chatController;
        this.chatService = chatService;
    }

    @PostMapping("/getAllByEmail")
    public ResponseEntity<List<FriendUserInfo>> getAllByEmail(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "search") String search
    ) {
        User findUser = userService.findByEmail(email);
        return ResponseEntity.ok(friendService.friends(findUser.getId()).stream()
                .filter(item ->
                        item.getFirstUser().getEmail().contains(email) ?
                                item.getSecondUser().getEmail().toLowerCase().contains(search.toLowerCase()) ||
                                        item.getSecondUser().getDisplayName().toLowerCase().contains(search.toLowerCase()) ||
                                        item.getSecondUser().getDisplayNameCode().toLowerCase().contains(search.toLowerCase()) ||
                                        item.getSecondUser().getFullName().toLowerCase().contains(search.toLowerCase())
                                : item.getFirstUser().getFullName().toLowerCase().contains(search.toLowerCase()) ||
                                item.getFirstUser().getEmail().toLowerCase().contains(search.toLowerCase()) ||
                                item.getFirstUser().getDisplayName().toLowerCase().contains(search.toLowerCase()) ||
                                item.getFirstUser().getDisplayNameCode().toLowerCase().contains(search.toLowerCase())
                )
                .map(friendConverter::FriendToFriendUserInfo).toList());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteFriend(
            @RequestParam(name = "firstUserEmail") String firstUserEmail,
            @RequestParam(name = "secondUserEmail") String secondUserEmail
    ) {
        User firstUser = userService.findByEmail(firstUserEmail);
        User secondUser = userService.findByEmail(secondUserEmail);

        Friend friend = friendService.findByFirstUserAndSecondUser(firstUser, secondUser);
        friendService.delete(friend);

        if (!chatController.checkChat(firstUserEmail, secondUserEmail).getBody()) {
            Chat chat = chatService.findByFirstUserAndSecondUser(firstUser, secondUser);
            Chat newChat = Chat.builder()
                    .id(chat.getId())
                    .firstUser(chat.getFirstUser())
                    .secondUser(chat.getSecondUser())
                    .build();
            chatService.registerChat(newChat);
            chatService.delete(newChat);
        }

        return ResponseEntity.ok(true);
    }
}
