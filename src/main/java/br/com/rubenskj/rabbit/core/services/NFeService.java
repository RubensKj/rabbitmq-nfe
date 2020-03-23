package br.com.rubenskj.rabbit.core.services;

import br.com.rubenskj.rabbit.core.dtos.NFeDTO;
import br.com.rubenskj.rabbit.core.dtos.Ticket;
import br.com.rubenskj.rabbit.core.exceptions.NFeNotFoundException;
import br.com.rubenskj.rabbit.core.models.NFe;
import br.com.rubenskj.rabbit.core.models.StatusSeFaz;
import br.com.rubenskj.rabbit.core.repositories.INFeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Slf4j
@Service
public class NFeService {

    private final INFeRepository INFeRepository;
    private final RabbitService rabbitService;

    public NFeService(INFeRepository INFeRepository, RabbitService rabbitService) {
        this.INFeRepository = INFeRepository;
        this.rabbitService = rabbitService;
    }

    public Ticket returnKeyOfNFe(NFeDTO nFeDTO) {
        log.info("Creating Ticket UUID");
        String ticketUUID = UUID.randomUUID().toString();

        log.info("Saving NFe Information.");
        NFe nFe = this.save(ticketUUID, nFeDTO, StatusSeFaz.VALIDANDO);

        log.info("Emitting event of NFeReceived.");
        this.rabbitService.emitNFeReceived(ticketUUID);

        return new Ticket(nFe.getChaveVinculo(), nFe.getCreatedAt());
    }

    public NFe save(String uuid, NFeDTO nFeDTO, StatusSeFaz statusSeFaz) {
        this.validate(nFeDTO);

        NFe nFe = new NFe(uuid, nFeDTO.getProductName(), nFeDTO.getPrice(), statusSeFaz);
        this.INFeRepository.save(nFe);

        return nFe;
    }

    private void validate(NFeDTO nFeDTO) {
        log.info("Validating NFeDTO");

        if (StringUtils.isEmpty(nFeDTO.getProductName())) {
            throw new IllegalArgumentException("Product Name cannot be null");
        }

        if (nFeDTO.getPrice().equals(0)) {
            throw new IllegalArgumentException("Price cannot be 0");
        }
    }

    public NFe findByTicketUUID(String ticketUUID) {
        return this.INFeRepository.findByChaveVinculo(ticketUUID).orElseThrow(() -> new NFeNotFoundException("NFe cannot be found with this ticket uuid."));
    }

    public NFe updateStatus(NFe nFe, StatusSeFaz statusSeFaz) {
        log.info("Updating status from SeFaz");

        nFe.setStatusSeFaz(statusSeFaz);

        this.INFeRepository.save(nFe);

        return nFe;
    }
}
