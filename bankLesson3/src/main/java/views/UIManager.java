package views;

import model.User;
import org.apache.log4j.Logger;
import services.Manager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UIManager {

    Manager manager = new Manager();

    private static final Logger log = Logger.getLogger(Manager.class);

    public UIManager() {
        hello();
    }

    public void hello() {
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
                    create(); // Заменить на create()
                } catch (IOException | SQLException e) {
                    log.error("Ошибка в создании пользователя - " + e.getMessage());
                }
                break;
            case 2:
                log.info("Введите id ");
                int id = scanner.nextInt();
                try {
                    manager.deleteUser(id);
                } catch (SQLException e) {
                    log.error("Ошибка в удалении пользователя - " + e.getMessage());
                }
                break;
            case 3:
                try {
                    update();
                } catch (SQLException e) {
                    log.error("Ошибка в обновлении пользователя - " + e.getMessage());
                }
                break;
            case 4:
                try {
                    read();
                } catch (SQLException e) {
                    log.error("Ошибка в просмотре базы данных - " + e.getMessage());
                }
                break;
        }
    }

    public void create() throws IOException, SQLException {
        User currentUser = new User();
        Scanner scanner = new Scanner(System.in);
        log.info("Введите name, surname");
        String userInfo = scanner.nextLine();
        String[] userInfoList = userInfo.split(", ", 0);
        if (userInfoList.length > 1) {
            currentUser.setName(userInfoList[0]);
            currentUser.setSurname(userInfoList[1]);
            manager.createUser(currentUser.getName(), currentUser.getSurname());
        } else {
            throw new IOException("Не хватает данных");
        }
    }

    public void read() throws SQLException {
        List<User> listUsers = manager.readUsers();
        for(Object object : listUsers) {
            User user = (User) object;
            log.info("Пользователь - id = " + user.getId() + ", имя = " +
                    user.getName() + ", фамилия = " + user.getSurname());
        }
    }

    public void delete() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        log.info("Введите id ");
        int id = scanner.nextInt();
        manager.deleteUser(id);
    }

    public void update() throws SQLException{
        User currentUser = new User();
        log.info("Введите id, name, surname");
        Scanner scan = new Scanner(System.in);
        String userInfo = scan.nextLine();
        String[] userInfoList = userInfo.split(", ", 0);
        currentUser.setId(Integer.parseInt(userInfoList[0]));
        currentUser.setName(userInfoList[1]);
        currentUser.setSurname(userInfoList[2]);
        manager.updateUser(currentUser.getId(), currentUser.getName(),
                currentUser.getSurname());
    }

}
