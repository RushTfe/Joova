package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AccionesModel {
    private BooleanProperty presentacion;
    private BooleanProperty puestaEnMarcha;
    private BooleanProperty experiencia;
    private BooleanProperty accionEspecial;
    private StringProperty observaciones;

    public AccionesModel() {
        presentacion = new SimpleBooleanProperty(this, "presentacion");
        puestaEnMarcha = new SimpleBooleanProperty(this, "puestaEnMarcha");
        experiencia = new SimpleBooleanProperty(this, "experiencia");
        accionEspecial = new SimpleBooleanProperty(this, "accionEspecial");
        observaciones = new SimpleStringProperty(this, "observaciones");
    }

    public boolean isPresentacion() {
        return presentacion.get();
    }

    public BooleanProperty presentacionProperty() {
        return presentacion;
    }

    public void setPresentacion(boolean presentacion) {
        this.presentacion.set(presentacion);
    }

    public boolean isPuestaEnMarcha() {
        return puestaEnMarcha.get();
    }

    public BooleanProperty puestaEnMarchaProperty() {
        return puestaEnMarcha;
    }

    public void setPuestaEnMarcha(boolean puestaEnMarcha) {
        this.puestaEnMarcha.set(puestaEnMarcha);
    }

    public boolean isExperiencia() {
        return experiencia.get();
    }

    public BooleanProperty experienciaProperty() {
        return experiencia;
    }

    public void setExperiencia(boolean experiencia) {
        this.experiencia.set(experiencia);
    }

    public boolean isAccionEspecial() {
        return accionEspecial.get();
    }

    public BooleanProperty accionEspecialProperty() {
        return accionEspecial;
    }

    public void setAccionEspecial(boolean accionEspecial) {
        this.accionEspecial.set(accionEspecial);
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
}
