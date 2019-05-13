package model;

import javafx.beans.property.*;

public class Participante {
    private ObjectProperty<ClienteModel> cliente;
    private BooleanProperty compra;
    private BooleanProperty regalo;
    private StringProperty observacionParticipante;

    public Participante() {
        cliente = new SimpleObjectProperty<>(this, "cliente");
        compra = new SimpleBooleanProperty(this, "compra");
        regalo = new SimpleBooleanProperty(this, "regalo");
        observacionParticipante = new SimpleStringProperty(this, "observacionesParticipante");
    }

    public String getObservacionParticipante() {
        return observacionParticipante.get();
    }

    public StringProperty observacionParticipanteProperty() {
        return observacionParticipante;
    }

    public void setObservacionParticipante(String observacionParticipante) {
        this.observacionParticipante.set(observacionParticipante);
    }

    public Participante(ClienteModel cliente, boolean compra) {
        this();
        this.setCliente(cliente);
        this.setCompra(compra);
    }

    public boolean isRegalo() {
        return regalo.get();
    }

    public BooleanProperty regaloProperty() {
        return regalo;
    }

    public void setRegalo(boolean regalo) {
        this.regalo.set(regalo);
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

}
