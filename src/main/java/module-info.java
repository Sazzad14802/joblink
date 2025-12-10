module com.joblink {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;

    opens com.joblink to javafx.fxml;
    opens com.joblink.controller to javafx.fxml;
    exports com.joblink;
}
