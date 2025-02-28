package com.github.alym62.service.cashback.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventOrderCreated(
        String status,
        BigDecimal price,
        String description,
        String protocol,
        LocalDateTime createdAt
) {
}
