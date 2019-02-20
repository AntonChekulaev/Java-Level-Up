package services;

import exceptions.MyException;
import main.DatabaseConnection;
import pojo.Account;
import pojo.User;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.apache.log4j.Logger;

public class AccountManager {

    private static final Logger log = Logger.getLogger(AccountManager.class);

    String csv = "src/main/resources/data.csv";
    String accountCsv = "src/main/resources/account.csv";

    public AccountManager() {
        hello();
    }

    private void hello() {
        log.info("Добрый день, я аккаунт менеджер, "
                            + "чем могу вам помочь?");
        log.info("1 - Открыть счёт");
        log.info("2 - Удалить счёт");
        log.info("3 - Положить средства на счёт");
        log.info("4 - Посмотреть профиль");
        log.info("5 - Посмотреть все профили");
        log.info("6 - Тест исключения");

        log.info("Введите число: ");
        Scanner scanner = new Scanner(System.in);
        int numberAccountManager = scanner.nextInt();

        switch (numberAccountManager) {
            case 1:
                try {
                    addAccount();
                } catch (SQLException e) {
                    log.error("Ошибка в создании аккаунта - " + e.getMessage());
                }
                break;
            case 2:
                try {
                    deleteAccount();
                } catch (SQLException e) {
                    log.error("Ошибка в удалении аккаунта - " + e.getMessage());
                }
                break;
            case 3:
                log.info("Введите количество внесённых средств в рублях: ");
                try {
                    changeAccount(scanner.nextInt());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    watchAccounts();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                getInformationAboutUsers();
                break;
            case 6:
                try {
                    testException();
                } catch (MyException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void addAccount() throws SQLException {
        User currentUser = findUser();
        if (currentUser != null) {
            int min = 10000000;
            int max = 99999999;
            Account account = new Account();
            account.setUserId(currentUser.getId());
            account.setAccountNumber(min + (int) (Math.random() * max));
            account.setAccountMoney(0.0);
            String query = "INSERT INTO accounts VALUES (\'"
                    + account.getAccountNumber()
                    + "\', \'" + account.getUserId()
                    + "\', \'" + account.getAccountMoney()
                    + "\')";

            sqlExec(query);
            log.info("Аккаунт создан");
        }
    }

    private void deleteAccount() throws SQLException {
        Account delAccount = null;
        List<Account> listAccounts = readAccounts();
        User currentUser = findUser();
        Scanner scanner = new Scanner(System.in);
        if (watchAccounts(currentUser) != 0) {
            log.info("Введите номер счёта");
            int currentAccount = scanner.nextInt();

            for (Account account : listAccounts) {
                if (account.getUserId() == currentUser.getId() &&
                        account.getAccountNumber() == currentAccount) {
                    delAccount = account;
                }
            }

            if (delAccount != null) {
                String query = "DELETE FROM accounts WHERE accountNumber = "
                        + delAccount.getAccountNumber();
                sqlExec(query);
                log.info("Аккаунт успешно удалён");
            } else {
                throw new SQLException("Не найдено такого аккаунта");
            }
        }
    }

    //Для добавления средств (изменение)
    private void changeAccount(double money) throws SQLException {
        Account currentAccount = null;
        List<Account> listAccounts = readAccounts();
        User currentUser = findUser();
        if (watchAccounts(currentUser) != 0) {
            Scanner scanner = new Scanner(System.in);
            log.info("Введите номер счёта");
            String currentAccountNumber = scanner.nextLine();

            for (Account account : listAccounts) {
                if (account.getUserId() == currentUser.getId() &&
                        account.getAccountNumber() == Integer.parseInt(currentAccountNumber)) {
                    currentAccount = account;
                }
            }
            String query = "UPDATE accounts SET accountMoney = " +
                    (currentAccount.getAccountMoney() + money) ;
            sqlExec(query);
        }
    }

    private void watchAccounts() throws IOException {
        int count = 0;
        User currentUser = findUser();
        List<Account> listAccounts = readAccounts();
        for (Account account : listAccounts) {
            if (currentUser.getId() == account.getUserId()) {
                count = count + 1;
                log.info("Профиль " + currentUser.getSurname() + " :");
                log.info("Номер аккаунта - " + account.getAccountNumber());
                log.info("Денежные средства на аккаунте - " + account.getAccountMoney());
            }
        }
        if (count == 0) {
            log.info("Вы ещё не создали аккаунт");
            throw new IOException("Вы ешё не создали аккаунт");
        }
    }

    private int watchAccounts(User currentUser) {
        int count = 0;
        List<Account> listAccounts = readAccounts();
        for (Account account : listAccounts) {
            if (currentUser.getId() == account.getUserId()) {
                count = count + 1;
                log.info("Профиль " + currentUser.getSurname() + " :");
                log.info("Номер аккаунта - " + account.getAccountNumber());
                log.info("Денежные средства на аккаунте - " + account.getAccountMoney());
            }
        }
        if (count == 0) {
            log.info("Вы ещё не создали аккаунт");
        }
        return count;
    }

    private void getInformationAboutUsers() {
        Map<Integer, User> infoUsers = convertListUsersToMap
                (readUsersWithAccounts(readUsers(), readAccounts()));
        log.info("Список всех пользователей: ");
        //log.info(readUsers().get(0));
        infoUsers.forEach((key, value) -> {
            log.info("");
            log.info("Пользователь - id = " + value.getId() +
                    ", Имя = " + value.getName() + ", Фамилия = " + value.getSurname());
            log.info("Счета этого пользователя");
            if (value.getAccount() == null) {
                log.info("У этого пользователя нет счетов");
            } else {
                for(Account account : value.getAccount()) {
                    log.info("Номер счёта - " + account.getAccountNumber());
                    log.info("Количество денежных средств - " + account.getAccountMoney());
                }
            }
            log.info("");
        });
    }

    private void testException() throws MyException {
        throw new MyException("Все сломалось");
    }


    // Вспомогательные классы

    private void sqlExec (String query) throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        Statement statement = connection.getConnection().createStatement();
        statement.execute(query);
        connection.getConnection().close();
    }

    private User findUser() {
        User user = new User();

        Scanner scanner = new Scanner(System.in);
        log.info("Введите id пользователя");
        int id = scanner.nextInt();

        DatabaseConnection connection = new DatabaseConnection();
        String query = "select * from users where id = " + id;

        try {
            Statement statement = connection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            user.setId(resultSet.getInt("id"));
            user.setName(resultSet.getString("name"));
            user.setSurname(resultSet.getString("surname"));

            connection.getConnection().close();

        } catch (SQLException e) {
            log.info("Запрос не выполнился");
            e.printStackTrace();
        } finally {
            try {
                connection.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        query = "SELECT * FROM accounts INNER JOIN" +
                " users ON accounts.userId = users.id WHERE userId = " + id;

        List<Account> listAccounts = new ArrayList<>();

        try {
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

        } catch (SQLException e) {
            log.info("Запрос не выполнился");
            e.printStackTrace();
        } finally {
            try {
                connection.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

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

    private List<User> readUsers() {
        List<User> listUsers = new ArrayList<>();
        DatabaseConnection connection = new DatabaseConnection();

        String query = "select * from users";

        try {
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

        } catch (SQLException e) {
            log.info("Запрос не выполнился");
            e.printStackTrace();
        } finally {
            try {
                connection.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return listUsers;
    }

    private List<Account> readAccounts() {
        List<Account> listAccounts = new ArrayList<>();
        DatabaseConnection connection = new DatabaseConnection();

        String query = "select * from accounts";

        try {
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

        } catch (SQLException e) {
            log.info("Запрос не выполнился");
            e.printStackTrace();
        } finally {
            try {
                connection.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return listAccounts;
    }

}
