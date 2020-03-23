package br.com.rubenskj.rabbit.core.events.models;

import br.com.rubenskj.rabbit.core.events.eventconfiguration.Event;
import br.com.rubenskj.rabbit.core.models.NFe;
import lombok.Getter;

@Getter
public class NFeReceivedEvent extends Event {

    private NFe nFe;

    public NFeReceivedEvent(Object source, NFe nFe) {
        super(source);
        this.nFe = nFe;
    }
}
