package services;

import exceptions.myException;
import pojo.Account;
import pojo.User;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;
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
                } catch (IOException e) {
                    log.error("Ошибка в создании аккаунта - " + e.getMessage());
                }
                break;
            case 2:
                try {
                    deleteAccount();
                } catch (IOException e) {
                    log.error("Ошибка в удалении аккаунта - " + e.getMessage());
                }
                break;
            case 3:
                log.info("Введите количество внесённых средств в рублях: ");
                try {
                    changeAccount(scanner.nextInt());
                } catch (IOException e) {
                    log.error("Ошибка в изменении аккаунта - " + e.getMessage());
                }
                break;
            case 4:
                try {
                    watchAccounts();
                } catch (IOException e) {
                    log.error("Ошибка в просмотре аккаунта - " + e.getMessage());
                }
                break;
            case 5:
                try {
                    getInformationAboutUsers();
                } catch (IOException e){
                    log.error("Ошибка в просмотре аккаунтов - " + e.getMessage());
                }
            case 6:
                try {
                    testException();
                } catch (myException e) {
                    e.printStackTrace();
                }
        }
    }

    private void addAccount() throws IOException {
        User currentUser = findUser();
        if (currentUser != null) {
            int min = 10000000;
            int max = 99999999;
            Account account = new Account();
            account.setUserId(currentUser.getId());
            account.setAccountNumber(min + (int) (Math.random() * max));
            account.setAccountMoney(0.0);
            if (new File(accountCsv).exists()) {
                // Перезаписывает файл
                CSVWriter writer = new CSVWriter(new FileWriter(accountCsv, true), ',', CSVWriter.NO_QUOTE_CHARACTER);
                writer.writeNext(new String[]{account.toString()});
                writer.close();
            } else {
                // Создает новый файл
                CSVWriter writer = new CSVWriter(new FileWriter(accountCsv), ',', CSVWriter.NO_QUOTE_CHARACTER);
                writer.writeNext(new String[]{account.toString()});
                writer.close();
            }
        }
    }

    private void deleteAccount() throws IOException {
        Account delAccount = null;
        List<Account> listAccounts = readAccounts();
        User currentUser = findUser();
        watchAccounts(currentUser);
        Scanner scanner = new Scanner(System.in);
        log.info("Введите номер счёта");
        int currentAccount = scanner.nextInt();

        for (Account account : listAccounts) {
            if (account.getUserId() == currentUser.getId() &&
                    account.getAccountNumber() == currentAccount) {
                delAccount = account;
            }
        }

        if (delAccount != null) {
            listAccounts.remove(delAccount);
            CSVWriter writer = new CSVWriter(new FileWriter(accountCsv), ',', CSVWriter.NO_QUOTE_CHARACTER);
            if (!listAccounts.isEmpty()) {
                for(Account account : listAccounts ) {
                    writer.writeNext(new String[]{account.toString()});
                }
            }
            writer.close();
            log.info("Аккаунт успешно удалён");
        } else {
            throw new IOException("Не найдено такого аккаунта");
        }

    }

    //Для добавления средств (изменение)
    private void changeAccount(double acсountMoney) throws IOException {
        Account currentAccount = null;
        List<Account> listAccounts = readAccounts();
        User currentUser = findUser();
        watchAccounts(currentUser);
        Scanner scanner = new Scanner(System.in);
        log.info("Введите номер счёта");
        String currentAccountNumber = scanner.nextLine();

        for (Account account : listAccounts) {
            if (account.getUserId() == currentUser.getId() &&
                    account.getAccountNumber() == Integer.parseInt(currentAccountNumber)) {
                currentAccount = account;
            }
        }

        listAccounts.remove(currentAccount);
        currentAccount.setAccountMoney(currentAccount.getAccountMoney() + acсountMoney);
        listAccounts.add(currentAccount);

        if (currentAccount != null) {
            CSVWriter writer = new CSVWriter(new FileWriter(accountCsv), ',', CSVWriter.NO_QUOTE_CHARACTER);
            if (!listAccounts.isEmpty()) {
                for(Account account : listAccounts ) {
                    writer.writeNext(new String[]{account.toString()});
                }
            }
            writer.close();
            log.info("Аккаунт успешно изменён");
        } else {
            throw new IOException("Вы ещё не создали аккаунт");
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
        }
    }

    private void watchAccounts(User currentUser) throws IOException {
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
        if (count > 0) {
            log.info("Вы ещё не создали аккаунт");
        }
    }

    private void getInformationAboutUsers() throws IOException {
        Map<Integer, User> infoUsers = convertListUsersToMap
                (readUsersWithAccounts(readUsers(), readAccounts()));
        log.info("Список всех пользователей: ");
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

    private void testException() throws myException {
        throw new myException("Все сломалось");
    }


    // Вспомогательные классы

    private int addUserInfo() {
        Scanner scanner = new Scanner(System.in);
        log.info("Введите id");
        String userInfo = scanner.nextLine();
        return Integer.parseInt(userInfo);
    }

    private User findUser() throws IOException {
        int currentUserId = addUserInfo();
        List listUsers = readUsersWithAccounts(readUsers(), readAccounts());
        for(Object object : listUsers) {
            User user = (User) object;
            if (currentUserId == user.getId()) {
                log.info("Пользователь находится в базе данных");
                return user;
            }
        }
        log.info("К сожалению, такого пользователя нет в базе данных");
        return null;
    }

    private Map<Integer, User> convertListUsersToMap(List<User> listUsers) {
        Map<Integer, User> info = new HashMap<Integer, User>();
        for (User user : listUsers) {
            info.put(user.getId(), user);
        }
        return info;
    }

    //методы для работы с чтением файлов

    private List<User> readUsersWithAccounts(List<User> listUsers, List<Account> listAccounts) throws IOException {
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

    private List<User> readUsers() throws IOException {
        List<User> listUsers = new ArrayList<>();
        FileInputStream fstream = new FileInputStream(csv);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        String[] userInfoList;
        while ((strLine = br.readLine()) != null) {
            userInfoList = strLine.split(", ", 0);
            User user = new User();
            user.setId(Integer.parseInt(userInfoList[0]));
            user.setName(userInfoList[1]);
            user.setSurname(userInfoList[2]);
            listUsers.add(user);
        }
        return listUsers;
    }

    private List<Account> readAccounts() throws IOException {
        List<Account> listAccounts = new ArrayList<>();
        FileInputStream streamAccount = new FileInputStream(accountCsv);
        BufferedReader readerAccount = new BufferedReader(new InputStreamReader(streamAccount));
        String strLineAccount;
        String[] accountInfoList;
        while ((strLineAccount = readerAccount.readLine()) != null) {
            accountInfoList = strLineAccount.split(", ", 0);
            Account account = new Account();
            account.setUserId(Integer.parseInt(accountInfoList[0]));
            account.setAccountNumber(Integer.parseInt(accountInfoList[1]));
            account.setAccountMoney(Double.parseDouble(accountInfoList[2]));
            listAccounts.add(account);
        }
        return listAccounts;
    }

}
