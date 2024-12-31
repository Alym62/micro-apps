package com.github.alym62.gateway.api.entity.enums;

public enum StatusOrder {
    PENDING("Pendente"),
    ERROR("Erro"),
    SUCCESS("Sucesso");

    String description;

    StatusOrder(String description) {
        this.description = description;
    }
}
