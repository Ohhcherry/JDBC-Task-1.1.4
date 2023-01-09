package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
//Создание таблицы для User(ов) – не должно приводить к исключению, если такая таблица уже существует
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `db_test`.`users` (`id` INT NOT NULL AUTO_INCREMENT," +
                                                                                       " `name` VARCHAR(100) NULL, " +
                                                                                       "`lastName` VARCHAR(100) NULL," +
                                                                                       "`age` INT(3) NULL, " +
                                                                                       "PRIMARY KEY (`id`))");
            System.out.println("Таблица пользователей создана.");
        } catch (SQLException e) {
            System.out.println("Таблица пользователей не создана " + e);
        }
    }

    public void dropUsersTable() {
// Удаление таблицы User(ов) – не должно приводить к исключению, если таблицы не существует
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS users");
            System.out.println("Таблица пользователей удалена");
        } catch (SQLException e) {
            System.out.println("Таблица пользователей не удалена " + e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
//Сохранение параметров пользователей в таблицу
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(name, lastname, age) VALUES (?,?,?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();

            System.out.println("User с именем – " + name + " фамилией " + lastName + " возрастом " + age + " добавлен в базу данных");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
// Удаление User из таблицы ( по id )

        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = (?)")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Пользователь удален");
        }catch (SQLException e) {
            System.out.println("Пользователь не удален " + e);
        }
    }

    public List<User> getAllUsers() {
// Получение всех User(ов) из таблицы
        List <User> userList = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

            while(resultSet.next()){
                User user1 = new User();

                user1.setId(resultSet.getLong("id"));
                user1.setName(resultSet.getString("name"));
                user1.setLastName(resultSet.getString("lastName"));
                user1.setAge(resultSet.getByte("age"));

                userList.add(user1);
            }
            System.out.println("Получили всех пользователей");
        } catch (SQLException e) {
            System.out.println("Не получили всех пользователей " + e);
        }
        return userList;
    }

    public void cleanUsersTable() {
// Очистка содержания таблицы
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM users");
            System.out.println("Таблица очищена");
        }catch (SQLException e) {
            System.out.println("Таблица не очищена " + e);
        }
  }
}
