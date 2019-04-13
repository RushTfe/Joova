package controller;

import database.HooverDataBase;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import model.ClienteModel;
import model.TipoPagoModel;
import model.VentasModel;
import nuevoproducto.NuevoProductoModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VentasController implements Initializable {
    // Base de datos
    private HooverDataBase db;

    // Lista de productos disponibles
    private ListProperty<NuevoProductoModel> listaProductosDisponibles;

    // Productos actualmente añadidos
    private ListProperty<NuevoProductoModel> listaProductosAnadidos;

    @FXML
    private ComboBox<TipoPagoModel> tipoPagoComboBox;

    @FXML
    private Button anadirTipoPago;

    @FXML
    private BorderPane rootVentas;

    @FXML
    private CheckBox mostrarProductosCB;

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
    private TableView<NuevoProductoModel> productosTable;

    @FXML
    private TableColumn<NuevoProductoModel, String> nombreColumn;

    @FXML
    private TableColumn<NuevoProductoModel, String> precioColumn;

    @FXML
    private TableView<NuevoProductoModel> listaProductosTable;

    @FXML
    private TableColumn<NuevoProductoModel, String> nombreEnListaProductos;

    @FXML
    private Label totalLabel;

    @FXML
    private Label precioTotalLabel;

    private VentasModel model;

    public VentasController(HooverDataBase db) {
        this.db = db;
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
        listaProductosDisponibles = new SimpleListProperty<>(this, "listaProductosDisponibles", FXCollections.observableArrayList());
        listaProductosAnadidos = new SimpleListProperty<>(this, "listaProductosAnadidos", FXCollections.observableArrayList());

        // Al combobox se le asignan los datos directamente desde el controlador principal, para así evitar estar creando mas
        // duplicidades de datos innecesarias.

        // Bindings
        model.codContratoProperty().bindBidirectional(contratoTextField.textProperty());
        model.fechaVentaProperty().bindBidirectional(fechaDatePicker.valueProperty());
        model.observacionesVentaProperty().bindBidirectional(observacionesTextArea.textProperty());
        precioTotalLabel.textProperty().bind(model.precioAsString());
        model.mostrarProductosProperty().bindBidirectional(mostrarProductosCB.selectedProperty());

        // Bindeos para la vista de la segunda tabla
        listaProductosTable.visibleProperty().bind(model.mostrarProductosProperty());
        listaProductosTable.managedProperty().bind(model.mostrarProductosProperty());
        listaProductosTable.prefWidthProperty().bind(Bindings.when(model.mostrarProductosProperty().not()).then(0).otherwise(200));
        listaProductosTable.maxWidthProperty().bind(Bindings.when(model.mostrarProductosProperty().not()).then(0).otherwise(200));
        listaProductosTable.minWidthProperty().bind(Bindings.when(model.mostrarProductosProperty().not()).then(0).otherwise(200));

        //Valor de la celda de la tabla de productos a elegir.
        nombreEnListaProductos.setCellValueFactory(v -> v.getValue().nombreProductoProperty().concat("     ").concat(v.getValue().getPrecioProducto()).concat("€"));

        //Valor de las celdas de la tabla de productos añadidos
        nombreColumn.setCellValueFactory(v -> v.getValue().nombreProductoProperty());
        precioColumn.setCellValueFactory(v -> v.getValue().precioAsString());



        //Bindeamos la tabla con la lista de productos AÑADIDOS OJO no a la de todos los productos.
        // En esta segunda lista pueden haber productos repetidos las veces que sean necesarias.
        productosTable.itemsProperty().bindBidirectional(listaProductosAnadidosProperty());
        listaProductosTable.itemsProperty().bindBidirectional(listaProductosDisponiblesProperty());

        listaProductosAnadidosProperty().addListener((ListChangeListener<NuevoProductoModel>) v -> onCambioListaProductos());

        // Botones
        anadirProductoButton.setOnAction(e -> onAnadirProductoAction());
        eliminarProductoButton.setOnAction(e -> onEliminarProductoAction());
        validarVentaButton.setOnAction(e -> onValidarVentaAction());
    }

    private void onValidarVentaAction() {
        // Se inserta la compra en la Base de Datos
        db.insertCompra(model);

        // Y se recorre la tabla de productos añadidos, para irlos metiendo uno a uno en el detalle de la compra.
        for (int i = 0; i < listaProductosAnadidos.size(); i++) {
            db.insertDetalleCompra(model.getCodContrato(), listaProductosAnadidos.get(i).getCodArticulo());
        }

    }

    private void onEliminarProductoAction() {
        getListaProductosAnadidos().remove(productosTable.getSelectionModel().getSelectedItem());
    }

    private void onAnadirProductoAction() {

        NuevoProductoModel producto = listaProductosTable.getSelectionModel().getSelectedItem();
        getListaProductosAnadidos().add(producto);
    }

    public void onCambioListaProductos() {
        if (getListaProductosAnadidos().size() == 0)
            model.setPrecioTotal(0);
        else {
            model.setPrecioTotal(0);
            for (int i = 0; i < getListaProductosAnadidos().size(); i++) {
                model.setPrecioTotal(model.getPrecioTotal() + getListaProductosAnadidos().get(i).getPrecioProducto());
            }
        }
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

    public ObservableList<NuevoProductoModel> getListaProductosDisponibles() {
        return listaProductosDisponibles.get();
    }

    public ListProperty<NuevoProductoModel> listaProductosDisponiblesProperty() {
        return listaProductosDisponibles;
    }

    public void setListaProductosDisponibles(ObservableList<NuevoProductoModel> listaProductosDisponibles) {
        this.listaProductosDisponibles.set(listaProductosDisponibles);
    }

    public ObservableList<NuevoProductoModel> getListaProductosAnadidos() {
        return listaProductosAnadidos.get();
    }

    public ListProperty<NuevoProductoModel> listaProductosAnadidosProperty() {
        return listaProductosAnadidos;
    }

    public void setListaProductosAnadidos(ObservableList<NuevoProductoModel> listaProductosAnadidos) {
        this.listaProductosAnadidos.set(listaProductosAnadidos);
    }
}
