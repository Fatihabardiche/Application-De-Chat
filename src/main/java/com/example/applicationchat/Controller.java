package com.example.applicationchat;

import com.example.applicationchat.dao.MysqlConnection;
import com.example.applicationchat.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.BreakIterator;
import java.util.Objects;


public class Controller extends Thread {
    @FXML
    private TextField username;
    @FXML
    private TextField userName;
    @FXML
    private TextField pass;
    @FXML
    private TextField email;
    @FXML
    private TextField password;

    Connection conn = null;
    ResultSet rs= null;
    PreparedStatement pst = null;


    @FXML
        private Stage stage;
    @FXML
        private Scene scene;
    @FXML
        private Parent root;


        public void switchToScene1(ActionEvent event) throws IOException {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SignUp.fxml")));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

        public void switchToScene2(ActionEvent event) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        public void createUser(ActionEvent event) throws IOException{
            String username = this.username.getText();
            String email = this.email.getText();
            String password=this.password.getText();
            User user = new User(username,email,password);
            System.out.println(user.CreateUser());
            this.switchToScene2(event);
        }

    @FXML
    private void Login (ActionEvent event ) throws  Exception {
        conn = MysqlConnection.getConn() ;
        String sql = "select * from users where username = ? and password = ?" ;
        try {
            pst = conn.prepareStatement(sql);

            pst.setString(1, userName.getText());
            pst.setString(2, pass.getText());
            rs = pst.executeQuery();
            if(rs.next ()){
                JOptionPane.showMessageDialog(null, "Username and password are correct");

            }else
                JOptionPane.showMessageDialog(null, "Invalid Username or password ");



        }catch (Exception e ) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

}