package pojo;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    @CsvBindByPosition(position = 0)
    private int id;
    private String name;
    private String surname;
    private List<Account> account;
    
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

    public List<Account> getAccount() {
        return account;
    }

    public void setAccount(List<Account> account) {
        this.account = account;
    }

    @Override
    public String toString()
    {
        return  id + ", " + name + ", " + surname + ", " + account.toString();
    }


}
