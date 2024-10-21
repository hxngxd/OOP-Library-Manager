module com.hxngxd.libraryManager {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;
    requires jbcrypt;
    requires org.apache.logging.log4j;
    requires java.desktop;

    opens com.hxngxd.libraryManager to javafx.fxml;
    exports com.hxngxd.libraryManager;
    opens com.hxngxd.ui to javafx.fxml;
}