package pojo;

import java.io.Serializable;

public class Account implements Serializable {

    private int userId;
    private int accountNumber;
    private double accountMoney;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAccountMoney() {
        return accountMoney;
    }

    public void setAccountMoney(double accountMoney) {
        this.accountMoney = accountMoney;
    }

    @Override
    public String toString()
    {
        return  userId + ", " + accountNumber + ", " + accountMoney;
    }
}
