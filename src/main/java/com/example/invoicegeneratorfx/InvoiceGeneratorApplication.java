package com.example.invoicegeneratorfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InvoiceGeneratorApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InvoiceGeneratorApplication.class.getResource("InvoiceGeneratorFXUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 620, 440);
        InvoiceGeneratorController controller = (InvoiceGeneratorController) fxmlLoader.getController();
        controller.application = this;
        stage.setTitle("Invoice Generator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}