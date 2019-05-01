package model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class PresentacionesModel {
    private IntegerProperty codPresentacion;
    private StringProperty codCliente;
    private StringProperty nombreCliente;
    private StringProperty direccionCliente;
    private StringProperty observaciones;
    private ObjectProperty<LocalDate> fechaPresentacion;
    private BooleanProperty ventaRealizada;

    public PresentacionesModel() {
        codPresentacion = new SimpleIntegerProperty(this, "codPresentacion");
        codCliente = new SimpleStringProperty(this, "codCliente");
        nombreCliente = new SimpleStringProperty(this, "nombreCliente");
        direccionCliente = new SimpleStringProperty(this, "direccionCliente");
        observaciones = new SimpleStringProperty(this, "observaciones");
        fechaPresentacion = new SimpleObjectProperty<>(this, "fechaPresentacion");
        ventaRealizada = new SimpleBooleanProperty(this, "ventaRealizada", false);
    }

    public int getCodPresentacion() {
        return codPresentacion.get();
    }

    public IntegerProperty codPresentacionProperty() {
        return codPresentacion;
    }

    public void setCodPresentacion(int codPresentacion) {
        this.codPresentacion.set(codPresentacion);
    }

    public String getCodCliente() {
        return codCliente.get();
    }

    public StringProperty codClienteProperty() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente.set(codCliente);
    }

    public String getObservaciones() {
        return observaciones.get();
    }

    public StringProperty observacionesProperty() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones.set(observaciones);
    }

    public String getNombreCliente() {
        return nombreCliente.get();
    }

    public StringProperty nombreClienteProperty() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente.set(nombreCliente);
    }

    public String getDireccionCliente() {
        return direccionCliente.get();
    }

    public StringProperty direccionClienteProperty() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente.set(direccionCliente);
    }

    public LocalDate getFechaPresentacion() {
        return fechaPresentacion.get();
    }

    public ObjectProperty<LocalDate> fechaPresentacionProperty() {
        return fechaPresentacion;
    }

    public void setFechaPresentacion(LocalDate fechaPresentacion) {
        this.fechaPresentacion.set(fechaPresentacion);
    }

    public boolean isVentaRealizada() {
        return ventaRealizada.get();
    }

    public BooleanProperty ventaRealizadaProperty() {
        return ventaRealizada;
    }

    public void setVentaRealizada(boolean ventaRealizada) {
        this.ventaRealizada.set(ventaRealizada);
    }
}
