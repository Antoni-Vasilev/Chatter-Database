package com.chatter.database.controller;

import com.chatter.database.converter.ChatConverter;
import com.chatter.database.converter.MessageConverter;
import com.chatter.database.dto.chat.ChatMyChatsDto;
import com.chatter.database.dto.chat.ChatRegisterInDto;
import com.chatter.database.dto.chat.ChatRegisterOutDto;
import com.chatter.database.dto.message.MessageInfo;
import com.chatter.database.dto.message.SendMessageDto;
import com.chatter.database.exception.DuplicateRecordException;
import com.chatter.database.model.Chat;
import com.chatter.database.model.Message;
import com.chatter.database.model.User;
import com.chatter.database.service.ChatService;
import com.chatter.database.service.MessageService;
import com.chatter.database.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final UserService userService;
    private final ChatService chatService;
    private final ChatConverter chatConverter;
    private final MessageConverter messageConverter;
    private final MessageService messageService;

    @Autowired
    public ChatController(UserService userService, ChatService chatService, ChatConverter chatConverter, MessageConverter messageConverter, MessageService messageService) {
        this.userService = userService;
        this.chatService = chatService;
        this.chatConverter = chatConverter;
        this.messageConverter = messageConverter;
        this.messageService = messageService;
    }

    @GetMapping("/myChats")
    public ResponseEntity<List<ChatMyChatsDto>> myChats(@RequestParam(name = "email") String email, @RequestParam(name = "search") String search) {
        List<Chat> chats = chatService.findAll();
        chats = chats.stream()
                .filter(it -> it.getFirstUser().getEmail().equals(email) || it.getSecondUser().getEmail().equals(email))
                .filter(it -> it.getFirstUser().getEmail().equals(email)
                        ? it.getSecondUser().getFullName().toLowerCase().contains(search.toLowerCase())
                        || it.getSecondUser().getEmail().toLowerCase().contains(search.toLowerCase())
                        || it.getSecondUser().getDisplayNameCode().toLowerCase().contains(search.toLowerCase())
                        || it.getSecondUser().getDisplayName().toLowerCase().contains(search.toLowerCase())
                        : it.getFirstUser().getFullName().toLowerCase().contains(search.toLowerCase())
                        || it.getFirstUser().getDisplayName().toLowerCase().contains(search.toLowerCase())
                        || it.getFirstUser().getDisplayNameCode().toLowerCase().contains(search.toLowerCase())
                        || it.getFirstUser().getEmail().toLowerCase().contains(search.toLowerCase()))
                .toList();

        List<ChatMyChatsDto> myChats = chats.stream().map(it -> chatConverter.ChatToChatMyChatsDto(it, email)).toList();

        return ResponseEntity.ok(myChats);
    }

    @PostMapping("/registerChat")
    public ResponseEntity<ChatRegisterOutDto> registerChat(@RequestBody ChatRegisterInDto chatRegisterInDto, @RequestParam(name = "email") String email) {
        User firstUser = userService.findByEmail(chatRegisterInDto.getFirstUserEmail());
        User secondUser = userService.findByEmail(chatRegisterInDto.getSecondUserEmail());

        List<Chat> chats = chatService.findAll()
                .stream().filter(it -> it.getFirstUser().getId().equals(firstUser.getId()) && it.getSecondUser().getId().equals(secondUser.getId())
                        || it.getFirstUser().getId().equals(secondUser.getId()) && it.getSecondUser().getId().equals(firstUser.getId())).toList();

        if (chats.size() != 0) throw new DuplicateRecordException("Chat exist");

        Chat chat = chatConverter.BuildChat(firstUser, secondUser);
        Chat savedChat = chatService.registerChat(chat);

        ChatRegisterOutDto chatRegisterOutDto = chatConverter.ChatToChatRegisterOutDto(savedChat, email);
        return ResponseEntity.ok(chatRegisterOutDto);
    }

    @GetMapping("/checkChat")
    public ResponseEntity<Boolean> checkChat(@RequestParam(name = "firstUserEmail") String firstUserEmail,
                                             @RequestParam(name = "secondUserEmail") String secondUserEmail) {
        User firstUser = userService.findByEmail(firstUserEmail);
        User secondUser = userService.findByEmail(secondUserEmail);

        List<Chat> chats = chatService.findAll();
        chats = chats.stream()
                .filter(it ->
                        it.getFirstUser().getEmail().equals(firstUser.getEmail()) && it.getSecondUser().getEmail().equals(secondUser.getEmail())
                                || it.getSecondUser().getEmail().equals(firstUser.getEmail()) && it.getFirstUser().getEmail().equals(secondUser.getEmail()))
                .toList();

        return ResponseEntity.ok(chats.size() == 0);
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<Boolean> sendMessage(@RequestBody SendMessageDto sendMessageDto, @RequestParam("chatId") long chatId) {
        Message message = messageConverter.SendMessageDtoToMessage(sendMessageDto);

        chatService.addMessage(messageService.registerMessage(message), chatId);

        return ResponseEntity.ok(true);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<MessageInfo>> getAllMessagesFromChat(@RequestParam(name = "chatId") long id) {
        Chat chat = chatService.findAllById(id);
        List<MessageInfo> messageInfoList = chat.getMessages().stream().map(messageConverter::MessageToMessageInfo).toList();

        return ResponseEntity.ok(messageInfoList);
    }

    @GetMapping("/message/new")
    public ResponseEntity<List<MessageInfo>> getAllNewMessages(@RequestParam(name = "chatId") long id, @RequestParam(name = "lastMessage") long lastMessageId) {
        Chat chat = chatService.findAllById(id);
        List<MessageInfo> messageInfoList = chat.getMessages().stream()
                .filter(item -> item.getId() > lastMessageId)
                .map(messageConverter::MessageToMessageInfo).toList();

        return ResponseEntity.ok(messageInfoList);
    }

    @GetMapping("/chatById")
    public ResponseEntity<ChatMyChatsDto> getChatById(@RequestParam(name = "chatId") long id, @RequestParam(name = "email") String email) {
        Chat chat = chatService.findAllById(id);
        Chat newChat = chatConverter.BuildChatById(chat.getId(), chat.getFirstUser(), chat.getSecondUser());

        return ResponseEntity.ok(chatConverter.ChatToChatMyChatsDto(newChat, email));
    }
}
