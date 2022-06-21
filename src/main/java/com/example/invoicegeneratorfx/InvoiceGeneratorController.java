package com.example.invoicegeneratorfx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class InvoiceGeneratorController {

    private static final String API ="http://localhost:8080/invoices";
    @FXML
    private Label invoiceStatusText;

    @FXML
    private TextField txtfieldCustomerId;


    @FXML
    protected void generateInvoice() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(API + "/customer_id" + txtfieldCustomerId.getText()))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        txtfieldCustomerId.setText("");

    }
}