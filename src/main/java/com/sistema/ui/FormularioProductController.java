package com.sistema.ui;

import com.sistema.dao.ProductoDAO;
import com.sistema.dao.ProveedorDAO;
import com.sistema.modelo.Producto;
import com.sistema.modelo.Proveedor;
import com.sistema.servicios.GestorInventario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para el formulario de registro de nuevos productos.
 * Permite al usuario ingresar los datos de un producto (código, nombre, descripción,
 * precios, stock y proveedor) y guardarlo en la base de datos mediante {@link ProductoDAO}.
 *
 * <p>Funcionalidades principales:
 * <ul>
 *   <li>Validación de campos obligatorios y conversión de tipos (String a int/double).</li>
 *   <li>Carga dinámica de proveedores en un {@link ComboBox} usando {@link ProveedorDAO}.</li>
 *   <li>Registro del producto y, si tiene stock inicial, registro automático de movimiento
 *       de entrada mediante {@link GestorInventario}.</li>
 *   <li>Retroalimentación visual con {@link Label} de error/exito y alertas modales.</li>
 *   <li>Limpieza del formulario tras guardado exitoso.</li>
 * </ul>
 * </p>
 *
 * <p>El método {@link #isProductoGuardado()} permite a la ventana padre saber si se debe
 * recargar la lista de productos. Se inicializa con información sobre cómo agregar proveedores.</p>
 */
public class FormularioProductController {

    @FXML
    private TextField codigoNuevo, nombreNuevo, descripcionNuevo,
            preCompraNuevo, preVentaNuevo,
            stockActNuevo, stockMinNuevo;

    @FXML
    private ComboBox<String> comboProveedor;

    @FXML
    private Label errorLabel, infoLabel;

    /**
     * Inicializa los componentes de la vista del formulario de producto.
     * Configura el mensaje informativo sobre proveedores y carga la lista de proveedores en el ComboBox.
     */
    @FXML
    private void initialize(){
        infoLabel.setText("Si no encuantras el proveedor, tienes que \n"
                + "agregarlo en el panel correspondiente.");
        infoLabel.setVisible(true);

        cargarProveedoresAccion();

    }

    /**
     * Maneja el evento del botón "Guardar" para registrar un nuevo producto.
     * @param actionEvent El evento de acción generado por el botón.
     */
    @FXML
    public void btnGuardarEvent(ActionEvent actionEvent) {
        guardarProducto();
    }

    /**
     * Maneja el evento del botón "Cancelar" para cerrar la ventana del formulario.
     * @param actionEvent El evento de acción generado por el botón.
     */
    @FXML
    public void btnCancelarEvent(ActionEvent actionEvent){
        Stage stage = (Stage) nombreNuevo.getScene().getWindow();
        stage.close();
    }

    /**
     * Carga la lista de proveedores desde la base de datos y los muestra en el ComboBox.
     * Convierte los objetos Proveedor en una lista de nombres para la visualización.
     */
    @FXML
    public void cargarProveedoresAccion(){
        ProveedorDAO proveedor = new ProveedorDAO();
        List<Proveedor> listaPro = proveedor.listaProveedores();
        List<String> listaProveedores = new ArrayList<>();

        for(Proveedor pro: listaPro){
            String nombrePro = pro.getNombre();
            listaProveedores.add(nombrePro);
        }
        ObservableList<String> datos = FXCollections.observableArrayList(listaProveedores);
        comboProveedor.setItems(datos);
    }

    boolean productoGuardado = false;
    Producto aGuardar = new Producto();

    /**
     * Valida los datos ingresados y guarda un nuevo producto en la base de datos.
     * Realiza validaciones de campos obligatorios, crea el objeto Producto y lo registra.
     * Muestra mensajes de éxito o error según el resultado de la operación.
     */
    public void guardarProducto() {
        // Validaciones básicas
        if (nombreNuevo.getText().isEmpty() || codigoNuevo.getText().isEmpty() || descripcionNuevo.getText().isEmpty()
            || preCompraNuevo.getText().isEmpty() || preVentaNuevo.getText().isEmpty() || stockActNuevo.getText().isEmpty()
            || stockMinNuevo.getText().isEmpty()) {
            errorLabel.setText("Faltan campos obligatorios.");
            errorLabel.setVisible(true);
            errorLabel.setTextFill(Color.rgb(232, 11, 11));
            return;
        }

        String proveedor = comboProveedor.getValue();
        Proveedor temp = new ProveedorDAO().buscarPorNombre(proveedor);


        aGuardar.setCodigoSku(codigoNuevo.getText());
        aGuardar.setNombre(nombreNuevo.getText());
        aGuardar.setDescripcion(descripcionNuevo.getText());
        aGuardar.setPrecioCompra(Double.parseDouble(preCompraNuevo.getText()));
        aGuardar.setPrecioVenta(Double.parseDouble(preVentaNuevo.getText()));
        aGuardar.setStockActual(Integer.parseInt(stockActNuevo.getText()));
        aGuardar.setStockMinimo(Integer.parseInt(stockMinNuevo.getText()));
        aGuardar.setIdProveedor(temp.getId());

        if(new ProductoDAO().agregarNuevoProducto(aGuardar)){

            errorLabel.setText("Producto Guardado !");
            productoGuardado = true;
            errorLabel.setVisible(true);
            errorLabel.setTextFill(Color.rgb(88, 130, 232));
            codigoNuevo.setText("");  nombreNuevo.setText("");
            descripcionNuevo.setText("");  preCompraNuevo.setText("");
            preVentaNuevo.setText("");  stockMinNuevo.setText("");
            stockActNuevo.setText("");  comboProveedor.setValue("");
        }

        if(new GestorInventario().registroEntrada(aGuardar.getId(), aGuardar.getStockActual(),
                "ENTRADA")){

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Movimiento Registrado");
            alert.initOwner(comboProveedor.getScene().getWindow());
            alert.showAndWait();

        }else{

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error al registrar el movimiento");
            alert.initOwner(comboProveedor.getScene().getWindow());
            alert.showAndWait();
        }

    }


    public boolean isProductoGuardado(){
        return productoGuardado;
    }


}
