package br.com.rubenskj.rabbit.core.services;

import br.com.rubenskj.rabbit.core.models.NFe;
import br.com.rubenskj.rabbit.core.models.Retry;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static br.com.rubenskj.rabbit.core.utils.RabbitExchangesDeclare.NFE_RECEIVED;
import static br.com.rubenskj.rabbit.core.utils.RabbitExchangesDeclare.NFE_UPDATE_STATUS;

@Slf4j
@Service
public class RabbitService {

    private final RetryService retryService;

    public RabbitService(RetryService retryService) {
        this.retryService = retryService;
    }

    public void emitNFeReceived(String ticketUUID) {
        try (Connection connection = this.createConnection(); Channel channel = connection.createChannel()) {

            channel.queueDeclare(NFE_RECEIVED, false, false, false, null);

            channel.basicPublish("", NFE_RECEIVED, null, ticketUUID.getBytes(StandardCharsets.UTF_8));

            log.info("Emitting " + NFE_RECEIVED + " | Ticket UUID: " + ticketUUID);
        } catch (IOException | TimeoutException e) {
            log.error("Error -> []", e.getCause());
        }
    }

    public void emitNFeUpdatedStatus(NFe nFe) {
        try (Connection connection = this.createConnection(); Channel channel = connection.createChannel()) {
            String ticketUUID = nFe.getChaveVinculo();

            channel.queueDeclare(NFE_UPDATE_STATUS, false, false, false, null);

            channel.basicPublish("", NFE_UPDATE_STATUS, null, ticketUUID.getBytes(StandardCharsets.UTF_8));

            log.info("Emitting " + NFE_UPDATE_STATUS + " | Ticket UUID: " + ticketUUID);
        } catch (TimeoutException | IOException e) {
            log.error("Error -> []", e.getCause());
        }
    }

    public void putNFeOnRetry(NFe nFe) {
        String ticketUUID = nFe.getChaveVinculo();

        if (!this.retryService.existsByTicketUUID(ticketUUID)) {
            this.retryService.save(ticketUUID, 1);
            this.emitNFeReceived(ticketUUID);
            return;
        }

        Retry retry = this.retryService.findByTicketUuid(ticketUUID);

        if (!this.retryService.retryExceed(retry)) {
            log.info("Retrying communication of SeFaz");

            this.emitNFeReceived(ticketUUID);
            this.retryService.increaseTimes(retry);
        } else {
            this.retryService.deleteByTicketUuid(ticketUUID);
        }
    }

    @SneakyThrows
    public Connection createConnection() {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setRequestedChannelMax(5);

        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("admin");

        return factory.newConnection();
    }
}
