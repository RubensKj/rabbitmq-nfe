package br.com.rubenskj.rabbit.core.rest;

import br.com.rubenskj.rabbit.core.dtos.NFeDTO;
import br.com.rubenskj.rabbit.core.dtos.Ticket;
import br.com.rubenskj.rabbit.core.models.NFe;
import br.com.rubenskj.rabbit.core.models.RespostaSefaz;
import br.com.rubenskj.rabbit.core.models.StatusSeFaz;
import br.com.rubenskj.rabbit.core.services.NFeService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
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

    // This is for a stress test, must not to be execute in production.
    @GetMapping("/emitir-notas")
    public void emitirNotas() {
        NFeDTO nFeDTO = new NFeDTO("Corona", BigDecimal.valueOf(32.00));

        for (int i = 0; i < 10000; i++) {
            this.nFeService.returnKeyOfNFe(nFeDTO);
        }
    }

}
