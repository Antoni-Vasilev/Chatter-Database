package com.chatter.database.service.Impl;

import com.chatter.database.model.Chat;
import com.chatter.database.model.Message;
import com.chatter.database.model.User;
import com.chatter.database.repository.ChatRepository;
import com.chatter.database.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    @Override
    public Chat findAllById(long id) {
        return chatRepository.findAllById(id);
    }

    @Override
    public Chat registerChat(Chat chat) {
        return chatRepository.save(chat);
    }

    @Override
    public Chat findByFirstUserAndSecondUser(User firstUser, User secondUser) {
        return chatRepository.findByFirstUserAndSecondUserOrSecondUserAndFirstUser(firstUser, secondUser, firstUser, secondUser);
    }

    @Override
    public void addMessage(Message message, long chatId) {
        Optional<Chat> chat = chatRepository.findById(chatId);
        chat.get().getMessages().add(message);
        registerChat(chat.get());
    }

    @Override
    public void delete(Chat chat) {
        chatRepository.delete(chat);
    }
}
