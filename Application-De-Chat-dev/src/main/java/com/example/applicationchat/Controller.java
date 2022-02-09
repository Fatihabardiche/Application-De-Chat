package com.example.applicationchat;

import com.example.applicationchat.dao.MysqlConnection;
import com.example.applicationchat.models.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;


public class Controller extends Thread implements Initializable {
    MysqlConnection mysqlConnection = new MysqlConnection();
    Connection con = mysqlConnection.getConn();

    @FXML
    private Label errors;
    @FXML
    private TextField EmailUserForget;
    @FXML
    private Button IdObliePassword;
    @FXML
    private ImageView backButton;

    @FXML
    private Button SendButton;

    @FXML
    private CheckBox isVisible;

    @FXML
    private Button BtnSignIn;
    @FXML
    private TextField username;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private TextField password1;
    @FXML
    private TextField confirmpassword;


    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;


    public void switchToScene1(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SignUp.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene2(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene3(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view1.fxml"));
        Parent root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        ControllerClient controller = loader.getController();
        stage.setTitle(username.getText());
        stage.setScene(scene);
        stage.show();
    }

    public void createUser(ActionEvent event) throws IOException, SQLException {
        String username = this.username.getText();
        String email = this.email.getText();
        String password = this.password.getText();
        String confirmpassword = this.confirmpassword.getText();
        if (this.username.getText().isBlank() || this.email.getText().isBlank() || this.password.getText().isBlank()) {
            errors.setText("Fill all the fields");
            return;
        }
        String regEmail = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";
        System.out.println(email.matches(regEmail));
        if (!email.matches(regEmail)) {
            ;
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERREUR !");
            dialog.setContentText("Email est invalide !!");
            dialog.showAndWait();
            return;
        }
        if (password.length() < 8) {
            errors.setText("The password must be over 8 character!!");
            return;
        }
        //Username
        if (this.CheckUsername(username)) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERREUR !");
            dialog.setContentText("username already exist  !!");
            dialog.showAndWait();
        }
        if (this.CheckUserEmail(email)) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERREUR !");
            dialog.setContentText("Email already exist !!");
            dialog.showAndWait();
            return;
        }
        if (!confirmpassword.equals(password)) {
            errors.setText("Passwords must be the same !!");
            return;
        }


        User user = new User(username, email, password);
        String encryptedPassword = mysqlConnection.chiffrer(password);
        System.out.println(encryptedPassword);
        user.setPassword(encryptedPassword);
        System.out.println(user.CreateUser());
        this.switchToScene2(event);
    }

    private boolean CheckUsername(String username) throws SQLException {
        Statement st;
        st = con.createStatement();
        String sql = "Select username from users where username = '" + username + "'";
        ResultSet result = st.executeQuery(sql);
        if (result.next()) System.out.println(result.getString("username"));
        return result.next();

    }

    private boolean CheckUserEmail(String email) throws SQLException {
        Statement st;
        st = con.createStatement();
        String sql = "Select email from users where email = '" + email + "'";
        ResultSet result = st.executeQuery(sql);

        return result.next();

    }

    @FXML
    private void Login(ActionEvent event) throws Exception {
        if (this.username.getText().isBlank() || this.password.getText().isBlank()) {
            errors.setText("Fill all the fields");
            return;
        }
        con = MysqlConnection.getConn();
        String encryptedPassword = mysqlConnection.chiffrer(password.getText());
        String sql = "select * from users where username = ? and password = ?";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, username.getText());
            pst.setString(2, encryptedPassword);
            rs = pst.executeQuery();
            if (rs.next()) {

                switchToScene3(event);

            } else
                errors.setText("Invalid Username or password ");
        } catch (Exception e) {
            System.out.println(e);
            errors.setText("Erreur de serveur");
        }
    }

    @FXML
    public void toSendOldPassword() throws IOException {
        this.IdObliePassword.getScene().getWindow().hide();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("forgetPassword.fxml"));
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setResizable(false);
        Image icon = new Image(getClass().getResourceAsStream("logo.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Reset password");
        // primaryStage.getIcons().add(new Image("file:../../Images/icon.png"));
        primaryStage.show();
    }


   @FXML
    public void SendOldPassword(ActionEvent event) throws Exception {
        String reg="^$";
        String regEmail = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";
        if(this.EmailUserForget.getText().matches(reg)) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERREUR !");
            dialog.setContentText("Le champs de l'émail est vide !!");
            dialog.showAndWait();
        }else if(!this.EmailUserForget.getText().matches(regEmail)) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("Erreur.");
            dialog.setContentText("Le synatxe de l'email est faux !!");
            dialog.showAndWait();
        }else {
            User user =new User();
            long IdUser = user.getIdOfEmail(this.EmailUserForget.getText());
            if(IdUser != -1 ) {
                if(EmailClient.sendAsHtml(this.EmailUserForget.getText(),user.GetMyPassword(IdUser))) {
                    Alert dialog = new Alert(AlertType.CONFIRMATION);
                    dialog.setTitle("Succès !");
                    dialog.setContentText("Mot de passe envoyé, vérifiez votre boîte de réception!!");
                    Optional<ButtonType> result = dialog.showAndWait();
                    if(result.get() == ButtonType.OK) {
                        this.SendButton.getScene().getWindow().hide();
                        this.switchToScene2(event);
                    }
                }else {
                    Alert dialog = new Alert(AlertType.ERROR);
                    dialog.setTitle("ERREUR !.");
                    dialog.setContentText("Problème !!");
                    dialog.showAndWait();
                }
            }else {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERREUR !.");
                dialog.setContentText("Cet e-mail n'existe pas !!");
                dialog.showAndWait();
            }
        }
    }
    public void BacktoLogin(MouseEvent event) throws IOException {
        this.SendButton.getScene().getWindow().hide();
        Stage login = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Image icon = new Image(getClass().getResourceAsStream("logo.png"));
        Scene scene = new Scene(root);
        login.getIcons().add(icon);
        login.setScene(scene);
        login.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(this.BtnSignIn!=null){
            this.password1.setVisible(false);
        }
    }
    @FXML
    public void showPassword() {
        if(!isVisible.isSelected()){
            this.password.setVisible(true);
            this.password1.setVisible(false);
            this.password.setText(password1.getText());
        }
        else {
            this.password.setVisible(false);
            this.password1.setVisible(true);
            this.password1.setText(password.getText());
        }

    }
}