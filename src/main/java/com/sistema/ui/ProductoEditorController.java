package com.sistema.ui;

import com.sistema.dao.ProductoDAO;
import com.sistema.dao.ProveedorDAO;
import com.sistema.modelo.Producto;
import com.sistema.modelo.Proveedor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para la ventana de edición de productos existentes.
 * Permite modificar todos los campos de un producto previamente registrado,
 * incluyendo su proveedor, y persiste los cambios en la base de datos.
 *
 * <p>Funcionalidades principales:
 * <ul>
 *   <li>Carga los datos del producto por ID usando {@link ProductoDAO#buscarPorId(int)}.</li>
 *   <li>Actualización completa del producto mediante {@link ProductoDAO#actualizarProducto(Producto)}.</li>
 *   <li>Carga dinámica de proveedores en un {@link ComboBox} usando {@link ProveedorDAO}.</li>
 *   <li>Conversión segura de campos numéricos (String a double/int).</li>
 *   <li>Retroalimentación visual con alertas de éxito o error.</li>
 *   <li>Cierre seguro de ventana al cancelar.</li>
 * </ul>
 * </p>
 *
 * <p>El método {@link #setProductoData(int)} es clave para inicializar el formulario
 * con los datos actuales del producto. Utiliza búsqueda por nombre para obtener el ID
 * en la actualización, asumiendo unicidad de nombres.</p>
 */
public class ProductoEditorController {

    @FXML
    private Label nombreLabel;

    @FXML
    private TextField codigoTF, nombreTF, descripcionTF, preCompraTF, preVentaTF, stockActualTF, stockMinTF;

    @FXML
    private ComboBox<String> proveedorCB;

    /**
     * Inicializa el controlador al cargar la ventana.
     * Carga la lista de proveedores en el ComboBox para que esté disponible al abrir la ventana.
     */
    @FXML
    public void initialize(){
        cargarProveedoresAccion();
    }


    /**
     * Maneja la acción del botón "Confirmar" para actualizar un producto.
     * Recoge los datos ingresados en los campos de la interfaz, crea un objeto Producto
     * y lo actualiza en la base de datos. Muestra una alerta de éxito o error según el resultado.
     *
     * @param actionEvent El evento de acción generado por el botón.
     */
    @FXML
    public void btnConfirmarAction(ActionEvent actionEvent){
        Producto pro = new Producto();

        pro.setId(new ProductoDAO().buscarUnicoPorNombre(nombreTF.getText()).getId());
        pro.setCodigoSku(codigoTF.getText());
        pro.setNombre(nombreTF.getText());
        pro.setDescripcion(descripcionTF.getText());
        pro.setPrecioCompra(Double.parseDouble(preCompraTF.getText()));
        pro.setPrecioVenta(Double.parseDouble(preVentaTF.getText()));
        pro.setStockActual(Integer.parseInt(stockActualTF.getText()));
        pro.setStockMinimo(Integer.parseInt(stockMinTF.getText()));
        pro.setIdProveedor(new ProveedorDAO().buscarPorNombre(proveedorCB.getValue()).getId());

        if(new ProductoDAO().actualizarProducto(pro)){

            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exito");
            alert.setHeaderText(null);
            alert.setContentText("Producto Actualizado");
            alert.initOwner(nombreLabel.getScene().getWindow());
            alert.showAndWait();

        }else{

            Alert alert =  new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("A ocurrido un error");
            alert.initOwner(nombreLabel.getScene().getWindow());
            alert.showAndWait();

        }

    }

    /**
     * Maneja la acción del botón "Cancelar" para cerrar la ventana de edición.
     * Cierra la ventana actual sin guardar cambios.
     *
     * @param actionEvent El evento de acción generado por el botón.
     */
    @FXML
    public void btnCancelarAction(ActionEvent actionEvent){
        Stage stage = (Stage) nombreLabel.getScene().getWindow();
        stage.close();
    }

    /**
     * Carga los datos de un producto en los campos de la interfaz para su edición.
     * Busca el producto por su ID en la base de datos y llena los campos de texto
     * y el ComboBox con los valores correspondientes.
     *
     * @param id El identificador del producto a cargar.
     */
    public void setProductoData(int id) {
        Producto p = new ProductoDAO().buscarPorId(id);
        if (p != null) {
            nombreLabel.setText(p.getNombre());
            codigoTF.setText(p.getCodigoSku());
            nombreTF.setText(p.getNombre());
            descripcionTF.setText(p.getDescripcion());
            preCompraTF.setText(String.valueOf(p.getPrecioCompra()));
            preVentaTF.setText(String.valueOf(p.getPrecioVenta()));
            stockActualTF.setText(String.valueOf(p.getStockActual()));
            stockMinTF.setText(String.valueOf(p.getStockMinimo()));
            proveedorCB.setValue(new ProveedorDAO().buscarPorId(p.getIdProveedor()).getNombre());
        }
    }

    /**
     * Carga la lista de proveedores en el ComboBox de la interfaz.
     * Obtiene los nombres de los proveedores desde la base de datos y los establece
     * como opciones seleccionables en el ComboBox.
     */
    public void cargarProveedoresAccion(){
        ProveedorDAO proveedor = new ProveedorDAO();
        List<Proveedor> listaPro = proveedor.listaProveedores();
        List<String> listaProveedores = new ArrayList<>();

        for(Proveedor pro: listaPro){
            String nombrePro = pro.getNombre();
            listaProveedores.add(nombrePro);
        }
        ObservableList<String> datos = FXCollections.observableArrayList(listaProveedores);
        proveedorCB.setItems(datos);
    }
}
