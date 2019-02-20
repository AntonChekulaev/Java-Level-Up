package bank;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

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
