package fr.ziarzer.commands;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static Connection connectionInstance = null;
    private static final String DATABASE_URL = "jdbc:sqlite:" + System.getProperty("user.dir") + "/bestille-plugin.db";

    public static Connection getInstance() throws SQLException {
        if (connectionInstance == null) {
            connectionInstance = DriverManager.getConnection(DATABASE_URL);
        }
        return connectionInstance;
    }
}
