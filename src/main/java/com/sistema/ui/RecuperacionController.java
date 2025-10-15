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
     * Gestion de eventos del boton de enviar correo
     * @param actionEvent evento del boton
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
     * Gestion del evento del boton verificar codigo
     * @param actionEvent evento del boton
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
     * Gestor de eventos para confirmar cambio de contraseña
     * @param actionEvent Evento del boton
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
