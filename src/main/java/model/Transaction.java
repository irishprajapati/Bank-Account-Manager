package model;
import java.time.LocalDateTime;
public class Transaction {
    private int id;
    private int accountId;
    private TransactionType type;
    private double amount;
    private LocalDateTime date;

    public Transaction(int id, TransactionType type, LocalDateTime date, double amount, int accountId) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.accountId = accountId;
    }
    public Transaction( TransactionType type, LocalDateTime date, double amount, int accountId) {
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.accountId = accountId;
    }
    public int getId() {
        return id;
    }
    public int getAccountId() {
        return accountId;
    }
    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
