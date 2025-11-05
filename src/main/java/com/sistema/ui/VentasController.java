package com.sistema.ui;

import com.sistema.dao.MovimientoDAO;
import com.sistema.dao.ProductoDAO;
import com.sistema.dao.VentasDAO;
import com.sistema.modelo.*;
import com.sistema.servicios.GestorInventario;
import com.sistema.util.UsuarioSesion;
import com.sistema.util.VentaRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Controlador para la gestión de ventas en la interfaz gráfica.
 * Permite registrar ventas, agregar productos al carrito, buscar ventas por nombre o fecha,
 * y visualizar el historial de ventas con formato de moneda colombiana.
 *
 * <p>Funcionalidades principales:
 * <ul>
 *   <li>Registro de ventas con múltiples productos (hasta 10) mediante transacción atómica.</li>
 *   <li>Actualización automática de stock y registro de movimientos de salida con {@link GestorInventario}.</li>
 *   <li>Búsqueda de ventas por nombre de producto o fecha usando {@link DatePicker}.</li>
 *   <li>Selección dinámica de productos con actualización de precio unitario y límite de cantidad (stock - stock mínimo).</li>
 *   <li>Formato de moneda colombiana en precios y totales.</li>
 *   <li>Columna de acción "Editar" en la tabla de ventas (funcionalidad pendiente).</li>
 *   <li>Acumulación de subtotales, contador de productos y limpieza de formulario.</li>
 * </ul>
 * </p>
 *
 * <p>Utiliza {@link VentasDAO} para persistencia, {@link ProductoDAO} para productos disponibles,
 * y {@link UsuarioSesion} para obtener el ID del usuario actual. Implementa validaciones de stock,
 * formato de precios y límites de venta.</p>
 */
public class VentasController {

    @FXML
    private TextField buscarVentasTF, precioVenta;

    @FXML
    private DatePicker buscarPorFecha;

    @FXML
    private TableView<VentaRow> tablaVentas;

    @FXML
    private TableColumn<VentaRow, String> productoColumn;

    @FXML
    private TableColumn<VentaRow, Integer> cantidadProd;

    @FXML
    private TableColumn<VentaRow, LocalDateTime> fechaV;

    @FXML
    private TableColumn<VentaRow, Double> precioV;

    @FXML
    private TableColumn<VentaRow, Void> accionC;

    @FXML
    private Spinner<Integer> cantidadPrVenta;

    @FXML
    private ComboBox<String> productosVenta;

    @FXML
    private Label contadorProductos, totalVenta;


    VentasDAO dao = new VentasDAO();
    List<VentaRow> lista = dao.listaVentas();
    private double totalAcumuladoVenta = 0;
    private final List<DetalleVenta> detallesVenta = new ArrayList<>();
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    /**
     * Inicializa el controlador después de cargar el archivo FXML.
     * Configura las fábricas de valores de las celdas de la tabla, aplica formato de moneda
     * a la columna de precio y carga los datos iniciales.
     */
    @FXML
    public void initialize(){

        productoColumn.setCellValueFactory(new PropertyValueFactory<>("productoVendido"));
        cantidadProd.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        precioV.setCellValueFactory(new PropertyValueFactory<>("precioTotal"));
        fechaV.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        precioV.setCellFactory(column -> new TableCell<VentaRow, Double>() { // Changed to VentaRow
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

        //Inicializa spinner con rango provicional
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1, 1);
        cantidadPrVenta.setValueFactory(valueFactory);

        productosVenta.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldProducto, newProducto) ->
                        actualizarSpinner(new ProductoDAO().buscarUnicoPorNombre(newProducto))
        );

