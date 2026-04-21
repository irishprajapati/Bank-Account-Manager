package org.eris;

import dao.AccountDAO;
import dao.TransactionDAO;
import db.DBConnection;
import model.Account;
import model.Transaction;
import model.TransactionType;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    static AccountDAO accountDAO = new AccountDAO();
    static TransactionDAO transactionDAO = new TransactionDAO();
    static Scanner scanner = new Scanner(System.in);

    static void main(String[] args) throws SQLException {
        while(true){
            System.out.println("Select a operation: ");
            System.out.println("1, Create Account: ");
            System.out.println("2, Get Account Information by Id: ");
            System.out.println("3, Get All Accounts Information: ");
            System.out.println("4, Update Balance: ");
            System.out.println("5, Delete Account: ");
            System.out.println("6, Add Transaction: ");
            System.out.println("7, Get Transaction By Account: ");
            System.out.println("8, Exit: ");
            System.out.println("Choose: ");
            int userChoice = scanner.nextInt();
            scanner.nextLine();
            switch (userChoice){
                case 1-> createAccount();
                case 2-> getAccountById();
                case 3 -> getAllAccounts();
                case 4 -> updateBalance();
                case 5 -> deleteAccount();
                case 6 -> addTransaction();
                case 7 -> getTransactionsByAccount();
                case 8 -> transfer();
                case 9 -> {
                    System.out.println("Exiting from system..");
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }
static void createAccount() throws SQLException {
    System.out.println("Enter name: ");
    String ownerName = scanner.nextLine();
    System.out.println("Enter Account Type: ");
    String accountType = scanner.nextLine();
    System.out.println("Enter balance: ");
    double balance = scanner.nextDouble();
    scanner.nextLine();
    //created date can be fetched automatically or input automatically to the database
    Account account = new Account(ownerName, accountType, balance, LocalDateTime.now());
    accountDAO.createAccount(account);
}
static void getAccountById() throws SQLException{
    System.out.println("Enter account Id: ");
    int user_id = scanner.nextInt();
    scanner.nextLine();
    Account account = accountDAO.getAccountById(user_id);
    if(account != null){
        System.out.println(account);
    }else{
        System.out.println("Account not found.");
    }
}
static void getAllAccounts() throws SQLException{
        accountDAO.getAllAccounts().forEach(System.out::println);
}

static void updateBalance() throws SQLException {
    System.out.print("Enter account id: ");
    int id = scanner.nextInt();
    System.out.print("Enter new balance: ");
    double balance = scanner.nextDouble();
    scanner.nextLine();

    try (Connection con = DBConnection.getConnection()) {
        accountDAO.updateBalance(id, balance, con);
        System.out.println("Balance updated successfully");
    }
}

static void deleteAccount() throws SQLException{
    getAllAccounts();
    System.out.println("Enter Id to delete: ");
    int idToBeDeleted = scanner.nextInt();
    scanner.nextLine();
    try{
        accountDAO.deleteAccount(idToBeDeleted);
    } catch (SQLException e) {
        System.out.println("Database error: " + e.getMessage());
    }
}
    static void addTransaction() throws SQLException {
        System.out.print("Enter account id: ");
        int accountId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter type (DEPOSIT/WITHDRAWAL/TRANSFER_IN/TRANSFER_OUT): ");
        String input = scanner.nextLine().toUpperCase(); // normalize to uppercase

        TransactionType type;
        try {
            type = TransactionType.valueOf(input); // validates against enum
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid transaction type: " + input);
            return; // exit early
        }

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        Transaction t = new Transaction(type, LocalDateTime.now(), amount, accountId);
        try (var con = DBConnection.getConnection()) {
            transactionDAO.addTransaction(t, con);
        }
    }
static void getTransactionsByAccount() throws SQLException{
    System.out.println("Enter account id: ");
    int accountId = scanner.nextInt();
    scanner.nextLine();
    transactionDAO.getTransactionsByAccount(accountId);
}

static void transfer() throws SQLException {
    System.out.println("Enter sender account  id: ");
    int senderId = scanner.nextInt();
    System.out.println("Enter receiver account id: ");
    int receiverId = scanner.nextInt();
    System.out.println("Enter amount to transfer: ");
    double amount = scanner.nextDouble();
    scanner.nextLine();
    //fetch information from both accounts
    Account sender = accountDAO.getAccountById(senderId);
    Account receiver = accountDAO.getAccountById(receiverId);
    if (sender == null) {
        System.out.println("Sender account information not found.");
        return;
    }
    if (receiver == null) {
        System.out.println("Receiver account information not found.");
        return;
    }
    if(sender.getBalance() < amount){
        System.out.println("Dear " + sender.getOwnerName() + " , you dont have sufficient balance for transaction.");
        return;
    }
    double newSenderBalance = sender.getBalance() - amount;
    double newReceiverBalance = receiver.getBalance() + amount;

    Transaction debitRecord = new Transaction(TransactionType.TRANSFER_OUT, LocalDateTime.now(),amount, senderId);
    Transaction creditRecord = new Transaction(TransactionType.TRANSFER_IN, LocalDateTime.now(),amount, receiverId);

    Connection con = DBConnection.getConnection();
    con.setAutoCommit(false);

    try{
        accountDAO.updateBalance(senderId, newSenderBalance, con);
        accountDAO.updateBalance(receiverId, newReceiverBalance, con);
        transactionDAO.addTransaction(debitRecord, con);
        transactionDAO.addTransaction(creditRecord, con);
        con.commit();
        System.out.println("Transfer successful");
    }catch (SQLException ex){
        con.rollback();
        System.out.println("Transfer failed, rolled back: " + ex.getMessage());
    }finally {
        con.close();
    }



}
}
