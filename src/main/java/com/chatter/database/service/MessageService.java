package com.chatter.database.service;

import com.chatter.database.model.Message;

public interface MessageService {

    Message registerMessage(Message message);

    void delete(Message message);
}
