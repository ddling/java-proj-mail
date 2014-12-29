package com.ddling.User;

import com.ddling.DBManage.DBManage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lingdongdong on 14/12/26.
 */
public class UserManage {

    public List<User> getUsers() throws SQLException {
        DBManage dbManage = new DBManage();
        List<User> userList = new LinkedList<User>();
        String sql = "SELECT * FROM USER;";
        ResultSet resultSet = dbManage.executeQuery(sql);
        while (resultSet.next()) {
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String email = resultSet.getString("email");
            User newUser = new User(username, password, email);
            userList.add(newUser);
        }

        dbManage.close();

        return userList;
    }

    public void insertNewUser(User newUser) {

        DBManage dbManage = new DBManage();

        String username = newUser.getUsername();
        String password = newUser.getPassword();
        String email = newUser.getEmail();

        String sql = String.format("INSERT INTO USER VALUES (" +
                                   "NULL, '%s', '%s', '%s')",
                                   username, password, email);

        dbManage.executeUpdate(sql);

        dbManage.close();
    }

    public void deleteUser(String username) {

        DBManage dbManage = new DBManage();

        String sql = String.format("DELETE FROM USER WHERE username = '%s'", username);

        dbManage.executeUpdate(sql);

        dbManage.close();
    }

    public static void main(String[] args) {
        UserManage userManage = new UserManage();

        userManage.deleteUser("ddd");

    }
}
