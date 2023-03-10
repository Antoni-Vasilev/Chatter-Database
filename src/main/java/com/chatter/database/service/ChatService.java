package com.chatter.database.service;

import com.chatter.database.model.Chat;
import com.chatter.database.model.Message;
import com.chatter.database.model.User;

import java.util.List;

public interface ChatService {

    List<Chat> findAll();

    Chat findAllById(long id);

    Chat registerChat(Chat chat);

    Chat findByFirstUserAndSecondUser(User firstUser, User secondUser);

    void addMessage(Message message, long chatId);

    void delete(Chat chat);
}
