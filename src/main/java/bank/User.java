package bank;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String name;
    private String surname;
    private int accountNumber;
    private double accountMoney;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public double getAccountMoney() {
        return accountMoney;
    }

    public void setAccountMoney(double accountMoney) {
        this.accountMoney = accountMoney;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    @Override
    public String toString()
    {
        return  id + ", " + name + ", " + surname + ", " + accountNumber 
                + ", " + accountMoney;
    }


}
