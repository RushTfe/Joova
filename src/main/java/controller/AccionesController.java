package controller;

import database.HooverDataBase;
import dialogs.DialogoNuevaPresentacion;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.AccionesModel;
import model.ClienteModel;
import model.PresentacionesModel;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class AccionesController implements Initializable {
    /**************
     * Listado de clientes, bindeado desde el MainController
     **************/
    private ListProperty<ClienteModel> listaClientes;

    // Base de datos
    private HooverDataBase db;

    /**************
     * Tabla de presentaciones
     **************/
    private TableView<PresentacionesModel> tablaPresentaciones;
    private TableColumn<PresentacionesModel, String> columnaNombre;
    private TableColumn<PresentacionesModel, String> columnaDireccion;
    private TableColumn<PresentacionesModel, LocalDate> columnaFecha;
    private TableColumn<PresentacionesModel, Boolean> columnaVenta;
    private ListProperty<PresentacionesModel> listaPresentaciones;


    @FXML
    private BorderPane rootAcciones;

    @FXML
    private HBox radioButtonBox;

    @FXML
    private RadioButton presentacionRB;

    @FXML
    private RadioButton puestaEnMarchaRB;

    @FXML
    private RadioButton experienciasRB;

    @FXML
    private RadioButton accionesEspecialesRB;

    @FXML
    private HBox botoneraHbox;

    @FXML
    private Button nuevoButton;

    @FXML
    private Button modifButton;

    @FXML
    private Button eliminarButton;

    @FXML
    private Button buscarButton;

    @FXML
    private SplitPane contentSplitPane;

    @FXML
    private AnchorPane contentAnchorPane;

    @FXML
    private TextArea observacionesTextArea;


    //Modelo
    private AccionesModel model;

    public AccionesController(HooverDataBase db) {
        this.db = db;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AccionesView.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Modelo
        model = new AccionesModel();
        listaClientes = new SimpleListProperty<>(this, "listaClientes", FXCollections.observableArrayList());


        /*************************************************************
         *
         * Inicializaciones de presentaciones
         *
         *************************************************************/

        // Tabla
        tablaPresentaciones = new TableView<>();
        columnaDireccion = new TableColumn<>();
        columnaFecha = new TableColumn<>();
        columnaNombre = new TableColumn<>();
        columnaVenta = new TableColumn<>();
        listaPresentaciones = new SimpleListProperty<>(this, "listaPresentaciones", FXCollections.observableArrayList());

        // Añadir las columnas
        tablaPresentaciones.getColumns().add(0, columnaNombre);
        tablaPresentaciones.getColumns().add(1, columnaDireccion);
        tablaPresentaciones.getColumns().add(2, columnaFecha);
        tablaPresentaciones.getColumns().add(3, columnaVenta);

        // Columnas crecen junto al tamaño de la tabla
        tablaPresentaciones.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Tamaño de la tabla
        AnchorPane.setTopAnchor(tablaPresentaciones, 0d);
        AnchorPane.setBottomAnchor(tablaPresentaciones, 0d);
        AnchorPane.setLeftAnchor(tablaPresentaciones, 0d);
        AnchorPane.setRightAnchor(tablaPresentaciones, 0d);

        // Valores de las columnas
        columnaNombre.setCellValueFactory(v -> v.getValue().nombreClienteProperty());
        columnaNombre.setText("Nombre");
        columnaDireccion.setCellValueFactory(v -> v.getValue().direccionClienteProperty());
        columnaDireccion.setText("Direccion");
        columnaFecha.setCellValueFactory(v -> v.getValue().fechaPresentacionProperty());
        columnaFecha.setText("Fecha");
        columnaVenta.setCellValueFactory(v -> v.getValue().ventaRealizadaProperty());
        columnaVenta.setText("Venta");


        /*************************************************************
         *
         * Bindings
         *
         *************************************************************/

        // Modelo del controlador
        model.observacionesProperty().bindBidirectional(observacionesTextArea.textProperty());
        model.presentacionProperty().bind(presentacionRB.selectedProperty());
        model.puestaEnMarchaProperty().bind(puestaEnMarchaRB.selectedProperty());
        model.experienciaProperty().bind(experienciasRB.selectedProperty());
        model.accionEspecialProperty().bind(accionesEspecialesRB.selectedProperty());

        // Tabla de presentaciones
        tablaPresentaciones.itemsProperty().bind(listaPresentaciones);

        // Añadir la tabla por primera vez al controlador
        contentAnchorPane.getChildren().add(tablaPresentaciones);

        /*********************************************
         *
         * Toggle group para meter en el los radioButtons correspondientes
         *
         *********************************************/
        ToggleGroup radioButtons = new ToggleGroup();
        presentacionRB.setToggleGroup(radioButtons);
        puestaEnMarchaRB.setToggleGroup(radioButtons);
        experienciasRB.setToggleGroup(radioButtons);
        accionesEspecialesRB.setToggleGroup(radioButtons);

        /*********************************************
         *
         * Convertir observaciones en no editables
         *
         *********************************************/
        observacionesTextArea.setEditable(false);

        /*********************************************
         *
         * Listeners
         *
         *********************************************/
        radioButtons.selectedToggleProperty().addListener(e -> onExpChanged());
        buscarButton.setOnAction(e -> onBuscarAction());
        nuevoButton.setOnAction(e -> onNuevoAction());

        actualizarDatos();

        // FIXME al cambiar de radioButton y volver, no funciona el listener
        tablaPresentaciones.getSelectionModel().selectedItemProperty().addListener(e -> actualizarTextArea());
    }

    private void actualizarTextArea() {
        System.out.println(model.isPresentacion());
        if (model.isPresentacion())
            model.setObservaciones(tablaPresentaciones.getSelectionModel().getSelectedItem().getObservaciones());
    }

    private void onNuevoAction() {
        if (model.isPresentacion()) {
            DialogoNuevaPresentacion dialogo = new DialogoNuevaPresentacion(listaClientes);
            Optional<PresentacionesModel> resul = dialogo.showAndWait();
            if (resul.isPresent()) {
                db.insertPresentacion(resul.get());
                listaPresentaciones.add(resul.get());
            }
        }
    }

    private void onBuscarAction() {
    }

    private void actualizarDatos() {
        listaPresentaciones.clear();
        db.consultaTodasPresentaciones(listaPresentaciones);
    }

    private void onExpChanged() {
        if (model.isPresentacion()) {
            setContent(tablaPresentaciones);

        }
    }

    public void setContent(Node node) {
        contentAnchorPane.getChildren().clear();
        contentAnchorPane.getChildren().add(node);
    }

    public ListProperty<ClienteModel> listaClientesProperty() {
        return listaClientes;
    }

    public BorderPane getRootAcciones() {
        return rootAcciones;
    }
}
