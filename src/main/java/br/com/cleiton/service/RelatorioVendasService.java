package br.com.cleiton.service;

import br.com.cleiton.dto.Venda;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class RelatorioVendasService {

    public static final Comparator<Map.Entry<String, Integer>> POR_QUANTIDADE_MAIOR_E_NOME_CRESCENTE =
            Map.Entry.<String, Integer>comparingByValue().thenComparing(Map.Entry.comparingByKey(Comparator.reverseOrder()));

    public Map<String, BigDecimal> valorTotalPorProduto(List<Venda> vendas) {

        return vendas.stream()
                .filter(Venda::vendaValida)
                .filter(v -> v.quantidade() > 0)
                .collect(Collectors.toMap(
                        Venda::produto,
                        Venda::valorTotal,
                        BigDecimal::add
                ));
    }

    public Optional<String> produtoMaisVendido(List<Venda> vendas) {

        return vendas.stream()
                .filter(Venda::vendaValida)
                .filter(v -> v.quantidade() > 0)
                .collect(Collectors.toMap(
                        Venda::produto,
                        Venda::quantidade,
                        Integer::sum))
                .entrySet()
                .stream()
                .filter(e -> !e.getValue().equals(0))
                .max(POR_QUANTIDADE_MAIOR_E_NOME_CRESCENTE)
                .map(Map.Entry::getKey);
    }

    public Optional<YearMonth> mesMaiorFaturamento(List<Venda> vendas) {

        return vendas.stream()
                .filter(Venda::vendaValida)
                .collect(Collectors.toMap(
                        Venda::mes,
                        Venda::valorTotal,
                        BigDecimal::add
                ))
                .entrySet()
                .stream()
                .filter(e -> e.getValue().compareTo(BigDecimal.ZERO) != 0)
                .max(Map.Entry.<YearMonth, BigDecimal>comparingByValue().thenComparing(Map.Entry.comparingByKey()))
                .map(Map.Entry::getKey);
    }

    public List<String> produtosAcimaDaMedia(List<Venda> vendas) {

        var vendasValidas = vendas.stream()
                .filter(Venda::vendaValida)
                .toList();

        if(vendasValidas.isEmpty()){
            return Collections.emptyList();
        }

        var mediaProduto = vendasValidas.stream()
                .collect(
                        Collectors.groupingBy(
                                Venda::produto,
                                Collectors.averagingDouble(Venda::quantidade)
                        )
                );

        double mediaGeral = mediaProduto.values().stream().mapToDouble(m -> m).average().orElse(0.0);

        return mediaProduto.entrySet().stream()
                .filter(m -> m.getValue() > mediaGeral)
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();

    }

}
