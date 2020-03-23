package br.com.rubenskj.rabbit.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RespostaSefaz {

    private StatusSeFaz statusSeFaz;
    private LocalDateTime timeRetorno;
}
