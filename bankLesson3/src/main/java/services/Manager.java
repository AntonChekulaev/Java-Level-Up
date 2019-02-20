package services;


import main.DatabaseConnection;
import model.User;
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

    DatabaseConnection connection;
    
    public Manager() {      
        this.connection = new DatabaseConnection();
    }

    // CRUD методы

    public void createUser(String name, String surname) throws SQLException {
        String query = "INSERT INTO users (name, surname) VALUES (\'" + name
                + "\', \'" + surname + "\')";
        sqlExec(query);
    }

    public void deleteUser(int id) throws SQLException {
        String query = "DELETE FROM users WHERE id = " + id;
        sqlExec(query);
    }

    public void updateUser(int id, String name, String surname) throws SQLException {
        String query = "UPDATE users SET name = \'" + name +
                "\', surname = \'" + surname + "\' WHERE id = " + id;
        sqlExec(query);
    }

    public List<User> readUsers() throws SQLException{
        String query = "SELECT * FROM users";
        List<User> listUsers = new ArrayList<>();
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

    // Вспомогательный класс

    private void sqlExec(String query) throws SQLException {
        Statement statement = connection.getConnection().createStatement();
        statement.execute(query);
        connection.getConnection().close();
    }

}
