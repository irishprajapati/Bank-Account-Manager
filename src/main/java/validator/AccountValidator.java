package validator;

import model.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountValidator {
    public List<String> validate(Account a){
        List<String> error = new ArrayList<>();
        if(a.getOwnerName()== null || a.getOwnerName().isBlank()){
            error.add("Name can't be empty");
        }
        if(a.getAccountType().isBlank()){
            error.add("Account type can't be empty");
        }
        if(a.getBalance()<=0){
            error.add("Balance can't be negative");
        }
        return error;
    }
}
