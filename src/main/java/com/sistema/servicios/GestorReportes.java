package com.sistema.servicios;

import com.sistema.ui.ReportesController;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Clase de gestión de reportes analíticos del sistema de inventario.
 * Proporciona constantes para tipos de reporte y periodos, cálculo de rangos de fechas
 * y generación de archivos de reporte en formatos TXT y CSV.
 *
 * <p>Funcionalidades principales:
 * <ul>
 *   <li>Definición estática de tipos de reporte y periodos disponibles.</li>
 *   <li>Cálculo automático de fecha de inicio según el periodo seleccionado.</li>
 *   <li>Generación de archivos TXT y CSV con datos simulados (extensible a datos reales).</li>
 *   <li>Manejo seguro de archivos con try-with-resources.</li>
 * </ul>
 * </p>
 *
 * <p>Actúa como capa de servicio para el {@link ReportesController}, permitiendo
 * la exportación de reportes personalizados. Los métodos de generación son extensibles
 * para integrar consultas reales a la base de datos según el tipo de reporte.</p>
 */
public class GestorReportes {

    /**
     * Lista estática inmutable con los tipos de reporte disponibles en el sistema.
     * Se utiliza para poblar ComboBox en la interfaz y validar entradas.
     */
    public static final List<String> TIPOS_REPORTE = Arrays.asList(
            "Total de ventas",
            "Total de compras",
            "Total de ganancias",
            "Promedio diario de ventas",
            "Ventas por empleado",
            "Productos más vendidos",
            "Productos sin movimiento"
    );

    /**
     * Lista estática inmutable con los periodos de tiempo disponibles para los reportes.
     * Cada opción corresponde a un rango relativo a la fecha actual.
     */
    public static final List<String> PERIODOS = Arrays.asList(
            "Última semana",
            "Últimas 2 semanas",
            "Último mes",
            "Últimos 2 meses",
            "Últimos 3 meses",
            "Último semestre",
            "Último año"
    );

    /**
     * Calcula la fecha de inicio del periodo seleccionado.
     * Utiliza {@link LocalDate#now()} como referencia y resta el intervalo correspondiente.
     *
     * @param periodo El nombre del periodo (ej. "Último mes", "Últimos 3 meses").
     * @return La fecha de inicio del rango. Por defecto, devuelve hace 1 mes si el periodo no es reconocido.
     */
    public LocalDate obtenerFechaInicio(String periodo) {
        LocalDate hoy = LocalDate.now();
        return switch (periodo) {
            case "Última semana" -> hoy.minusWeeks(1);
            case "Últimas 2 semanas" -> hoy.minusWeeks(2);
            case "Último mes" -> hoy.minusMonths(1);
            case "Últimos 2 meses" -> hoy.minusMonths(2);
            case "Últimos 3 meses" -> hoy.minusMonths(3);
            case "Último semestre" -> hoy.minusMonths(6);
            case "Último año" -> hoy.minusYears(1);
            default -> hoy.minusMonths(1); // Por defecto, último mes
        };
    }

    /**
     * Genera un archivo de reporte en formato TXT con información simulada.
     * El archivo se guarda en el directorio actual con nombre basado en el tipo de reporte.
     * Incluye encabezado con tipo, periodo y rango de fechas.
     *
     * @param tipo El tipo de reporte seleccionado (ej. "Productos más vendidos").
     * @param periodo El periodo seleccionado (ej. "Últimos 3 meses").
     */
    public void generarReporteTXT(String tipo, String periodo) {

        LocalDate desde = obtenerFechaInicio(periodo);
        LocalDate hasta = LocalDate.now();

        ServicioReportes servicio = new ServicioReportes();
        String contenido = servicio.obtenerContenidoReporte(tipo, desde, hasta);

        try (FileWriter writer = new FileWriter("Reporte_" + tipo.replace(" ", "_") + ".txt")) {
            writer.write(contenido);
            System.out.println("Reporte TXT generado correctamente: " + tipo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Genera un archivo de reporte en formato CSV con estructura tabular.
     * Incluye una fila de encabezado y una fila de datos simulada.
     * El archivo se guarda en el directorio actual con extensión .csv.
     *
     * @param tipo El tipo de reporte seleccionado.
     * @param periodo El periodo seleccionado.
     */
    public void generarReporteCSV(String tipo, String periodo) {
        LocalDate desde = obtenerFechaInicio(periodo);
        LocalDate hasta = LocalDate.now();

        // Simulación
        String contenido = "Tipo,Periodo,FechaInicio,FechaFin\n" +
                tipo + "," + periodo + "," + desde + "," + hasta + "\n";

        try (FileWriter writer = new FileWriter("Reporte_" + tipo.replace(" ", "_") + ".csv")) {
            writer.write(contenido);
            System.out.println("Reporte CSV generado correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


