package com.sistema.ui;

import com.sistema.dao.UsuarioDAO;
import com.sistema.modelo.Usuario;
import com.sistema.servicios.ServicioEmail;
import com.sistema.servicios.SistemaAutenticacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Controlador para la ventana de recuperación de contraseña.
 * Implementa un flujo de tres pasos: envío de código por correo, verificación del código
 * y cambio seguro de contraseña con validación de seguridad.
 *
 * <p>Funcionalidades principales:
 * <ul>
 *   <li>Envío de código de 6 dígitos a través de {@link ServicioEmail} si el correo está registrado.</li>
 *   <li>Verificación del código ingresado y habilitación progresiva de campos.</li>
 *   <li>Validación de nueva contraseña: coincidencia, no vacía y cumplimiento de política de seguridad.</li>
 *   <li>Encriptación con BCrypt antes de actualizar en la base de datos mediante {@link UsuarioDAO}.</li>
 *   <li>Retroalimentación visual con colores y mensajes claros en cada etapa.</li>
 * </ul>
 * </p>
 *
 * <p>Utiliza {@link SistemaAutenticacion} para generación de códigos y validación de contraseñas.
 * El flujo es secuencial y seguro, evitando exposición innecesaria de campos.</p>
 */
public class RecuperacionController {

    @FXML
    private TextField correoTextField, codigoVerificacion;

    @FXML
    private PasswordField primeraContra, segundaContra;

    @FXML
    private Button btnEnviarCorreo, btnVerificarCodigo, btnConfirmarCambio;

    @FXML
    private Label estadoCodigo, estadoContra, cambioExistoso;

    SistemaAutenticacion auth = new SistemaAutenticacion();
    String codigo = auth.codigoRecuperacion();

    /**
     * Maneja el evento del botón para enviar un correo con el código de verificación.
     * Verifica si el correo ingresado está registrado, envía el código de recuperación
     * y habilita los campos para ingresar el código de verificación.
     * @param actionEvent El evento de acción generado por el botón.
     */
    @FXML
    public void btnEnviarCorreoAction(ActionEvent actionEvent){
        Usuario u = new Usuario();
        String correo = correoTextField.getText();
        u.setEmail(correo);
        if(new UsuarioDAO().estaRegistrado(u)){
            codigoVerificacion.setDisable(false);
            btnVerificarCodigo.setDisable(false);
            btnEnviarCorreo.setDisable(true);
            correoTextField.setDisable(true);
            System.out.println("Codigo " + codigo);
            ServicioEmail.enviarCorreo(correo,
                    "Cambio de contraseña",
                    "Estas intentando iniciar sesion al sistema de ElectroStock.\n" +
                            "Para segui con el proseso de cambio de contraseña\n" +
                            "Tu código de verificación es el siguiente:\n" + codigo);
            estadoCodigo.setText("Codigo enviado");
            estadoCodigo.setTextFill(Color.rgb(88, 130, 232));
            estadoCodigo.setVisible(true);

        }else{
            estadoCodigo.setText("El correo ingresado no tiene cuenta asociada");
            estadoCodigo.setTextFill(Color.rgb(232, 11, 11));
            estadoCodigo.setVisible(true);
            correoTextField.setText("");
        }
    }

    /**
     * Maneja el evento del botón para verificar el código de recuperación.
     * Compara el código ingresado con el enviado y, si es correcto, habilita los campos
     * para ingresar la nueva contraseña.
     * @param actionEvent El evento de acción generado por el botón.
     */
    @FXML
    public void btnVerificarAction(ActionEvent actionEvent){
        String codDigitado = codigoVerificacion.getText();

        if(codDigitado.equals(codigo)){
            codigoVerificacion.setDisable(true);
            btnVerificarCodigo.setDisable(true);
            primeraContra.setDisable(false);
            segundaContra.setDisable(false);
            btnConfirmarCambio.setDisable(false);
            estadoCodigo.setText("Codigo correcto, por favor ingresa la nueva contraseña: ");
            estadoCodigo.setTextFill(Color.rgb(88, 130, 232));
        }else{
            estadoCodigo.setText("El codigo no es correcto");
            estadoCodigo.setTextFill(Color.rgb(232, 11, 11));
            estadoCodigo.setVisible(true);
        }
    }

    /**
     * Maneja el evento del botón para confirmar el cambio de contraseña.
     * Valida que las contraseñas ingresadas coincidan y cumplan con los requisitos de seguridad,
     * actualiza la contraseña en la base de datos y muestra un mensaje de éxito o error.
     * @param actionEvent El evento de acción generado por el botón.
     */
    @FXML
    public void btnConfirmarAction(ActionEvent actionEvent) {

        String correo = correoTextField.getText();
        SistemaAutenticacion auth = new SistemaAutenticacion();

        if(primeraContra.getText().isEmpty() || segundaContra.getText().isEmpty()){
            primeraContra.setText("");
            segundaContra.setText("");
            estadoContra.setText("Los dos campos son requeridos");
            estadoContra.setTextFill(Color.rgb(232, 11, 11));
            estadoContra.setVisible(true);

        }else if(!(primeraContra.getText().equals(segundaContra.getText()))){
            primeraContra.setText("");
            segundaContra.setText("");
            estadoContra.setText("Las contraseñas no coinciden");
            estadoContra.setTextFill(Color.rgb(232, 11, 11));
            estadoContra.setVisible(true);

        } else if (!auth.contrasenaSegura(segundaContra.getText())) {
            primeraContra.setText("");
            segundaContra.setText("");
            estadoContra.setText("La contraseña debe tener mayuscula, minuscula, numeros y caracter especial");
            estadoContra.setTextFill(Color.rgb(232, 11, 11));
            estadoContra.setVisible(true);

        }else{
            estadoContra.setVisible(false);
            String contraHash = BCrypt.hashpw(segundaContra.getText(), BCrypt.gensalt());
            Usuario user = new Usuario();
            user.setEmail(correo);
            user.setContrasenaHash(contraHash);
            if(new UsuarioDAO().cambiarPassword(user)){
                cambioExistoso.setVisible(true);
            }else{
                cambioExistoso.setVisible(true);
                cambioExistoso.setText("Error al conectar a la base de datos");
                cambioExistoso.setTextFill(Color.rgb(232, 11, 11));
            }
        }

    }
}
