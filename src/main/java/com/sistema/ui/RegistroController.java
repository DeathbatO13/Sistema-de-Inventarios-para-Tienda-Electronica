package com.sistema.ui;

import com.sistema.servicios.ServicioEmail;
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
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.io.IOException;

/**
 * Controlador para la ventana de registro de nuevos usuarios.
 * Gestiona el flujo de registro, validación de datos, envío de código de verificación
 * y verificación de cuenta mediante correo electrónico.
 *
 * <p>Funcionalidades principales:
 * <ul>
 *   <li>Validación de campos obligatorios, formato de correo y seguridad de contraseña.</li>
 *   <li>Registro del usuario mediante {@link SistemaAutenticacion#registrarUsuario}.</li>
 *   <li>Envío automático de código de verificación por correo vía {@link ServicioEmail}.</li>
 *   <li>Verificación del token ingresado con {@link SistemaAutenticacion#verificarCuenta}.</li>
 *   <li>Retroalimentación visual progresiva: campos ocultos hasta que se necesiten.</li>
 *   <li>Navegación de regreso al login con cierre seguro de ventana actual.</li>
 * </ul>
 * </p>
 *
 * <p>Utiliza {@link BCrypt} para encriptar contraseñas y políticas de validación
 * definidas en {@link SistemaAutenticacion}. El flujo es secuencial y seguro,
 * habilitando campos solo cuando se cumplen las condiciones previas.</p>
 */
public class RegistroController {

    @FXML
    private Label errorRegistroLabel, registroLabel;

    @FXML
    private TextField nombreTextField, correoTextField, tokenTextField;

    @FXML
    private PasswordField passwordFieldRegistro;

    @FXML
    private Button btnVerificar;

    /**
     * Inicia el controlador, manejando los label de estado y estableciendolos
     * invisibles hasta cuando se requiera
     */
    @FXML
    public void initialize(){
        errorRegistroLabel.setVisible(false);
        registroLabel.setVisible(false);
    }


    SistemaAutenticacion auth = new SistemaAutenticacion();

    /**
     * Maneja el evento del botón de registro.
     * Valida que los campos de nombre, correo y contraseña estén completos, que el correo tenga un formato válido
     * y que la contraseña cumpla con los requisitos de seguridad. Registra al usuario y muestra los campos
     * para verificar el código enviado por correo.
     * @param actionEvent El evento de acción generado por el botón de registrarse.
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
        if(!auth.correoCorrecto(correo)){
            errorRegistroLabel.setVisible(true);
            errorRegistroLabel.setText("Error en el formato de correo \n"
                    + "Debe ser como: 'user@dominio.com'");
            correoTextField.setText("");
            passwordFieldRegistro.setText("");
            return;
        }
        //Verificacion de contraseña segura
        if(!auth.contrasenaSegura(contrasena)){
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
     * Maneja el evento del botón para verificar el código de verificación.
     * Comprueba si el código ingresado es válido y actualiza el estado de la verificación del usuario.
     * Muestra un mensaje de éxito o error según el resultado.
     * @param actionEvent El evento de acción generado por el botón de verificar.
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
     * Maneja el evento del botón para volver a la ventana de inicio de sesión.
     * Cierra la ventana de registro y abre la ventana de inicio de sesión.
     * @param actionEvent El evento de acción generado por el botón de volver.
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
