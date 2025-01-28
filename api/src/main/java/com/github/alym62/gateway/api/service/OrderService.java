package com.github.alym62.gateway.api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.alym62.gateway.api.entity.Order;
import com.github.alym62.gateway.api.exceptions.BusinessException;
import com.github.alym62.gateway.api.payload.EventOrderCreated;
import com.github.alym62.gateway.api.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Value("${app.exchange.fanout}")
    private String exchangeFanout;

    private final OrderRepository repository;
    private final RabbitTemplate rabbitTemplate;

    public List<Order> getList() {
        return repository.findAll();
    }

    public Order getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Ops! Não foi possível achar esse registro."));
    }

    @Transactional
    public Order create(Order entity) {
        var order = repository.save(entity);
        var payload = EventOrderCreated.builder()
                .status(entity.getStatus())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .email(entity.getEmail())
                .protocol(entity.getProtocol())
                .createdAt(entity.getCreatedAt()).build();

        final int priority = order.getPrice().compareTo(new BigDecimal("1000")) >= 0 ? 10 : 1;

        final MessagePostProcessor messagePostProcessor = (message) -> {
            var messageProperties = message.getMessageProperties();
            messageProperties.setPriority(priority);
            return message;
        };

        rabbitTemplate.convertAndSend(exchangeFanout, "", payload, messagePostProcessor);

        return order;
    }

    @Transactional
    public Order update(UUID id, Order entity) {
        var order = this.getById(id);
        BeanUtils.copyProperties(entity, order, "id", "createdAt", "protocol", "removed");
        return repository.save(order);
    }

    @Transactional
    public void delete(UUID id) {
        var order = this.getById(id);
        order.setRemoved(Boolean.TRUE);
        repository.save(order);
    }
}
