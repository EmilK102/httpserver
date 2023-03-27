package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DataBaseConnection
{
    private final String url;
    private final Properties connect;

    public DataBaseConnection(String url, String user, String password) {
        this.url = url;
        connect = new Properties();
        connect.setProperty("user",user);
        connect.setProperty("password",password);
    }
}
