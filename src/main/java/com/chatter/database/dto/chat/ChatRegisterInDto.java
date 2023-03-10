package com.chatter.database.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChatRegisterInDto {

    private String firstUserEmail;
    private String secondUserEmail;
}
