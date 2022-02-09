package com.example.applicationchat;
import com.example.applicationchat.dao.MysqlConnection;
import com.example.applicationchat.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.EventObject;


public class Client {
    private  Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    MysqlConnection mysqlConnection =new MysqlConnection();
    Connection con=mysqlConnection.getConn();
    PreparedStatement pst = null;
    ResultSet rs= null;

    @FXML
    private Stage stage;

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter =new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error creating client");
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }
    public void sendMessageToServer(String messageToServer) {
        try {
            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println(e);
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    public void add_msg(String text, int x ) throws Exception {
        String sql = "insert into "+ControllerClient.variable+" (text,ref) values (?,?)";

        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, text);
            pst.setInt(2,x);
            pst.executeUpdate();
            rs=pst.executeQuery("SELECT * FROM "+ControllerClient.variable+"");

            while(rs.next()){

                System.out.println(rs.getString("text"));

            }

        } catch (Exception e) {
            System.out.println(e);


        }

    }
    public void receiveMessageFromServer(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                    try {
                        String messageFromServer =bufferedReader.readLine();
                        ControllerClient.addLabel(messageFromServer,vBox);
                        String [] tab = new String[2];
                        tab = messageFromServer.split(":");
                        try {
                            add_msg(tab[1],1);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }catch (IOException e){
                        System.out.println(e);
                        closeEverything(socket,bufferedReader,bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }
    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        try {
            if (bufferedReader !=null){
                bufferedReader.close();
            }

            if (bufferedWriter!= null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }

        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