        productosVenta.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldProducto, newProducto) ->
                        actualizarPrecioUnitario(new ProductoDAO().buscarUnicoPorNombre(newProducto))
        );

        cargarListaVentas();
        agregarColumna();
        cargarProductosAvender();
        contadorProductos.setText(String.valueOf(0));
        String formatoTotalV = formatoMoneda.format(0);
        totalVenta.setText(formatoTotalV);

    }

    /**
     * Maneja la acción de buscar ventas basada en el texto ingresado.
     *
     * @param actionEvent el evento de acción desencadenado por el botón o campo
     */
    public void buscarVentaAction(ActionEvent actionEvent){

        String nombreProd = buscarVentasTF.getText().toLowerCase().trim();

        List<VentaRow> filtrada = lista.stream()
                .filter(v -> v.getProductoVendido().toLowerCase().contains(nombreProd))
                .collect(Collectors.toList());

        ObservableList<VentaRow> datos = FXCollections.observableArrayList(filtrada);
        tablaVentas.setItems(datos);

    }

    /**
     * Maneja la acción de buscar ventas por fecha seleccionada.
     *
     * @param actionEvent el evento de acción desencadenado por el selector de fecha
     */
    public void buscarFechaAction(ActionEvent actionEvent) {

        LocalDate fechaSeleccionada = buscarPorFecha.getValue();

        if (fechaSeleccionada == null) {
            tablaVentas.setItems(FXCollections.observableArrayList(lista));
            return;
        }

        List<VentaRow> filtradas = lista.stream()
                .filter(v -> v.getFecha().toLocalDate().equals(fechaSeleccionada))
                .collect(Collectors.toList());

        tablaVentas.setItems(FXCollections.observableArrayList(filtradas));

    }

    /**
     * Maneja la acción de registrar una nueva venta.
     *
     * @param actionEvent el evento de acción desencadenado por el botón de registro
     */
    public void btnRegistrarVentaAction(ActionEvent actionEvent){

        if (detallesVenta.isEmpty()) {
            mostrarAlerta("Debe agregar al menos un producto antes de registrar la venta.");
            return;
        }

        int idUser = UsuarioSesion.getIdUsuarioActual();
        Venta v = new Venta(LocalDateTime.now(), totalAcumuladoVenta, idUser);

        VentasDAO ventasDAO = new VentasDAO();
        boolean ventReg = ventasDAO.registrarVenta(v, detallesVenta);

        // 5️⃣ Resultado
        if (ventReg) {

            MovimientoDAO movimientoDAO = new MovimientoDAO();

            for (DetalleVenta det : detallesVenta) {


                boolean regEx = GestorInventario.registroSalida(
                        det.getIdProducto(),
                        det.getCantidad(),
                        "Venta registrada (ID producto: " + det.getIdProducto() + ")"
                );

                if (!regEx) {
                    System.err.println("No se pudo registrar el movimiento ni actualizar stock para producto ID: " + det.getIdProducto());
                }
            }

            mostrarAlerta("Venta registrada con éxito.");

            // 🔄 Limpiar interfaz y lista
            detallesVenta.clear();
            totalAcumuladoVenta = 0.0;
            totalVenta.setText(formatoMoneda.format(totalAcumuladoVenta));
            contadorProductos.setText("0");
            precioVenta.clear();
            productosVenta.getSelectionModel().clearSelection();
            cantidadPrVenta.getValueFactory().setValue(1);

        } else {
            mostrarAlerta("Ocurrió un error al registrar la venta. Intente nuevamente.");
        }

        tablaVentas.refresh();
    }

    /**
     * Maneja la acción de agregar producto a la operación de venta.
     *
     * @param actionEvent el evento de acción desencadenado por el botón de agregar producto
     */
    public void btnAgregarProdAction(ActionEvent actionEvent){

        Producto productoSeleccionado = new ProductoDAO().buscarUnicoPorNombre(productosVenta
                .getSelectionModel().getSelectedItem());
        if (productoSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un producto.");
            return;
        }

        String raw = precioVenta.getText();
        if (raw == null || raw.trim().isEmpty()) {
            mostrarAlerta("El precio unitario no está disponible.");
            return;
        }

        // Limpieza
        String textoPrecio;
        if (raw.contains(",")) {

            textoPrecio = raw.replaceAll("[^\\d,]", "") // elimina $ u otros símbolos y puntos de miles
                    .replace(",", ".");         // coma decimal -> punto
        } else {

            textoPrecio = raw.replaceAll("[^\\d.]", "");
        }

        double precioUnitario = Double.parseDouble(textoPrecio);
        int cantidad = cantidadPrVenta.getValue();
        double subtotal = cantidad * precioUnitario;

        // Evita agregar más de 10 productos
        if (detallesVenta.size() >= 10) {
            mostrarAlerta("Solo se pueden agregar hasta 10 productos por venta.");
            return;
        }

        DetalleVenta detalle = new DetalleVenta(
                0,
                0,
                productoSeleccionado.getId(),
                cantidad,
                precioUnitario,
                subtotal
        );

        detallesVenta.add(detalle);

        totalAcumuladoVenta += subtotal;
        String formatoTotalV = formatoMoneda.format(totalAcumuladoVenta);
        totalVenta.setText(formatoTotalV);

        contadorProductos.setText(String.valueOf(detallesVenta.size()));

        productosVenta.getSelectionModel().clearSelection();

    }

    /**
     * Maneja la acción de cancelar una operación de venta.
     *
     * @param actionEvent el evento de acción desencadenado por el botón de cancelar
     */
    public void btnCancelarAction(ActionEvent actionEvent) {

        productosVenta.getSelectionModel().clearSelection();

        cantidadPrVenta.getValueFactory().setValue(1);

        totalAcumuladoVenta = 0.0;
        totalVenta.setText(String.valueOf(totalAcumuladoVenta));
        contadorProductos.setText("0");
        precioVenta.setText("");

        detallesVenta.clear();
    }



    /**
     * Agrega una columna de acciones (botón "Editar") a la tabla de ventas.
     * El botón permite realizar una acción de edición en la fila seleccionada.
     */
    private void agregarColumna() {
        accionC.setCellFactory(param -> new TableCell<VentaRow, Void>() {

            private final Button btnEditar = new Button("Editar");
            private final HBox contenedorBotones = new HBox();

            {
                // Estilos
                btnEditar.setStyle(
                        "-fx-background-color: #5882E8; -fx-text-fill: white; -fx-font-size: 13px; "
                                + "-fx-font-weight: bold; -fx-background-radius: 7;"
                );

                contenedorBotones.getChildren().add(btnEditar);
                contenedorBotones.setSpacing(5);
                contenedorBotones.setAlignment(Pos.CENTER);

                // Evento del botón
                btnEditar.setOnAction(event -> {

                    VentaRow venta = getTableView().getItems().get(getIndex());

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditarVenta.fxml"));
                        Parent root = loader.load();

                        EditarVentaController edCont = loader.getController();

                        edCont.setVentaEditor(venta);

                        Stage stage = new Stage();
                        Image icono = new Image(getClass().getResource("/img/Icon.png").toExternalForm());
                        stage.getIcons().add(icono);
                        stage.setTitle("Editar Venta");
                        stage.setScene(new Scene(root));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initOwner(tablaVentas.getScene().getWindow());
                        stage.showAndWait();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    tablaVentas.refresh();
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


    /**
     * Carga la lista de ventas desde la base de datos y la muestra en la tabla.
     * Utiliza un DAO para obtener los datos y los convierte en una lista observable.
     */
    private void cargarListaVentas(){

        ObservableList<VentaRow> datos = FXCollections.observableArrayList(lista);
        tablaVentas.setItems(datos);
    }

    /**
     * Carga la lista de productos disponibles para vender desde la base de datos.
     * Extrae los nombres de los productos y los asigna al ComboBox.
     */
    private void cargarProductosAvender(){
        ProductoDAO p = new ProductoDAO();
        List<Producto> list = p.listaProductos();
        List<String> nombres = new ArrayList<>();

        for(Producto pro : list){
            String nombre = pro.getNombre();
            nombres.add(nombre);
        }

        ObservableList<String> datos = FXCollections.observableArrayList(nombres);
        productosVenta.setItems(datos);
    }


    /**
     * Actualiza el precio unitario mostrado en el campo de texto cuando se selecciona
     * un producto en el ComboBox. Formatea el precio de venta del producto seleccionado
     * con el formato de moneda colombiana (COP).
     *
     * @param prodSelec El producto seleccionado del ComboBox, obtenido mediante búsqueda por nombre.
     *                  Puede ser {@code null} si no hay selección o no se encuentra.
     */
    private void actualizarPrecioUnitario(Producto prodSelec){
        if(prodSelec != null){

            double precio = prodSelec.getPrecioVenta();
            NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            String precioFormateado = formatoMoneda.format(precio);
            precioVenta.setText(precioFormateado);

        }
    }

    /**
     * Configura el rango permitido del {@link Spinner} de cantidad según el stock disponible
     * del producto seleccionado. El máximo permitido es {@code stockActual - stockMinimo},
     * con un mínimo de 1 para evitar valores inválidos.
     *
     * @param prodSelect El producto seleccionado del ComboBox.
     *                   Si es {@code null}, el spinner mantiene su valor por defecto.
     */
    private void actualizarSpinner(Producto prodSelect){
        if (prodSelect != null) {
            int stockActual = prodSelect.getStockActual();
            int stockMinimo = prodSelect.getStockMinimo();

            int maximoPermitido = stockActual - stockMinimo;
            if (maximoPermitido < 1) {
                maximoPermitido = 1;
            }

            SpinnerValueFactory<Integer> valueFactory =
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maximoPermitido, 1);

            cantidadPrVenta.setValueFactory(valueFactory);
        }
    }

    /**
     * Muestra una alerta informativa modal con el mensaje proporcionado.
     * Se utiliza para notificar al usuario sobre resultados de operaciones,
     * validaciones o errores no críticos.
     *
     * @param mens El mensaje a mostrar en el cuerpo de la alerta.
     */
    private void mostrarAlerta(String mens){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(mens);
        alert.initOwner(totalVenta.getScene().getWindow());
        alert.showAndWait();

    }
}
