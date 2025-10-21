package com.sistema.ui;

import com.sistema.servicios.SistemaAutenticacion;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;


/**
 * Controlador para la vista de inicio de sesión.
 * Gestiona la autenticación de usuarios, el acceso al formulario de registro, la recuperación de contraseñas
 * y el bloqueo temporal de intentos fallidos de inicio de sesión.
 */
public class LoginController {
    @FXML
    private Button btnIniciarSesion, btnRegistrarse;

    @FXML
    private TextField campoCorreo;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Hyperlink linkRecContra;

    private int contadorInicio = 0;

    /**
     * Maneja el evento del botón de inicio de sesión.
     * Verifica las credenciales del usuario, muestra el panel principal si son correctas,
     * o incrementa el contador de intentos fallidos y bloquea los campos si se alcanza el límite.
     * @param actionEvent El evento de acción generado por el botón de iniciar sesión.
     */
    @FXML
    public void iniciarSesionAction(ActionEvent actionEvent) {
        String correo = campoCorreo.getText();
        String contrasena = passwordField.getText();
        SistemaAutenticacion auth = new SistemaAutenticacion();
        boolean estadoInicioSesion = auth.iniciarSesion(correo, contrasena);

        if (estadoInicioSesion) {
            System.out.println("Inicio de sesión correcto");
            errorLabel.setVisible(false);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PanelPrincipalVista.fxml"));
                Parent root = loader.load();
                // Crear una nueva ventana (Stage)
                Stage stage = new Stage();
                Image icono = new Image(getClass().getResource("/img/Icon.png").toExternalForm());
                stage.getIcons().add(icono);
                stage.setTitle("Panel Principal");
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

        } else {
            contadorInicio++;
            errorLabel.setVisible(true);
            linkRecContra.setVisible(true);
            System.out.println("Credenciales incorrectas");

            if (contadorInicio < 5) {
                errorLabel.setText("Credenciales incorrectas. Intento " + contadorInicio + " de 5");
            } else {
                bloqueoIntentos();
            }
        }
    }

    /**
     * Maneja el evento del botón de registrarse.
     * Abre la ventana de registro de usuario y cierra la ventana actual de inicio de sesión.
     * @param actionEvent El evento de acción generado por el botón de registrarse.
     */
    @FXML
    public void registrarseAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegistroVista.fxml"));
            Parent root = loader.load();
            // Crear una nueva ventana (Stage)
            Stage stage = new Stage();
            Image icono = new Image(getClass().getResource("/img/Icon.png").toExternalForm());
            stage.getIcons().add(icono);
            stage.setTitle("Registro de Usuario");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

            // Cerrar la ventana actual (login)
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
        }
        catch (IOException e) {
            System.err.println("Error al lanzar pantalla de registro" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Bloquea los campos de entrada y el botón de inicio de sesión tras alcanzar el límite de intentos fallidos.
     * Inicia un temporizador de 60 segundos antes de permitir nuevos intentos.
     */
    private void bloqueoIntentos() {
        campoCorreo.setEditable(false);
        passwordField.setEditable(false);
        btnIniciarSesion.setDisable(true);

        final int[] segundosRestantes = {60};
        errorLabel.setText("Límite alcanzado. Espera 60 segundos para reintentar.");

        Timeline temporizador = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    segundosRestantes[0]--;
                    errorLabel.setText("Bloqueado. Reintenta en " + segundosRestantes[0] + " segundos.");
                    if (segundosRestantes[0] <= 0) {
                        campoCorreo.setEditable(true);
                        passwordField.setEditable(true);
                        btnIniciarSesion.setDisable(false);
                        contadorInicio = 0;
                        errorLabel.setText("Puedes intentar iniciar sesión nuevamente.");
                    }
                })
        );
        temporizador.setCycleCount(60);
        temporizador.play();
    }

    /**
     * Maneja el evento del enlace para recuperar contraseña.
     * Abre una ventana modal para la recuperación de contraseña.
     * @param actionEvent El evento de acción generado por el enlace.
     */
    public void recuperarContraAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RecuperacionPassword.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            Image icono = new Image(getClass().getResource("/img/Icon.png").toExternalForm());
            stage.getIcons().add(icono);
            stage.setTitle("Recuperar Contraseña");
            stage.setScene(new Scene(root));

            // --- Configurar como ventana modal ---
            stage.initModality(Modality.APPLICATION_MODAL);

            // Mostrar y esperar a que se cierre
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
