package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Participante {
    private ObjectProperty<ClienteModel> cliente;
    private BooleanProperty compra;

    public Participante() {
        cliente = new SimpleObjectProperty<>(this, "cliente");
        compra = new SimpleBooleanProperty(this, "compra");
    }

    public Participante(ClienteModel cliente, boolean compra) {
        this();
        this.setCliente(cliente);
        this.setCompra(compra);
    }

    public ClienteModel getCliente() {
        return cliente.get();
    }

    public ObjectProperty<ClienteModel> clienteProperty() {
        return cliente;
    }

    public void setCliente(ClienteModel cliente) {
        this.cliente.set(cliente);
    }

    public boolean isCompra() {
        return compra.get();
    }

    public BooleanProperty compraProperty() {
        return compra;
    }

    public void setCompra(boolean compra) {
        this.compra.set(compra);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        Participante cliente = (Participante)obj;
        if (this.getCliente().getDni().equals(cliente.getCliente().getDni()))
            return true;
        ClienteModel nuevoCliente = (ClienteModel) obj;
        if (this.getCliente().getDni().equals(nuevoCliente.getDni()))
            return true;
        return false;
    }
}
