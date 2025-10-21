package com.sistema.ui;

import com.sistema.dao.ProductoDAO;
import com.sistema.dao.ProveedorDAO;
import com.sistema.modelo.Producto;
import com.sistema.modelo.Proveedor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ProductoEditorController {

    @FXML
    private Label nombreLabel;

    @FXML
    private TextField codigoTF, nombreTF, descripcionTF, preCompraTF, preVentaTF, stockActualTF, stockMinTF;

    @FXML
    private ComboBox<String> proveedorCB;

    @FXML
    public void initialize(){
        cargarProveedoresAccion();
    }


    @FXML
    public void btnConfirmarAction(ActionEvent actionEvent){
        Producto pro = new Producto();

        pro.setId(new ProductoDAO().buscarUnicoPorNombre(nombreTF.getText()).getId());
        pro.setCodigoSku(codigoTF.getText());
        pro.setNombre(nombreTF.getText());
        pro.setDescripcion(descripcionTF.getText());
        pro.setPrecioCompra(Double.parseDouble(preCompraTF.getText()));
        pro.setPrecioVenta(Double.parseDouble(preVentaTF.getText()));
        pro.setStockActual(Integer.parseInt(stockActualTF.getText()));
        pro.setStockMinimo(Integer.parseInt(stockMinTF.getText()));
        pro.setIdProveedor(new ProveedorDAO().buscarPorNombre(proveedorCB.getValue()).getId());

        if(new ProductoDAO().actualizarProducto(pro)){

            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exito");
            alert.setHeaderText(null);
            alert.setContentText("Producto Actualizado");
            alert.initOwner(nombreLabel.getScene().getWindow());
            alert.showAndWait();

        }else{

            Alert alert =  new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("A ocurrido un error");
            alert.initOwner(nombreLabel.getScene().getWindow());
            alert.showAndWait();

        }

    }

    @FXML
    public void btnCancelarAction(ActionEvent actionEvent){
        Stage stage = (Stage) nombreLabel.getScene().getWindow();
        stage.close();
    }

    public void setProductoData(int id) {
        Producto p = new ProductoDAO().buscarPorId(id);
        if (p != null) {
            nombreLabel.setText(p.getNombre());
            codigoTF.setText(p.getCodigoSku());
            nombreTF.setText(p.getNombre());
            descripcionTF.setText(p.getDescripcion());
            preCompraTF.setText(String.valueOf(p.getPrecioCompra()));
            preVentaTF.setText(String.valueOf(p.getPrecioVenta()));
            stockActualTF.setText(String.valueOf(p.getStockActual()));
            stockMinTF.setText(String.valueOf(p.getStockMinimo()));
            proveedorCB.setValue(new ProveedorDAO().buscarPorId(p.getIdProveedor()).getNombre());
        }
    }

    public void cargarProveedoresAccion(){
        ProveedorDAO proveedor = new ProveedorDAO();
        List<Proveedor> listaPro = proveedor.listaProveedores();
        List<String> listaProveedores = new ArrayList<>();

        for(Proveedor pro: listaPro){
            String nombrePro = pro.getNombre();
            listaProveedores.add(nombrePro);
        }
        ObservableList<String> datos = FXCollections.observableArrayList(listaProveedores);
        proveedorCB.setItems(datos);
    }
}
