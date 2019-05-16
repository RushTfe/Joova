package controller;

import app.JoovaApp;
import database.HooverDataBase;
import dialogs.DialogoNuevoCliente;
import dialogs.DialogoReporteCliente;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import model.ClienteModel;
import nuevoproducto.NuevoProductoModel;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClienteController implements Initializable {

    private HooverDataBase db;

    private ListProperty<ClienteModel> listaClientes;

    private ListProperty<NuevoProductoModel> listaProductos;

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
    private TableColumn<ClienteModel, NuevoProductoModel> aspiradoraColumn;

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
        listaProductos = new SimpleListProperty<>(this, "listaProductos", FXCollections.observableArrayList());
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
        aspiradoraColumn.setCellValueFactory(v -> v.getValue().modeloAspiradoraProperty());
        huerfanoColumn.setCellValueFactory(v -> v.getValue().huerfanoProperty());
        clientTable.setEditable(true);

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

        clientTable.setRowFactory(tv -> {
            TableRow<ClienteModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    onDoubleClickedClient(row);
                }
            });
            return  row;
        });

    }

    private void onDoubleClickedClient(TableRow<ClienteModel> row) {
        //TODO Cambiar cuando tenga lista la vista
        ClienteModel cliente = clientTable.getSelectionModel().getSelectedItem();
        DialogoReporteCliente dialogoReporteCliente = new DialogoReporteCliente(JoovaApp.getPrimaryStage(), db, cliente, listaProductos);
        dialogoReporteCliente.show();
    }

    private void onBusquedaRealizada() {
        listaClientes.clear();
        db.consultaClientesWhere(listaClientes, busquedaClienteProperty.get());
    }

    private void onModificarAction() {
        try {
            DialogoNuevoCliente aModificar = new DialogoNuevoCliente(JoovaApp.getPrimaryStage(), listaProductos);
            ClienteModel clienteSeleccionado = clientTable.getSelectionModel().getSelectedItem();
            aModificar.setTitle("Modificando a " + clienteSeleccionado.getNombre() + " " + clienteSeleccionado.getApellidos());

            // Establecemos los valores que habran dentro del dialogo, correspondientes a la fila que marquemos de la tabla.
            aModificar.getDNIField().setDisable(true);
            aModificar.getModel().setDni(clienteSeleccionado.getDni());
            aModificar.getModel().setNombre(clienteSeleccionado.getNombre());
            aModificar.getModel().setApellidos(clienteSeleccionado.getApellidos());
            aModificar.getModel().setTelefono(clienteSeleccionado.getTelefono());
            aModificar.getModel().setEmail(clienteSeleccionado.getEmail());
            aModificar.getModel().setFechaNacimiento(clienteSeleccionado.getFechaNacimiento());
            aModificar.getModel().setDireccion(clienteSeleccionado.getDireccion());
            aModificar.getModel().setObservaciones(clienteSeleccionado.getObservaciones());
            aModificar.getProductosComboBox().getSelectionModel().select(clienteSeleccionado.getModeloAspiradora());
            aModificar.getModel().setHuerfano(clienteSeleccionado.isHuerfano());

            Optional<ClienteModel> resul = aModificar.showAndWait();
            if (resul.isPresent()) {
                db.updateClient(resul.get());
                actualizarClientes();
            }
        } catch (NullPointerException e) {
            MainController.alertaError("No se ha podido modificar", "No ha seleccionado ningun cliente en la tabla", "Por favor, seleccione al cliente que desea modificar");
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
            MainController.alertaError("No se ha podido borrar", "No ha seleccionado ningun cliente en la tabla", "Por favor, seleccione al cliente que desea borrar");
        }
    }

    private void onAnadirClienteButton() {
        DialogoNuevoCliente nuevoCliente = new DialogoNuevoCliente(JoovaApp.getPrimaryStage(), listaProductos);

        Optional<ClienteModel> resul = nuevoCliente.showAndWait();
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

    public ListProperty<NuevoProductoModel> listaProductosProperty() {
        return listaProductos;
    }

    public BorderPane getRootClientes() {
        return rootClientes;
    }
}
