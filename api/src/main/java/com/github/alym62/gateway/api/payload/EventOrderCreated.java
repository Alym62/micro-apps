package com.github.alym62.gateway.api.payload;

import com.github.alym62.gateway.api.entity.enums.StatusOrder;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record EventOrderCreated(
        StatusOrder status,
        BigDecimal price,
        String description,
        String protocol,
        LocalDateTime createdAt
) {
}
