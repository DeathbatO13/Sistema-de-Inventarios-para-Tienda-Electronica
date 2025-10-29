package com.sistema.ui;

import com.sistema.dao.ProductoDAO;
import com.sistema.modelo.Producto;
import com.sistema.servicios.GestorInventario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;


public class ExistenciasController {

    @FXML
    private Label productoLabel;

    @FXML
    private Spinner<Integer> cantidadSpinner;

    private Producto product;

    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1, 1);
        cantidadSpinner.setValueFactory(valueFactory);
    }

    public void setProducto(Producto producto) {
        this.product = producto;
        productoLabel.setText(producto.getNombre());
    }

    @FXML
    public void btnCancelarAction(ActionEvent actionEvent) {
        Stage stage = (Stage) productoLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void btnGuardarAction(ActionEvent actionEvent) {

        int cantidad = cantidadSpinner.getValue();

        if (new GestorInventario().ajustarStock(product.getId(), cantidad, "AJUSTE")) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cambio Confirmado");
            alert.setHeaderText(null);
            alert.setContentText("Existencias agregadas exitosamente");
            alert.initOwner(productoLabel.getScene().getWindow());
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Ocurrió un error al agregar existencias");
            alert.initOwner(productoLabel.getScene().getWindow());
            alert.showAndWait();
        }
    }
}

