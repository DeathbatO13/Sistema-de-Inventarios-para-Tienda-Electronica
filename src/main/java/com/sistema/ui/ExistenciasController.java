package com.sistema.ui;

import com.sistema.modelo.Producto;
import com.sistema.servicios.GestorInventario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

/**
 * Controlador para la ventana de ajuste de existencias de un producto.
 * Permite al usuario ingresar una cantidad mediante un {@link Spinner} y
 * actualizar el inventario llamando a {@link GestorInventario#ajustarStock(int, int, String)}.
 *
 * <p>La interfaz incluye:
 * <ul>
 *   <li>Un {@link Label} que muestra el nombre del producto.</li>
 *   <li>Un {@link Spinner} para seleccionar la cantidad a agregar (1-100).</li>
 *   <li>Botones "Guardar" y "Cancelar".</li>
 * </ul>
 * </p>
 *
 * <p>Al hacer clic en "Guardar", se intenta ajustar el stock y se muestra una alerta
 * de confirmación o error. La ventana se cierra en caso de éxito o al cancelar.</p>
 */
public class ExistenciasController {

    @FXML
    private Label productoLabel;

    @FXML
    private Spinner<Integer> cantidadSpinner;

    private Producto product;

    /**
     * Inicializa el controlador después de que su raíz haya sido completamente procesada.
     * Configura el {@link Spinner} de cantidad con un rango de 1 a 100, valor inicial 1 e incremento de 1.
     */
    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1, 1);
        cantidadSpinner.setValueFactory(valueFactory);
    }

    /**
     * Establece el producto a mostrar y actualiza el {@link Label} con su nombre.
     *
     * @param producto el {@link Producto} a asignar
     */
    public void setProducto(Producto producto) {
        this.product = producto;
        productoLabel.setText(producto.getNombre());
    }

    /**
     * Acción del botón Cancelar. Cierra la ventana actual.
     *
     * @param actionEvent el evento que disparó la acción
     */
    @FXML
    public void btnCancelarAction(ActionEvent actionEvent) {
        Stage stage = (Stage) productoLabel.getScene().getWindow();
        stage.close();
    }

    /**
     * Acción del botón Guardar. Obtiene la cantidad del {@link Spinner}, ajusta el stock del producto
     * mediante {@link GestorInventario#ajustarStock(int, int, String)} y muestra una alerta de éxito o error.
     * Cierra la ventana si la operación es exitosa.
     *
     * @param actionEvent el evento que disparó la acción
     */
    @FXML
    public void btnGuardarAction(ActionEvent actionEvent) {

        int cantidad = cantidadSpinner.getValue();

        if (new GestorInventario().ajustarStock(product.getId(), cantidad, "AJUSTE")) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cambio Confirmado");
            alert.setHeaderText(null);
            alert.setContentText("Existencias agregadas exitosamente");
            alert.initOwner(productoLabel.getScene().getWindow());
            alert.showAndWait();
            ((Stage) productoLabel.getScene().getWindow()).close();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Ocurrió un error al agregar existencias");
            alert.initOwner(productoLabel.getScene().getWindow());
            alert.showAndWait();
        }
    }
}

