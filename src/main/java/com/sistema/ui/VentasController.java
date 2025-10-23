package com.sistema.ui;

import com.sistema.dao.ProductoDAO;
import com.sistema.dao.VentasDAO;
import com.sistema.modelo.Producto;
import com.sistema.modelo.Venta;
import com.sistema.util.VentaRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class VentasController {

    @FXML
    private TextField buscarVentasTF, precioVenta;

    @FXML
    private DatePicker buscarPorFecha, fecharRegistro;

    @FXML
    private TableView<VentaRow> tablaVentas;

    @FXML
    private TableColumn<VentaRow, String> productoColumn;

    @FXML
    private TableColumn<VentaRow, Integer> cantidadProd;

    @FXML
    private TableColumn<VentaRow, LocalDateTime> fechaV;

    @FXML
    private TableColumn<VentaRow, Double> precioV;

    @FXML
    private TableColumn<VentaRow, Void> accionC;

    @FXML
    private Spinner<Integer> cantidadPrVenta;

    @FXML
    private ComboBox<Producto> productosVenta;

    @FXML
    private Label contadorProductos;


    @FXML
    public void initialize(){

        productoColumn.setCellValueFactory(new PropertyValueFactory<>("productoVendido"));
        cantidadProd.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        precioV.setCellValueFactory(new PropertyValueFactory<>("precioTotal"));
        fechaV.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        precioV.setCellFactory(column -> new TableCell<VentaRow, Double>() { // Changed to VentaRow
            @Override
            protected void updateItem(Double valor, boolean empty) {
                super.updateItem(valor, empty);
                if (empty || valor == null) {
                    setText(null);
                } else {
                    setText(formatoMoneda.format(valor));
                }
            }
        });

        cargarListaVentas();
        agregarColumna();
    }


    public void buscarVentaAction(ActionEvent actionEvent){
        
    }
    
    
    public void buscarFechaAction(ActionEvent actionEvent) {
    }

    
    public void btnRegistrarVentaAction(ActionEvent actionEvent) {
    }


    public void btnCancelarAction(ActionEvent actionEvent) {
    }


    public void btnAgregarProdAction(ActionEvent actionEvent) {
    }


    private void agregarColumna() {
        accionC.setCellFactory(param -> new TableCell<VentaRow, Void>() {

            private final Button btnEditar = new Button("Editar");
            private final HBox contenedorBotones = new HBox();

            {
                // Estilos
                btnEditar.setStyle(
                        "-fx-background-color: #5882E8; -fx-text-fill: white; -fx-font-size: 13px; "
                                + "-fx-font-weight: bold; -fx-background-radius: 7;"
                );

                contenedorBotones.getChildren().add(btnEditar);
                contenedorBotones.setSpacing(5);
                contenedorBotones.setAlignment(Pos.CENTER);

                // Evento del botón
                btnEditar.setOnAction(event -> {
                    VentaRow venta = getTableView().getItems().get(getIndex());
                    System.out.println("Editar Venta: " + venta.getProductoVendido());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(contenedorBotones);
                }
            }
        });
    }



    private void cargarListaVentas(){
        VentasDAO dao = new VentasDAO();
        List<VentaRow> lista = dao.listaVentas();

        ObservableList<VentaRow> datos = FXCollections.observableArrayList(lista);
        tablaVentas.setItems(datos);
    }


}
