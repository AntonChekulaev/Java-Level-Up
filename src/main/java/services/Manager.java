package services;


import main.DatabaseConnection;
import pojo.User;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                    log.error("Ошибка в создании пользователя - " + e.getMessage());
                }
                break;
            case 2:
                log.info("Введите id ");
                int id = scanner.nextInt();
                deleteUser(id);
                break;
            case 3:
                User currentUser = createUserModel();
                updateUser(currentUser.getId(), currentUser.getName(),
                        currentUser.getSurname());
                break;
            case 4:
                try {
                    watchUsers(readUsers());
                } catch (Exception e) {
                    log.error("Ошибка в просмотре файла");
                    e.printStackTrace();
                }
                break;
        }
    }

    // CRUD методы

    private void createUser() throws IOException {
        User currentUser = new User();
        Scanner scanner = new Scanner(System.in);
        log.info("Введите name, surname");
        String userInfo = scanner.nextLine();
        String[] userInfoList = userInfo.split(", ", 0);
        if (userInfoList.length > 1) {
            currentUser.setName(userInfoList[0]);
            currentUser.setSurname(userInfoList[1]);

            String query = "INSERT INTO users (name, surname) VALUES (\'" + currentUser.getName()
                    + "\', \'" + currentUser.getSurname() + "\')";

            try {
                DatabaseConnection connection = new DatabaseConnection();
                Statement statement = connection.getConnection().createStatement();
                statement.execute(query);
                connection.getConnection().close();
            } catch (SQLException e) {
                log.info("Запрос не выполнился");
                e.printStackTrace();
            }
        } else {
            throw new IOException("Не хватает данных");
        }
    }

    private void deleteUser(int id) {
        String query = "DELETE FROM users WHERE id = " + id;
        try {
            DatabaseConnection connection = new DatabaseConnection();
            Statement statement = connection.getConnection().createStatement();
            statement.execute(query);
            connection.getConnection().close();
        } catch (SQLException e) {
            log.info("Запрос не выполнился");
            e.printStackTrace();
        }
    }

    private void updateUser(int id, String name, String surname) {
        String query = "UPDATE users SET name = \'" + name +
                "\', surname = \'" + surname + "\' WHERE id = " + id;
        try {
            DatabaseConnection connection = new DatabaseConnection();
            Statement statement = connection.getConnection().createStatement();
            statement.execute(query);
            connection.getConnection().close();
        } catch (SQLException e) {
            log.info("Запрос не выполнился");
            e.printStackTrace();
        }
    }

    private List<User> readUsers() {
        String query = "SELECT * FROM users";
        List<User> listUsers = new ArrayList<>();

        try {
            DatabaseConnection connection = new DatabaseConnection();
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
        }

        return listUsers;
    }

    // Вспомогательные методы

    private User createUserModel() {
        User currentUser = new User();
        log.info("Введите id, name, surname");
        Scanner scan = new Scanner(System.in);
        String userInfo = scan.nextLine();
        String[] userInfoList = userInfo.split(", ", 0);
        currentUser.setId(Integer.parseInt(userInfoList[0]));
        currentUser.setName(userInfoList[1]);
        currentUser.setSurname(userInfoList[2]);
        return currentUser;
    }

    private void watchUsers(List<User> listUsers) {
        for(Object object : listUsers) {
            User user = (User) object;
            log.info("Пользователь - id = " + user.getId() + ", имя = " +
                    user.getName() + ", фамилия = " + user.getSurname());
        }
    }

}
