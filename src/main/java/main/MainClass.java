package main;

import org.apache.log4j.Logger;
import pojo.User;
import services.AccountManager;
import services.Manager;
import services.SecurityGuard;

import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class MainClass {

    private static final Logger log = Logger.getLogger(MainClass.class);

    public static void main(String args[]){

        // Подключение к базе данных
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
                log.info(user);
            }

            connection.getConnection().close();

        } catch (SQLException e) {
            log.info("Запрос не выполнился");
            e.printStackTrace();
        }

        while (true) {
            log.info("1 - Поговорить с охранником");
            log.info("2 - Поговорить с менеджером");
            log.info("3 - Поговорить с менеджером по счетам");

            Scanner scanner = new Scanner(System.in);
            log.info("Введите число: ");
            int number = scanner.nextInt();

            switch (number) {
                case 1:
                    SecurityGuard security = new SecurityGuard();
                    break;
                case 2:
                    Manager manager = new Manager();
                    break;
                case 3:
                    AccountManager accountManager = new AccountManager();
                    break;
            }  

        }

    }

}
