package br.com.cleiton.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Venda(
        Long id,
        String produto,
        Integer quantidade,
        BigDecimal valorUnitario,
        LocalDate data,
        String status
) {

    public BigDecimal valorTotal(){
        return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public boolean vendaValida(){
        return !status.equals(StatusVenda.CANCELADO.name());
    }

    public String mes(){
        return data.getMonth().name();
    }
}
