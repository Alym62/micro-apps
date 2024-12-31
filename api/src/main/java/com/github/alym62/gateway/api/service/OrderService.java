package com.github.alym62.gateway.api.service;

import com.github.alym62.gateway.api.entity.Order;
import com.github.alym62.gateway.api.exceptions.BusinessException;
import com.github.alym62.gateway.api.payload.EventOrderCreated;
import com.github.alym62.gateway.api.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
                .protocol(entity.getProtocol())
                .createdAt(entity.getCreatedAt()).build();

        rabbitTemplate.convertAndSend(exchangeFanout, "", payload);

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
