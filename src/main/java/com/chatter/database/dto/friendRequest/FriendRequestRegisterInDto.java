package com.chatter.database.dto.friendRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FriendRequestRegisterInDto {
    private String fromEmail;
    private String toEmail;
}
