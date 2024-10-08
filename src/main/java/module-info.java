module com.hxngxd.library_manager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires jbcrypt;
    requires static lombok;

    opens com.hxngxd.library_manager to javafx.fxml;
    exports com.hxngxd.library_manager;
}