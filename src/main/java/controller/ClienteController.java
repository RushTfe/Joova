package controller;

import NuevoCliente.NuevoClienteController;
import NuevoCliente.NuevoClienteModel;
import database.HooverDataBase;
import dialogs.DialogoNuevoCliente;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import model.ClienteModel;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private Button buscarClienteButton;


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

        // Preparación de la tabla
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

        //Bindeo de la tabla a la lista de objetos
        clientTable.itemsProperty().bind(listaClientes);

        actualizarClientes();

        // Botones
        anadirClienteButton.setOnAction(e -> onAnadirClienteButton());
        eliminarClienteButton.setOnAction(e -> onEliminarClienteAction());

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
