package com.sistema.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador para la vista principal del panel de la aplicación.
 * Gestiona la navegación entre diferentes vistas (Dashboard, Productos, Proveedores, Ventas, Reportes)
 * mediante un menú, actualiza los estilos de los botones de navegación y carga dinámicamente las vistas
 * en un panel.
 */
public class PanelPrincipalController {

    @FXML
    private Button btnMenuDashboard, btnMenuProductos, btnMenuProveedores, btnMenuVentas, btnMenuReportes;

    @FXML
    private AnchorPane panelDinamico;

    @FXML
    private SplitPane splitPane;

    private Button botonActivo = null;

    /**
     * Inicializa el controlador, configurando el botón de Dashboard como seleccionado por defecto
     * y cargando la vista correspondiente. También ajusta propiedades del SplitPane para evitar
     * interacciones no deseadas con el divisor.
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
     * Maneja el evento del botón de menú para la vista de Dashboard.
     * Actualiza el estilo del botón seleccionado y carga la vista de Dashboard.
     * @param actionEvent El evento de acción generado por el botón.
     */
    public void btnDashboardMenu(ActionEvent actionEvent) {
        actualizarEstiloBotones(btnMenuDashboard);
        cargarVista("Dashboard.fxml");
    }

    /**
     * Maneja el evento del botón de menú para la vista de Productos.
     * Actualiza el estilo del botón seleccionado y carga la vista de Productos.
     * @param actionEvent El evento de acción generado por el botón.
     */
    public void btnProductosMenu(ActionEvent actionEvent) {
        actualizarEstiloBotones(btnMenuProductos);
        cargarVista("Productos.fxml");
    }

    /**
     * Maneja el evento del botón de menú para la vista de Proveedores.
     * Actualiza el estilo del botón seleccionado y carga la vista de Proveedores.
     * @param actionEvent El evento de acción generado por el botón.
     */
    public void btnProveedoresMenu(ActionEvent actionEvent) {
        actualizarEstiloBotones(btnMenuProveedores);
        cargarVista("Proveedores.fxml");
    }

    /**
     * Maneja el evento del botón de menú para la vista de Ventas.
     * Actualiza el estilo del botón seleccionado.
     * @param actionEvent El evento de acción generado por el botón.
     */
    public void btnVentasMenu(ActionEvent actionEvent) {
        actualizarEstiloBotones(btnMenuVentas);
        cargarVista("Ventas.fxml");
    }

    /**
     * Maneja el evento del botón de menú para la vista de Reportes.
     * Actualiza el estilo del botón seleccionado.
     * @param actionEvent El evento de acción generado por el botón.
     */
    public void btnReportesMenu(ActionEvent actionEvent) {
        actualizarEstiloBotones(btnMenuReportes);
        cargarVista("Reportes.fxml");
    }

    /**
     * Maneja el evento del botón para cerrar la sesión.
     * Cierra la ventana actual del panel principal y abre la ventana de inicio de sesión.
     * @param actionEvent El evento de acción generado por el botón de cerrar sesión.
     */
    public void btnCerrarSesionAction(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginVista.fxml"));
            Parent root = loader.load();
            // Crear una nueva ventana
            Stage stage = new Stage();
            Image icono = new Image(getClass().getResource("/img/Icon.png").toExternalForm());
            stage.getIcons().add(icono);
            stage.setTitle("ElectroStock - Inicio de Sesión");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
        }
        catch (IOException e) {
            System.err.println("Error al lanzar pantalla de registro" + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Actualiza el estilo visual de los botones del menú para resaltar el botón seleccionado.
     * Restaura el estilo del botón previamente activo y aplica un estilo distintivo al nuevo botón activo.
     * @param botonSeleccionado El botón del menú seleccionado.
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
     * Carga dinámicamente una vista FXML en el panel dinámico de la interfaz.
     * @param ruta La ruta relativa del archivo FXML a cargar (por ejemplo, "Dashboard.fxml").
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
