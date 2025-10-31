package com.sistema.ui;

import com.sistema.dao.VentasDAO;
import com.sistema.servicios.GestorReportes;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;


/**
 * Controlador para la vista de reportes del sistema de inventario.
 * Permite visualizar estadísticas de ventas en forma de tabla y gráfico de área,
 * con datos de productos más vendidos y ventas totales en los últimos tres meses.
 *
 * <p>Funcionalidades principales:
 * <ul>
 *   <li>Tabla de productos más vendidos con nombre, cantidad y ganancia total.</li>
 *   <li>Gráfico de área que muestra la evolución de ventas diarias en los últimos 3 meses.</li>
 *   <li>ComboBoxes para seleccionar tipo de reporte y periodo (funcionalidad en desarrollo).</li>
 *   <li>Botones para exportar reportes en TXT y CSV (pendiente implementación).</li>
 *   <li>Total acumulado de ventas mostrado en formato moneda colombiana.</li>
 * </ul>
 * </p>
 *
 * <p>Utiliza {@link VentasDAO} para obtener datos agregados y {@link GestorReportes}
 * para constantes de tipos y periodos. El gráfico se estiliza para una presentación limpia
 * y profesional, ocultando elementos innecesarios como leyendas y marcas de eje X.</p>
 */
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

    /**
     * Inicializa los componentes de la vista al cargar el FXML.
     * Configura la gráfica de área, carga los productos más vendidos, inicializa los ComboBox
     * con opciones de reporte y periodo, y muestra el total de ventas en formato moneda.
     */
    @FXML
    public void initialize(){

        //Carga los datos y estilos de la grafica
        cargarGafica();

        // Total vendido en el periodo
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        totalLabel.setText(String.valueOf(formatoMoneda.format(totalVendido)));

        // Carga de valores los Combo Box para generar reportes
        tipoRepoCB.getItems().addAll(GestorReportes.TIPOS_REPORTE);
        periodoRepoCB.getItems().addAll(GestorReportes.PERIODOS);

        // Carga de datos en la tabla
        cargarProductosMasVendidos();
    }

    /**
     * Maneja la acción del botón para generar un reporte en formato TXT.
     * Invoca al {@link GestorReportes} para crear el archivo con los datos actuales
     * del reporte seleccionado.
     *
     * @param actionEvent El evento desencadenado por el botón de exportar TXT.
     */
    public void btnReporteTXTAction(ActionEvent actionEvent) {

        if(tipoRepoCB.getValue() == null || periodoRepoCB.getValue() == null){

            mostrarAlerta("Debe seleccionar tipo y periodo del reporte","Error");

        }

        String tipo = tipoRepoCB.getValue();
        String periodo = periodoRepoCB.getValue();

        if(new GestorReportes().generarReporteTXT(tipo, periodo)){

            mostrarAlerta("Reporte Generado con exito", "Estado reporte");

        }else{

            mostrarAlerta("No se pudo generar el reporte", "Error");
        }

    }

    /**
     * Maneja la acción del botón para generar un reporte en formato CSV.
     * Invoca al {@link GestorReportes} para exportar los datos en formato compatible
     * con hojas de cálculo.
     *
     * @param actionEvent El evento desencadenado por el botón de exportar CSV.
     */
    public void btnReporteCSVAction(ActionEvent actionEvent) {

    }

    /**
     * Carga en la tabla los productos más vendidos en los últimos 3 meses.
     * Obtiene los datos mediante {@link VentasDAO#productosMasVendidosUltimos3Meses()}
     * y configura las columnas con {@link PropertyValueFactory}.
     */
    private void cargarProductosMasVendidos() {
        VentasDAO dao = new VentasDAO();
        List<VentaRow> list = dao.productosMasVendidosUltimos3Meses();

        ObservableList<VentaRow> data = FXCollections.observableArrayList(list);

        productoRepo.setCellValueFactory(new PropertyValueFactory<>("productoVendido"));
        cantidadRepo.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        profitRepo.setCellValueFactory(new PropertyValueFactory<>("precioTotal"));

        tablaReporte.setItems(data);
    }

    /**
     * Configura y carga el gráfico de área con las ventas diarias de los últimos 3 meses.
     * <ul>
     *   <li>Formatea las fechas en el eje X (aunque se ocultan visualmente).</li>
     *   <li>Agrega los puntos de datos desde {@link VentasDAO#obtenerVentasUltimosTresMeses()}.</li>
     *   <li>Aplica estilo minimalista: sin símbolos, sin leyenda, sin marcas en X.</li>
     *   <li>Acumula el total de ventas para mostrarlo en la etiqueta correspondiente.</li>
     * </ul>
     */
    private void cargarGafica(){
        //Configuracion de la grafica
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
    }


    public void mostrarAlerta(String mens, String title){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(mens);
        alert.initOwner(totalLabel.getScene().getWindow());
        alert.showAndWait();

    }
}
