package com.chatter.database;

import com.chatter.database.server.RealtimeChats;
import com.chatter.database.server.RealtimeMessages;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DatabaseApplication {

    public static void main(String[] args) {
        new Thread(new RealtimeMessages()).start();
        new Thread(new RealtimeChats()).start();

        SpringApplication.run(DatabaseApplication.class, args);
    }

}
