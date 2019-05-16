package dialogs;

import controller.ReporteClienteController;
import database.HooverDataBase;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import model.*;

public class DialogoReporteCliente extends Dialog<ReporteClienteModel> {
    private ReporteClienteController root;
    private HooverDataBase db;
    private Stage primaryStage;
    private ListProperty<PMyPresentacionesModel> listaPuestasEnMarcha;
    private ListProperty<PMyPresentacionesModel> listaPresentaciones;
    private ListProperty<AccionEspecialModel> listaAccionesEspeciales;
    private ListProperty<ExperienciaModel> listaExperiencias;
    private ListProperty<InteresesModel> listaIntereses;
    private ListProperty<VentasModel> listaVentas;

    public DialogoReporteCliente(Stage primaryStage, HooverDataBase db, ClienteModel clienteModel, ListProperty<NuevoProductoModel> listaProductos) {
        root = new ReporteClienteController(primaryStage);
        this.db = db;
        root.setCliente(clienteModel);
        this.primaryStage = primaryStage;

        // Creamos y poblamos las listas recogiendo los valores de la base de datos para mostrarlos en el reporte
        listaPuestasEnMarcha = new SimpleListProperty(this, "listaPuestasEnMarcha", FXCollections.observableArrayList());
        listaPresentaciones = new SimpleListProperty<>(this, "listaPresentaciones", FXCollections.observableArrayList());
        listaAccionesEspeciales = new SimpleListProperty<>(this, "listAccionesespeciales", FXCollections.observableArrayList());
        listaExperiencias = new SimpleListProperty<>(this, "listaExperiencias", FXCollections.observableArrayList());
        listaVentas = new SimpleListProperty<>(this, "listaVentas", FXCollections.observableArrayList());
        listaIntereses = new SimpleListProperty<>(this, "listaIntereses", FXCollections.observableArrayList());
        db.consultaPuestaMarchaCliente(listaPuestasEnMarcha, clienteModel.getDni());
        db.consultaPresentacionCliente(listaPresentaciones, clienteModel.getDni());
        db.consultaAccionesEspecialesCodCliente(listaAccionesEspeciales, clienteModel.getDni());
        db.consultaExperienciasCodCliente(listaExperiencias, clienteModel.getDni());
        db.consultaVentasCliente(listaVentas, clienteModel.getDni());
        db.consultaInteresesCliente(listaIntereses, clienteModel.getDni());
        root.getModel().puestaEnMarchaModelProperty().bind(listaPuestasEnMarcha);
        root.getModel().presentacionesModelsProperty().bind(listaPresentaciones);
        root.getModel().accionEspecialModelProperty().bind(listaAccionesEspeciales);
        root.getModel().experienciaModelProperty().bind(listaExperiencias);
        root.getModel().ventasModelProperty().bind(listaVentas);
        root.getModel().listaProductosProperty().bind(listaProductos);
        root.getModel().interesesModelProperty().bind(listaIntereses);

        // Listeners
        root.getAnadirProductoInteresButton().setOnAction(e -> onAnadirInteres(clienteModel.getDni()));
        root.getEliminarProductoInteresButton().setOnAction(e -> onEliminarInteres());
        root.getBusquedaComprasTextField().textProperty().addListener(e -> onBusquedaCompra(clienteModel.getDni()));

        if (listaPresentaciones.size() > 0)
            root.setFecha(listaPresentaciones.get(0).getFechaEvento().toString());
        else
            root.setFecha("Cliente aún no tiene presentación");
        initOwner(primaryStage);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK);

        getDialogPane().setContent(root);

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK)
                return root.getModel();
            return null;
        });
    }

    private void onBusquedaCompra(String codCliente) {
        listaVentas.clear();
        db.consultaVentasClienteWhere(listaVentas, root.getBusquedaComprasTextField().getText(), codCliente);
    }

    private void onEliminarInteres() {
        try {
            db.deleteInteres(root.getInteresesListView().getSelectionModel().getSelectedItem());
            listaIntereses.remove(root.getInteresesListView().getSelectionModel().getSelectedItem());
        } catch (NullPointerException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("No ha seleccionado ningun elemento para eliminar");
            alerta.setContentText("Por favor, seleccione un elemento de la lista de intereses para borrarlo");
            alerta.show();
        }
    }

    private void onAnadirInteres(String dni) {
        try {
            InteresesModel interesesModel = new InteresesModel();
            interesesModel.setArticulo(root.getProductosComboBox().getSelectionModel().getSelectedItem());
            interesesModel.setCodCliente(dni);
            db.insertInteresCliente(interesesModel);
            listaIntereses.add(interesesModel);
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se ha seleccionado un producto");
            alert.setContentText("Por favor, elija un producto del desplegable");
            alert.show();
        }
    }
}
