package com.chatter.database.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SendMessageDto {
    private String message;
    private String senderEmail;
    private byte[] fileBytes;
    private MessageType type;
}
