package com.example.applicationchat.dao;


import lombok.NoArgsConstructor;

import java.sql.*;

@NoArgsConstructor
public class MysqlConnection {
    private static Connection conn;

    public static Connection getConn() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/applicationchat?autoReconnect=true&useSSL=false", "root", "");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur de connexion à la base de donnée");
            return null;
        }
    }

}
