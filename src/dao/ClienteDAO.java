package dao;

import model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void insert(Cliente c) throws SQLException {
        String sql = "INSERT INTO cliente(nome, cpf, email, telefone, endereco, dataNascimento, cidade, estado, cep) VALUES(?,?,?,?,?,?,?,?,?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getNome());
            ps.setString(2, c.getCpf());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getTelefone());
            ps.setString(5, c.getEndereco());

            if (c.getDataNascimento() != null) {
                ps.setDate(6, Date.valueOf(c.getDataNascimento()));
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.setString(7, c.getCidade());
            ps.setString(8, c.getEstado());
            ps.setString(9, c.getCep());
            ps.executeUpdate();
        }
    }

    public void update(Cliente c) throws SQLException {
        String sql = "UPDATE cliente SET nome=?, cpf=?, email=?, telefone=?, endereco=?, dataNascimento=?, cidade=?, estado=?, cep=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getNome());
            ps.setString(2, c.getCpf());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getTelefone());
            ps.setString(5, c.getEndereco());

            if (c.getDataNascimento() != null) {
                ps.setDate(6, Date.valueOf(c.getDataNascimento()));
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.setString(7, c.getCidade());
            ps.setString(8, c.getEstado());
            ps.setString(9, c.getCep());
            ps.setInt(10, c.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Cliente getById(int id) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente();
                    c.setId(rs.getInt("id"));
                    c.setNome(rs.getString("nome"));
                    c.setCpf(rs.getString("cpf"));
                    c.setEmail(rs.getString("email"));
                    c.setTelefone(rs.getString("telefone"));
                    c.setEndereco(rs.getString("endereco"));

                    Date dt = rs.getDate("dataNascimento");
                    if (dt != null) c.setDataNascimento(dt.toLocalDate());

                    c.setCidade(rs.getString("cidade"));
                    c.setEstado(rs.getString("estado"));
                    c.setCep(rs.getString("cep"));
                    return c;
                }
            }
        }
        return null;
    }

    public List<Cliente> getAll() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setCpf(rs.getString("cpf"));
                c.setEmail(rs.getString("email"));
                c.setTelefone(rs.getString("telefone"));
                c.setEndereco(rs.getString("endereco"));

                Date dt = rs.getDate("dataNascimento");
                if (dt != null) c.setDataNascimento(dt.toLocalDate());

                c.setCidade(rs.getString("cidade"));
                c.setEstado(rs.getString("estado"));
                c.setCep(rs.getString("cep"));
                clientes.add(c);
            }
        }
        return clientes;
    }
}
