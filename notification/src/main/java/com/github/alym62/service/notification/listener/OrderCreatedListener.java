package com.github.alym62.service.notification.listener;

import com.github.alym62.service.notification.events.EventOrderCreated;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class OrderCreatedListener {

    @RabbitListener(queues = "${app.queue}")
    public void onOrderCreated(EventOrderCreated event) {
        log.info(event);
    }
}
