package dao;

import model.Pedido;
import model.Cliente;
import model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public void insert(Pedido p) throws SQLException {
        if (p.getCliente() == null || p.getCliente().getId() <= 0) {
            throw new SQLException("Pedido deve ter um cliente válido antes de inserir.");
        }

        verificarEstoqueSuficiente(p.getProdutos());

        String sqlPedido = "INSERT INTO pedido(cliente_id, dataPedido, total, status, formaPagamento, observacoes, codigoPedido, entregue) VALUES(?,?,?,?,?,?,?,?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, p.getCliente().getId());
            if (p.getDataPedido() != null) {
                ps.setDate(2, Date.valueOf(p.getDataPedido()));
            } else {
                ps.setNull(2, Types.DATE);
            }
            ps.setDouble(3, p.getTotal());
            ps.setString(4, p.getStatus());
            ps.setString(5, p.getFormaPagamento());
            ps.setString(6, p.getObservacoes());
            ps.setString(7, p.getCodigoPedido());
            ps.setBoolean(8, p.isEntregue());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int pedidoId = generatedKeys.getInt(1);
                    p.setId(pedidoId);
                    inserirProdutosPedido(conn, pedidoId, p.getProdutos());
                }
            }
        }
    }

    public void update(Pedido p) throws SQLException {
        if (p.getCliente() == null || p.getCliente().getId() <= 0) {
            throw new SQLException("Pedido deve ter um cliente válido antes de atualizar.");
        }

        verificarEstoqueSuficiente(p.getProdutos());

        String sql = "UPDATE pedido SET cliente_id=?, dataPedido=?, total=?, status=?, formaPagamento=?, observacoes=?, codigoPedido=?, entregue=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getCliente().getId());
            if (p.getDataPedido() != null) {
                ps.setDate(2, Date.valueOf(p.getDataPedido()));
            } else {
                ps.setNull(2, Types.DATE);
            }
            ps.setDouble(3, p.getTotal());
            ps.setString(4, p.getStatus());
            ps.setString(5, p.getFormaPagamento());
            ps.setString(6, p.getObservacoes());
            ps.setString(7, p.getCodigoPedido());
            ps.setBoolean(8, p.isEntregue());
            ps.setInt(9, p.getId());
            ps.executeUpdate();

            String sqlDelete = "DELETE FROM pedido_produto WHERE pedido_id=?";
            try (PreparedStatement psDel = conn.prepareStatement(sqlDelete)) {
                psDel.setInt(1, p.getId());
                psDel.executeUpdate();
            }

            inserirProdutosPedido(conn, p.getId(), p.getProdutos());
        }
    }

    public void delete(int id) throws SQLException {
        String sqlItem = "DELETE FROM pedido_produto WHERE pedido_id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement psItem = conn.prepareStatement(sqlItem)) {

            psItem.setInt(1, id);
            psItem.executeUpdate();
        }

        String sql = "DELETE FROM pedido WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Pedido getById(int id) throws SQLException {
        String sql = "SELECT p.*, c.nome AS clienteNome FROM pedido p JOIN cliente c ON p.cliente_id = c.id";
        sql += " WHERE p.id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pedido p = new Pedido();
                    p.setId(rs.getInt("id"));

                    Cliente cli = new Cliente();
                    cli.setId(rs.getInt("cliente_id"));
                    cli.setNome(rs.getString("clienteNome"));
                    p.setCliente(cli);

                    Date dt = rs.getDate("dataPedido");
                    if (dt != null) p.setDataPedido(dt.toLocalDate());

                    p.setTotal(rs.getDouble("total"));
                    p.setStatus(rs.getString("status"));
                    p.setFormaPagamento(rs.getString("formaPagamento"));
                    p.setObservacoes(rs.getString("observacoes"));
                    p.setCodigoPedido(rs.getString("codigoPedido"));
                    p.setEntregue(rs.getBoolean("entregue"));

                    p.setProdutos(buscarProdutosPedido(conn, id));
                    return p;
                }
            }
        }
        return null;
    }

    public List<Pedido> getAll() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.*, c.nome AS clienteNome FROM pedido p JOIN cliente c ON p.cliente_id = c.id";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int pedidoId = rs.getInt("id");
                Pedido p = new Pedido();
                p.setId(pedidoId);

                Cliente cli = new Cliente();
                cli.setId(rs.getInt("cliente_id"));
                cli.setNome(rs.getString("clienteNome"));
                p.setCliente(cli);

                Date dt = rs.getDate("dataPedido");
                if (dt != null) p.setDataPedido(dt.toLocalDate());

                p.setTotal(rs.getDouble("total"));
                p.setStatus(rs.getString("status"));
                p.setFormaPagamento(rs.getString("formaPagamento"));
                p.setObservacoes(rs.getString("observacoes"));
                p.setCodigoPedido(rs.getString("codigoPedido"));
                p.setEntregue(rs.getBoolean("entregue"));

                p.setProdutos(buscarProdutosPedido(conn, pedidoId));
                pedidos.add(p);
            }
        }
        return pedidos;
    }

    private void verificarEstoqueSuficiente(List<Produto> produtos) throws SQLException {
        if (produtos == null || produtos.isEmpty()) return;

        for (Produto prod : produtos) {
            if (prod != null && prod.getId() > 0) {
                int qtd = 1;
                Produto produtoAtual = produtoDAO.getById(prod.getId());
                if (produtoAtual == null || produtoAtual.getEstoque() < qtd) {
                    throw new SQLException("Estoque insuficiente para o produto: " + prod.getNome());
                }
            }
        }
    }

    private void inserirProdutosPedido(Connection conn, int pedidoId, List<Produto> produtos) throws SQLException {
        if (produtos == null || produtos.isEmpty()) return;

        String sqlItem = "INSERT INTO pedido_produto(pedido_id, produto_id, quantidade) VALUES(?,?,?)";
        try (PreparedStatement psItem = conn.prepareStatement(sqlItem)) {
            for (Produto prod : produtos) {
                if (prod != null && prod.getId() > 0) {
                    int qtd = 1;
                    psItem.setInt(1, pedidoId);
                    psItem.setInt(2, prod.getId());
                    psItem.setInt(3, qtd);
                    psItem.addBatch();
                    atualizarEstoque(conn, prod.getId(), qtd);
                }
            }
            psItem.executeBatch();
        }
    }

    private void atualizarEstoque(Connection conn, int produtoId, int qtd) throws SQLException {
        String sqlEstoque = "UPDATE produto SET estoque = estoque - ? WHERE id=? AND estoque >= ?";
        try (PreparedStatement psEst = conn.prepareStatement(sqlEstoque)) {
            psEst.setInt(1, qtd);
            psEst.setInt(2, produtoId);
            psEst.setInt(3, qtd);
            int rowsAffected = psEst.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Falha ao atualizar estoque: estoque insuficiente ou produto não encontrado.");
            }
        }
    }

    private List<Produto> buscarProdutosPedido(Connection conn, int pedidoId) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sqlProd = "SELECT pr.id, pr.nome, pr.precoVenda, pp.quantidade FROM produto pr " +
                "JOIN pedido_produto pp ON pr.id = pp.produto_id WHERE pp.pedido_id=?";

        try (PreparedStatement psProd = conn.prepareStatement(sqlProd)) {
            psProd.setInt(1, pedidoId);
            try (ResultSet rsProd = psProd.executeQuery()) {
                while (rsProd.next()) {
                    Produto prod = new Produto();
                    prod.setId(rsProd.getInt("id"));
                    prod.setNome(rsProd.getString("nome"));
                    prod.setPreco(rsProd.getDouble("precoVenda"));
                    prod.setQuantidade(rsProd.getInt("quantidade"));
                    produtos.add(prod);
                }
            }
        }
        return produtos;
    }
}
