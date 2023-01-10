package com.chatter.database.exception;

import lombok.*;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

    private String message;
}
