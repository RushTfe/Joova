package model;

import Joova.ProductoCardModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class VentasModel {
    private ListProperty<ClienteModel> listaClientes;
    private ListProperty<ProductoCardModel> listaProductos;
    private StringProperty codContrato;
    private DoubleProperty precioTotal;
    private ObjectProperty<LocalDate> fechaVenta;
    private StringProperty observacionesVenta;

    public double getPrecioTotal() {
        return precioTotal.get();
    }

    public DoubleProperty precioTotalProperty() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal.set(precioTotal);
    }

    public VentasModel() {
        listaClientes = new SimpleListProperty<>(this, "listaClientes", FXCollections.observableArrayList());
        listaProductos = new SimpleListProperty<>(this, "listaProductos", FXCollections.observableArrayList());
        codContrato = new SimpleStringProperty(this, "codContrato");
        precioTotal = new SimpleDoubleProperty(this, "precioTotal", 0.0);
        fechaVenta = new SimpleObjectProperty<>(this, "fechaVenta", LocalDate.now());
        observacionesVenta = new SimpleStringProperty(this, "observacionesVenta");
    }

    public ObservableList<ClienteModel> getListaClientes() {
        return listaClientes.get();
    }

    public ListProperty<ClienteModel> listaClientesProperty() {
        return listaClientes;
    }

    public void setListaClientes(ObservableList<ClienteModel> listaClientes) {
        this.listaClientes.set(listaClientes);
    }

    public String getCodContrato() {
        return codContrato.get();
    }

    public StringProperty codContratoProperty() {
        return codContrato;
    }

    public void setCodContrato(String codContrato) {
        this.codContrato.set(codContrato);
    }

    public LocalDate getFechaVenta() {
        return fechaVenta.get();
    }

    public ObjectProperty<LocalDate> fechaVentaProperty() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta.set(fechaVenta);
    }

    public String getObservacionesVenta() {
        return observacionesVenta.get();
    }

    public StringProperty observacionesVentaProperty() {
        return observacionesVenta;
    }

    public void setObservacionesVenta(String observacionesVenta) {
        this.observacionesVenta.set(observacionesVenta);
    }

    public ObservableList<ProductoCardModel> getListaProductos() {
        return listaProductos.get();
    }

    public ListProperty<ProductoCardModel> listaProductosProperty() {
        return listaProductos;
    }

    public void setListaProductos(ObservableList<ProductoCardModel> listaProductos) {
        this.listaProductos.set(listaProductos);
    }

    public StringProperty precioAsString() {
        StringProperty string = new SimpleStringProperty(this, "string", precioTotal.getName());
        string.bind(Bindings.concat(precioTotal, "€"));
        return string;
    }
}
