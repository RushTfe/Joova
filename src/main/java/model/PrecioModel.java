package model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class PrecioModel {
    private IntegerProperty codArticulo;
    private DoubleProperty precioArticulo;
    private ObjectProperty<LocalDate> fechaCambio;

    public PrecioModel() {
        codArticulo = new SimpleIntegerProperty(this, "codArticulo");
        precioArticulo = new SimpleDoubleProperty(this, "precioArticulo");
        fechaCambio = new SimpleObjectProperty<>(this, "fechaCambio", LocalDate.now());
    }

    public int getCodArticulo() {
        return codArticulo.get();
    }

    public IntegerProperty codArticuloProperty() {
        return codArticulo;
    }

    public void setCodArticulo(int codArticulo) {
        this.codArticulo.set(codArticulo);
    }

    public double getPrecioArticulo() {
        return precioArticulo.get();
    }

    public DoubleProperty precioArticuloProperty() {
        return precioArticulo;
    }

    public void setPrecioArticulo(double precioArticulo) {
        this.precioArticulo.set(precioArticulo);
    }

    public LocalDate getFechaCambio() {
        return fechaCambio.get();
    }

    public ObjectProperty<LocalDate> fechaCambioProperty() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDate fechaCambio) {
        this.fechaCambio.set(fechaCambio);
    }
}
