package model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class PMyPresentacionesModel {
    private IntegerProperty codigoEvento;
    private StringProperty codCliente;
    private StringProperty nombreCliente;
    private StringProperty direccionCliente;
    private StringProperty observaciones;
    private ObjectProperty<LocalDate> fechaEvento;
    private BooleanProperty ventaRealizada;

    public PMyPresentacionesModel() {
        codigoEvento = new SimpleIntegerProperty(this, "codigoEvento");
        codCliente = new SimpleStringProperty(this, "codCliente");
        nombreCliente = new SimpleStringProperty(this, "nombreCliente");
        direccionCliente = new SimpleStringProperty(this, "direccionCliente");
        observaciones = new SimpleStringProperty(this, "observaciones");
        fechaEvento = new SimpleObjectProperty<>(this, "fechaEvento");
        ventaRealizada = new SimpleBooleanProperty(this, "ventaRealizada", false);
    }

    public int getCodigoEvento() {
        return codigoEvento.get();
    }

    public IntegerProperty codigoEventoProperty() {
        return codigoEvento;
    }

    public void setCodigoEvento(int codigoEvento) {
        this.codigoEvento.set(codigoEvento);
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

    public LocalDate getFechaEvento() {
        return fechaEvento.get();
    }

    public ObjectProperty<LocalDate> fechaEventoProperty() {
        return fechaEvento;
    }

    public void setFechaEvento(LocalDate fechaEvento) {
        this.fechaEvento.set(fechaEvento);
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

    public StringProperty fechaAsString() {
        return new SimpleStringProperty(this, "fechaEventoAsString", getFechaEvento().toString());
    }

    @Override
    public String toString() {
        return getFechaEvento().toString();
    }
}
