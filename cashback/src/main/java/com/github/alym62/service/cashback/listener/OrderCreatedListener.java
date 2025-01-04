package com.github.alym62.service.cashback.listener;

import com.github.alym62.service.cashback.events.EventOrderCreated;
import com.github.alym62.service.cashback.exceptions.EventException;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Log4j2
public class OrderCreatedListener {
    @RabbitListener(queues = "${app.queue}")
    public void onOrderCreated(EventOrderCreated event) {
        log.info(event);
        if (event.price().equals(BigDecimal.ZERO)) {
            throw new EventException("Ops! O valor não pode ser igual a zero. Valor recebido: " + event.price());
        }
    }
}
