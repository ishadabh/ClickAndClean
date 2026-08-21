package com.clickandclean.dao;

import com.clickandclean.model.User;
import com.clickandclean.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public User validateUser(String loginId, String password, String role) {

        String sql = """
                SELECT user_id, name, login_id, password, role, points
                FROM users
                WHERE LOWER(TRIM(login_id)) = LOWER(?)
                AND BINARY password = ?
                AND LOWER(TRIM(role)) = LOWER(?)
                """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, loginId != null ? loginId.trim() : "");
            statement.setString(2, password != null ? password.trim() : "");
            statement.setString(3, role != null ? role.trim() : "");

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();

                    user.setUserId(resultSet.getInt("user_id"));
                    user.setName(resultSet.getString("name"));
                    user.setLoginId(resultSet.getString("login_id"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRole(resultSet.getString("role"));
                    user.setPoints(resultSet.getInt("points"));

                    return user;
                }
            }

        } catch (Exception e) {
            System.err.println("Database Error in UserDAO:");
            e.printStackTrace();
        }

        return null;
    }
}