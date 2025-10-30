package com.sistema.ui;

import com.sistema.dao.VentasDAO;
import com.sistema.util.VentaRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.util.List;


public class ReportesController {

    @FXML
    private TableView<VentaRow> tablaReporte;

    @FXML
    private TableColumn<VentaRow, String> productoRepo;

    @FXML
    private TableColumn<VentaRow, Integer> cantidadRepo;

    @FXML
    private TableColumn<VentaRow, Double> profitRepo;

    @FXML
    private AreaChart<LocalDateTime, Double> grafico;

    @FXML
    private ComboBox<String> tipoRepoCB, periodoRepoCB;

    @FXML
    private Label gagnaciasLabel;


    public void initialize(){
        cargarProductosMasVendidos();
    }


    public void btnReporteTXTAction(ActionEvent actionEvent) {
    }

    public void btnReporteCSVAction(ActionEvent actionEvent) {
    }

    private void cargarProductosMasVendidos() {
        VentasDAO dao = new VentasDAO();
        List<VentaRow> list = dao.productosMasVendidosUltimos3Meses();

        ObservableList<VentaRow> data = FXCollections.observableArrayList(list);

        productoRepo.setCellValueFactory(new PropertyValueFactory<>("productoVendido"));
        cantidadRepo.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        profitRepo.setCellValueFactory(new PropertyValueFactory<>("precioTotal"));

        tablaReporte.setItems(data);
    }
}
