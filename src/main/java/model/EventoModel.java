package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EventoModel {
    private IntegerProperty codTipoEvento;
    private StringProperty nombreTipoEvento;

    public EventoModel() {
        codTipoEvento = new SimpleIntegerProperty(this, "codTipoEvento");
        nombreTipoEvento = new SimpleStringProperty(this, "nombreTipoEvento");
    }

    public int getCodTipoEvento() {
        return codTipoEvento.get();
    }

    public IntegerProperty codTipoEventoProperty() {
        return codTipoEvento;
    }

    public void setCodTipoEvento(int codTipoEvento) {
        this.codTipoEvento.set(codTipoEvento);
    }

    public String getNombreTipoEvento() {
        return nombreTipoEvento.get();
    }

    public StringProperty nombreTipoEventoProperty() {
        return nombreTipoEvento;
    }

    public void setNombreTipoEvento(String nombreTipoEvento) {
        this.nombreTipoEvento.set(nombreTipoEvento);
    }

    @Override
    public String toString() {
        return getNombreTipoEvento();
    }
}
