package com.example.applicationchat.models;

import com.example.applicationchat.dao.MysqlConnection;
import lombok.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@NoArgsConstructor
@Data
@AllArgsConstructor
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

}
