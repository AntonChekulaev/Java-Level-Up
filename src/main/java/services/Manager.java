package services;


import pojo.User;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Manager {

    private static final Logger log = Logger.getLogger(Manager.class);
    
    String csv = "src/main/resources/data.csv";
    
    public Manager() {      
       hello();            
    }

    private void hello(){
        log.info("1 - Зарегистрироваться в банке");
        log.info("2 - Снять регистрацию с пользователя");
        log.info("3 - Изменить данные");
        log.info("4 - Посмотреть список пользователей");

        log.info("Введите число: ");
        Scanner scanner = new Scanner(System.in);
        int numberManager = scanner.nextInt();
        switch (numberManager) {
            case 1:
                try {
                    createUser();
                } catch (IOException e) {
                    log.error("Ошибка в создании нового пользователя");
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    int id = addUserInfo();
                    if (id != -1) {
                        deleteUser(id);
                    } else {
                        log.error("Ошибка в удалении пользователя");
                    }
                } catch (IOException e) {
                    log.error("Ошибка в удалении пользователя");
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    updateUser();
                } catch (IOException e) {
                    log.error("Ошибка в изменении данных");
                    e.printStackTrace();
                }
                log.info("Изменение выполнено без ошибок");
                break;
            case 4:
                try {
                    watchUsers(readUsers());
                } catch (IOException e) {
                    log.error("Ошибка в просмотре файла");
                    e.printStackTrace();
                }
                break;
        }
    }
    
    // Метод createUser и AddUserInfo связаны и выполняются операцию CREATE
    private User createUser() throws IOException {
        //log.info(new File(".").getAbsolutePath());
        User userInfo = null;
        if (new File(csv).exists()) {
            // Перезаписывает файл
            CSVWriter writer = new CSVWriter(new FileWriter(csv, true), ',', CSVWriter.NO_QUOTE_CHARACTER);
            userInfo = addUserInfoCreate();
            if (findUser(userInfo) != null) {
                writer.writeNext(new String[]{userInfo.toString()});
                writer.close();
            }
        } else {
            // Создает новый файл
            CSVWriter writer = new CSVWriter(new FileWriter(csv), ',', CSVWriter.NO_QUOTE_CHARACTER);
            userInfo = addUserInfoCreate();
            if (findUser(userInfo) != null) {
                writer.writeNext(new String[]{userInfo.toString()});
                writer.close();
            }
        }
        return userInfo;
    }

    private User findUser(User currentUser) throws IOException {
        List listUsers = readUsers();
        for(Object object : listUsers) {
            User user = (User) object;
            if (currentUser.getId() == user.getId()) {
                log.info("Пользователь находится в базе данных");
                return null;
            }
        }
        return currentUser;
    }

    private User addUserInfoCreate() {
        User currentUser = new User();
        Scanner scanner = new Scanner(System.in);
        log.info("Введите id, name, surname");
        String userInfo = scanner.nextLine();
        String[] userInfoList = userInfo.split(", ", 0);
        currentUser.setId(Integer.parseInt(userInfoList[0]));
        currentUser.setName(userInfoList[1]);
        currentUser.setSurname(userInfoList[2]);
        return currentUser;
    }

    private int addUserInfo() {
        Scanner scanner = new Scanner(System.in);
        log.info("Введите id");
        String userInfo = scanner.nextLine();
        try {
            return Integer.parseInt(userInfo);
        } catch (Exception e) {
            log.error("Некорректные данные");
        }
        return -1;
    }

    private User deleteUser(int id) throws IOException {
        List listUsers = readUsers();
        User delUser = null;
        // Поиск строки, которую нужно удалить
        for(Object object : listUsers) {
            User user = (User) object;
            if (user.getId() == id) {
                delUser = user;
            }
        }
        if (delUser != null) {
            listUsers.remove(delUser);
        } else {
            log.error("Нет пользователя в базе");
        }

        CSVWriter writer = new CSVWriter(new FileWriter(csv), ',', CSVWriter.NO_QUOTE_CHARACTER);
        if (!listUsers.isEmpty()) {
            for(Object object : listUsers) {
                User user = (User) object;
                writer.writeNext(new String[]{user.toString()});
            }
        }
        writer.close();
        return delUser;
    }

    private void updateUser() throws IOException {
        int userId = addUserInfo();
        if (userId != -1) {
            if (deleteUser(userId) != null) {
                log.info("Введите новые значения");
                createUser();
            }
        }
    }

    private void watchUsers(List<User> listUsers) {
        for(Object object : listUsers) {
            User user = (User) object;
            System.out.println(user);
        } 
    }

    private List<User> readUsers() {
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
                listUsers.add(user);
            }
        }catch (IOException e){
            System.out.println("Ошибка");
        }
        return listUsers;
    }

}
