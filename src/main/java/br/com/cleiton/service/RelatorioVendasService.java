package br.com.cleiton.service;

import br.com.cleiton.dto.Venda;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RelatorioVendasService {

    public Map<String, BigDecimal> valorTotalVendidoPorProduto(List<Venda> vendas) {

        return vendas.stream()
                .filter(Venda::vendaValida)
                .collect(Collectors.toMap(
                        Venda::produto,
                        Venda::valorTotal,
                        BigDecimal::add
                ));
    }

    public Map<String, Integer> produtoMaisVendido(List<Venda> vendas) {

        Map<String, Integer> produtoQuantidade = vendas.stream()
                .filter(Venda::vendaValida)
                .collect(Collectors.toMap(
                        Venda::produto,
                        Venda::quantidade,
                        Integer::sum))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(p -> Collections.singletonMap(p.getKey(), p.getValue()))
                .orElse(Collections.emptyMap());

        return produtoQuantidade;
    }

    public Map<String, BigDecimal> mesMaiorFaturamento(List<Venda> vendas) {

        Map<String, BigDecimal> mesFaturamento = vendas.stream()
                .filter(Venda::vendaValida)
                .collect(Collectors.toMap(
                        Venda::mes,
                        Venda::valorTotal,
                        BigDecimal::add
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> Collections.singletonMap(e.getKey(), e.getValue()))
                .orElse(Collections.emptyMap());

        return mesFaturamento;
    }

    public List<Venda> produtosAcimaDaMedia(List<Venda> vendas) {

        List<Venda> vendasValidas = vendas.stream()
                .filter(Venda::vendaValida)
                .toList();

        if(vendasValidas.isEmpty()){
            return Collections.emptyList();
        }

        BigDecimal valorMedia = vendasValidas.stream()
                .map(Venda::valorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(vendasValidas.size()), 2, RoundingMode.HALF_UP);

        return vendasValidas.stream()
                .filter(v -> acimaDaMedia(v.valorTotal(), valorMedia))
                .toList();

    }

    private boolean acimaDaMedia(BigDecimal valorTotal, BigDecimal valorMedia) {
        return valorTotal.compareTo(valorMedia) > 0;
    }

}
