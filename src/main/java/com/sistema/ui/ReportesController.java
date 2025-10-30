package com.sistema.ui;

import com.sistema.dao.VentasDAO;
import com.sistema.util.VentaGraficaRow;
import com.sistema.util.VentaRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;


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
    private AreaChart<String, Number> grafico;

    @FXML
    private ComboBox<String> tipoRepoCB, periodoRepoCB;

    @FXML
    private Label totalLabel;

    private double totalVendido = 0;

    @FXML
    public void initialize(){

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        List<VentaGraficaRow> ventas = new VentasDAO().obtenerVentasUltimosTresMeses();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (VentaGraficaRow v : ventas) {
            series.getData().add(new XYChart.Data<>(v.getFecha().format(formatter), v.getTotalVentas()));
            totalVendido += v.getTotalVentas();
        }

        grafico.getData().clear();
        grafico.getData().add(series);

        // 🎨 Estilo limpio
        grafico.setCreateSymbols(false); // Ocultar los puntos
        grafico.setLegendVisible(false); // Ocultar la leyenda

        // 🔹 Configuración de ejes
        CategoryAxis xAxis = (CategoryAxis) grafico.getXAxis();
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setOpacity(0);

        NumberAxis yAxis = (NumberAxis) grafico.getYAxis();
        yAxis.setTickLabelsVisible(true);
        yAxis.setTickMarkVisible(true);
        grafico.setHorizontalGridLinesVisible(true);
        grafico.setVerticalGridLinesVisible(false);

        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

        totalLabel.setText(String.valueOf(formatoMoneda.format(totalVendido)));

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
