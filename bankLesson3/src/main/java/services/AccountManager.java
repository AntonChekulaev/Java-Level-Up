package services;

import exceptions.MyException;
import main.DatabaseConnection;
import model.Account;
import model.User;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.apache.log4j.Logger;

public class AccountManager {

    private static final Logger log = Logger.getLogger(AccountManager.class);

    DatabaseConnection connection;

    public AccountManager() {
        connection = new DatabaseConnection();
    }

    public void addAccount(int id) throws SQLException, IOException {
        User currentUser = getUser(id);
        if (currentUser != null) {
            Account account = new Account();
            account.setUserId(currentUser.getId());
            account.setAccountMoney(0.0);
            String query = "INSERT INTO accounts (userId, accountMoney) VALUES (\'"
                    + account.getUserId()
                    + "\', \'" + account.getAccountMoney()
                    + "\')";

            sqlExec(query);
        }
    }

    public void deleteAccount(int accountNumber) throws SQLException {
        Account listAccounts = findAccount(accountNumber);
        String query = "DELETE FROM accounts WHERE accountNumber = " + accountNumber;
        sqlExec(query);
    }

    //Для добавления средств (изменение)
    public void changeAccount(int accountNumber, double money) throws SQLException { // Изменить как приду
        String query = "UPDATE accounts SET accountMoney = accountMoney + " + money
                + "WHERE accountNumber = " + accountNumber;
        sqlExec(query);
    }

    public List<Account> getUserAccounts(int id) throws IOException, SQLException {
        List<Account> listAccounts = new ArrayList<>();
        String query = "SELECT * FROM accounts WHERE userId = " + id;
        Statement statement = connection.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Account account = new Account();
            account.setAccountNumber(resultSet.getInt("accountNumber"));
            account.setUserId(resultSet.getInt("userId"));
            account.setAccountMoney(resultSet.getDouble("accountMoney"));
            listAccounts.add(account);
        }
        return listAccounts;
    }

    public Map<Integer, User> getInformationAboutUsers() throws SQLException {
        Map<Integer, User> infoUsers = convertListUsersToMap
                (readUsersWithAccounts(readUsers(), readAccounts()));
        return infoUsers;
    }

    // Вспомогательные классы

    private void sqlExec (String query) throws SQLException {
        Statement statement = connection.getConnection().createStatement();
        statement.execute(query);
        connection.getConnection().close();
    }

    public User getUser(int id) throws SQLException, IOException {
        User user = new User();
        String query = "SELECT * FROM users WHERE id = " + id;
        Statement statement = connection.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
         if (resultSet.next()) {
             user.setId(resultSet.getInt("id"));
             user.setName(resultSet.getString("name"));
             user.setSurname(resultSet.getString("surname"));
         } else {
             throw new SQLException("Нет такого пользователя");
         }
        connection.getConnection().close();

        List<Account> listAccounts = getUserAccounts(id);

        user.setAccount(listAccounts);
        return user;
    }

    private Map<Integer, User> convertListUsersToMap(List<User> listUsers) {
        Map<Integer, User> info = new HashMap<Integer, User>();
        for (User user : listUsers) {
            info.put(user.getId(), user);
        }
        return info;
    }

    //методы для работы с чтением файлов

    private List<User> readUsersWithAccounts(List<User> listUsers, List<Account> listAccounts) {
        List<Account> userListAccounts = new ArrayList<>();
        for (User user : listUsers) {
            for (Account account : listAccounts) {
                if (user.getId() == account.getUserId()) {
                    userListAccounts.add(account);
                }
            }
            user.setAccount(userListAccounts);
            userListAccounts = new ArrayList<>();
        }
        return listUsers;
    }

    private List<User> readUsers() throws SQLException {
        List<User> listUsers = new ArrayList<>();

        String query = "SELECT * FROM users";

        Statement statement = connection.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setName(resultSet.getString("name"));
            user.setSurname(resultSet.getString("surname"));
            listUsers.add(user);
        }

        connection.getConnection().close();

        return listUsers;
    }

    private List<Account> readAccounts() throws SQLException {
        List<Account> listAccounts = new ArrayList<>();
        String query = "SELECT * FROM accounts";
            Statement statement = connection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Account account = new Account();
                account.setAccountNumber(resultSet.getInt("accountNumber"));
                account.setUserId(resultSet.getInt("userId"));
                account.setAccountMoney(resultSet.getDouble("accountMoney"));
                listAccounts.add(account);
            }

            connection.getConnection().close();
        return listAccounts;
    }

    private Account findAccount(int accountNumber) throws SQLException {
        String query = "SELECT * FROM accounts WHERE accountNumber = " + accountNumber;
        Statement statement = connection.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        Account account = new Account();
        account.setAccountNumber(resultSet.getInt("accountNumber"));
        account.setUserId(resultSet.getInt("userId"));
        account.setAccountMoney(resultSet.getDouble("accountMoney"));
        connection.getConnection().close();
        return account;
    }

}
