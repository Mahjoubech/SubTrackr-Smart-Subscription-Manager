package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConn {
    public static DbConn instance;
    public Connection conn;
    private static final String url = "jdbc:mysql://localhost:3306/Abonnment";
    private static final String user = "root";
    private static final String password = "Mahjoub@1230";

    private DbConn() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conexion succes");
        } catch (SQLException | ClassNotFoundException ex) {
            throw new RuntimeException("Erreur de connexion : " + ex.getMessage());
        }
    }

    public static synchronized DbConn getInstance() throws SQLException {
        if (instance == null || instance.getConn().isClosed()) {
            instant = new DbConn();
        }
        return instant;
    }

    public Connection getConn() {
        return conn;
    }

}



}
}
