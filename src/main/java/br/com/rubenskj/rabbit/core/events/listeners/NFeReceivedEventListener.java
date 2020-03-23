package br.com.rubenskj.rabbit.core.events.listeners;

import br.com.rubenskj.rabbit.core.events.eventconfiguration.ListenerAdapter;
import br.com.rubenskj.rabbit.core.events.models.NFeReceivedEvent;
import br.com.rubenskj.rabbit.core.models.NFe;
import br.com.rubenskj.rabbit.core.models.RespostaSefaz;
import br.com.rubenskj.rabbit.core.services.NFeService;
import br.com.rubenskj.rabbit.core.services.RabbitService;
import br.com.rubenskj.rabbit.core.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

@Slf4j
@Component
public class NFeReceivedEventListener implements ListenerAdapter<NFeReceivedEvent> {

    private final RabbitService rabbitService;
    private final NFeService nFeService;

    public NFeReceivedEventListener(RabbitService rabbitService, NFeService nFeService) {
        this.rabbitService = rabbitService;
        this.nFeService = nFeService;
    }

    @Override
    public void execute(NFeReceivedEvent event) throws Exception {
        log.info("Handling NFeReceived");
        log.info("Validating with SeFaz");

        RestTemplate restTemplate = HttpUtil.getRestTemplate();
        UriComponents uriComponents = HttpUtil.createUriComponents("/api/sefaz/valida");

        try {
            ResponseEntity<RespostaSefaz> respostaSefazResponseEntity = restTemplate.postForEntity(uriComponents.toUriString(), event.getNFe(), RespostaSefaz.class);
            handleRespostaSefaz(respostaSefazResponseEntity, event.getNFe());
        } catch (Exception e) {
            log.info("Cannot communicate with SeFaz. Putting NFe on retry");
            this.rabbitService.putNFeOnRetry(event.getNFe());

            log.error("Error -> []", e.getCause());
        }
    }

    private void handleRespostaSefaz(ResponseEntity<RespostaSefaz> resposta, NFe nFe) {
        if (resposta.getStatusCode().is2xxSuccessful()) {
            log.info("NFe {}. Handling with SeFaz's answer", resposta.getBody().getStatusSeFaz());
            log.debug("RespostaSeFaz -> {}", resposta);

            this.nFeService.updateStatus(nFe, resposta.getBody().getStatusSeFaz());

            this.rabbitService.emitNFeUpdatedStatus(nFe);
        }

        if (resposta.getStatusCodeValue() >= 400) {
            log.info("Status code SeFaz: {}, put NFe in retry. (Ticket UUID: {}) ", resposta.getStatusCodeValue(), nFe.getChaveVinculo());
            this.rabbitService.putNFeOnRetry(nFe);

            log.debug("Error in Resposta -> {}", resposta);
        }
    }
}
