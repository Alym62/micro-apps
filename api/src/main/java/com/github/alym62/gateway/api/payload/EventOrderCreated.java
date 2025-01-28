package com.github.alym62.gateway.api.payload;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.github.alym62.gateway.api.entity.enums.StatusOrder;

import lombok.Builder;

@Builder
public record EventOrderCreated(
                StatusOrder status,
                BigDecimal price,
                String description,
                String email,
                String protocol,
                LocalDateTime createdAt) {
}
