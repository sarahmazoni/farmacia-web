package factory;

import model.Cliente;
import model.Pedido;
import model.Produto;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class PedidoFactory {

    public static Pedido createPedido(Cliente cliente, List<Produto> produtos, String status, String formaPagamento) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo ao criar um pedido.");
        }
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);

        if (produtos == null) {
            pedido.setProdutos(java.util.Collections.emptyList());
        } else {
            pedido.setProdutos(produtos);
        }

        pedido.setDataPedido(LocalDate.now());
        pedido.setStatus(status != null ? status : "");
        pedido.setFormaPagamento(formaPagamento != null ? formaPagamento : "");
        pedido.setCodigoPedido("PED-" + System.currentTimeMillis());
        pedido.setEntregue(false);
        pedido.setTotal(calcularTotal(pedido.getProdutos()));
        return pedido;
    }

    private static double calcularTotal(List<Produto> produtos) {
        if (produtos == null || produtos.isEmpty()) return 0.0;
        return produtos.stream().mapToDouble(Produto::getPrecoVenda).sum();
    }
}
