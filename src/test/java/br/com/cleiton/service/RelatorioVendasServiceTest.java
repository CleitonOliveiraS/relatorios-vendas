package br.com.cleiton.service;

import br.com.cleiton.dto.StatusVenda;
import br.com.cleiton.dto.Venda;
import org.junit.jupiter.api.*;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

class RelatorioVendasServiceTest {

    private RelatorioVendasService service;

    @BeforeEach
    void setUp() {
        this.service = new RelatorioVendasService();
    }

    @Nested
    class valorTotalPorProduto {
        @Test
        @DisplayName("Deve calcular o valor total por produto ignorando vendas canceladas")
        void valorTotalPorProdutoC1() {

            List<Venda> vendas = List.of(
                    new Venda(1L, "Notebook", 2, new BigDecimal("2500.00"), LocalDate.of(2026,1,15), StatusVenda.CONCLUIDO.name()),
                    new Venda(2L, "Mouse", 2, new BigDecimal("50.00"), LocalDate.of(2026,1,20), StatusVenda.CONCLUIDO.name()),
                    new Venda(3L, "Notebook", 1, new BigDecimal("2500.00"), LocalDate.of(2026,2,10), StatusVenda.CANCELADO.name()),
                    new Venda(4L, "Teclado", 1, new BigDecimal("120.00"), LocalDate.of(2026,2,25), StatusVenda.CONCLUIDO.name()),
                    new Venda(5L, "Mouse", 1, new BigDecimal("50.00"), LocalDate.of(2026,3,5), StatusVenda.CONCLUIDO.name())
            );

            Map<String, BigDecimal> teste = Map.ofEntries(
                    entry("Mouse", new BigDecimal("150.00")),
                    entry("Notebook", new BigDecimal("5000.00")),
                    entry("Teclado", new BigDecimal("120.00"))
            );

            var total = service.valorTotalPorProduto(vendas);

            assertThat(total).containsExactlyInAnyOrderEntriesOf(teste);

        }

        @Test
        @DisplayName("Deve ignorar venda no status CANCELADO, retorna vazio")
        void valorTotalPorProdutoC2() {

            List<Venda> vendas = List.of(
                    new Venda(1L, "Notebook", 2, new BigDecimal("2500.00"), LocalDate.of(2026,1,15), StatusVenda.CANCELADO.name()),
                    new Venda(2L, "Mouse", 2, new BigDecimal("50.00"), LocalDate.of(2026,1,20), StatusVenda.CANCELADO.name())
            );

            var total = service.valorTotalPorProduto(vendas);

            assertThat(total).isNotNull();
            assertThat(total).isEmpty();

        }

        @Test
        @DisplayName("Deve verificar se agrupa e soma")
        void valorTotalPorProdutoC3() {

            List<Venda> vendas = List.of(
                    new Venda(1L, "Mouse", 2, new BigDecimal("50.00"), LocalDate.of(2026,1,15), StatusVenda.CONCLUIDO.name()),
                    new Venda(2L, "Mouse", 3, new BigDecimal("50.00"), LocalDate.of(2026,1,20), StatusVenda.CONCLUIDO.name()),
                    new Venda(3L, "Mouse", 5, new BigDecimal("50.00"), LocalDate.of(2026,1,25), StatusVenda.CONCLUIDO.name())
            );

            var total = service.valorTotalPorProduto(vendas);

            assertThat(total).containsKey("Mouse").containsValue(new BigDecimal("500.00"));

        }

        @Test
        @DisplayName("Deve verificar se a lista de vendas esta vazia, retornar um map vazio")
        void valorTotalPorProdutoC4() {

            var total = service.valorTotalPorProduto(List.of());

            assertThat(total).isEmpty();

        }

        @Test
        @DisplayName("Deve verificar se ao receber uma lista de venda com o atributo quantidade 0, retorna map vazio")
        void valorTotalPorProdutoC5() {

            List<Venda> vendas = List.of(
                    new Venda(1L, "Notebook", 0, new BigDecimal("2500.00"), LocalDate.of(2026,1,15), StatusVenda.CONCLUIDO.name()),
                    new Venda(2L, "Mouse", 0, new BigDecimal("50.00"), LocalDate.of(2026,1,20), StatusVenda.CONCLUIDO.name()),
                    new Venda(3L, "Teclado", 0, new BigDecimal("120.00"), LocalDate.of(2026,1,25), StatusVenda.CONCLUIDO.name())
            );

            var total = service.valorTotalPorProduto(vendas);

            assertThat(total).isNotNull();
            assertThat(total).isEmpty();

        }
    }

