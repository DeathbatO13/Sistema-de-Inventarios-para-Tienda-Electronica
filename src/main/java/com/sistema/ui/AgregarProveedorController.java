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


public class AgregarProveedorController {

    @FXML
    private Label errorLabel, titleLabel;

    @FXML
    private TextField nombreTF, contactoTF, telefonoTF, correoTF;

    SistemaAutenticacion auth = new SistemaAutenticacion();

    @FXML
    public void initialize(String titulo){
        titleLabel.setText(titulo + " Proveedor");
    }

    @FXML
    public void initialize(String titulo, Proveedor proveedor){
        titleLabel.setText(titulo + " Proveedor");
        nombreTF.setText(proveedor.getNombre());
        contactoTF.setText(proveedor.getContacto());
        telefonoTF.setText(proveedor.getTelefono());
        correoTF.setText(proveedor.getEmail());
    }


    public void btnGuardarAction(ActionEvent actionEvent) {
        //Verificaciones de campos
        if(nombreTF.getText().isEmpty() || contactoTF.getText().isEmpty() || telefonoTF.getText().isEmpty()
                || correoTF.getText().isEmpty()){

            errorLabel.setText("Todos los campos son obligatorios");
            errorLabel.setVisible(true);

        }else if(!auth.correoCorrecto(correoTF.getText().trim())) {

            errorLabel.setText("Formato de correo no es correcto");
            errorLabel.setVisible(true);

        }else if(!auth.telefonoCorrecto(telefonoTF.getText())){

            errorLabel.setText("Numero de telefono no valido");
            errorLabel.setVisible(true);

        }else{

            Proveedor p = new Proveedor();

            p.setNombre(nombreTF.getText());
            p.setContacto(contactoTF.getText());
            p.setTelefono(telefonoTF.getText());
            p.setEmail(correoTF.getText());

            if(new ProveedorDAO().agregarProveedor(p)){

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Exito");
                alert.setHeaderText(null);
                alert.setContentText("Provedor agregado!");
                alert.initOwner(nombreTF.getScene().getWindow());
                alert.showAndWait();

            }else{

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("A ocurrido un error al agregar el proveedor");
                alert.initOwner(nombreTF.getScene().getWindow());
                alert.showAndWait();

            }

        }

    }

    public void btnCancelarAction(ActionEvent actionEvent) {
        Stage stage = (Stage) nombreTF.getScene().getWindow();
        stage.close();
    }
}
