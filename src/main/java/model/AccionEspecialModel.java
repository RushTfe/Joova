package model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class AccionEspecialModel {
    private IntegerProperty codEvento;
    private StringProperty nombreEvento;
    private ObjectProperty<TipoPagoyEventoModel> tipoEvento;
    private StringProperty direccionEvento;
    private StringProperty observacionesEvento;
    private ObjectProperty<LocalDate> fechaEvento;

    public AccionEspecialModel() {
        codEvento = new SimpleIntegerProperty(this, "codEvento");
        nombreEvento = new SimpleStringProperty(this, "nombreEvento");
        tipoEvento = new SimpleObjectProperty(this, "tipoEvento");
        direccionEvento = new SimpleStringProperty(this, "direccionEvento");
        observacionesEvento = new SimpleStringProperty(this, "observacionesEvento");
        fechaEvento = new SimpleObjectProperty<>(this, "fechaEvento");
    }

    public int getCodEvento() {
        return codEvento.get();
    }

    public IntegerProperty codEventoProperty() {
        return codEvento;
    }

    public void setCodEvento(int codEvento) {
        this.codEvento.set(codEvento);
    }

    public String getObservacionesEvento() {
        return observacionesEvento.get();
    }

    public StringProperty observacionesEventoProperty() {
        return observacionesEvento;
    }

    public void setObservacionesEvento(String observacionesEvento) {
        this.observacionesEvento.set(observacionesEvento);
    }

    public String getDireccionEvento() {
        return direccionEvento.get();
    }

    public StringProperty direccionEventoProperty() {
        return direccionEvento;
    }

    public void setDireccionEvento(String direccionEvento) {
        this.direccionEvento.set(direccionEvento);
    }

    public TipoPagoyEventoModel getTipoEvento() {
        return tipoEvento.get();
    }

    public ObjectProperty<TipoPagoyEventoModel> tipoEventoProperty() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoPagoyEventoModel tipoEvento) {
        this.tipoEvento.set(tipoEvento);
    }

    public String getNombreEvento() {
        return nombreEvento.get();
    }

    public StringProperty nombreEventoProperty() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento.set(nombreEvento);
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
}
