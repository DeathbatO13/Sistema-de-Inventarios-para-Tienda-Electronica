package com.sistema.ui;

import com.sistema.dao.ProductoDAO;
import com.sistema.dao.ProveedorDAO;
import com.sistema.modelo.Producto;
import com.sistema.modelo.Proveedor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
    public void btnConfirmarAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnCancelarAction(ActionEvent actionEvent) {
    }

    public void setProductoData(int id) {
        Producto p = new ProductoDAO().buscarPorId(id);
        if (p != null) {
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
