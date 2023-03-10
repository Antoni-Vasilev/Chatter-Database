package com.chatter.database.service.Impl;

import com.chatter.database.model.Message;
import com.chatter.database.repository.MessageRepository;
import com.chatter.database.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message registerMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public void delete(Message message) {
        Message mess = Message.builder()
                .id(message.getId())
                .sender(message.getSender())
                .type(message.getType())
                .sendDate(message.getSendDate())
                .message(message.getMessage())
                .fileName(message.getFileName())
                .build();
        registerMessage(mess);
        messageRepository.delete(message);
    }
}
