package com.example.applicationchat;

import com.example.applicationchat.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;


public class Controller extends Thread {
    @FXML
    private TextField username;
    @FXML
    private TextField email;
    @FXML
    private TextField password;


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
    }