package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TipoPagoModel {
    private IntegerProperty codTipoPago;
    private StringProperty nombreTipoPago;
    private StringProperty descripcionTipoPago;

    public TipoPagoModel() {
        codTipoPago = new SimpleIntegerProperty(this, "codTipoPago");
        nombreTipoPago = new SimpleStringProperty(this, "nombreTipoPago");
        descripcionTipoPago = new SimpleStringProperty(this, "descripcionTipoPago");
    }

    public int getCodTipoPago() {
        return codTipoPago.get();
    }

    public IntegerProperty codTipoPagoProperty() {
        return codTipoPago;
    }

    public void setCodTipoPago(int codTipoPago) {
        this.codTipoPago.set(codTipoPago);
    }

    public String getNombreTipoPago() {
        return nombreTipoPago.get();
    }

    public StringProperty nombreTipoPagoProperty() {
        return nombreTipoPago;
    }

    public void setNombreTipoPago(String nombreTipoPago) {
        this.nombreTipoPago.set(nombreTipoPago);
    }

    public String getDescripcionTipoPago() {
        return descripcionTipoPago.get();
    }

    public StringProperty descripcionTipoPagoProperty() {
        return descripcionTipoPago;
    }

    public void setDescripcionTipoPago(String descripcionTipoPago) {
        this.descripcionTipoPago.set(descripcionTipoPago);
    }

    @Override
    public String toString() {
        return getNombreTipoPago();
    }
}
