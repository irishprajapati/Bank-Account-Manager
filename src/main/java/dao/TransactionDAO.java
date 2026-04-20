package dao;

import db.DBConnection;
import model.Transaction;

import java.sql.*;

public class TransactionDAO {
    public void addTransaction(Transaction t, Connection con) throws SQLException{
        String sql = "INSERT INTO transactions(accountId, type, amount, date) VALUES (?,?,?,?)";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, t.getAccountId());
            ps.setString(2, t.getType());
            ps.setDouble(3, t.getAmount());
            ps.setTimestamp(4, Timestamp.valueOf(t.getDate()));
            int data = 0;
            if(data>0){
                System.out.println("Transaction added successfully");
            }
        }catch (SQLException e){
            System.out.println("Error: " + e.getMessage());
        }

    }
    public void getTransactionsByAccount(int accountId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE accountId = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, accountId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Transaction transaction = new Transaction(
                            rs.getInt("id"),
                            rs.getString("type"),
                            rs.getTimestamp("date").toLocalDateTime(),
                            rs.getDouble("amount"),
                            rs.getInt("accountId")
                    );

                    System.out.println(transaction); // or store in list
                }
            }
        }
    }
}
