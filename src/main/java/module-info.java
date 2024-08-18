module com.example.tescik {
    requires javafx.controls;
    requires javafx.fxml;
    requires opencv;
    requires java.desktop;


    opens com.example.tescik to javafx.fxml;
    exports com.example.tescik;
}