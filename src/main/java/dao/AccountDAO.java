package dao;

import db.DBConnection;
import model.Account;
import java.util.Scanner;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    static Scanner scanner = new Scanner(System.in);
    public void createAccount(Account a) throws SQLException{
        String sql = "INSERT INTO accounts(ownerName, accountType, balance, createdDate) WHERE values(?,?,?,?)";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, a.getOwnerName());
            ps.setString(2, a.getAccountType());
            ps.setDouble(3, a.getBalance());
            ps.setTimestamp(4, Timestamp.valueOf(a.getCreatedDate()));
            int rows = ps.executeUpdate();{
                if(rows > 0){
                    System.out.println("Account created successfully");
                }else{
                    System.out.println("Something went wrong");
                }
            }
        }catch (SQLException e){
            System.out.println("Database exception: " + e.getMessage());
        }
    }
    public Account getAccountById(int id) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new Account(
                        rs.getInt("id"),
                        rs.getString("owner_name"),
                        rs.getString("account_type"),
                        rs.getDouble("balance"),
                        rs.getDate("created_date").toLocalDate().atStartOfDay()
                );
            }
        } catch(SQLException e){
            System.out.println("Database error: " + e.getMessage());
        }
        return null; // account not found
    }
    public List<Account> getAllAccounts() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Account account = new Account(
                        rs.getInt("id"),
                        rs.getString("owner_name"),
                        rs.getString("account_type"),
                        rs.getDouble("balance"),
                        rs.getTimestamp("created_date").toLocalDateTime()
                );
                accounts.add(account);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching account details: " + e.getMessage());
        }
        return accounts;
    }
    public void updateBalance(int accountId, double newBalance, Connection con) throws SQLException{
        String sql = "UPDATE accounts SEET balance = ? WHERE id = ?";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setDouble(1, newBalance);
            ps.setInt(2, accountId);
            int rows = ps.executeUpdate();
            if(rows == 0){
                throw new SQLException("Account not found with: " + accountId);
            }
        }
    }
    public void deleteAccount(int id) throws SQLException{
    try{
        System.out.println("Enter account Id: ");
        int accountId = scanner.nextInt();
        AccountDAO accountDAO = new AccountDAO();
        accountDAO.deleteAccount(accountId);
    }catch (SQLException e){
        System.out.println("Database exception: " + e.getMessage());
    }
    }
    }

