module com.example.weatherfxml {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


    opens weatherfxml to javafx.fxml;
    exports weatherfxml;
}