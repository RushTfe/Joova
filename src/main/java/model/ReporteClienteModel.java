package model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nuevoproducto.NuevoProductoModel;

public class ReporteClienteModel {
    private ListProperty<PMyPresentacionesModel> puestaEnMarchaModel;
    private ListProperty<PMyPresentacionesModel> presentacionesModels;
    private ListProperty<AccionEspecialModel> accionEspecialModel;
    private ListProperty<ExperienciaModel> experienciaModel;
    private ObjectProperty<ClienteModel> clienteModel;
    private ListProperty<VentasModel> ventasModel;
    private ListProperty<InteresesModel> interesesModel;
    private ListProperty<NuevoProductoModel> listaProductos;

    public ReporteClienteModel() {
        puestaEnMarchaModel = new SimpleListProperty<>(this, "puestaEnMarchaModel", FXCollections.observableArrayList());
        presentacionesModels = new SimpleListProperty<>(this, "presentacionesModels", FXCollections.observableArrayList());
        accionEspecialModel = new SimpleListProperty<>(this, "accionEspecialModel", FXCollections.observableArrayList());
        experienciaModel = new SimpleListProperty<>(this, "experienciaModel", FXCollections.observableArrayList());
        clienteModel = new SimpleObjectProperty<>(this, "clienteModel");
        ventasModel = new SimpleListProperty<>(this, "ventasModel", FXCollections.observableArrayList());
        interesesModel = new SimpleListProperty<>(this, "interesesModel", FXCollections.observableArrayList());
        listaProductos = new SimpleListProperty<>(this, "listaProductos", FXCollections.observableArrayList());
    }

    public ObservableList<PMyPresentacionesModel> getPresentacionesModels() {
        return presentacionesModels.get();
    }

    public ListProperty<PMyPresentacionesModel> presentacionesModelsProperty() {
        return presentacionesModels;
    }

    public void setPresentacionesModels(ObservableList<PMyPresentacionesModel> presentacionesModels) {
        this.presentacionesModels.set(presentacionesModels);
    }

    public ObservableList<PMyPresentacionesModel> getPuestaEnMarchaModel() {
        return puestaEnMarchaModel.get();
    }

    public ListProperty<PMyPresentacionesModel> puestaEnMarchaModelProperty() {
        return puestaEnMarchaModel;
    }

    public void setPuestaEnMarchaModel(ObservableList<PMyPresentacionesModel> puestaEnMarchaModel) {
        this.puestaEnMarchaModel.set(puestaEnMarchaModel);
    }

    public ObservableList<AccionEspecialModel> getAccionEspecialModel() {
        return accionEspecialModel.get();
    }

    public ListProperty<AccionEspecialModel> accionEspecialModelProperty() {
        return accionEspecialModel;
    }

    public void setAccionEspecialModel(ObservableList<AccionEspecialModel> accionEspecialModel) {
        this.accionEspecialModel.set(accionEspecialModel);
    }

    public ObservableList<ExperienciaModel> getExperienciaModel() {
        return experienciaModel.get();
    }

    public ListProperty<ExperienciaModel> experienciaModelProperty() {
        return experienciaModel;
    }

    public void setExperienciaModel(ObservableList<ExperienciaModel> experienciaModel) {
        this.experienciaModel.set(experienciaModel);
    }

    public ClienteModel getClienteModel() {
        return clienteModel.get();
    }

    public ObjectProperty<ClienteModel> clienteModelProperty() {
        return clienteModel;
    }

    public void setClienteModel(ClienteModel clienteModel) {
        this.clienteModel.set(clienteModel);
    }

    public ObservableList<VentasModel> getVentasModel() {
        return ventasModel.get();
    }

    public ListProperty<VentasModel> ventasModelProperty() {
        return ventasModel;
    }

    public void setVentasModel(ObservableList<VentasModel> ventasModel) {
        this.ventasModel.set(ventasModel);
    }

    public ObservableList<InteresesModel> getInteresesModel() {
        return interesesModel.get();
    }

    public ListProperty<InteresesModel> interesesModelProperty() {
        return interesesModel;
    }

    public void setInteresesModel(ObservableList<InteresesModel> interesesModel) {
        this.interesesModel.set(interesesModel);
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
