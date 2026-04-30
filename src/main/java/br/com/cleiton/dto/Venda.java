package br.com.cleiton.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public record Venda(
        Long id,
        String produto,
        Integer quantidade,
        BigDecimal valorUnitario,
        LocalDate data,
        String status
) {

    public BigDecimal valorTotal(){
        return quantidade == 0 ? BigDecimal.ZERO : valorUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public boolean vendaValida(){
        return !status.equals(StatusVenda.CANCELADO.name());
    }

    public YearMonth mes(){
        return YearMonth.from(data);
    }
}
