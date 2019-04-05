package controller;

import NuevoCliente.NuevoClienteModel;
import database.HooverDataBase;
import dialogs.DialogoNuevoCliente;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import model.ClienteModel;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClienteController implements Initializable {

    private HooverDataBase db;

    private ListProperty<ClienteModel> listaClientes;

    @FXML
    private BorderPane rootClientes;

    @FXML
    private TableView<ClienteModel> clientTable;

    @FXML
    private TableColumn<ClienteModel, String> observacionesColumn;

    @FXML
    private TableColumn<ClienteModel, String> dniColumn;

    @FXML
    private TableColumn<ClienteModel, String> nombreColumn;

    @FXML
    private TableColumn<ClienteModel, String> apellidoColumn;

    @FXML
    private TableColumn<ClienteModel, String> emailColumn;

    @FXML
    private TableColumn<ClienteModel, String> tlfColumn;

    @FXML
    private TableColumn<ClienteModel, LocalDate> nacimientoColumn;

    @FXML
    private TableColumn<ClienteModel, String> direccionColumn;

    @FXML
    private TableColumn<ClienteModel, String> generoColumn;

    @FXML
    private TableColumn<ClienteModel, Boolean> huerfanoColumn;

    @FXML
    private Button anadirClienteButton;

    @FXML
    private Button eliminarClienteButton;

    @FXML
    private Button modificarClienteButton;

    @FXML
    private TextField busquedaCliente;

    private StringProperty busquedaClienteProperty;


    public ClienteController(HooverDataBase db) {
        this.db = db;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientesView.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaClientes = new SimpleListProperty<>(this, "listaClientes", FXCollections.observableArrayList());
        busquedaClienteProperty = new SimpleStringProperty(this, "busquedaClienteProperty");

        // PREPARACION DE LA TABLA
        dniColumn.setCellValueFactory(v -> v.getValue().dniProperty());
        nombreColumn.setCellValueFactory(v -> v.getValue().nombreProperty());
        apellidoColumn.setCellValueFactory(v -> v.getValue().apellidosProperty());
        tlfColumn.setCellValueFactory(v -> v.getValue().telefonoProperty());
        emailColumn.setCellValueFactory(v -> v.getValue().emailProperty());
        nacimientoColumn.setCellValueFactory(v -> v.getValue().fechaNacimientoProperty());
        direccionColumn.setCellValueFactory(v -> v.getValue().direccionProperty());
        observacionesColumn.setCellValueFactory(v -> v.getValue().observacionesProperty());
        generoColumn.setCellValueFactory(v -> v.getValue().generoProperty());
        huerfanoColumn.setCellValueFactory(v -> v.getValue().huerfanoProperty());

        //BINDEO DE LA TABLA A LA LISTA
        clientTable.itemsProperty().bind(listaClientes);

        // BINDEO DE EL PROPERTY DE LA BUSQUEDA
        busquedaClienteProperty.bind(busquedaCliente.textProperty());

        actualizarClientes();

        // BOTONES
        anadirClienteButton.setOnAction(e -> onAnadirClienteButton());
        eliminarClienteButton.setOnAction(e -> onEliminarClienteAction());
        modificarClienteButton.setOnAction(e -> onModificarAction());

        // LISTENERS
        busquedaClienteProperty.addListener(e -> onBusquedaRealizada());

    }

    private void onBusquedaRealizada() {
        listaClientes.clear();
        db.consultaClientesWhere(listaClientes, busquedaClienteProperty.get());
    }

    private void onModificarAction() {
        try {
            DialogoNuevoCliente aModificar = new DialogoNuevoCliente();
            ClienteModel clienteSeleccionado = clientTable.getSelectionModel().getSelectedItem();

            // Establecemos los valores que habran dentro del dialogo, correspondientes a la fila que marquemos de la tabla.
            aModificar.getDNIField().setDisable(true);
            aModificar.getNuevoClienteModel().setDniCliente(clienteSeleccionado.getDni());
            aModificar.getNuevoClienteModel().setNombreCliente(clienteSeleccionado.getNombre());
            aModificar.getNuevoClienteModel().setApellidosCliente(clienteSeleccionado.getApellidos());
            aModificar.getNuevoClienteModel().setTelefonoCliente(clienteSeleccionado.getTelefono());
            aModificar.getNuevoClienteModel().setMailCliente(clienteSeleccionado.getEmail());
            aModificar.getNuevoClienteModel().setNacimientoCliente(clienteSeleccionado.getFechaNacimiento());
            aModificar.getNuevoClienteModel().setDireccion(clienteSeleccionado.getDireccion());
            aModificar.getNuevoClienteModel().setObservacionesCliente(clienteSeleccionado.getObservaciones());
            aModificar.getNuevoClienteModel().setClienteHuefano(clienteSeleccionado.isHuerfano());

            // Trabajamos directamente sobre la vista porque esta el bindeo establecido desde el otro proyecto. Se podr;ia desbindear y volver a bindear, pero veo esto mas comodo.
            if ("Mujer".equals(clienteSeleccionado.getGenero()))
                aModificar.getController().getMujerRadioButton().setSelected(true);
            else if ("Hombre".equals(clienteSeleccionado.getGenero()))
                aModificar.getController().getHombreRadioButton().setSelected(true);
            else
                aModificar.getController().getOtrosRadioButton().setSelected(true);

            Optional<NuevoClienteModel> resul = aModificar.showAndWait();

            if (resul.isPresent()) {
                db.updateClient(resul.get());
                actualizarClientes();
            }
        } catch (NullPointerException e) {
            MainController.alertaError("No se ha podido modificar", "No ha seleccionado ninguna celda de la tabla", "Por favor, seleccione arriba al cliente que desea modificar");
        }

    }

    private void onEliminarClienteAction() {

        try {
            ClienteModel aEliminar = clientTable.getSelectionModel().getSelectedItem();

            Optional<ButtonType> result = MainController.alertaConfirmation("ATENCIÓN", "el siguiente cliente será eliminado", aEliminar.getNombre() + " " + aEliminar.getApellidos());

            if(result.isPresent() && !result.get().getButtonData().isCancelButton()) {
                db.deleteCliente(aEliminar.getDni());
                actualizarClientes();
            }

        } catch (NullPointerException e) {
            MainController.alertaError("No se ha podido borrar", "No ha seleccionado ninguna celda de la tabla", "Por favor, seleccione arriba al cliente que desea borrar");
        }
    }

    private void onAnadirClienteButton() {
        DialogoNuevoCliente nuevoCliente = new DialogoNuevoCliente();

        Optional<NuevoClienteModel> resul = nuevoCliente.showAndWait();

        if (resul.isPresent()) {
            db.insertClient(resul.get());
            actualizarClientes();
        }

    }

    public void actualizarClientes() {
        listaClientes.clear();
        db.consultaTodosClientes(listaClientes);
    }

    public ListProperty<ClienteModel> listaClientesProperty() {
        return listaClientes;
    }

    public BorderPane getRootClientes() {
        return rootClientes;
    }
}
