package com.chatter.database.dto.chat;

import com.chatter.database.dto.message.MessageInfo;
import com.chatter.database.dto.user.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChatMyChatsDto {

    private Long id;
    private UserInfoDto userInfo;
    private MessageInfo lastMessage;
}
