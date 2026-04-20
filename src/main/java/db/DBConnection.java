package db;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
public class DBConnection {
    private static final String url = "jdbc:postgresql://localhost:5432/bank-account-manager";
    private static final String username = "irishprajapati";
    private static final String password = "4696";
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url, username, password);
    }
}
