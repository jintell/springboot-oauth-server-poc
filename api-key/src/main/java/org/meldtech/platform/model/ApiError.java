package org.meldtech.platform.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiError(UUID id,
                       int status,
                       String message,
                       String details,
                       LocalDateTime timestamp,
                       String path) {
    public ApiError(int status, String message, String details, String path) {
        this(UUID.randomUUID(), status, message, details, LocalDateTime.now(Clock.systemUTC()), path);
    }
}
