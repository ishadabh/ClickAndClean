package com.clickandclean.dao;

import com.clickandclean.model.Report;
import com.clickandclean.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    public boolean createReport(
            int userId,
            String description,
            String location, String imagePath ) {

        String sql = """
                INSERT INTO reports
                (user_id, description, location, status, reward)
                VALUES (?, ?, ?, 'Pending', 50)
                COMMIT
                """;

        try (Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setString(2, description);
            statement.setString(3, location);

            int rows = statement.executeUpdate();

            if (rows > 0) {

                updateUserPoints(connection, userId);
                return true;
            }
        } catch (Exception e) {

            System.err.println("Database Error in createReport:");
            e.printStackTrace();
        }
        return false;
    }
    private void updateUserPoints(Connection connection, int userId) throws Exception {

        String sql = """
                UPDATE users
                SET points = points + 50
                WHERE user_id = ?
                COMMIT
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
        	
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
    }

    public List<Report> getReportsByUser(int userId) {

        List<Report> reports = new ArrayList<>();

        String sql = """
                SELECT report_id, user_id, description,
                       location, image_path, status,
                       assigned_driver, reward, created_at
                FROM reports
                WHERE user_id = ?
                ORDER BY report_id DESC
                """;
        try (Connection connection =DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    reports.add(extractReport(resultSet));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reports;
    }

    public List<Report> getAllReports() {

        List<Report> reports = new ArrayList<>();

        String sql = """
                SELECT report_id, user_id, description, location, 
                    image_path, status, assigned_driver, reward,created_at
                FROM reports
                ORDER BY report_id DESC
                """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                reports.add(extractReport(resultSet));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reports;
    }
    public boolean assignReport(int reportId,String driver) {
        String sql = """
                UPDATE reports
                SET status = 'Assigned', assigned_driver = ?
                WHERE report_id = ?
                COMMIT
                """;
        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, driver);
            statement.setInt(2, reportId);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

    public boolean completeReport(int reportId) {

        String sql = """ UPDATE reports SET status = 'Completed' WHERE report_id = ? COMMIT""";

        try (
            Connection connection = DBConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reportId);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

    private Report extractReport(ResultSet resultSet) throws Exception {

        Report report = new Report();

        report.setReportId(resultSet.getInt("report_id"));
        report.setUserId(resultSet.getInt("user_id"));
        report.setDescription(resultSet.getString("description"));
        report.setLocation(resultSet.getString("location"));
        report.setImagePath(resultSet.getString("image_path"));
        report.setStatus(resultSet.getString("status"));
        report.setAssignedDriver(resultSet.getString("assigned_driver"));
        report.setReward(resultSet.getInt("reward"));
        report.setCreatedAt(resultSet.getString("created_at"));

        return report;
    }
}