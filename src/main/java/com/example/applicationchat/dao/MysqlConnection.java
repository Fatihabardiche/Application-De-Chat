package com.example.applicationchat.dao;


import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;

@NoArgsConstructor
public class MysqlConnection {
    Connection conn;
    public Connection getConn(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/applicationchat?autoReconnect=true&useSSL=false","root","");
            return conn;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erreur de connexion à la base de donnée");
            return null;
        }
    }

}
