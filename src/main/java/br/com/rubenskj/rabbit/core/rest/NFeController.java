package br.com.rubenskj.rabbit.core.rest;

import br.com.rubenskj.rabbit.core.dtos.NFeDTO;
import br.com.rubenskj.rabbit.core.dtos.Ticket;
import br.com.rubenskj.rabbit.core.models.NFe;
import br.com.rubenskj.rabbit.core.models.RespostaSefaz;
import br.com.rubenskj.rabbit.core.models.StatusSeFaz;
import br.com.rubenskj.rabbit.core.services.NFeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class NFeController {

    private final NFeService nFeService;

    public NFeController(NFeService nFeService) {
        this.nFeService = nFeService;
    }

    @PostMapping("/emitir-nota")
    public Ticket createNFe(@Valid @RequestBody NFeDTO nFeDTO) {
        return this.nFeService.returnKeyOfNFe(nFeDTO);
    }

    // This is a fake validation with sefaz
    @PostMapping("/sefaz/valida")
    public RespostaSefaz getResposta(@Valid @RequestBody NFe nFe) {
        return new RespostaSefaz(StatusSeFaz.AUTORIZADO, LocalDateTime.now());
    }

}
