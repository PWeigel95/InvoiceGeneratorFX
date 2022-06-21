module com.example.invoicegeneratorfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;


    opens com.example.invoicegeneratorfx to javafx.fxml;
    exports com.example.invoicegeneratorfx;
}