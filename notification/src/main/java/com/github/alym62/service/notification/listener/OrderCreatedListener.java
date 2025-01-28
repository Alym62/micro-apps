package com.github.alym62.service.notification.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.github.alym62.service.notification.events.EventOrderCreated;
import com.github.alym62.service.notification.services.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
@RequiredArgsConstructor
public class OrderCreatedListener {
    private final EmailService emailService;

    @RabbitListener(queues = "${app.queue}")
    public void onOrderCreated(EventOrderCreated event) {
        emailService.sendEmailOfNotification(event.email(), "Pedido criado #" + event.protocol(),
                "Pedido feito com sucesso! Parabéns por ter comprado no nosso site");
    }
}
