package com.sistema.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

import java.io.IOException;

public class PanelPrincipalController {

    @FXML
    private Button btnMenuDashboard, btnMenuProductos, btnMenuProveedores, btnMenuVentas, btnMenuReportes;

    @FXML
    private AnchorPane panelDinamico;

    @FXML
    private SplitPane splitPane;

    private Button botonActivo = null;

    /**
     * Inicializa el boton de Dashboard como seleccionado
     */
    @FXML
    public void initialize() {
        // Al iniciar, define el botón activo (Dashboard)
        splitPane.lookupAll(".split-pane-divider").forEach(div -> div.setMouseTransparent(true));
        botonActivo = btnMenuDashboard;
        actualizarEstiloBotones(btnMenuDashboard);
        cargarVista("Dashboard.fxml");
    }

    /**
     *
     * @param actionEvent evento del boton
     */
    public void btnDashboardMenu(ActionEvent actionEvent) {
        actualizarEstiloBotones(btnMenuDashboard);
        cargarVista("Dashboard.fxml");
    }

    public void btnProductosMenu(ActionEvent actionEvent) {
        actualizarEstiloBotones(btnMenuProductos);
        cargarVista("Productos.fxml");
    }

    public void btnProveedoresMenu(ActionEvent actionEvent) {
        actualizarEstiloBotones(btnMenuProveedores);
        cargarVista("Proveedores.fxml");
    }

    public void btnVentasMenu(ActionEvent actionEvent) {
        actualizarEstiloBotones(btnMenuVentas);
    }

    public void btnReportesMenu(ActionEvent actionEvent) {
        actualizarEstiloBotones(btnMenuReportes);
    }


    /**
     * Funcion que actualiza el estilo del boton del menu del dashboard
     * para mostrar mas claro en que pantalla se encuentra
     * @param botonSeleccionado botn del menu seleccionado
     */
    private void actualizarEstiloBotones(Button botonSeleccionado) {
        // Restaurar el estilo del botón previamente activo
        if (botonActivo != null) {
            botonActivo.setBackground(Background.fill(Color.TRANSPARENT));
            botonActivo.setTextFill(Color.BLACK);
            botonActivo.setStyle(""); // limpiar estilos previos
        }

        // Aplicar estilo al nuevo botón activo
        botonSeleccionado.setBackground(Background.fill(Color.rgb(221, 221, 221)));
        botonSeleccionado.setTextFill(Color.rgb(88, 130, 232));
        botonSeleccionado.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");

        // Guardar el botón actual como activo
        botonActivo = botonSeleccionado;
    }

    /**
     * Funcion para buscar las vistas de cada una de las opciones del menu
     * y cambiarlas en el panel dinamico
     * @param ruta
     */
    private void cargarVista(String ruta){
        try {
            AnchorPane newVista = FXMLLoader.load(getClass().getResource("/fxml/"+ruta));
            panelDinamico.getChildren().setAll(newVista);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
