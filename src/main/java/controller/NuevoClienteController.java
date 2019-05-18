package controller;

import NuevoCliente.NuevoClienteModel;
import javafx.beans.property.ListProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.ClienteModel;
import model.NuevoProductoModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NuevoClienteController implements Initializable {
    private ClienteModel model;
    ListProperty<NuevoProductoModel> listaProductos;

    @FXML
    private GridPane rootClienteNuevo;

    @FXML
    private TextField nombreField;

    @FXML
    private TextField apellidoField;

    @FXML
    private TextField dniField;

    @FXML
    private TextField tlfField;

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker nacimientoField;

    @FXML
    private TextField direccionField;

    @FXML
    private CheckBox huefanoBox;

    @FXML
    private ComboBox<NuevoProductoModel> productosCombobox;

    @FXML
    private TextArea obervacionesBox;

    public NuevoClienteController(ListProperty<NuevoProductoModel> listaProductos) {
        this.listaProductos = listaProductos;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NuevoClienteView.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {model = new ClienteModel();

        nombreField.textProperty().bindBidirectional(model.nombreProperty());
        apellidoField.textProperty().bindBidirectional(model.apellidosProperty());
        dniField.textProperty().bindBidirectional(model.dniProperty());
        tlfField.textProperty().bindBidirectional(model.telefonoProperty());
        emailField.textProperty().bindBidirectional(model.emailProperty());
        nacimientoField.valueProperty().bindBidirectional(model.fechaNacimientoProperty());
        direccionField.textProperty().bindBidirectional(model.direccionProperty());
        huefanoBox.selectedProperty().bindBidirectional(model.huerfanoProperty());
        productosCombobox.itemsProperty().bindBidirectional(listaProductos);
        model.modeloAspiradoraProperty().bind(productosCombobox.getSelectionModel().selectedItemProperty());
        obervacionesBox.textProperty().bindBidirectional(model.observacionesProperty());

        nacimientoField.setEditable(false);
        obervacionesBox.setWrapText(true);
    }

    public GridPane getRootClienteNuevo() {
        return rootClienteNuevo;
    }

    public ClienteModel getModel() {
        return model;
    }

    public TextField getDniField() {
        return dniField;
    }

    public ComboBox<NuevoProductoModel> getProductosCombobox() {
        return productosCombobox;
    }
}
