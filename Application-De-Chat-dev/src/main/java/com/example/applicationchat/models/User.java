package com.example.applicationchat.models;

import com.example.applicationchat.dao.MysqlConnection;
import lombok.*;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
    private Integer id;
    private String username;
    private String email;
    private String password;
    MysqlConnection mysqlConnection =new MysqlConnection();
    Connection con=mysqlConnection.getConn();

    public User(String username, String email, String password) {
        this.username=username;
        this.email=email;
        this.password=password;
    }
    public boolean CreateUser() {
        boolean b;
        try {
            Statement st;
            st = con.createStatement();
            String sql = "insert into users (username,email,password) values ('"+this.username + "','" + this.email + "','" + this.password + "')";
            st.executeUpdate(sql);
            b = true;
        } catch (SQLException e) {
            b = false;
        }
        return b;

    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password){
        this.password=password;
    }


    public User() {
        super();
    }

    public  long getIdOfEmail(String email) {
        long IdUser = -1;
        try {
            Statement st;
            st = con.createStatement();
            ResultSet resu;
            String sql = "select id from users where email = '"+email+"'";
            resu = st.executeQuery(sql);
            while(resu.next()) {
                IdUser = resu.getLong("id");
            }
        }catch(SQLException e) {
            IdUser = -1;
        }
        return IdUser;
    }
    public String GetMyPassword(long idusr) {
        String password = "";
        try {
            Statement st;
            st = con.createStatement();
            ResultSet resu;
            String sql=null;
            sql = "select * from users where id = '"+idusr+"'";
            resu = st.executeQuery(sql);
            while(resu.next()) {
                password = mysqlConnection.dechiffrer(resu.getString("password"));
            }
        }catch(SQLException e) {
            password = null;
        }
        return password;
    }

}
