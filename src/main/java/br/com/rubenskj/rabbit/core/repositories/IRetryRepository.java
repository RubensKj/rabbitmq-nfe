package br.com.rubenskj.rabbit.core.repositories;

import br.com.rubenskj.rabbit.core.models.Retry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRetryRepository extends MongoRepository<Retry, String> {
    Optional<Retry> findByTicketUUID(String ticketUuid);

    boolean existsByTicketUUID(String ticketUuid);

    Integer deleteByTicketUUID(String ticketUuid);
}
