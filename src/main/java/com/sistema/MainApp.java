package com.sistema;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.IOException;

/**
 * Clase principal de la aplicación ElectroStock.
 * Extiende la clase Application de JavaFX para iniciar la aplicación y cargar la vista de inicio de sesión.
 */
public class MainApp extends Application {

    /**
     * Inicia la aplicación JavaFX, configurando y mostrando la ventana de inicio de sesión.
     * Carga el archivo FXML de la vista de login, establece el título, ícono y propiedades de la ventana.
     * @param stage El escenario principal (ventana) de la aplicación.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/LoginVista.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Image icono = new Image(getClass().getResource("/img/Icon.png").toExternalForm());
        stage.getIcons().add(icono);
        stage.setTitle("ElectroStock - Inicio de Sesión");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Punto de entrada principal de la aplicación.
     * Lanza la aplicación JavaFX.
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
