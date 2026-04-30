package br.com.cleiton;

import br.com.cleiton.dto.Venda;
import br.com.cleiton.service.RelatorioVendasService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

       RelatorioVendasService service = new RelatorioVendasService();

        List<Venda> vendas = List.of(
                new Venda(1L, "Notebook", 2, new BigDecimal("2500.00"), LocalDate.of(2026,1,15), "CONCLUIDO"),
                new Venda(2L, "Mouse", 2, new BigDecimal("50.00"), LocalDate.of(2026,1,20), "CONCLUIDO"),
                new Venda(3L, "Notebook", 1, new BigDecimal("2500.00"), LocalDate.of(2026,2,10), "CANCELADO"),
                new Venda(4L, "Teclado", 1, new BigDecimal("120.00"), LocalDate.of(2026,2,25), "CONCLUIDO"),
                new Venda(5L, "Mouse", 1, new BigDecimal("50.00"), LocalDate.of(2026,3,5), "CONCLUIDO")
        );

        service.valorTotalPorProduto(vendas).forEach((key, value) -> System.out.println(key + ": " + value));
        System.out.println();
        System.out.println(service.produtoMaisVendido(vendas).orElse("Nenhum resultado"));
        System.out.println();
        System.out.println(service.mesMaiorFaturamento(vendas).orElse(null));
        System.out.println();
        service.produtosAcimaDaMedia(vendas).forEach(System.out::println);
    }
}