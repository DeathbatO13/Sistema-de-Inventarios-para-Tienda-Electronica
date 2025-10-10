package com.sistema.ui;

import com.sistema.modelo.Producto;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.control.skin.TableHeaderRow;

public class DashboardController {

    @FXML
    private Label productosTotalesLabel, proveedoresTotalesLabel, totalVentasLabel;

    @FXML
    private TableView<Producto> tablaExistencias;

    @FXML
    private TableColumn<Producto, String> productoColumn;

    @FXML
    private TableColumn<Producto, Integer> unidadesColumn;

    @FXML
    private TableColumn<Producto, String> proveedorColumn;

    @FXML
    private TableColumn<Producto, Button> accionColumn;

    /**
     * Funcion para darle estilos a los cabezales de la tabla
     */
    @FXML
    public void initialize() {
        tablaExistencias.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin instanceof TableViewSkin<?>) {
                TableViewSkin<?> skin = (TableViewSkin<?>) newSkin;

                for (Node node : skin.getChildren()) {
                    if (node instanceof TableHeaderRow) {
                        TableHeaderRow header = (TableHeaderRow) node;
                        header.setStyle("-fx-background-color: transparent; -fx-font-family: 'Corbel'; -fx-font-size: 14px;");
                    }
                }
            }
        });
    }


}
