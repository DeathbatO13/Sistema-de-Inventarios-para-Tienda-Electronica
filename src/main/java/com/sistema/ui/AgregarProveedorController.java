package com.sistema.ui;

import com.sistema.dao.ProveedorDAO;
import com.sistema.modelo.Proveedor;
import com.sistema.servicios.SistemaAutenticacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador para la vista de agregar o editar proveedores.
 * Gestiona el formulario para ingresar o modificar datos de proveedores, valida los campos
 * y realiza las operaciones de guardado o edición en la base de datos.
 */
public class AgregarProveedorController {

    @FXML
    private Label errorLabel, titleLabel;

    @FXML
    private TextField nombreTF, contactoTF, telefonoTF, correoTF;

    private Proveedor proveedorActual;

    SistemaAutenticacion auth = new SistemaAutenticacion();

    /**
     * Inicializa la vista del formulario con un título personalizado.
     * Establece el texto del título en la interfaz de usuario.
     * @param titulo El título a mostrar en la vista (por ejemplo, "Agregar" o "Editar").
     */
    @FXML
    public void initialize(String titulo){
        titleLabel.setText(titulo + " Proveedor");
    }

    /**
     * Inicializa la vista del formulario con un título y un proveedor existente para edición.
     * Establece el texto del título y carga los datos del proveedor en los campos del formulario.
     * @param titulo El título a mostrar en la vista (por ejemplo, "Editar").
     * @param proveedor El proveedor cuyos datos se cargarán para edición.
     */
    @FXML
    public void initialize(String titulo, Proveedor proveedor){
        titleLabel.setText(titulo + " Proveedor");
        nombreTF.setText(proveedor.getNombre());
        contactoTF.setText(proveedor.getContacto());
        telefonoTF.setText(proveedor.getTelefono());
        correoTF.setText(proveedor.getEmail());
        setProveedorParaEdicion(proveedor);
    }

    /**
     * Maneja el evento del botón para guardar o actualizar un proveedor.
     * Valida los campos del formulario, incluyendo el formato del correo y teléfono,
     * y realiza la operación de agregar o editar un proveedor en la base de datos.
     * Muestra una alerta con el resultado de la operación.
     * @param actionEvent El evento de acción generado por el botón de guardar.
     */
    public void btnGuardarAction(ActionEvent actionEvent) {
        //Verificaciones de campos
        if(nombreTF.getText().isEmpty() || contactoTF.getText().isEmpty() || telefonoTF.getText().isEmpty()
                || correoTF.getText().isEmpty()){

            errorLabel.setText("Todos los campos son obligatorios");
            errorLabel.setVisible(true);
            return;

        }else if(!auth.correoCorrecto(correoTF.getText().trim())) {

            errorLabel.setText("Formato de correo no es correcto");
            errorLabel.setVisible(true);
            return;

        }else if(!auth.telefonoCorrecto(telefonoTF.getText())){

            errorLabel.setText("Numero de telefono no valido");
            errorLabel.setVisible(true);
            return;
        }

        boolean operacionExitosa;
        String mensajeExito;
        String mensajeError;

        if (proveedorActual == null) {
            proveedorActual = new Proveedor();
        }

        proveedorActual.setNombre(nombreTF.getText());
        proveedorActual.setContacto(contactoTF.getText());
        proveedorActual.setTelefono(telefonoTF.getText());
        proveedorActual.setEmail(correoTF.getText());

        // Si el ID es mayor que 0, es un registro existente (Modo Edición)
        if (proveedorActual.getId() > 0) {
            operacionExitosa = new ProveedorDAO().editarProveedor(proveedorActual);
            mensajeExito = "Proveedor actualizado correctamente!";
            mensajeError = "Ha ocurrido un error al actualizar el proveedor.";
        }
        // Si el ID es 0, es un registro nuevo (Modo Agregar)
        else {
            operacionExitosa = new ProveedorDAO().agregarProveedor(proveedorActual);
            mensajeExito = "Proveedor agregado correctamente!";
            mensajeError = "Ha ocurrido un error al agregar el proveedor.";
        }

        // 4. Mostrar el resultado
        if (operacionExitosa) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText(mensajeExito);
            alert.initOwner(nombreTF.getScene().getWindow());
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(mensajeError);
            alert.initOwner(nombreTF.getScene().getWindow());
            alert.showAndWait();
        }

    }

    /**
     * Maneja el evento del botón para cancelar la operación.
     * Cierra la ventana del formulario sin guardar cambios.
     * @param actionEvent El evento de acción generado por el botón de cancelar.
     */
    public void btnCancelarAction(ActionEvent actionEvent) {
        Stage stage = (Stage) nombreTF.getScene().getWindow();
        stage.close();
    }

    /**
     * Configura el formulario para la edición de un proveedor existente.
     * Establece el proveedor actual y carga sus datos en los campos del formulario.
     * @param p El proveedor cuyos datos se cargarán para edición.
     */
    public void setProveedorParaEdicion(Proveedor p) {

        this.proveedorActual = p;
        nombreTF.setText(p.getNombre());
        contactoTF.setText(p.getContacto());
        telefonoTF.setText(p.getTelefono());
        correoTF.setText(p.getEmail());
    }

}
