package com.example.applicationchat.dao;


import java.sql.Connection;
import java.sql.DriverManager;

public class MysqlConnection {
    private static Connection conn;
   // Connection conn;
    public static Connection getConn(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn= DriverManager.getConnection("jdbc:mysql://localhost:8080/applicationchat?autoReconnect=true&useSSL=false","root","1234567");
            return conn;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erreur de connexion à la base de donnée");
            return null;
        }
    }

}
