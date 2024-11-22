module com.hxngxd.libraryManager {
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;
    requires jbcrypt;
    requires org.apache.logging.log4j;
    requires java.desktop;
    requires de.jensd.fx.glyphs.fontawesome;
    requires javafx.controls;
    requires jdk.compiler;

    opens com.hxngxd.libraryManager to javafx.fxml;
    exports com.hxngxd.libraryManager;
    opens com.hxngxd.ui to javafx.fxml;
    opens com.hxngxd.ui.controller.book to javafx.fxml;
    opens com.hxngxd.ui.manage to javafx.fxml;
}