package com.github.alym62.gateway.api.entity;

import com.github.alym62.gateway.api.entity.enums.StatusOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "tb_orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private StatusOrder status;

    @Column(nullable = false)
    private String description;

    @Column(updatable = false)
    private String protocol;

    private BigDecimal price;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private Boolean removed;

    @PrePersist
    public void onPrePersist() {
        this.setCreatedAt(LocalDateTime.now());
        this.setUpdatedAt(LocalDateTime.now());
        this.setRemoved(Boolean.FALSE);
        this.setProtocol(String.format("BRA%s", LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"))));
    }

    @PreUpdate
    public void onPreUpdate() {
        this.setUpdatedAt(LocalDateTime.now());
    }
}
