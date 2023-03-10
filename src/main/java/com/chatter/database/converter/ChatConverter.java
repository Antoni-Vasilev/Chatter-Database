package com.chatter.database.converter;

import com.chatter.database.dto.chat.ChatMyChatsDto;
import com.chatter.database.dto.chat.ChatRegisterOutDto;
import com.chatter.database.model.Chat;
import com.chatter.database.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ChatConverter {

    private final UserConverter userConverter;
    private final MessageConverter messageConverter;

    @Autowired
    public ChatConverter(UserConverter userConverter, MessageConverter messageConverter) {
        this.userConverter = userConverter;
        this.messageConverter = messageConverter;
    }

    public ChatMyChatsDto ChatToChatMyChatsDto(Chat chat, String email) {
        return ChatMyChatsDto.builder()
                .id(chat.getId())
                .userInfo(userConverter.UserToUserInfoDto(chat.getFirstUser().getEmail().equals(email) ? chat.getSecondUser() : chat.getFirstUser()))
                .lastMessage(chat.getMessages().size() != 0 ? messageConverter.MessageToMessageInfo(chat.getMessages().get(chat.getMessages().size() - 1)) : null)
                .build();
    }

    public Chat BuildChat(User firstUser, User secondUser) {
        return Chat.builder()
                .firstUser(firstUser)
                .secondUser(secondUser)
                .messages(new ArrayList<>())
                .build();
    }

    public Chat BuildChatById(Long id, User firstUser, User secondUser) {
        return Chat.builder()
                .id(id)
                .firstUser(firstUser)
                .secondUser(secondUser)
                .messages(new ArrayList<>())
                .build();
    }

    public ChatRegisterOutDto ChatToChatRegisterOutDto(Chat chat, String email) {
        return ChatRegisterOutDto.builder()
                .id(chat.getId())
                .userInfo(userConverter.UserToUserInfoDto(chat.getFirstUser().getEmail().equals(email) ? chat.getSecondUser() : chat.getFirstUser()))
                .lastMessage(chat.getMessages().size() != 0 ? messageConverter.MessageToMessageInfo(chat.getMessages().get(chat.getMessages().size() - 1)) : null)
                .build();
    }
}
