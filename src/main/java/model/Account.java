package model;
import java.time.LocalDateTime;
public class Account {
    private int id;
    private String ownerName;
    private String accountType;
    private double balance;
    private LocalDateTime createdDate;
    public Account(int id, String ownerName, String accountType, double balance, LocalDateTime createdDate){
        this.id = id;
        this.ownerName = ownerName;
        this.accountType = accountType;
        this.balance = balance;
        this.createdDate = createdDate;
    }
    //Constructor overloading
    public Account( String ownerName, String accountType, double balance, LocalDateTime createdDate){
        this.ownerName = ownerName;
        this.accountType = accountType;
        this.balance = balance;
        this.createdDate = createdDate;
    }
    public int getId() {return id;}

    public double getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getOwnerName() {
        return ownerName;
    }
    public void setBalance(double balance){
        this.balance = balance;
    }
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", ownerName='" + ownerName + '\'' +
                ", accountType='" + accountType + '\'' +
                ", balance=" + balance +
                ", createdDate=" + createdDate +
                '}';
    }

}
