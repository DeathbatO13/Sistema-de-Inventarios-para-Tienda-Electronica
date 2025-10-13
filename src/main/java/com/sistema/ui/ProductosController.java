package com.sistema.ui;

import com.sistema.dao.ProductoDAO;
import com.sistema.modelo.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductosController {

    @FXML
    private TextField buscarProducto;

    @FXML
    private Button btnAgregarProducto;

    @FXML
    private TableView<Producto> tablaProductos;

    @FXML
    private TableColumn<Producto, String> codigoProducto;

    @FXML
    private TableColumn<Producto, String> nombreProducto;

    @FXML
    private TableColumn<Producto, Double> precioProducto;

    @FXML
    private TableColumn<Producto, Integer> stockDisponible;

    @FXML
    private TableColumn<Producto, Void> accionesProducto;

    /**
     * Inicializa los valores de los componentes por defecto
     * de la vista y caracteristicas de los mismos
     *
     */
    @FXML
    public void initialize(){
        codigoProducto.setCellValueFactory(new PropertyValueFactory<>("codigoSku"));
        nombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        precioProducto.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
        stockDisponible.setCellValueFactory(new PropertyValueFactory<>("stockActual"));

        // Formato de moneda para la columna de precios
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        precioProducto.setCellFactory(column -> new TableCell<Producto, Double>() {
            @Override
            protected void updateItem(Double valor, boolean empty) {
                super.updateItem(valor, empty);
                if (empty || valor == null) {
                    setText(null);
                } else {
                    setText(formatoMoneda.format(valor));
                }
            }
        });
        //Tooltip para mostrar descripcion
        nombreProducto.setCellFactory(columna -> new TableCell<Producto, String>() {
            private final Tooltip tooltip = new Tooltip();

            {
                tooltip.setStyle(
                        "-fx-font-size: 13px; -fx-font-family: 'Corbel'; " +
                                "-fx-background-color: #f9f9f9; -fx-text-fill: black; -fx-padding: 8px;"
                );
                tooltip.setShowDelay(javafx.util.Duration.millis(0)); // delay
                tooltip.setWrapText(true); // que haga wrap si es largo
                tooltip.setMaxWidth(300);  // ancho máximo del tooltip
            }

            @Override
            protected void updateItem(String nombre, boolean empty) {
                super.updateItem(nombre, empty);

                if (empty || nombre == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(nombre);

                    // Obtener el producto de forma segura desde la fila
                    Producto producto = getTableRow() != null ? (Producto) getTableRow().getItem() : null;

                    if (producto != null && producto.getDescripcion() != null && !producto.getDescripcion().isBlank()) {
                        tooltip.setText(producto.getDescripcion());
                        setTooltip(tooltip);
                    } else {
                        setTooltip(null);
                    }
                }
            }
        });

        // Cargar elementos a la tabla
        cargarListaProductos();
        // Agregar columnas a la tabla
        agregarColumnaAccion();
    }


    /**
     * Evento del boton de agregar producto, despliega una ventana para agregar producto
     * @param actionEvent evento del boton
     */
    @FXML
    public void agregarProductoEvent(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FormularioProducto.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            Image icono = new Image(getClass().getResource("/img/Icon.png").toExternalForm());
            stage.getIcons().add(icono);
            stage.setTitle("Nuevo Producto");
            stage.setScene(new Scene(root));

            // --- Configurar como ventana modal ---
            stage.initModality(Modality.APPLICATION_MODAL); // bloquea la ventana principal
            stage.initOwner(tablaProductos.getScene().getWindow()); // la asocia a la ventana principal

            // Mostrar y esperar a que se cierre
            stage.showAndWait();

            // Al cerrarse, verificar si se guardó un producto nuevo
            FormularioProductController controller = loader.getController();
            if (controller.isProductoGuardado()) {
                cargarListaProductos(); // refrescar tabla si se agregó algo
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Control del evento al buscar un producto por su nombre
     * @param actionEvent evento del campo de busqueda
     */
    @FXML
    public void buscarNombreProducto(ActionEvent actionEvent) {
        String producto = buscarProducto.getText().trim();
        ProductoDAO dao = new ProductoDAO();

        List<Producto> results = dao.buscarPorNombre(producto);
        ObservableList<Producto> datos = FXCollections.observableArrayList(results);
        tablaProductos.setItems(datos);

        agregarColumnaAccion();
    }

    /**
     * Funcion para cargar los datos de los productos a la tabla
     */
    private void cargarListaProductos(){
        ProductoDAO dao = new ProductoDAO();
        List<Producto> lista = dao.listaProductos();

        ObservableList<Producto> datos = FXCollections.observableArrayList(lista);
        tablaProductos.setItems(datos);
    }

    private void agregarColumnaAccion() {
        accionesProducto.setCellFactory(param -> new TableCell<Producto, Void>() {

            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox contenedorBotones = new HBox(8); // separación entre botones

            {
                // Estilos
                btnEliminar.setStyle("-fx-background-color: #cf0808; -fx-text-fill: white; -fx-font-size: 13px; " +
                        "-fx-font-weight: bold; -fx-background-radius: 7;");
                btnEditar.setStyle("-fx-background-color: #5882E8; -fx-text-fill: white; -fx-font-size: 13px; " +
                        "-fx-font-weight: bold; -fx-background-radius: 7;");

                // Margen interno y alineación
                contenedorBotones.setAlignment(Pos.CENTER);
                contenedorBotones.getChildren().addAll(btnEditar, btnEliminar);

                // Acciones
                btnEditar.setOnAction(event -> {
                    Producto producto = getTableView().getItems().get(getIndex());
                    System.out.println("Editar producto: " + producto.getNombre());
                });

                btnEliminar.setOnAction(event -> {
                    Producto producto = getTableView().getItems().get(getIndex());
                    System.out.println("Eliminar producto: " + producto.getNombre());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(contenedorBotones);
                }
            }
        });
    }

}
