package model;

import javafx.beans.property.*;
import nuevoproducto.NuevoProductoModel;

public class InteresesModel {
    private StringProperty codCliente;
    private ObjectProperty<NuevoProductoModel> articulo;

    public InteresesModel() {
        codCliente = new SimpleStringProperty(this, "codCliente");
        articulo = new SimpleObjectProperty<>(this, "articulo");
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

    public NuevoProductoModel getArticulo() {
        return articulo.get();
    }

    public ObjectProperty<NuevoProductoModel> articuloProperty() {
        return articulo;
    }

    public void setArticulo(NuevoProductoModel articulo) {
        this.articulo.set(articulo);
    }

    @Override
    public String toString() {
        return getArticulo().getNombreProducto();
    }
}
