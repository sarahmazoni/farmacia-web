package dao;

import model.NotaFiscal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotaFiscalDAO {


public void inserir(NotaFiscal nf) throws SQLException {
    String sql = "INSERT INTO nota_fiscal (pedido_id, numero, dataEmissao, valorTotal) VALUES (?,?,?,?)";
    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, nf.getPedidoId());
        stmt.setString(2, nf.getNumero());
        stmt.setDate(3, new java.sql.Date(nf.getDataEmissao().getTime()));
        stmt.setDouble(4, nf.getValorTotal());
        stmt.executeUpdate();
    }
}

public NotaFiscal buscarPorPedidoId(int pedidoId) throws SQLException {
    String sql = "SELECT * FROM nota_fiscal WHERE pedido_id = ?";
    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, pedidoId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            NotaFiscal nf = new NotaFiscal();
            nf.setId(rs.getInt("id"));
            nf.setPedidoId(rs.getInt("pedido_id"));
            nf.setNumero(rs.getString("numero"));
            nf.setDataEmissao(rs.getDate("dataEmissao"));
            nf.setValorTotal(rs.getDouble("valorTotal"));
            return nf;
        }
    }
    return null;
}

public List<NotaFiscal> listarTodos() throws SQLException {
    List<NotaFiscal> lista = new ArrayList<>();
    String sql = "SELECT * FROM nota_fiscal";
    try (Connection conn = Database.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            NotaFiscal nf = new NotaFiscal();
            nf.setId(rs.getInt("id"));
            nf.setPedidoId(rs.getInt("pedido_id"));
            nf.setNumero(rs.getString("numero"));
            nf.setDataEmissao(rs.getDate("dataEmissao"));
            nf.setValorTotal(rs.getDouble("valorTotal"));
            lista.add(nf);
        }
    }
    return lista;
}


}
