package br.com.rubenskj.rabbit.core.services;

import br.com.rubenskj.rabbit.core.models.Retry;
import br.com.rubenskj.rabbit.core.repositories.IRetryRepository;
import br.com.rubenskj.rabbit.core.utils.RetryTimes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class RetryService {

    private final IRetryRepository iRetryRepository;

    public RetryService(IRetryRepository iRetryRepository) {
        this.iRetryRepository = iRetryRepository;
    }

    public Retry save(String ticketUuid, int times) {
        this.validate(ticketUuid, times);

        log.info("Saving retry of event");
        log.info("Ticket UUID of Retry -> {} - times: {}", ticketUuid, times);
        Retry retry = new Retry(ticketUuid, times);

        this.iRetryRepository.save(retry);

        return retry;
    }

    private void validate(String ticketUuid, int times) {
        log.info("Validating retry");

        if (StringUtils.isEmpty(ticketUuid)) {
            throw new IllegalArgumentException("Ticket UUID cannot be null.");
        }

        if (times < 0 || times > 5) {
            throw new IllegalArgumentException("Times cannot be lower than 0 and higher than 5.");
        }
    }

    public Retry findByTicketUuid(String ticketUuid) {
        return this.iRetryRepository.findByTicketUUID(ticketUuid).orElseThrow(() -> new IllegalStateException("Retry wasn't found with this ticket uuid."));
    }

    public boolean retryExceed(Retry retry) {
        if (retry.getTimes() >= RetryTimes.TIMES_TO_EXCEED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean existsByTicketUUID(String ticketUuid) {
        return this.iRetryRepository.existsByTicketUUID(ticketUuid);
    }

    public void increaseTimes(Retry retry) {
        log.info("Increasing times of retry");

        retry.setTimes((retry.getTimes() + 1));

        log.info("Ticket UUID of Retry -> {} - times: {}", retry.getTicketUUID(), retry.getTimes());

        this.iRetryRepository.save(retry);
    }

    public void deleteByTicketUuid(String ticketUUID) {
        if (this.iRetryRepository.existsByTicketUUID(ticketUUID)) {
            log.info("Deleting retry after " + RetryTimes.TIMES_TO_EXCEED + " times.");
            this.iRetryRepository.deleteByTicketUUID(ticketUUID);
        }
    }
}
