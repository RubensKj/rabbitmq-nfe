package br.com.rubenskj.rabbit.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Retry {

    @Id
    private String id;
    private String ticketUUID;
    private int times;

    public Retry(String ticketUUID, int times) {
        this.ticketUUID = ticketUUID;
        this.times = times;
    }
}
