package services;

import pojo.Account;
import pojo.User;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class AccountManager {

    private static final Logger log = Logger.getLogger(AccountManager.class);

    String csv = "src/main/resources/data.csv";

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

        log.info("Введите число: ");
        Scanner scanner = new Scanner(System.in);
        int numberAccountManager = scanner.nextInt();

        switch (numberAccountManager) {
            case 1:
                try {
                    addAccount();
                } catch (Exception ex) {
                    log.error("Ошибка в создании аккаунта");
                }
                break;
            case 2:
                try {
                    deleteAccount();
                } catch (Exception ex) {
                    log.error("Ошибка в удалении аккаунта");
                }
                break;
            case 3:
                log.info("Введите количество внесённых средств в рублях: ");
                try {
                    changeAccount(scanner.nextInt());
                } catch (Exception ex) {
                    log.error("Ошибка в создании аккаунта");
                }
                break;
            case 4:
                try {
                    watchAccount();
                } catch (Exception ex) {
                    log.error("Ошибка в создании аккаунта");
                }
                break;
        }
    }

    private void addAccount() throws IOException {
        /*
            User currentUser = findUser();
            int min = 10000000;
            int max = 99999999;
            if (currentUser.getAccountNumber() == 0) {
                deleteUser(currentUser.getId());
                currentUser.setAccountNumber(min + (int) (Math.random() * max));
                currentUser.setAccountMoney(0);
                CSVWriter writer = new CSVWriter(new FileWriter(csv), ',', CSVWriter.NO_QUOTE_CHARACTER);
                writer.writeNext(new String[]{currentUser.toString()});
                writer.close();
                log.info("Ваш номер счёта - " + currentUser.getAccountNumber());
            } else {
                log.error("У вас уже имеется другой номер счёта");
            }
        */

        User currentUser = findUser();
        int min = 10000000;
        int max = 99999999;
        List<Account> acc = currentUser.getAccount();
        Scanner scanner = new Scanner(System.in);
        String newAccount = scanner.nextLine();
        String[] newAccountArray = newAccount.split(", ", 0);
        Account account = new Account();
        account.setAccountNumber(Integer.parseInt(newAccountArray[0]));
        account.setAccountMoney(Integer.parseInt(newAccountArray[1]));
        acc.add(account);
    }

    private User findAccountReturnUser(int accountNumber) throws IOException {
        List<String[]> listUsers = readUsers();
        List<Account> listAccount;
        for(Object object : listUsers) {
            User user = (User) object;
            listAccount = user.getAccount();
            for (Account account : listAccount) {
                if (account.getAccountNumber() == accountNumber) {
                    return user;
                }
            }
        }
        log.info("К сожалению, такого пользователя нет в базе данных");
        return null;
    }

    private void deleteAccount() throws IOException {
        User currentUser = findUser();
        if (currentUser.getAccountNumber() != 0) {
            deleteUser(currentUser.getId());
            currentUser.setAccountNumber(0);
            currentUser.setAccountMoney(0.0);
            CSVWriter writer = new CSVWriter(new FileWriter(csv), ',', CSVWriter.NO_QUOTE_CHARACTER);
            writer.writeNext(new String[]{currentUser.toString()});
            writer.close();
            log.info("Аккаунт успешно удалён");
        } else {
            log.error("Вашего аккаунта уже не существует");
        }

    }

    //Для добавления средств (изменение)
    private void changeAccount(double account) throws IOException {
        User currentUser = findUser();
        if (currentUser.getAccountNumber() != 0) {
            deleteUser(currentUser.getId());
            currentUser.setAccountMoney(currentUser.getAccountMoney() + account);
            CSVWriter writer = new CSVWriter(new FileWriter(csv), ',', CSVWriter.NO_QUOTE_CHARACTER);
            writer.writeNext(new String[]{currentUser.toString()});
            writer.close();
            log.info("Аккаунт успешно изменён");
        } else {
            log.error("Вы ещё не завели счёт");
        }
    }

    private void watchAccount() throws IOException {
        User currentUser = findUser();
        if (currentUser != null && currentUser.getAccountNumber() != 0) {
            log.info("Ваш профиль : ");
            log.info("Номер аккаунта - " + currentUser.getAccountNumber());
            log.info("Денежные средства на аккаунте - " + currentUser.getAccountMoney());
        } else {
            log.error("Вы ещё не завели счёт");
        }
    }


    // Вспомогательные классы

    private int addUserInfo() {
        Scanner scanner = new Scanner(System.in);
        log.info("Введите id");
        String userInfo = scanner.nextLine();
        return Integer.parseInt(userInfo);
    }

    private void deleteUser(int id) throws IOException {
        List listUsers = readUsers();
        User delUser = null;
        // Поиск строки, которую нужно удалить
        for(Object object : listUsers) {
            User user = (User) object;
            if (user.getId() == id) {
                delUser = user;
                break;
            }
            listUsers.remove(delUser);
        }
        CSVWriter writer = new CSVWriter(new FileWriter(csv), ',', CSVWriter.NO_QUOTE_CHARACTER);
        if (!listUsers.isEmpty()) {
            for(Object object : listUsers) {
                User user = (User) object;
                writer.writeNext(new String[]{user.toString()});
            }
        } else {
            log.info("Нет данных");
        }
        writer.close();
    }

    private User findUser() throws IOException {
        int currentUserId = addUserInfo();
        List listUsers = readUsers();
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

    private List<User> readUsers() throws IOException {
        List<User> listUsers = new ArrayList<>();
        try{
            FileInputStream fstream = new FileInputStream(csv);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            String[] userInfoList;
            while ((strLine = br.readLine()) != null){
                userInfoList = strLine.split(", ", 0);
                User user = new User();
                user.setId(Integer.parseInt(userInfoList[0]));
                user.setName(userInfoList[1]);
                user.setSurname(userInfoList[2]);
                List<Account> listAccount = new ArrayList<>();
                for (int i = 4; i < userInfoList.length; i++) {
                    Account account = new Account();
                    account.setAccountNumber(Integer.parseInt(userInfoList[i-1]));
                    account.setAccountMoney(Double.parseDouble(userInfoList[i]));
                    listAccount.add(account);
                }
                user.setAccount(listAccount);
                listUsers.add(user);
            }
        }catch (IOException e){
            System.out.println("Ошибка");
        }
        return listUsers;
    }

}
