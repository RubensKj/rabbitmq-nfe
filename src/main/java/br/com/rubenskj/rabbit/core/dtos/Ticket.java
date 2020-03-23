package br.com.rubenskj.rabbit.core.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Ticket {

    private String ticketUUID;
    private LocalDateTime createdAt;
}
