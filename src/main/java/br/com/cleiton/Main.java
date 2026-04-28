package br.com.cleiton;

import br.com.cleiton.dto.Venda;
import br.com.cleiton.service.RelatorioVendasService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {

       RelatorioVendasService service = new RelatorioVendasService();

       List<Venda> vendas = List.of(
               new Venda(1L, "Notebook", 2, new BigDecimal("2500.00"), LocalDate.of(2026,1,15), "CONCLUIDO"),
               new Venda(2L, "Mouse", 10, new BigDecimal("50.00"), LocalDate.of(2026,1,20), "CONCLUIDO"),
               new Venda(3L, "Notebook", 1, new BigDecimal("2500.00"), LocalDate.of(2026,2,10), "CANCELADO"),
               new Venda(4L, "Teclado", 5, new BigDecimal("120.00"), LocalDate.of(2026,2,25), "CONCLUIDO"),
               new Venda(5L, "Monitor", 1, new BigDecimal("900.00"), LocalDate.of(2026,3,05), "CONCLUIDO"),
               new Venda(6L, "Cadeira Gamer", 5, new BigDecimal("1500.00"), LocalDate.of(2026,3,12), "CONCLUIDO"),
               new Venda(7L, "Mouse", 2, new BigDecimal("50.00"), LocalDate.of(2026,3,15), "CANCELADO"),
               new Venda(8L, "Webcam", 3, new BigDecimal("350.00"), LocalDate.of(2026,4,01), "CONCLUIDO")
       );

        System.out.println(service.valorTotalVendidoPorProduto(vendas));
        System.out.println(service.produtoMaisVendido(vendas));
        System.out.println(service.mesMaiorFaturamento(vendas));
        System.out.println(service.produtosAcimaDaMedia(vendas));
    }
}