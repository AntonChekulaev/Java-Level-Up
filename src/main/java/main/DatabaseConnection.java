package main;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Properties;

public class DatabaseConnection {

    private static final Logger log = Logger.getLogger(DatabaseConnection.class);
    private static Connection connection;

    public Connection getConnection() {
        Properties properties = new Properties();
        try {
            File fileProperties = new File("src/main/resources/config.properties");
            properties.load(new FileReader(fileProperties));
        } catch (IOException e) {
            log.error("Файл свойст отсутствует");
        }

        try {
            connection = DriverManager.getConnection(
                    properties.getProperty("dburl"),
                    properties.getProperty("dbuser"),
                    properties.getProperty("dbpassword"));

        } catch (SQLException e) {
            log.error("Не удалось загрузить класс драйвера!");
        }

        return connection;

    }

}
