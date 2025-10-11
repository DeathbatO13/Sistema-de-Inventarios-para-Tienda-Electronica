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

import javax.swing.*;
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

    /**
     * Control de evento de registro para el boton con el mismo nombre
     * @param actionEvent evento de boton
     */
    public void btnRegistrarseAction(ActionEvent actionEvent) {
        String nombre = nombreTextField.getText();
        String contrasena = passwordFieldRegistro.getText();
        String correo = correoTextField.getText();
        //Verificacion de que todos los campos esten diligenciados
        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            errorRegistroLabel.setVisible(true);
            errorRegistroLabel.setText("Todos los campos son obligatorios");
            return;
        }
        //Verificacion de formato de correo valido
        if(!correoCorrecto(correo)){
            errorRegistroLabel.setVisible(true);
            errorRegistroLabel.setText("Error en el formato de correo \n"
                    + "Debe ser como: 'user@dominio.com'");
            correoTextField.setText("");
            passwordFieldRegistro.setText("");
            return;
        }
        //Verificacion de contraseña segura
        if(!contrasenaSegura(contrasena)){
            errorRegistroLabel.setVisible(true);
            errorRegistroLabel.setText("La contraseña debe tener por lo menos \n"+
                    "una letra mayuscula, minuscula, un numero\n" +
                    "y un caracter especial");
            passwordFieldRegistro.setText("");
            return;
        }
        //Registro del Usuario
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
     * Control de evento para verificar el codigo enviado por correo
     * @param actionEvent evento del boton verificar
     */
    public void btnVerificarAction(ActionEvent actionEvent) {
        String tokenDigitado = tokenTextField.getText();
        SistemaAutenticacion auth = new SistemaAutenticacion();
        boolean estadoVerificacion = auth.verificarCuenta(tokenDigitado);

        if(estadoVerificacion){
            registroLabel.setText("Verificación exitosa! Ya puedes iniciar sesion y acceder al inventario");

        }else{
            registroLabel.setText("Codigo invalido, verifica que este correcto por favor o intentalo de nuevo");
        }
    }

    /**
     * Control de evento para volver a la ventana de inicio de sesion en caso de entrar a la de registro
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

    /**
     * Funcion implementada para verificar que el campo del correo tenga un formato valido
     *
     * @param correo correo digitado en el registro
     * @return true si el correo tiene un formato valido, false si no
     */
    public boolean correoCorrecto(String correo){
        String patron = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@"+
                "(?:[a-zA-Z0-9-]+\\.)+[a-z]{2,7}$";

        return correo.matches(patron);
    }

    /**
     * Funcion para verificar que la contraseña tenga minumo 8 carcateres, una mayuscula, minuscula,
     * numero y caracter especial
     * @param contrasena contraseña a verificar
     * @return true si es segura, false si falta algun parametro
     */
    public boolean contrasenaSegura(String contrasena){
        boolean mayus = false;
        boolean minus = false;
        boolean numero = false;
        boolean especial = false;

        for(char c : contrasena.toCharArray()){
            if(Character.isUpperCase(c)) mayus = true;
            if(Character.isLowerCase(c)) minus = true;
            if(Character.isDigit(c)) numero = true;
            if(!Character.isLetterOrDigit(c)) especial = true;
        }
        return (mayus && minus && numero && especial && (contrasena.length() >= 8));
    }

}
