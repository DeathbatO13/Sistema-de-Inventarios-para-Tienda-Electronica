package com.sistema.ui;

import com.sistema.dao.ProductoDAO;
import com.sistema.dao.ProveedorDAO;
import com.sistema.modelo.Producto;
import com.sistema.modelo.Proveedor;
import com.sistema.servicios.ProveedorCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Controlador para la vista de gestión de proveedores.
 * Gestiona la visualización de proveedores en una lista, permite buscar proveedores por nombre,
 * muestra detalles de proveedores seleccionados y sus productos asociados en una tabla,
 * y configura acciones para agregar, editar y eliminar proveedores.
 */
public class ProveedoresController {

    @FXML
    private TextField buscarTextfield;

    @FXML
    private ListView<Proveedor> listaView;

    @FXML
    private Label nombreProvLabel,nombreContLabel,telefonoLabel, correoLabel;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab resumenTab, productosTab;

    @FXML
    private Button btnAgregar, btnEditar, btnEliminar;

    @FXML
    private TableView<Producto> tablaProd;

    @FXML
    private TableColumn<Producto, String> prodName,prodDescripcion;

    @FXML
    private TableColumn<Producto, Integer> prodCantidad;

    @FXML
    private TableColumn<Producto, Double> prodPrecio;

    /**
     * Inicializa los componentes de la vista de proveedores.
     * Configura la tabla de productos, carga la lista inicial de proveedores,
     * establece un listener para mostrar detalles del proveedor seleccionado
     * y selecciona el primer proveedor de la lista si está disponible.
     */
    @FXML
    public void initialize() {

        inicializarTablaProductos();
        cargarProveedores();

        listaView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        mostrarDetalles(newValue);
                    }
                }
        );

        if (!listaView.getItems().isEmpty()) {
            listaView.getSelectionModel().selectFirst();
        }


    }

    /**
     * Maneja el evento del campo de texto para buscar proveedores por nombre.
     * Filtra y actualiza la lista de proveedores en el ListView según el texto ingresado.
     * @param actionEvent El evento de acción generado por el campo de texto.
     */
    @FXML
    public void buscarProvAction(ActionEvent actionEvent) {

        ProveedorDAO dao = new ProveedorDAO();
        List<Proveedor> lista = dao.buscarListaPorNombre(buscarTextfield.getText());
        ObservableList<Proveedor> datos = FXCollections.observableArrayList(lista);
        listaView.setItems(datos);

    }

    /**
     * Maneja el evento del botón para agregar un nuevo proveedor.
     * @param actionEvent El evento de acción generado por el botón.
     */
    @FXML
    public void btnAgregarAction(ActionEvent actionEvent){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FormularioProveedor.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            Image icono = new Image(getClass().getResource("/img/Icon.png").toExternalForm());
            stage.getIcons().add(icono);
            stage.setTitle("Nuevo Producto");
            stage.setScene(new Scene(root));

            // --- Configurar como ventana modal ---
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(listaView.getScene().getWindow());

            stage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Maneja el evento del botón para editar un proveedor.
     * @param actionEvent El evento de acción generado por el botón.
     */
    @FXML
    public void btnEditarAction(ActionEvent actionEvent){
    }

    /**
     * Maneja el evento del botón para eliminar un proveedor.
     * @param actionEvent El evento de acción generado por el botón.
     */
    @FXML
    public void btnElimnarAction(ActionEvent actionEvent){

    }

    /**
     * Carga la lista de proveedores desde la base de datos y la muestra en el ListView.
     * Aplica una celda personalizada para la visualización de los proveedores.
     */
    private void cargarProveedores() {

        try {
            List<Proveedor> listaProveedores = new ProveedorDAO().listaProveedores();

            ObservableList<Proveedor> data = FXCollections.observableArrayList(listaProveedores);

            listaView.setItems(data);
            listaView.setCellFactory(new ProveedorCellFactory());

        } catch (Exception e) {
            System.err.println("Error al cargar proveedores: " + e.getMessage());
        }
    }

    /**
     * Muestra los detalles del proveedor seleccionado en los campos de la interfaz
     * y carga los productos asociados en la tabla de productos.
     * @param proveedorSeleccionado El proveedor seleccionado en el ListView.
     */
    private void mostrarDetalles(Proveedor proveedorSeleccionado) {

        nombreProvLabel.setText(proveedorSeleccionado.getNombre());
        nombreContLabel.setText(proveedorSeleccionado.getContacto());
        telefonoLabel.setText(proveedorSeleccionado.getTelefono());
        correoLabel.setText(proveedorSeleccionado.getEmail());

        ProductoDAO dao = new ProductoDAO();
        List<Producto> lista = dao.buscarPorProveedor(proveedorSeleccionado.getId());
        ObservableList<Producto> datos = FXCollections.observableArrayList(lista);
        tablaProd.setItems(datos);

    }

    /**
     * Configura las columnas de la tabla de productos con celdas personalizadas.
     * Define el formato de las columnas para nombre, descripción, precio (con formato de moneda)
     * y cantidad, asegurando que el texto se ajuste al ancho de las columnas.
     */
    private void inicializarTablaProductos() {
    
        prodName.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        prodDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        prodPrecio.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
        prodCantidad.setCellValueFactory(new PropertyValueFactory<>("stockActual"));

        //CellFactory para redimensionar celda de nombre
        prodName.setCellFactory(tc -> new TableCell<Producto, String>() {
            private final Text text = new Text();

            {
                // Vincula el ancho del Text al ancho de la columna del Nombre
                text.wrappingWidthProperty().bind(prodName.widthProperty().subtract(5));
                text.getStyleClass().add("wrapped-text-name");
                setGraphic(text);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                text.setText(empty || item == null ? null : item);
            }
        });

        //CellFactory para redimensionar celda de descripcion
        prodDescripcion.setCellFactory(tc -> new TableCell<Producto, String>() {

            private final Text text = new Text(); // Usamos un nodo Text

            {
                // Propiedades del nodo Text
                text.wrappingWidthProperty().bind(prodDescripcion.widthProperty().subtract(5)); // Ajustar ancho al ancho de la columna - padding
                text.getStyleClass().add("wrapped-text-description"); // Para posibles estilos CSS

                // Si quieres que la celda crezca al tamaño del texto, debes usar setGraphic.
                setGraphic(text);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY); // Asegurar que solo se muestre el nodo Text
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    text.setText(null);
                } else {
                    text.setText(item);
                }
            }
        });

        // Formato de Moneda
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

        // Aplicación del CellFactory para el formato de moneda
        prodPrecio.setCellFactory(column -> new TableCell<Producto, Double>() {
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
        tablaProd.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }
}

