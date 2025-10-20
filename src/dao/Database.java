package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/projetofarmacia";
    private static final String USER = "root"; // ajuste seu usuário
    private static final String PASS = "12345"; // ajuste sua senha
    private static Connection connection = null;

    private Database() {}

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver JDBC não encontrado.", e);
            }
            connection = DriverManager.getConnection(URL, USER, PASS);
        }
        return connection;
    }
}
