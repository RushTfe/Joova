package model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import nuevoproducto.NuevoProductoModel;

import java.time.LocalDate;

public class VentasModel {
    //Recoge su valor de un bindeo desde MainController -> ocultarLogin();
    private StringProperty cliente;
    private StringProperty codContrato;
    private DoubleProperty precioTotal;
    private ObjectProperty<LocalDate> fechaVenta;
    private ObjectProperty<TipoPagoModel> tipoPago;
    private StringProperty observacionesVenta;
    private BooleanProperty mostrarProductos;

    public VentasModel() {
        cliente = new SimpleStringProperty(this, "cliente");
        codContrato = new SimpleStringProperty(this, "codContrato");
        precioTotal = new SimpleDoubleProperty(this, "precioTotal", 0.0);
        fechaVenta = new SimpleObjectProperty<>(this, "fechaVenta", LocalDate.now());
        tipoPago = new SimpleObjectProperty<>(this, "tipoPago");
        observacionesVenta = new SimpleStringProperty(this, "observacionesVenta");
        mostrarProductos = new SimpleBooleanProperty(this, "mostrarProductos");

        // Listener que detecta el cambio de productos en la lista, para sumar el precio total.
    }

    public TipoPagoModel getTipoPago() {
        return tipoPago.get();
    }

    public ObjectProperty<TipoPagoModel> tipoPagoProperty() {
        return tipoPago;
    }

    public void setTipoPago(TipoPagoModel tipoPago) {
        this.tipoPago.set(tipoPago);
    }

    public boolean isMostrarProductos() {
        return mostrarProductos.get();
    }

    public BooleanProperty mostrarProductosProperty() {
        return mostrarProductos;
    }

    public void setMostrarProductos(boolean mostrarProductos) {
        this.mostrarProductos.set(mostrarProductos);
    }

    public String getCliente() {
        return cliente.get();
    }

    public StringProperty clienteProperty() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente.set(cliente);
    }

    public double getPrecioTotal() {
        return precioTotal.get();
    }

    public DoubleProperty precioTotalProperty() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal.set(precioTotal);
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

    public StringProperty precioAsString() {
        StringProperty string = new SimpleStringProperty(this, "string", precioTotal.getName());
        string.bind(Bindings.concat(precioTotal, "€"));
        return string;
    }
}
