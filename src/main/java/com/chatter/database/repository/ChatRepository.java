package com.chatter.database.repository;

import com.chatter.database.model.Chat;
import com.chatter.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat findAllById(long id);

    Chat findByFirstUserAndSecondUserOrSecondUserAndFirstUser(User firstUser, User secondUser, User FirstUser, User SecondUser);
}
