module com.example.applicationchat {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires AnimateFX;
    requires java.sql;
    requires static lombok;
    requires javax.mail;


    opens com.example.applicationchat to javafx.fxml;
    exports com.example.applicationchat;
}