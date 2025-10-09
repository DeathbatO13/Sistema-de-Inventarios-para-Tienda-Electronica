package com.sistema;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.IOException;

public class MainApp extends Application {

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

    public static void main(String[] args) {
        launch(args);
    }
}
