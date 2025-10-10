package com.sistema.ui;

import com.sistema.servicios.SistemaAutenticacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class RegistroController {

    @FXML
    private Label errorRegistroLabel, registroLabel;

    @FXML
    private TextField nombreTextField, correoTextField, tokenTextField;

    @FXML
    private PasswordField passwordFieldRegistro;

    @FXML
    private Button btnRegistrarse, btnVerificar, btnVolverInicio;

    public void btnVerificarAction(ActionEvent actionEvent) {
    }

    public void btnRegistrarseAction(ActionEvent actionEvent) {
        String nombre = nombreTextField.getText();
        String contrasena = passwordFieldRegistro.getText();
        String correo = correoTextField.getText();
        SistemaAutenticacion auth = new SistemaAutenticacion();
        boolean estadoRegistro = auth.registrarUsuario(nombre, correo, contrasena);

        if(estadoRegistro){
            errorRegistroLabel.setVisible(false);
            registroLabel.setVisible(true);
            tokenTextField.setVisible(true);
            btnVerificar.setVisible(true);
            System.out.println("Registro exitoso y correo enviado");
        }else{
            errorRegistroLabel.setVisible(true);
            System.out.println("Correo que ya existe o algun otro error");
        }
    }

    /**
     * Funcion para volver a la ventana de inicio de sesion en caso de entrar a la de registro
     * por error
     * @param actionEvent evento del voton de volver.
     */
    public void btnVolverInicioAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginVista.fxml"));
            Parent root = loader.load();
            // Crear una nueva ventana (Stage)
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
}