    @Nested
    class produtoMaisVendido {

        @Test
        @DisplayName("Deve verificar se retorna o produto com maior quantidade com status de venda CONCLUIDO")
        void produtoMaisVendidoC1() {

            List<Venda> vendas = List.of(
                    new Venda(1L, "Notebook", 5, new BigDecimal("2500.00"), LocalDate.of(2026,1,15), StatusVenda.CONCLUIDO.name()),
                    new Venda(2L, "Mouse", 15, new BigDecimal("50.00"), LocalDate.of(2026,1,20), StatusVenda.CONCLUIDO.name()),
                    new Venda(3L, "Teclado", 10, new BigDecimal("120.00"), LocalDate.of(2026,1,25), StatusVenda.CONCLUIDO.name())
            );

            var maisVendido = service.produtoMaisVendido(vendas);

            assertThat(maisVendido).contains("Mouse");

        }

        @Test
        @DisplayName("Deve verificar se em caso de empate entre 2 produtos retorna o maior em por ordem alfabetica")
        void produtoMaisVendidoC2() {

            List<Venda> vendas = List.of(
                    new Venda(1L, "Mouse", 15, new BigDecimal("50.00"), LocalDate.of(2026,1,20), StatusVenda.CONCLUIDO.name()),
                    new Venda(2L, "Teclado", 15, new BigDecimal("120.00"), LocalDate.of(2026,1,25), StatusVenda.CONCLUIDO.name())
            );

            var maisVendido = service.produtoMaisVendido(vendas);

            assertThat(maisVendido).contains("Mouse");

        }

        @Test
        @DisplayName("Deve verificar se em caso de empate entre 3 produtos retorna o maior em por ordem alfabetica")
        void produtoMaisVendidoC3() {

            List<Venda> vendas = List.of(
                    new Venda(1L, "Mouse", 15, new BigDecimal("50.00"), LocalDate.of(2026,1,20), StatusVenda.CONCLUIDO.name()),
                    new Venda(2L, "Teclado", 15, new BigDecimal("120.00"), LocalDate.of(2026,1,25), StatusVenda.CONCLUIDO.name()),
                    new Venda(3L, "Notebook", 15, new BigDecimal("2500.00"), LocalDate.of(2026,1,24), StatusVenda.CONCLUIDO.name())
            );

            var maisVendido = service.produtoMaisVendido(vendas);

            assertThat(maisVendido).contains("Mouse");

        }

        @Test
        @DisplayName("Deve ignorar venda no status CANCELADO, retorna vazio")
        void produtoMaisVendidoC4() {

            List<Venda> vendas = List.of(
                    new Venda(1L, "Mouse", 15, new BigDecimal("50.00"), LocalDate.of(2026,1,20), StatusVenda.CANCELADO.name()),
                    new Venda(2L, "Teclado", 15, new BigDecimal("120.00"), LocalDate.of(2026,1,25), StatusVenda.CANCELADO.name()),
                    new Venda(3L, "Notebook", 15, new BigDecimal("2500.00"), LocalDate.of(2026,1,24), StatusVenda.CANCELADO.name())
            );

            var maisVendido = service.produtoMaisVendido(vendas);

            assertThat(maisVendido).isNotNull();
            assertThat(maisVendido).isEmpty();

        }

        @Test
        @DisplayName("Deve verificar se a lista de vendas esta vazia, retorna vazio")
        void produtoMaisVendidoC5() {

            var maisVendido = service.produtoMaisVendido(List.of());

            assertThat(maisVendido).isEmpty();

        }

