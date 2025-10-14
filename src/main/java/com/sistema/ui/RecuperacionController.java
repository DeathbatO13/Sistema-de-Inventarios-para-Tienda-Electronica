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

    @FXML
    public void btnVerificarAction(ActionEvent actionEvent){
        String codDigitado = codigoVerificacion.getText();

        if(codDigitado.equals(codigo)){
            /*btnEnviarCorreo.setDisable(true);
            correoTextField.setDisable(true);*/
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

    @FXML
    public void btnConfirmarAction(ActionEvent actionEvent) {
    }
}
