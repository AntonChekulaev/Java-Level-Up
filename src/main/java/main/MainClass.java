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