        @Test
        @DisplayName("Deve verificar se ao receber uma lista de venda com o atributo quantidade 0 se retorna vazio")
        void produtoMaisVendidoC6() {

            List<Venda> vendas = List.of(
                    new Venda(1L, "Mouse", 0, new BigDecimal("50.00"), LocalDate.of(2026,1,20), StatusVenda.CONCLUIDO.name()),
                    new Venda(2L, "Teclado", 0, new BigDecimal("120.00"), LocalDate.of(2026,1,25), StatusVenda.CONCLUIDO.name()),
                    new Venda(3L, "Notebook", 0, new BigDecimal("2500.00"), LocalDate.of(2026,1,24), StatusVenda.CONCLUIDO.name())
            );

            var maisVendido = service.produtoMaisVendido(vendas);

            assertThat(maisVendido).isEmpty();

        }
    }



    @Nested
    class mesMaiorFaturamento {

        @Test
        @DisplayName("Deve identificar o mes com maior faturamento")
        void mesMaiorFaturamentoC1(){

            List<Venda> vendas = List.of(
                    new Venda(1L, "Mouse", 15, new BigDecimal("50.00"), LocalDate.of(2026,3,20), StatusVenda.CONCLUIDO.name()),
                    new Venda(2L, "Teclado", 15, new BigDecimal("120.00"), LocalDate.of(2026,2,25), StatusVenda.CONCLUIDO.name()),
                    new Venda(3L, "Notebook", 15, new BigDecimal("2500.00"), LocalDate.of(2026,1,24), StatusVenda.CONCLUIDO.name())
            );

            var mesMaiorFaturamento = service.mesMaiorFaturamento(vendas);

            assertThat(mesMaiorFaturamento).contains(YearMonth.of(2026, 1));

        }

        @Test
        @DisplayName("Deve verifica se existe empate de faturamento e retornar o mes mais recente")
        void mesMaiorFaturamentoC2(){

            List<Venda> vendas = List.of(
                    new Venda(1L, "Notebook", 15, new BigDecimal("2500.00"), LocalDate.of(2026,3,20), StatusVenda.CONCLUIDO.name()),
                    new Venda(2L, "Notebook", 15, new BigDecimal("2500.00"), LocalDate.of(2026,1,24), StatusVenda.CONCLUIDO.name())
            );

            var mesMaiorFaturamento = service.mesMaiorFaturamento(vendas);

            assertThat(mesMaiorFaturamento).contains(YearMonth.of(2026, 3));

        }

        @Test
        @DisplayName("Deve ignorar meses com faturamento BigDecimal.ZERO")
        void mesMaiorFaturamentoC3(){

            List<Venda> vendas = List.of(
                    new Venda(1L, "Mouse", 0, new BigDecimal("50.00"), LocalDate.of(2026,3,20), StatusVenda.CONCLUIDO.name()),
                    new Venda(2L, "Notebook", 0, new BigDecimal("2500.00"), LocalDate.of(2026,2,24), StatusVenda.CONCLUIDO.name()),
                    new Venda(3L, "Teclado", 0, new BigDecimal("120.00"), LocalDate.of(2026,1,24), StatusVenda.CONCLUIDO.name())
            );

            var mesMaiorFaturamento = service.mesMaiorFaturamento(vendas);

            assertThat(mesMaiorFaturamento).isEmpty();

        }

        @Test
        @DisplayName("Deve ignorar meses com venda no status CANCELADO, retorna vazio")
        void mesMaiorFaturamentoC4(){

            List<Venda> vendas = List.of(
                    new Venda(1L, "Mouse", 3, new BigDecimal("50.00"), LocalDate.of(2026,3,20), StatusVenda.CANCELADO.name()),
                    new Venda(2L, "Notebook", 0, new BigDecimal("2500.00"), LocalDate.of(2026,2,24), StatusVenda.CANCELADO.name()),
                    new Venda(3L, "Teclado", 1, new BigDecimal("120.00"), LocalDate.of(2026,1,24), StatusVenda.CANCELADO.name())
            );

            var mesMaiorFaturamento = service.mesMaiorFaturamento(vendas);

            assertThat(mesMaiorFaturamento).isNotNull();
            assertThat(mesMaiorFaturamento).isEmpty();

        }

        @Test
        @DisplayName("Deve verificar se a lista de vendas esta vazia, retorna vazio")
        void mesMaiorFaturamentoC5(){

            var mesMaiorFaturamento = service.mesMaiorFaturamento(List.of());

            assertThat(mesMaiorFaturamento).isEmpty();

        }

