package dao;

import model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void insert(Produto p) throws SQLException {
        String sql = "INSERT INTO produto(nome, descricao, codigoBarras, fabricante, categoria, precoCompra, precoVenda, estoque, controlado) VALUES(?,?,?,?,?,?,?,?,?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getDescricao());
            ps.setString(3, p.getCodigoBarras());
            ps.setString(4, p.getFabricante());
            ps.setString(5, p.getCategoria());
            ps.setDouble(6, p.getPrecoCompra());
            ps.setDouble(7, p.getPrecoVenda());
            ps.setInt(8, p.getEstoque());
            ps.setBoolean(9, p.isControlado());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) p.setId(keys.getInt(1));
            }
        }
    }

    public void update(Produto p) throws SQLException {
        String sql = "UPDATE produto SET nome=?, descricao=?, codigoBarras=?, fabricante=?, categoria=?, precoCompra=?, precoVenda=?, estoque=?, controlado=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getDescricao());
            ps.setString(3, p.getCodigoBarras());
            ps.setString(4, p.getFabricante());
            ps.setString(5, p.getCategoria());
            ps.setDouble(6, p.getPrecoCompra());
            ps.setDouble(7, p.getPrecoVenda());
            ps.setInt(8, p.getEstoque());
            ps.setBoolean(9, p.isControlado());
            ps.setInt(10, p.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM produto WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Produto getById(int id) throws SQLException {
        String sql = "SELECT * FROM produto WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Produto p = new Produto();
                    p.setId(rs.getInt("id"));
                    p.setNome(rs.getString("nome"));
                    p.setDescricao(rs.getString("descricao"));
                    p.setCodigoBarras(rs.getString("codigoBarras"));
                    p.setFabricante(rs.getString("fabricante"));
                    p.setCategoria(rs.getString("categoria"));
                    p.setPrecoCompra(rs.getDouble("precoCompra"));
                    p.setPrecoVenda(rs.getDouble("precoVenda"));
                    p.setEstoque(rs.getInt("estoque"));
                    p.setControlado(rs.getBoolean("controlado"));
                    return p;
                }
            }
        }
        return null;
    }

    public List<Produto> getAll() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto";

        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setDescricao(rs.getString("descricao"));
                p.setCodigoBarras(rs.getString("codigoBarras"));
                p.setFabricante(rs.getString("fabricante"));
                p.setCategoria(rs.getString("categoria"));
                p.setPrecoCompra(rs.getDouble("precoCompra"));
                p.setPrecoVenda(rs.getDouble("precoVenda"));
                p.setEstoque(rs.getInt("estoque"));
                p.setControlado(rs.getBoolean("controlado"));
                produtos.add(p);
            }
        }
        return produtos;
    }
}
