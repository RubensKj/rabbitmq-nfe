package br.com.rubenskj.rabbit.core.repositories;

import br.com.rubenskj.rabbit.core.models.NFe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface INFeRepository extends MongoRepository<NFe, String> {
    Optional<NFe> findByChaveVinculo(String ticketUuid);
}