        @Test
        @DisplayName("Quando a lista de venda foram feitas em um unico mes/ano, retorna apenas um mes")
        void mesMaiorFaturamentoC6(){

            List<Venda> vendas = List.of(
                    new Venda(1L, "Mouse", 3, new BigDecimal("50.00"), LocalDate.of(2026,3,20), StatusVenda.CONCLUIDO.name()),
                    new Venda(2L, "Notebook", 2, new BigDecimal("2500.00"), LocalDate.of(2026,3,24), StatusVenda.CONCLUIDO.name()),
                    new Venda(3L, "Teclado", 1, new BigDecimal("120.00"), LocalDate.of(2026,3,24), StatusVenda.CONCLUIDO.name())
            );

            var mesMaiorFaturamento = service.mesMaiorFaturamento(vendas);

            assertThat(mesMaiorFaturamento).contains(YearMonth.of(2026, 3));

        }

    }

    @Nested
    class produtosAcimaDaMedia {

        @Test
        @DisplayName("Deve agrupar os produtos com mesmo nome, calcular a media de cada por quantidade e verificar qual esta acima da media geral")
        void produtosAcimaDaMediaC1(){

            //Mouse = (10 + 5) / 2 = 7,5
            //Notebook = 2
            //Teclado = 5
            //Media Geral = (7,5 + 2 + 5) / 3 = 4,83
            List<Venda> vendas = List.of(
                    new Venda(1L, "Notebook", 2, new BigDecimal("2500.00"), LocalDate.of(2026,1,15), StatusVenda.CONCLUIDO.name()),
                    new Venda(2L, "Mouse", 10, new BigDecimal("50.00"), LocalDate.of(2026,1,20), StatusVenda.CONCLUIDO.name()),
                    new Venda(3L, "Notebook", 1, new BigDecimal("2500.00"), LocalDate.of(2026,2,10), StatusVenda.CANCELADO.name()),
                    new Venda(4L, "Teclado", 5, new BigDecimal("120.00"), LocalDate.of(2026,2,25), StatusVenda.CONCLUIDO.name()),
                    new Venda(5L, "Mouse", 5, new BigDecimal("50.00"), LocalDate.of(2026,3,5), StatusVenda.CONCLUIDO.name())
            );

            var produtosAcimaDaMedia = service.produtosAcimaDaMedia(vendas);

            assertThat(produtosAcimaDaMedia).contains("Mouse");

        }

        @Test
        @DisplayName("Deve verificar quando todos sao iguais a media geral, retorna lista vazia")
        void produtosAcimaDaMediaC2(){

            //Mouse = 1
            //Notebook = 1
            //Teclado = 1
            //Media Geral = (1 + 1 + 1) / 3 = 1
            List<Venda> vendas = List.of(
                    new Venda(1L, "Notebook", 1, new BigDecimal("2500.00"), LocalDate.of(2026,1,15), StatusVenda.CONCLUIDO.name()),
                    new Venda(2L, "Mouse", 1, new BigDecimal("50.00"), LocalDate.of(2026,1,20), StatusVenda.CONCLUIDO.name()),
                    new Venda(3L, "Teclado", 1, new BigDecimal("120.00"), LocalDate.of(2026,2,25), StatusVenda.CONCLUIDO.name())
            );

            var produtosAcimaDaMedia = service.produtosAcimaDaMedia(vendas);

            assertThat(produtosAcimaDaMedia).isNotNull();
            assertThat(produtosAcimaDaMedia).isEmpty();

        }

        @Test
        @DisplayName("Quando uma quantidade de produto for igual a media geral e outro maior, retorna maior")
        void produtosAcimaDaMediaC3(){

            //Mouse = (2 + 1) / 2 = 1,5
            //Notebook = (2 + 2) / 2 = 2
            //Teclado = 1
            //Media Geral = (1,5 + 2 + 1) / 3 = 1,5
            List<Venda> vendas = List.of(
                    new Venda(1L, "Notebook", 2, new BigDecimal("2500.00"), LocalDate.of(2026,1,15), StatusVenda.CONCLUIDO.name()),
                    new Venda(2L, "Notebook", 2, new BigDecimal("2500.00"), LocalDate.of(2026,2,10), StatusVenda.CONCLUIDO.name()),
                    new Venda(3L, "Mouse", 2, new BigDecimal("50.00"), LocalDate.of(2026,1,20), StatusVenda.CONCLUIDO.name()),
                    new Venda(4L, "Teclado", 1, new BigDecimal("120.00"), LocalDate.of(2026,2,25), StatusVenda.CONCLUIDO.name()),
                    new Venda(5L, "Mouse", 1, new BigDecimal("50.00"), LocalDate.of(2026,3,5), StatusVenda.CONCLUIDO.name())
            );

            var produtosAcimaDaMedia = service.produtosAcimaDaMedia(vendas);

            assertThat(produtosAcimaDaMedia).contains("Notebook");

        }

