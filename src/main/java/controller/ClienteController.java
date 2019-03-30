package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import model.ClienteModel;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ClienteController implements Initializable {

    @FXML
    private BorderPane rootClientes;

    @FXML
    private TableView<ClienteModel> ClientTable;

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
    private TableColumn<ClienteModel, Boolean> huerfanoColumn;

    @FXML
    private Button anadirClienteButton;

    @FXML
    private Button eliminarClienteButton;

    @FXML
    private Button modificarClienteButton;

    @FXML
    private Button buscarClienteButton;


    public void ClienteController() {
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
    }

    public BorderPane getRootClientes() {
        return rootClientes;
    }
}
