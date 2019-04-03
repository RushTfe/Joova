package controller;

import Joova.ProductoCardModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import model.ClienteModel;
import model.VentasModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VentasController implements Initializable {

    @FXML
    private BorderPane rootVentas;

    @FXML
    private ComboBox<ClienteModel> clientesComboBox;

    @FXML
    private TextField contratoTextField;

    @FXML
    private DatePicker fechaDatePicker;

    @FXML
    private TextArea observacionesTextArea;

    @FXML
    private Button anadirProductoButton;

    @FXML
    private Button eliminarProductoButton;

    @FXML
    private Button anadirDescuentoButton;

    @FXML
    private Button validarVentaButton;

    @FXML
    private ListView<ProductoCardModel> productosListView;

    @FXML
    private Label totalLabel;

    @FXML
    private Label precioTotalLabel;

    private VentasModel model;

    public VentasController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/VentasView.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        model = new VentasModel();

        // Bindings
        precioTotalLabel.textProperty().bind(model.precioAsString());
    }

    public BorderPane getRootVentas() {
        return rootVentas;
    }

    public ComboBox<ClienteModel> getClientesComboBox() {
        return clientesComboBox;
    }

    public VentasModel getModel() {
        return model;
    }
}
