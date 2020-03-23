package br.com.rubenskj.rabbit.core.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NFeDTO {

    private String id;
    private String productName;
    private BigDecimal price;
}
