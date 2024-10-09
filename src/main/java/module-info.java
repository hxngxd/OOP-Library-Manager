module com.hxngxd.libraryManager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires jbcrypt;
    requires static lombok;

    opens com.hxngxd.libraryManager to javafx.fxml;
    exports com.hxngxd.libraryManager;
    opens com.hxngxd.controller to javafx.fxml;
}