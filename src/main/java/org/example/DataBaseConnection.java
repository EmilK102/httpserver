package org.example;

import java.sql.*;
import java.util.Properties;

public class DataBaseConnection
{
    private final String url;
    private final Properties connect;

    public DataBaseConnection() {
        url = "dbc:postgresql:my_project";
        connect = new Properties();
        connect.setProperty("user","testuser");
        connect.setProperty("password","test");
        try(Connection connection = DriverManager.getConnection(this.url,this.connect);) {
            System.out.println("Succseful!");
        }
        catch (Exception e){
            System.out.println("Fail");
            System.out.println(e.getMessage());
        }
    }
    public String getResult(){
        String result = "id\t|client\n";
        try{
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(this.url,this.connect);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM room");
            while (resultSet.next()){
                int id = resultSet.getInt(1);
                String client = resultSet.getString(2);
                result += String.format("%d\t|%s\n", id, client);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
