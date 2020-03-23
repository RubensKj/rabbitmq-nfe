package br.com.rubenskj.rabbit.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NFe {

    @Id
    private String id;
    private String chaveVinculo;
    private String productName;
    private BigDecimal price;
    private StatusSeFaz statusSeFaz;
    private LocalDateTime createdAt;

    public NFe(String chaveVinculo, String productName, BigDecimal price, StatusSeFaz statusSeFaz) {
        this.chaveVinculo = chaveVinculo;
        this.productName = productName;
        this.price = price;
        this.statusSeFaz = statusSeFaz;
        this.createdAt = LocalDateTime.now();
    }
}
