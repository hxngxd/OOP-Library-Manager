module com.hxngxd.library_manager {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.hxngxd.library_manager to javafx.fxml;
    exports com.hxngxd.library_manager;
}