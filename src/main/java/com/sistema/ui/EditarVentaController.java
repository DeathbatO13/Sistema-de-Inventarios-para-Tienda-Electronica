package com.sistema.ui;

import com.sistema.dao.UsuarioDAO;
import com.sistema.util.VentaRow;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controlador para la ventana de edición de ventas individuales.
 * Permite modificar cantidad, precio unitario y vendedor de una venta existente
 * en una interfaz modal, con validación de entrada y actualización en memoria.
 *
 * <p>Funcionalidades principales:
 * <ul>
 *   <li>Carga dinámica de usuarios (vendedores) desde {@link UsuarioDAO#listarUsuarios()}.</li>
 *   <li>Configuración del {@link Spinner} con rango de 1 a 999 unidades.</li>
 *   <li>Validación de formato numérico para el precio y selección obligatoria de vendedor.</li>
 *   <li>Actualización directa del objeto {@link VentaRow} original (pasado por referencia).</li>
 *   <li>Recálculo automático del subtotal (precio × cantidad).</li>
 *   <li>Cierre seguro de ventana modal con confirmación visual.</li>
 * </ul>
 * </p>
 *
 * <p>Nota: Los cambios se aplican en memoria. La persistencia en base de datos
 * debe manejarse en el controlador padre (ej. {@link VentasController}).</p>
 */
public class EditarVentaController {

    @FXML
    private Label productoLabel;

    @FXML
    private Spinner<Integer> cantidad;

    @FXML
    private TextField precioTF;

    @FXML
    private ComboBox<String> vendedor;

    private VentaRow ventaOriginal;


    /**
     * Inicializa los componentes de la interfaz al cargar el FXML.
     * Configura el rango del spinner de cantidad y carga la lista de vendedores
     * desde la base de datos en el ComboBox.
     */
    @FXML
    public void initialize(){

        cantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1));

        List<String> lista = new UsuarioDAO().listarUsuarios();

        vendedor.setItems(FXCollections.observableArrayList(lista));

    }

    /**
     * Maneja la acción del botón "Guardar" para aplicar los cambios a la venta.
     * Valida entrada, actualiza el objeto {@link VentaRow} y cierra la ventana.
     *
     * @param actionEvent Evento disparado por el botón de guardar.
     */
    public void btnGuardarAction(ActionEvent actionEvent) {
        try {
            int nuevaCantidad = cantidad.getValue();
            double nuevoPrecio = Double.parseDouble(precioTF.getText());
            String nuevoVendedor = vendedor.getValue();

            // Validación básica
            if (nuevoVendedor == null || nuevoVendedor.isEmpty()) {
                mostrarAlerta("Seleccione un vendedor antes de guardar.");
                return;
            }

            // Actualizar la venta original
            ventaOriginal.setCantidad(nuevaCantidad);
            ventaOriginal.setPrecioUnitario(nuevoPrecio);
            ventaOriginal.setVendedor(nuevoVendedor);

            // Calcular nuevo total (si tienes un campo precioTotal)
            ventaOriginal.setPrecioTotal(nuevaCantidad * nuevoPrecio);

            mostrarAlerta("Venta actualizada: " + ventaOriginal.getProductoVendido());

            // Cerrar la ventana
            Stage stage = (Stage) productoLabel.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error: el precio debe ser un número válido.");
        }
    }

    /**
     * Maneja la acción del botón "Cancelar" para cerrar la ventana sin guardar cambios.
     *
     * @param actionEvent Evento disparado por el botón de cancelar.
     */
    public void btnCancelarAction(ActionEvent actionEvent) {

        Stage stage = (Stage) productoLabel.getScene().getWindow();
        stage.close();
    }

    /**
     * Configura los campos del formulario con los datos de la venta a editar.
     * Se llama desde el controlador padre para precargar la información.
     *
     * @param v Objeto {@link VentaRow} con los datos originales de la venta.
     */
    public void setVentaEditor(VentaRow v) {

        this.ventaOriginal = v;

        productoLabel.setText(v.getProductoVendido());
        cantidad.getValueFactory().setValue(v.getCantidad());
        precioTF.setText(String.valueOf(v.getPrecioUnitario()));
        vendedor.setValue(v.getVendedor());
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
        alert.initOwner(precioTF.getScene().getWindow());
        alert.showAndWait();

    }
}
