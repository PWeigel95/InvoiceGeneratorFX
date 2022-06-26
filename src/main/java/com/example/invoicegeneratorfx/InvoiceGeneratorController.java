package com.example.invoicegeneratorfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class InvoiceGeneratorController {

    private static final int TIMER_PERIOD = 2000;

    private static final String API ="http://localhost:8080/invoices";

    @FXML
    public Pane pane;

    @FXML
    public Button btnGenerateInvoice;

    @FXML
    public Hyperlink invoiceLink;

    public InvoiceGeneratorApplication application;

    @FXML
    private Label invoiceStatusText;

    @FXML
    private TextField txtfieldCustomerId;


    @FXML
    protected void generateInvoice() throws URISyntaxException, IOException, InterruptedException {
        String customerId = txtfieldCustomerId.getText();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(API + "/" + customerId))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        txtfieldCustomerId.setText("");
        txtfieldCustomerId.setDisable(true);
        btnGenerateInvoice.setDisable(true);
        invoiceStatusText.setText("Generating...");
        invoiceLink.setVisible(false);

        Timer tr = new Timer();
        tr.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!pane.getScene().getWindow().isShowing()) {
                    tr.cancel();
                    Platform.runLater(() -> {
                        txtfieldCustomerId.setDisable(false);
                        btnGenerateInvoice.setDisable(false);
                    });
                }
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(new URI(API + "/" + customerId))
                            .GET()
                            .build();

                    HttpResponse<String> response = HttpClient.newBuilder()
                            .build()
                            .send(request, HttpResponse.BodyHandlers.ofString());

                    String body = response.body();
                    System.out.println("Response: " + body);
                    if (body.contains(";")) {
                        String[] splitted = body.split(";", 2);
                        if (splitted.length == 2) {
                            LocalDateTime creationDate = LocalDateTime.parse(splitted[0], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                            String filename = splitted[1];

                            tr.cancel();
                            Platform.runLater(() -> {
                                String creationDateStr = DateTimeFormatter.ofPattern("d. M. yyyy, HH:mm").format(creationDate);
                                invoiceStatusText.setText("Generated at " + creationDateStr + ":");
                                invoiceLink.setText(filename);
                                invoiceLink.setVisible(true);
                                txtfieldCustomerId.setDisable(false);
                                btnGenerateInvoice.setDisable(false);
                            });
                        }
                    }
                } catch (URISyntaxException | IOException | InterruptedException e) {
                    e.printStackTrace();
                    tr.cancel();
                    Platform.runLater(() -> {
                        invoiceStatusText.setText("Error: " + e);
                        txtfieldCustomerId.setDisable(false);
                        btnGenerateInvoice.setDisable(false);
                        invoiceLink.setVisible(false);
                    });
                }
            }
        }, TIMER_PERIOD, TIMER_PERIOD);

    }

    public void openInvoice(ActionEvent actionEvent) {
        String link = invoiceLink.getText();

        if (application != null) {
            application.getHostServices().showDocument(link);
        }
    }
}