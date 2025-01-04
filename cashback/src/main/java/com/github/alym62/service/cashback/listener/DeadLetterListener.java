package com.github.alym62.service.cashback.listener;

import com.github.alym62.service.cashback.events.EventOrderCreated;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
@RequiredArgsConstructor
public class DeadLetterListener {
    private static final String X_RETRY_HEADER = "x-dlq-retry";

    @Value("${app.queue-dead-letter}")
    private String queue;

    @Value("${app.queue-parking-lot}")
    private String queueParkingLot;

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "${app.queue-dead-letter}")
    public void processEventsInDeadLetterQueue(EventOrderCreated event, @Headers Map<String, Object> headers) {
        var retryHeader = (Integer) headers.get(X_RETRY_HEADER);
        if (retryHeader == null) {
            retryHeader = 0;
        }

        log.info("Reprocessando evento >>>");
        if (retryHeader < 3) {
            Map<String, Object> updateHeaders = new HashMap<>(headers);

            int tryCount = retryHeader + 1;
            updateHeaders.put(X_RETRY_HEADER, tryCount);

            // Reprocessamento
            final MessagePostProcessor messagePostProcessor = (message) -> {
                var messageProperties = message.getMessageProperties();
                updateHeaders.forEach(messageProperties::setHeader);
                return message;
            };

            log.info("Reenviando o event para a DLQ >>>");
            rabbitTemplate.convertAndSend(queue, event, messagePostProcessor);
        } else {
            log.info("Reprocessamento falhou, enviando para PLQ >>>");
            this.rabbitTemplate.convertAndSend(queueParkingLot, event);
        }
    }
}
