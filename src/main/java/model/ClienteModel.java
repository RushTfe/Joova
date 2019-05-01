package model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class ClienteModel {
    private StringProperty dni;
    private StringProperty nombre;
    private StringProperty apellidos;
    private StringProperty telefono;
    private StringProperty email;
    private ObjectProperty<LocalDate> fechaNacimiento;
    private StringProperty direccion;
    private StringProperty observaciones;
    private StringProperty genero;
    private BooleanProperty huerfano;

    public ClienteModel() {
        dni = new SimpleStringProperty(this, "dni");
        nombre = new SimpleStringProperty(this, "nombre");
        apellidos = new SimpleStringProperty(this, "apellidos");
        telefono = new SimpleStringProperty(this, "telefono");
        email = new SimpleStringProperty(this, "email");
        fechaNacimiento = new SimpleObjectProperty<>(this, "fechaNacimiento");
        direccion = new SimpleStringProperty(this, "direccion");
        observaciones = new SimpleStringProperty(this, "observaciones");
        genero = new SimpleStringProperty(this, "genero");
        huerfano = new SimpleBooleanProperty(this, "huerfano");
    }

    public String getDni() {
        return dni.get();
    }

    public StringProperty dniProperty() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni.set(dni);
    }

    public String getNombre() {
        return nombre.get();
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getApellidos() {
        return apellidos.get();
    }

    public StringProperty apellidosProperty() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos.set(apellidos);
    }

    public String getTelefono() {
        return telefono.get();
    }

    public StringProperty telefonoProperty() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono.set(telefono);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento.get();
    }

    public ObjectProperty<LocalDate> fechaNacimientoProperty() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento.set(fechaNacimiento);
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

    public String getObservaciones() {
        return observaciones.get();
    }

    public StringProperty observacionesProperty() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones.set(observaciones);
    }

    public String getGenero() {
        return genero.get();
    }

    public StringProperty generoProperty() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero.set(genero);
    }

    public boolean isHuerfano() {
        return huerfano.get();
    }

    public BooleanProperty huerfanoProperty() {
        return huerfano;
    }

    public void setHuerfano(boolean huerfano) {
        this.huerfano.set(huerfano);
    }

    @Override
    public String toString() {
        return getNombre() + " " + getApellidos();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof ClienteModel))
            return false;
        ClienteModel cliente = (ClienteModel)obj;
        if (this.getDni().equals(cliente.getDni()))
            return true;
        return false;
    }
}
