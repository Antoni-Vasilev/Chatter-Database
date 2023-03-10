package com.chatter.database.dto.message;

import com.chatter.database.dto.user.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MessageInfo {

    private Long id;
    private String message;
    private UserInfoDto sender;
    private String fileName;
    private MessageType type;
    private Date sendDate;
    private List<UserInfoDto> likes;
}
