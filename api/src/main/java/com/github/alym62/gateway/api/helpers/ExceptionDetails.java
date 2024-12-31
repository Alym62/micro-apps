package com.github.alym62.gateway.api.helpers;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionDetails {
    private String title;
    private int status;
    private String details;
    private LocalDateTime timestamp;
}
