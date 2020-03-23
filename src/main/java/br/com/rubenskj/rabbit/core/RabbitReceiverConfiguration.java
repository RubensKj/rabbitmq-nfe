package br.com.rubenskj.rabbit.core;

import br.com.rubenskj.rabbit.core.events.models.NFeReceivedEvent;
import br.com.rubenskj.rabbit.core.events.models.NFeUpdateStatusEvent;
import br.com.rubenskj.rabbit.core.events.publishers.EventPublisher;
import br.com.rubenskj.rabbit.core.models.NFe;
import br.com.rubenskj.rabbit.core.services.NFeService;
import br.com.rubenskj.rabbit.core.services.RabbitService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static br.com.rubenskj.rabbit.core.utils.RabbitExchangesDeclare.NFE_RECEIVED;
import static br.com.rubenskj.rabbit.core.utils.RabbitExchangesDeclare.NFE_UPDATE_STATUS;

@Slf4j
@Configuration
public class RabbitReceiverConfiguration {

    private final NFeService nFeService;
    private final RabbitService rabbitService;
    private final EventPublisher eventPublisher;

    public RabbitReceiverConfiguration(NFeService nFeService, RabbitService rabbitService, EventPublisher eventPublisher) {
        this.nFeService = nFeService;
        this.rabbitService = rabbitService;
        this.eventPublisher = eventPublisher;
    }

    @Bean
    public void handleNFeReceived() throws IOException {
        Connection connection = this.rabbitService.createConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("nfe-received", false, false, false, null);

        log.info("Waiting for " + NFE_RECEIVED + ".");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            log.info("Receiving message");
            String ticketUUID = new String(delivery.getBody(), StandardCharsets.UTF_8);

            NFe nFe = this.nFeService.findByTicketUUID(ticketUUID);

            NFeReceivedEvent nFeReceivedEvent = new NFeReceivedEvent(this, nFe);
            this.eventPublisher.publish(nFeReceivedEvent);
        };

        channel.basicConsume("nfe-received", true, deliverCallback, consumerTag -> {
            log.info("consumerTag -> {}", consumerTag);
        });
    }

    @Bean
    public void handleNFeUpdateStatus() throws IOException {
        Connection connection = this.rabbitService.createConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(NFE_UPDATE_STATUS, false, false, false, null);

        log.info("Waiting for " + NFE_UPDATE_STATUS + ".");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            log.info("Receiving message");
            String ticketUUID = new String(delivery.getBody(), StandardCharsets.UTF_8);

            NFe nFe = this.nFeService.findByTicketUUID(ticketUUID);

            NFeUpdateStatusEvent nFeUpdateStatusEvent = new NFeUpdateStatusEvent(this, nFe, nFe.getStatusSeFaz());
            this.eventPublisher.publish(nFeUpdateStatusEvent);
        };

        channel.basicConsume(NFE_UPDATE_STATUS, true, deliverCallback, consumerTag -> {
            log.info("consumerTag -> {}", consumerTag);
        });
    }
}
