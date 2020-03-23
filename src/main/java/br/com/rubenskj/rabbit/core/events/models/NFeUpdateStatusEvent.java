package br.com.rubenskj.rabbit.core.events.models;

import br.com.rubenskj.rabbit.core.events.eventconfiguration.Event;
import br.com.rubenskj.rabbit.core.models.NFe;
import br.com.rubenskj.rabbit.core.models.StatusSeFaz;
import lombok.Getter;

@Getter
public class NFeUpdateStatusEvent extends Event {

    private NFe nFe;
    private StatusSeFaz statusSeFaz;

    public NFeUpdateStatusEvent(Object source, NFe nFe, StatusSeFaz statusSeFaz) {
        super(source);
        this.nFe = nFe;
        this.statusSeFaz = statusSeFaz;
    }
}
