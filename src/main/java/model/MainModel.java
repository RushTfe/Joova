package model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class MainModel {
    private StringProperty nombre;
    private StringProperty pass;
    private ListProperty listaProductos;
    private ListProperty listaClientes;

    public Object getListaProductos() {
        return listaProductos.get();
    }

    public ListProperty listaProductosProperty() {
        return listaProductos;
    }

    public void setListaProductos(Object listaProductos) {
        this.listaProductos.set(listaProductos);
    }

    public Object getListaClientes() {
        return listaClientes.get();
    }

    public ListProperty listaClientesProperty() {
        return listaClientes;
    }

    public void setListaClientes(Object listaClientes) {
        this.listaClientes.set(listaClientes);
    }

    public MainModel() {
        nombre = new SimpleStringProperty(this, "nombre");
        pass = new SimpleStringProperty(this, "pass");
        listaClientes = new SimpleListProperty(this, "listaClientes", FXCollections.observableArrayList());
        listaProductos = new SimpleListProperty(this, "listaProductos", FXCollections.observableArrayList());

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

    public String getPass() {
        return pass.get();
    }

    public StringProperty passProperty() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass.set(pass);
    }
}
