package com.chatter.database.converter;

import com.chatter.database.dto.message.MessageInfo;
import com.chatter.database.dto.message.MessageType;
import com.chatter.database.dto.message.SendMessageDto;
import com.chatter.database.model.Message;
import com.chatter.database.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Component
public class MessageConverter {

    private final UserConverter userConverter;
    private final UserService userService;

    @Autowired
    public MessageConverter(UserConverter userConverter, UserService userService) {
        this.userConverter = userConverter;
        this.userService = userService;
    }

    public MessageInfo MessageToMessageInfo(Message message) {
        return MessageInfo.builder()
                .id(message.getId())
                .message(message.getMessage())
                .sendDate(message.getSendDate())
                .sender(userConverter.UserToUserInfoDto(message.getSender()))
                .fileName(message.getFileName())
                .type(message.getType())
                .likes(message.getLikes().stream().map(userConverter::UserToUserInfoDto).toList())
                .build();
    }

    public Message SendMessageDtoToMessage(SendMessageDto sendMessageDto) {
        String fileName = "";

        if (sendMessageDto.getType() == MessageType.FILE_MESSAGE
                || sendMessageDto.getType() == MessageType.IMAGE_MESSAGE
                || sendMessageDto.getType() == MessageType.VIDEO
                || sendMessageDto.getType() == MessageType.VIDEO_MESSAGE
                || sendMessageDto.getType() == MessageType.FILE
                || sendMessageDto.getType() == MessageType.IMAGE) {
            fileName = UUID.randomUUID().toString() + UUID.randomUUID() + UUID.randomUUID();
            try {
                if (sendMessageDto.getType() == MessageType.IMAGE_MESSAGE) {
                    Files.write(Path.of("files/chatFiles" + fileName + ".gif"), sendMessageDto.getFileBytes());
                } else if (sendMessageDto.getType() == MessageType.VIDEO_MESSAGE) {
                    Files.write(Path.of("files/chatFiles" + fileName + ".mp4"), sendMessageDto.getFileBytes());
                } else if (sendMessageDto.getType() == MessageType.FILE_MESSAGE) {
                    Files.write(Path.of("files/chatFiles" + fileName + ".txt"), sendMessageDto.getFileBytes());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return Message.builder()
                .message(sendMessageDto.getMessage())
                .fileName(fileName)
                .type(sendMessageDto.getType())
                .sender(userService.findByEmail(sendMessageDto.getSenderEmail()))
                .sendDate(new Date())
                .likes(new ArrayList<>())
                .build();
    }
}
