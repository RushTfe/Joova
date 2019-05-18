package controller;

import app.JoovaApp;
import database.HooverDataBase;
import dialogs.DialogoNuevoTipoPagoyEvento;
import dialogs.JoovaAlert;
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
import model.NuevoProductoModel;
import model.TipoPagoyEventoModel;
import model.VentasModel;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class VentasController implements Initializable {
    // Base de datos
    private HooverDataBase db;

    // Lista de productos disponibles
    private ListProperty<NuevoProductoModel> listaProductosDisponibles;

    // Productos actualmente añadidos
    private ListProperty<NuevoProductoModel> listaProductosAnadidos;

    // Lista de tipos de pago
    private ListProperty<TipoPagoyEventoModel> listaTipoPago;

    @FXML
    private ComboBox<TipoPagoyEventoModel> tipoPagoComboBox;

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
        // TODO Escondemos el botón de descuentos hasta que se apliquen realmente en la APP
        anadirDescuentoButton.setVisible(false);
        anadirDescuentoButton.setManaged(false);
        observacionesTextArea.setWrapText(true);
        model = new VentasModel();
        listaProductosDisponibles = new SimpleListProperty<>(this, "listaProductosDisponibles", FXCollections.observableArrayList());
        listaProductosAnadidos = new SimpleListProperty<>(this, "listaProductosAnadidos", FXCollections.observableArrayList());
        listaTipoPago = new SimpleListProperty<>(this, "listaTipoPago", FXCollections.observableArrayList());
        fechaDatePicker.setEditable(false);
        // Al combobox se le asignan los datos directamente desde el controlador principal, para así evitar estar creando mas
        // duplicidades de datos innecesarias.

        // Bindings
        model.codContratoProperty().bindBidirectional(contratoTextField.textProperty());
        model.fechaVentaProperty().bindBidirectional(fechaDatePicker.valueProperty());
        model.observacionesVentaProperty().bindBidirectional(observacionesTextArea.textProperty());
        precioTotalLabel.textProperty().bind(model.precioAsString());
        model.mostrarProductosProperty().bindBidirectional(mostrarProductosCB.selectedProperty());
        tipoPagoComboBox.itemsProperty().bind(listaTipoPago);
        model.tipoPagoProperty().bind(tipoPagoComboBox.getSelectionModel().selectedItemProperty());

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


        // Listeners
        listaProductosAnadidosProperty().addListener((ListChangeListener<NuevoProductoModel>) v -> onCambioListaProductos());
        clientesComboBox.getSelectionModel().selectedItemProperty().addListener(e -> {
            model.setCliente(clientesComboBox.getSelectionModel().getSelectedItem().getDni());
        });

        // Comportamiento botón de validar venta
        validarVentaButton.disableProperty().bind(Bindings
                .when(model.tipoPagoProperty().isNull()
                        .or(model.clienteProperty().isEmpty()
                                .or(model.codContratoProperty().isEmpty()
                                        .or(model.fechaVentaProperty().isNull()
                                                .or(listaProductosAnadidos.emptyProperty())))))
                .then(true)
                .otherwise(false));

        // Rellenar la lista de tipos de pago
        actualizarTiposDePago();

        // Botones
        anadirProductoButton.setOnAction(e -> onAnadirProductoAction());
        eliminarProductoButton.setOnAction(e -> onEliminarProductoAction());
        validarVentaButton.setOnAction(e -> onValidarVentaAction());
        anadirTipoPago.setOnAction(e -> onAnadirTipoPagoAction());
    }

    private void onAnadirTipoPagoAction() {
        TipoPagoyEventoModel tipoPago = new TipoPagoyEventoModel();
        DialogoNuevoTipoPagoyEvento dialogo = new DialogoNuevoTipoPagoyEvento(JoovaApp.getPrimaryStage());
        Optional<TipoPagoyEventoModel> resul = dialogo.showAndWait();

        if (resul.isPresent()) {
            tipoPago = resul.get();
            db.insertTipoPago(tipoPago.getNombreTipoPago(), tipoPago.getDescripcionTipoPago());
            listaTipoPago.add(tipoPago);
        }
    }

    private void actualizarTiposDePago() {
        listaTipoPago.clear();
        db.consultaTodosTiposPago(listaTipoPago);
    }

    private void onValidarVentaAction() {
        Optional<ButtonType> resul = JoovaAlert.alertConf("Está a punto de validar una venta", "La venta se añadirá a la base de datos", "Esta acción no se puede deshacer, asegúrese de que los datos sean correctos");

        if (resul.get().getButtonData().isCancelButton()) {
        } else {
            // Se inserta la compra en la Base de Datos
            db.insertCompra(model);
            // Y se recorre la tabla de productos añadidos, para irlos metiendo uno a uno en el detalle de la compra.
            for (int i = 0; i < listaProductosAnadidos.size(); i++) {
                db.insertDetalleCompra(model.getCodContrato(), listaProductosAnadidos.get(i).getCodArticulo());
            }
            limpiar();
        }
    }

    private void limpiar() {
        listaProductosAnadidos.clear();
        model.setFechaVenta(LocalDate.now());
        model.setObservacionesVenta("");
        model.setCodContrato("");

    }

    private void onEliminarProductoAction() {
        if (null != productosTable.getSelectionModel().getSelectedItem()) {
            getListaProductosAnadidos().remove(productosTable.getSelectionModel().getSelectedItem());
        } else {
            JoovaAlert.alertError("Error",
                    "No se ha seleccionado ningun artículo para borrar",
                    "Pulse sobre un artículo sonre su lista de añadidos a la compra, y luego pulse sobre este botón");
        }
    }

    private void onAnadirProductoAction() {
        if (null != listaProductosTable.getSelectionModel().getSelectedItem()) {
            NuevoProductoModel producto = listaProductosTable.getSelectionModel().getSelectedItem();
            getListaProductosAnadidos().add(producto);
        } else {
            JoovaAlert.alertError("Error",
                    "No se ha seleccionado ningún artículo de la lista",
                    "Para seleccionar un artículo, pulse sobre \"Mostrar lista de Productos\" y seleccione el que desee añadir a la compra");
        }
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
