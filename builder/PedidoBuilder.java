package builder;

import model.Cliente;
import model.Pedido;
import model.Produto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PedidoBuilder {

    private final Pedido pedido;

    public PedidoBuilder() {
        pedido = new Pedido();
        pedido.setProdutos(new ArrayList<>());
    }

    public PedidoBuilder setCliente(Cliente cliente) {
        pedido.setCliente(cliente);
        return this;
    }

    public PedidoBuilder addProduto(Produto produto) {
        if (produto != null) {
            if (pedido.getProdutos() == null) {
                pedido.setProdutos(new ArrayList<>());
            }
            pedido.getProdutos().add(produto);
        }
        return this;
    }

    public PedidoBuilder setStatus(String status) {
        pedido.setStatus(status);
        return this;
    }

    public PedidoBuilder setFormaPagamento(String formaPagamento) {
        pedido.setFormaPagamento(formaPagamento);
        return this;
    }

    public PedidoBuilder setObservacoes(String obs) {
        pedido.setObservacoes(obs);
        return this;
    }

    public Pedido build() {
        // Data e código
        pedido.setDataPedido(LocalDate.now());
        pedido.setCodigoPedido("PED-" + System.currentTimeMillis());

        double total = 0.0;
        List<Produto> produtos = pedido.getProdutos();
        if (produtos != null && !produtos.isEmpty()) {
            for (Produto pr : produtos) {
                if (pr != null) {
                    try {
                        Double preco = pr.getPrecoVenda();
                        if (preco != null) total += preco;
                    } catch (Exception e) {
                    }
                }
            }
        }

        total = Math.round(total * 100.0) / 100.0;
        pedido.setTotal(total);

        pedido.setEntregue(false);
        return pedido;
    }
}
