package main;

import org.apache.log4j.Logger;
import services.AccountManager;
import services.Manager;
import services.SecurityGuard;
import views.UIAccountManager;
import views.UIManager;

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
                    UIManager manager = new UIManager();
                    break;
                case 3:
                    UIAccountManager accountManager = new UIAccountManager();
                    break;
            }  

        }

    }

}
