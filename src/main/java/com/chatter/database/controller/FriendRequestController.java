package com.chatter.database.controller;

import com.chatter.database.converter.FriendConverter;
import com.chatter.database.converter.FriendRequestConverter;
import com.chatter.database.dto.friendRequest.*;
import com.chatter.database.exception.DuplicateRecordException;
import com.chatter.database.model.Friend;
import com.chatter.database.model.FriendRequest;
import com.chatter.database.model.User;
import com.chatter.database.service.FriendRequestService;
import com.chatter.database.service.FriendService;
import com.chatter.database.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friendRequest")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;
    private final FriendRequestConverter friendRequestConverter;
    private final UserService userService;
    private final FriendService friendService;
    private final FriendConverter friendConverter;

    @Autowired
    public FriendRequestController(FriendRequestService friendRequestService, FriendRequestConverter friendRequestConverter,
                                   UserService userService, FriendService friendService, FriendConverter friendConverter) {
        this.friendRequestService = friendRequestService;
        this.friendRequestConverter = friendRequestConverter;
        this.userService = userService;
        this.friendService = friendService;
        this.friendConverter = friendConverter;
    }

    @PostMapping("/registerRequest")
    public ResponseEntity<FriendRequestRegisterOutDto> registerRequest(@RequestBody FriendRequestRegisterInDto friendRequestRegisterInDto) {
        User from = userService.findByEmail(friendRequestRegisterInDto.getFromEmail());
        User to = userService.findByEmail(friendRequestRegisterInDto.getToEmail());

        boolean checkExist = friendRequestService.checkByIDs(from.getId(), to.getId());

        if (checkExist) {
            FriendRequest friendRequest = friendRequestConverter.FriendRequestRegisterInDtoToFriendRequest(from, to);
            FriendRequest savedRequest = friendRequestService.registerRequest(friendRequest);

            return ResponseEntity.ok(friendRequestConverter.FriendRequestToFriendRequestRegisterOutDto(savedRequest));
        } else {
            friendRequestService.deleteRequest(from.getId(), to.getId());
            throw new DuplicateRecordException("The request was removed");
        }
    }

    @PostMapping("/checkRequest")
    public ResponseEntity<Boolean> checkRequest(@RequestBody FriendRequestCheck friendRequestCheck) {
        User from = userService.findByEmail(friendRequestCheck.getFromEmail());
        User to = userService.findByEmail(friendRequestCheck.getToEmail());

        return ResponseEntity.ok(friendRequestService.checkByIDs(from.getId(), to.getId()));
    }

    @PostMapping("/acceptRequest")
    public ResponseEntity<Boolean> acceptRequest(@RequestBody FriendRequestAccept friendRequestAccept) {
        User from = userService.findByEmail(friendRequestAccept.getFromEmail());
        User to = userService.findByEmail(friendRequestAccept.getToEmail());

        friendRequestService.deleteRequest(from.getId(), to.getId());

        Friend friend = friendConverter.BuildFriend(from, to);
        friendService.registerFriend(friend);

        return ResponseEntity.ok(true);
    }

    @PostMapping("/rejectRequest")
    public ResponseEntity<Boolean> rejectRequest(@RequestParam(name = "requestId") Long id) {
        friendRequestService.deleteRequestById(id);

        return ResponseEntity.ok(true);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FriendRequestAll>> all(@RequestParam(name = "search") String search, @RequestParam(name = "email") String email) {
        List<FriendRequest> friendRequests = friendRequestService.all(email);
        friendRequests = friendRequests.stream().filter(item -> item.getFrom().getEmail().contains(search)
                || (item.getFrom().getDisplayName() + item.getFrom().getDisplayNameCode()).contains(search)
                || item.getFrom().getFullName().contains(search)).toList();

        List<FriendRequestAll> friendRequestAll = friendRequests.stream().map(friendRequestConverter::FriendRequestToFriendRequestAll).toList();

        return ResponseEntity.ok(friendRequestAll);
    }
}
