package com.sistema.servicios;

import com.sistema.modelo.Proveedor;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;


public class ProveedorCellFactory implements Callback<ListView<Proveedor>, ListCell<Proveedor>> {
    @Override
    public ListCell<Proveedor> call(ListView<Proveedor> param) {
        return new ListCell<Proveedor>() {
            @Override
            protected void updateItem(Proveedor proveedor, boolean empty) {
                super.updateItem(proveedor, empty);

                if (empty || proveedor == null) {
                    setText(null);
                    setGraphic(null);
                    // Asegura que no haya padding ni bordes en celdas vacías
                    setStyle("-fx-border-width: 0; -fx-padding: 0;");
                } else {

                    VBox info = new VBox(
                            new Label(proveedor.getNombre()));

                    HBox layout = new HBox(10, new ImageView("/img/Truck.png"), info);

                    setGraphic(layout);
                    // Debes establecer el texto en null cuando usas setGraphic
                    setText(null);
                }
            }
        };
    }
}