package pojo;

import java.io.Serializable;

public class Account implements Serializable {

    private int accountNumber;
    private double accountMoney;

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
        return  accountNumber + ", " + accountMoney;
    }
}
