module com.hxngxd.oop_library_manager {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.hxngxd.oop_library_manager to javafx.fxml;
    exports com.hxngxd.oop_library_manager;
}