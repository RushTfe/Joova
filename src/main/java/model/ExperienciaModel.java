package model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class ExperienciaModel {
    private IntegerProperty codExperiencia;
    private ListProperty<Participante> participantes;
    private ObjectProperty<LocalDate> fechaExperiencia;
    private StringProperty direccion;
    private StringProperty observaciones;

    public ExperienciaModel() {
        codExperiencia = new SimpleIntegerProperty(this, "codExperiencia");
        participantes = new SimpleListProperty<>(this, "participantes", FXCollections.observableArrayList());
        fechaExperiencia = new SimpleObjectProperty<>(this, "fechaExperiencia");
        direccion = new SimpleStringProperty(this, "direccion");
        observaciones = new SimpleStringProperty(this, "observaciones");

    }

    public int getCodExperiencia() {
        return codExperiencia.get();
    }

    public IntegerProperty codExperienciaProperty() {
        return codExperiencia;
    }

    public void setCodExperiencia(int codExperiencia) {
        this.codExperiencia.set(codExperiencia);
    }

    public String getDireccion() {
        return direccion.get();
    }

    public StringProperty direccionProperty() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion.set(direccion);
    }

    public ObservableList<Participante> getParticipantes() {
        return participantes.get();
    }

    public ListProperty<Participante> participantesProperty() {
        return participantes;
    }

    public void setParticipantes(ObservableList<Participante> participantes) {
        this.participantes.set(participantes);
    }

    public LocalDate getFechaExperiencia() {
        return fechaExperiencia.get();
    }

    public ObjectProperty<LocalDate> fechaExperienciaProperty() {
        return fechaExperiencia;
    }

    public void setFechaExperiencia(LocalDate fechaExperiencia) {
        this.fechaExperiencia.set(fechaExperiencia);
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

    @Override
    public String toString() {
        return getFechaExperiencia().toString();
    }
}
