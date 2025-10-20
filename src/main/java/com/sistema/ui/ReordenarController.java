package com.sistema.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class ReordenarController {

    @FXML
    private Label proveedorNameLabel;


    @FXML
    public void btnAceptarAction(ActionEvent actionEvent) {
        Stage stage = (Stage) proveedorNameLabel.getScene().getWindow();
        stage.close();
    }

    public void nombreALabel(String nombrePr){
        proveedorNameLabel.setText(nombrePr);
    }
}
