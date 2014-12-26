package com.ddling.DBManage;

import com.ddling.utils.LoggerFactory;
import org.apache.log4j.Logger;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by lingdongdong on 14/12/26.
 */
public class DBManage {

    public Logger logger = LoggerFactory.getLogger(DBManage.class);

    private Connection connection = null;

    public Connection getConnection() {
        return connection;
    }

    public DBManage() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:mail.db");
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            logger.error("org.sqlite.JDBC class Not Found!");
        } catch (SQLException e) {
            logger.error("sqlite connection fail!");
        }

        logger.info("open db successfully!");
    }

    public ResultSet executeQuery(String sql) {
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

        } catch (SQLException e) {
            logger.error("Create Query Statement fail!");
        }

        return resultSet;
    }

    public void executeUpdate(String sql) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Create Update Statement sql fail!");
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Close the Sqlite connection fail!");
        }

        logger.info("Close the Sqlite connection Successfully!");
    }

    public static void main(String[] args) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:mail.db");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.print("Open Done!!");
    }
}
