package views;

import exceptions.MyException;
import model.Account;
import model.User;
import org.apache.log4j.Logger;
import services.AccountManager;
import services.Manager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UIAccountManager {

    AccountManager accountManager = new AccountManager();

    private static final Logger log = Logger.getLogger(Manager.class);

    public UIAccountManager() {
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

        log.info("Введите число: ");
        Scanner scanner = new Scanner(System.in);
        int numberAccountManager = scanner.nextInt();

        switch (numberAccountManager) {
            case 1:
                try {
                    create();
                } catch (SQLException | IOException e) {
                    log.error("Ошибка в создании аккаунта - " + e.getMessage());
                }
                break;
            case 2:
                try {
                    delete();
                } catch (SQLException e) {
                    log.error("Ошибка в удалении аккаунта - " + e.getMessage());
                }
                break;
            case 3:
                log.info("Введите количество внесённых средств в рублях: ");
                try {
                    update();
                } catch (Exception e) {
                    log.error("Ошибка в изменении аккаунта - " + e.getMessage());
                }
                break;
            case 4:
                try {
                    read();
                } catch (IOException | SQLException e) {
                    log.error("Ошибка в просмотре аккаунта - " + e.getMessage());
                }
                break;
            case 5:
                try {
                    readAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void create() throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);
        log.info("Введите id пользователя");
        int id = scanner.nextInt();
        accountManager.addAccount(id);
    }

    public void delete() throws SQLException {
        Account delAccount = null;
        Scanner scanner = new Scanner(System.in);
        log.info("Введите номер счёта");
        int accountNumber = scanner.nextInt();
        accountManager.deleteAccount(accountNumber);
    }

    private void update() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        log.info("Введите номер счёта");
        int accountNumber = scanner.nextInt();
        log.info("Введите сумму, которую хотите вложить");
        double accountMoney = scanner.nextDouble();
        accountManager.changeAccount(accountNumber, accountMoney);
    }

    public void read() throws SQLException, IOException {
        int count = 0;
        Scanner scanner = new Scanner(System.in);
        log.info("Введите id пользователя");
        int id = scanner.nextInt();
        User currentUser = accountManager.getUser(id);
        List<Account> listAccounts = accountManager.getUserAccounts(id);
        for (Account account : listAccounts) {
            if (id == account.getUserId()) {
                count = count + 1;
                log.info("Профиль " + currentUser.getSurname() + " :");
                log.info("Номер аккаунта - " + account.getAccountNumber());
                log.info("Денежные средства на аккаунте - " + account.getAccountMoney());
            }
        }
    }

    public void readAll() throws SQLException {
        Map<Integer, User> infoUsers = accountManager.getInformationAboutUsers();
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


}
