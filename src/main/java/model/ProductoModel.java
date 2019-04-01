package model;

import Joova.ProductoCardModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductoModel {
    private ListProperty<ProductoCardModel> listaProductos;

    public ProductoModel() {
        listaProductos = new SimpleListProperty<>(this, "listaProductos", FXCollections.observableArrayList());
    }

    public ObservableList<ProductoCardModel> getListaProductos() {
        return listaProductos.get();
    }

    public ListProperty<ProductoCardModel> listaProductosProperty() {
        return listaProductos;
    }

    public void setListaProductos(ObservableList<ProductoCardModel> listaProductos) {
        this.listaProductos.set(listaProductos);
    }
}
