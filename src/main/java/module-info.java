module com.joblink {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.joblink to javafx.fxml;
    opens com.joblink.controller to javafx.fxml;
    exports com.joblink;
}
