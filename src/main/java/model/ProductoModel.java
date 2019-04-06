package model;

import Joova.ProductoCardModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nuevoproducto.NuevoProductoModel;

public class ProductoModel {
    private ListProperty<NuevoProductoModel> listaProductos;

    public ProductoModel() {
        listaProductos = new SimpleListProperty<>(this, "listaProductos", FXCollections.observableArrayList());
    }

    public ObservableList<NuevoProductoModel> getListaProductos() {
        return listaProductos.get();
    }

    public ListProperty<NuevoProductoModel> listaProductosProperty() {
        return listaProductos;
    }

    public void setListaProductos(ObservableList<NuevoProductoModel> listaProductos) {
        this.listaProductos.set(listaProductos);
    }
}
