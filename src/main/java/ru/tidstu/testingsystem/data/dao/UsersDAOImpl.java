package ru.tidstu.testingsystem.data.dao;

import lombok.extern.log4j.Log4j;
import ru.tidstu.testingsystem.utils.DataBase;
import ru.tidstu.testingsystem.data.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class UsersDAOImpl implements UsersDAO {

    private DataBase dataBase = DataBase.getInstance();
    private static User curUser;

    public boolean setCurrentUser(String login, String password){
        String query = "SELECT u.login, u.password, u.full_name, g.name_group " +
                       "FROM users u LEFT JOIN groups g ON u.id_group = g.id " +
                       "WHERE login = '" + login + "' AND password = '" + password + "'";
        ResultSet result = dataBase.execSelect(query);
        try {
            result.next();
            curUser = User.builder()
                    .login(result.getString("LOGIN"))
                    .password(result.getString("PASSWORD"))
                    .name(result.getString("FULL_NAME"))
                    .group(result.getString("NAME_GROUP"))
                    .build();
        } catch (SQLException e) {
            log.error("Error is reading dates from table Users. Query: " + query);
        }
        if(curUser == null){
            return false;
        }
        if(login.equals(curUser.getLogin()) && password.equals(curUser.getPassword())){
            return true;
        } else {
            return false;
        }
    }


    public User getCurrentUser(){
        return curUser;
    }

    public boolean isEmptyUserForSignUp(User user){
        String query = "SELECT u.login, u.password, u.full_name, g.name_group " +
                       "FROM users u LEFT JOIN groups g ON u.id_group = g.id " +
                       "WHERE login = '" + user.getLogin() + "'";
        try {
            ResultSet result = dataBase.execSelect(query);
            if(result.next()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            log.error("Error checks of user for sign up. Query: " + query);
        }
        return false;
    }

    public void delUser(String nameUser){
        String query = "DELETE FROM users WHERE full_name = '" + nameUser + "'";
        dataBase.execDelete(query);
    }

    public void addUser(User user){
        String query = "INSERT INTO users(login, password, full_name, id_group) " +
                       "VALUES('" + user.getLogin() + "','" + user.getPassword() + "','" + user.getName() + "'," +
                       "(SELECT g.id FROM groups g WHERE g.name_group = '" + user.getGroup() + "'))";
        dataBase.execInsert(query);
    }

    public List<User> getUsers()  {
        String query = "SELECT * " +
                       "FROM users u " +
                       "LEFT JOIN groups g ON u.id_group = g.id " +
                       "ORDER BY u.full_name";
        List<User> users = loadUsersFromDB(query);
        return users;
    }

    public List<User> getUsersFromGroup(String nameGroup){
        String query = "SELECT * " +
                       "FROM users u " +
                       "LEFT JOIN groups g ON u.id_group = g.id " +
                       "WHERE u.id_group = (SELECT id FROM groups WHERE name_group = '" + nameGroup + "') " +
                       "ORDER BY u.full_name";
        List<User> users = loadUsersFromDB(query);
        return users;
    }

    private List<User> loadUsersFromDB(String query){
        ArrayList<User> users = new ArrayList<User>();
        ResultSet result = dataBase.execSelect(query);
        try {
            while (result.next()){
                User user = User.builder()
                        .login(result.getString("LOGIN"))
                        .name(result.getString("FULL_NAME"))
                        .group(result.getString("NAME_GROUP"))
                        .bestResult(result.getString("COUNT_TRUE_ANSWERS") + "/" + result.getString("COUNT_QUESTIONS"))
                        .build();
                log.debug(user.toString());
                users.add(user);
            }
        } catch (SQLException e) {
            log.error("Error reading all users from data base. Query: " + query);
        }
        return users;
    }

}