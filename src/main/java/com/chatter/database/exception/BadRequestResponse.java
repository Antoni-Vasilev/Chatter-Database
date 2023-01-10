package com.chatter.database.exception;

import lombok.*;

import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class BadRequestResponse {

    private String message;
    private Map<String, Set<String>> errors;
}
