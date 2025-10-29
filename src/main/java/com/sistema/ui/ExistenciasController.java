package com.sistema.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;


public class ExistenciasController{

    @FXML
    private Label productoLabel;

    @FXML
    private Spinner<Integer> cantidadSpinner;

    @FXML
    void initialize(String nomPro){
        productoLabel.setText(nomPro);
    }


    public void btnCancelarAction(ActionEvent actionEvent) {
    }

    public void btnGuardarAction(ActionEvent actionEvent) {
    }
}
