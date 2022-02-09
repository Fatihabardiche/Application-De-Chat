package com.example.applicationchat;

import com.example.applicationchat.dao.MysqlConnection;
import com.example.applicationchat.models.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import server.ServerThread;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ControllerClient implements Initializable {
    private Client client;
    private User user;
    MysqlConnection mysqlConnection =new MysqlConnection();
    Connection con=mysqlConnection.getConn();
    PreparedStatement pst = null;
    ResultSet rs= null;

    @FXML
    private Button button_send;
    @FXML
    private TextField tf_message;
    @FXML
    private VBox vbox_messages;
    @FXML
    private ScrollPane sp_main;


    @FXML
    private Stage stage;
    public static String variable;
    public void add_msg(ActionEvent actionEvent,TextField text, int x ) throws Exception {
        stage=(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        String name = stage.getTitle();
        String sql = "insert into "+name+" (text,ref) values (?,?)";

        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, tf_message.getText());
            pst.setInt(2,x);
            pst.executeUpdate();
            rs=pst.executeQuery("SELECT * FROM "+name+"");

            while(rs.next()){

                System.out.println(rs.getString("text"));

            }

        } catch (Exception e) {
            System.out.println(e);


        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            client =new Client(new Socket("localhost",5000));
            System.out.println("connected to server");

        }catch (IOException e){
            e.printStackTrace();

        }
        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                sp_main.setVvalue((Double) t1);
            }
        });
        client.receiveMessageFromServer(vbox_messages);
        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage=(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                String messageToSend =tf_message.getText();
                String name = stage.getTitle();
                variable =name;
                String message =name +":"+messageToSend;
                if (!messageToSend.isEmpty()){
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);
                    hBox.setPadding(new Insets(5,5,5,10));
                    Text text =new Text(messageToSend);
                    TextFlow textFlow=new TextFlow(text);
                    textFlow.setStyle("-fx-color:rgb(239,242,255);"+"-fx-background-color:rgb(174, 214, 241);"+
                            "-fx-background-radius: 15px;");
                    textFlow.setPadding(new Insets(5,10,5,10));
                    text.setFill(Color.color(0.0,0.0,0.0));
                    hBox.getChildren().add(textFlow);
                    vbox_messages.getChildren().add(hBox);
                    client.sendMessageToServer(message);
                    try {

                        add_msg(actionEvent,tf_message,0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    tf_message.clear();
                }
            }
        });


    }
    public void handle1(String msg) {
        String messageToSend = msg;

        if (!messageToSend.isEmpty()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 5, 10));
            Text text = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-color:rgb(239,242,255);"+"-fx-background-color:rgb(174, 214, 241);"+
                    "-fx-background-radius: 15px;");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0, 0, 0));

            hBox.getChildren().add(textFlow);
            vbox_messages.getChildren().add(hBox);





        }
    }
    public static void addLabel(String msgFromServer,VBox vBox ){
        String [] tab = new String[2];
        tab = msgFromServer.split(":");
        HBox hBox=new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));
        Text text = new Text(tab[0]);
        Text text1 = new Text(tab[1]);
        TextFlow textFlow= new TextFlow(text);
        TextFlow textFlow1= new TextFlow(text1);
        textFlow.setStyle("-fx-color: rgb(207,52,118);"+
                "-fx-background-radius: 15px;");
        text.setStyle("-fx-color: rgb(207,52,118);");
        textFlow1.setStyle("-fx-background-color: rgb(233,233,235);"+
                "-fx-background-radius: 15px;");
        textFlow.setPadding(new Insets(5,5,5,10));
        textFlow1.setPadding(new Insets(5,5,5,10));
        hBox.getChildren().add(textFlow);
        hBox.getChildren().add(textFlow1);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });

    }
    public  void Previous(ActionEvent event) throws  Exception {
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        String name = stage.getTitle();

        String sql = "insert into "+name+" (text) values (?)";

        try {
            pst = con.prepareStatement(sql);
            rs=pst.executeQuery("SELECT * FROM "+name+"");

            while(rs.next()){

                if(rs.getInt("ref")==0) handle1(rs.getString("text"));
                else
                    ControllerClient.addLabel(rs.getString("text"),vbox_messages);

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);


        }

    }


}