        @Test
        @DisplayName("Quando a lista de venda tiver apenas um produto, retorna lista vazia")
        void produtosAcimaDaMediaC4(){

            //Notebook = 2
            //Media Geral = 2
            List<Venda> vendas = List.of(
                    new Venda(1L, "Notebook", 2, new BigDecimal("2500.00"), LocalDate.of(2026,1,15), StatusVenda.CONCLUIDO.name())
            );

            var produtosAcimaDaMedia = service.produtosAcimaDaMedia(vendas);

            assertThat(produtosAcimaDaMedia).isNotNull();
            assertThat(produtosAcimaDaMedia).isEmpty();

        }

        @Test
        @DisplayName("Deve verificar se a lista de vendas esta vazia, retorna lista vazia")
        void produtosAcimaDaMediaC5(){

            var produtosAcimaDaMedia = service.produtosAcimaDaMedia(List.of());

            assertThat(produtosAcimaDaMedia).isNotNull();
            assertThat(produtosAcimaDaMedia).isEmpty();

        }

        @Test
        @DisplayName("Deve verifica se o retorno esta ordenado quando existe mais de um acima da media geral")
        void produtosAcimaDaMediaC6(){

            //Notebook = 20
            //Mouse = 10
            //Teclado = 30
            //Mouse Pad = 5
            //Media Geral = (20 + 10 + 30 + 5) / 4 = 16,25
            List<Venda> vendas = List.of(
                    new Venda(1L, "Notebook", 20, new BigDecimal("2500.00"), LocalDate.of(2026,1,15), StatusVenda.CONCLUIDO.name()),
                    new Venda(3L, "Mouse", 10, new BigDecimal("50.00"), LocalDate.of(2026,1,20), StatusVenda.CONCLUIDO.name()),
                    new Venda(4L, "Teclado", 30, new BigDecimal("120.00"), LocalDate.of(2026,2,25), StatusVenda.CONCLUIDO.name()),
                    new Venda(5L, "Mouse Pad", 5, new BigDecimal("20.00"), LocalDate.of(2026,4,5), StatusVenda.CONCLUIDO.name())
            );

            var produtosAcimaDaMedia = service.produtosAcimaDaMedia(vendas);

            assertThat(produtosAcimaDaMedia).containsExactlyElementsOf(List.of("Teclado", "Notebook"));

        }

        @Test
        @DisplayName("Quando uma venda esta com status CANCELADO, nao entra no calculo da media")
        void produtosAcimaDaMediaC7(){

            //Mouse = (2 + 1) / 2 = 1,5
            //Notebook = 2
            //Media Geral = (1,5 + 2) / 2 = 1,75
            List<Venda> vendas = List.of(
                    new Venda(1L, "Notebook", 2, new BigDecimal("2500.00"), LocalDate.of(2026,1,15), StatusVenda.CONCLUIDO.name()),
                    new Venda(2L, "Notebook", 2, new BigDecimal("2500.00"), LocalDate.of(2026,2,10), StatusVenda.CANCELADO.name()),
                    new Venda(3L, "Mouse", 2, new BigDecimal("50.00"), LocalDate.of(2026,1,20), StatusVenda.CONCLUIDO.name()),
                    new Venda(4L, "Teclado", 3, new BigDecimal("120.00"), LocalDate.of(2026,2,25), StatusVenda.CANCELADO.name()),
                    new Venda(5L, "Mouse", 1, new BigDecimal("50.00"), LocalDate.of(2026,3,5), StatusVenda.CONCLUIDO.name())
            );

            var produtosAcimaDaMedia = service.produtosAcimaDaMedia(vendas);

            assertThat(produtosAcimaDaMedia).contains("Notebook");

        }

    }
}