package model;

import javafx.beans.property.*;

public class InteresesModel {
    private StringProperty codCliente;
    private IntegerProperty codArticulo;
    private StringProperty observaciones;
    private BooleanProperty comprado;

    public InteresesModel() {
        codCliente = new SimpleStringProperty(this, "codCliente");
        codArticulo = new SimpleIntegerProperty(this, "codArticulo");
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

    public int getCodArticulo() {
        return codArticulo.get();
    }

    public IntegerProperty codArticuloProperty() {
        return codArticulo;
    }

    public void setCodArticulo(int codArticulo) {
        this.codArticulo.set(codArticulo);
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

    public boolean isComprado() {
        return comprado.get();
    }

    public BooleanProperty compradoProperty() {
        return comprado;
    }

    public void setComprado(boolean comprado) {
        this.comprado.set(comprado);
    }
}
