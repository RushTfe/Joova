package controller;

import app.JoovaApp;
import database.HooverDataBase;
import dialogs.DialogoNuevaPyPM;
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
import model.PMyPresentacionesModel;

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
    private TableView<PMyPresentacionesModel> tablaPresentaciones;
    private TableColumn<PMyPresentacionesModel, String> columnaNombre;
    private TableColumn<PMyPresentacionesModel, String> columnaDireccion;
    private TableColumn<PMyPresentacionesModel, LocalDate> columnaFecha;
    private TableColumn<PMyPresentacionesModel, Boolean> columnaVenta;
    private ListProperty<PMyPresentacionesModel> listaPresentaciones;

    /**************
     * Tabla de Puestas en Marcha
     **************/
    private TableView<PMyPresentacionesModel> tablaPuestaMarcha;
    private TableColumn<PMyPresentacionesModel, String> codClientePuestaMarchaColumn;
    private TableColumn<PMyPresentacionesModel, String> nombreClientePuestaMarchaColumn;
    private TableColumn<PMyPresentacionesModel, LocalDate> fechaPuestaMarchaColumn;
    private ListProperty<PMyPresentacionesModel> listaPuestasMarcha;


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
        columnaFecha.setCellValueFactory(v -> v.getValue().fechaEventoProperty());
        columnaFecha.setText("Fecha");
        columnaVenta.setCellValueFactory(v -> v.getValue().ventaRealizadaProperty());
        columnaVenta.setText("Venta");


        /*************************************************************
         *
         * Inicializaciones de puestas en marcha
         *
         *************************************************************/

        //Tabla
        tablaPuestaMarcha = new TableView<>();
        codClientePuestaMarchaColumn = new TableColumn<>();
        nombreClientePuestaMarchaColumn = new TableColumn<>();
        fechaPuestaMarchaColumn = new TableColumn<>();
        listaPuestasMarcha = new SimpleListProperty<>(this, "listaPuestasMarcha", FXCollections.observableArrayList());

        // Ancho de columnas crecen con la tabla
        tablaPuestaMarcha.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Añadir columnas a la tabla
        tablaPuestaMarcha.getColumns().add(0, codClientePuestaMarchaColumn);
        tablaPuestaMarcha.getColumns().add(1, nombreClientePuestaMarchaColumn);
        tablaPuestaMarcha.getColumns().add(2, fechaPuestaMarchaColumn);

        //Ajustar al AnchorPane
        AnchorPane.setTopAnchor(tablaPuestaMarcha, 0d);
        AnchorPane.setBottomAnchor(tablaPuestaMarcha, 0d);
        AnchorPane.setRightAnchor(tablaPuestaMarcha, 0d);
        AnchorPane.setLeftAnchor(tablaPuestaMarcha, 0d);

        codClientePuestaMarchaColumn.setCellValueFactory(v -> v.getValue().codClienteProperty());
        codClientePuestaMarchaColumn.setText("Codigo Cliente");
        nombreClientePuestaMarchaColumn.setCellValueFactory(v -> v.getValue().nombreClienteProperty());
        nombreClientePuestaMarchaColumn.setText("Nombre Cliente");
        fechaPuestaMarchaColumn.setCellValueFactory(v -> v.getValue().fechaEventoProperty());
        fechaPuestaMarchaColumn.setText("Fecha de presentación");

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
        // Tabla Puestas en marcha
        tablaPuestaMarcha.itemsProperty().bind(listaPuestasMarcha);

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
        eliminarButton.setOnAction(e -> onEliminarAction());
        modifButton.setOnAction(e -> onModificarAction());

        actualizarDatos();

        // FIXME al cambiar de radioButton y volver, no funciona el listener
        // FIXME crea NullPointerException al eliminar un elemento de la lista
//        tablaPresentaciones.getSelectionModel().selectedItemProperty().addListener(e -> actualizarTextArea());
    }

    private void onModificarAction() {
        if (model.isPresentacion()) {
            PMyPresentacionesModel evento = tablaPresentaciones.getSelectionModel().getSelectedItem();
            ClienteModel cliente = new ClienteModel();
            cliente.setDni(evento.getCodCliente());
            DialogoNuevaPyPM dialogo = new DialogoNuevaPyPM(listaClientes);
            dialogo.setTitle("Modificar presentacion");
            dialogo.getObservaciones().setText(evento.getObservaciones());
            dialogo.getClientesComboBox().getSelectionModel().select(cliente);
            dialogo.getFecha().setValue(evento.getFechaEvento());
            dialogo.getVenta().setSelected(evento.isVentaRealizada());

            Optional<PMyPresentacionesModel> resul = dialogo.showAndWait();
            if (resul.isPresent()) {
                resul.get().setCodigoEvento(evento.getCodigoEvento());
                db.updatePresentacion(resul.get());
                actualizarDatos();
            }
        } else if (model.isPuestaEnMarcha()) {
            //FIXME Se paraliza al pulsar sobre la tabla hasta pulsar en otro sitio.
            ClienteModel cliente = new ClienteModel();
            PMyPresentacionesModel evento = tablaPuestaMarcha.getSelectionModel().getSelectedItem();
            cliente.setDni(evento.getCodCliente());
            DialogoNuevaPyPM dialogo = new DialogoNuevaPyPM(listaClientes);
            dialogo.setTitle("Modificar Puesta en Marcha");
            dialogo.getObservaciones().setText(evento.getObservaciones());
            dialogo.getClientesComboBox().getSelectionModel().select(cliente);
            dialogo.getFecha().setValue(evento.getFechaEvento());
            dialogo.getVenta().setSelected(evento.isVentaRealizada());

            Optional<PMyPresentacionesModel> resul = dialogo.showAndWait();
            if (resul.isPresent()) {
                resul.get().setCodigoEvento(evento.getCodigoEvento());
                db.updatePuestaMarcha(resul.get());
                actualizarDatos();
            }

        }
    }

    private void onEliminarAction() {
        PMyPresentacionesModel aBorrar;
        if (model.isPresentacion()) {
            aBorrar = tablaPresentaciones.getSelectionModel().getSelectedItem();
            String datos = "Código del evento: " + aBorrar.getCodigoEvento() + " DNI: " + aBorrar.getCodCliente() + " Nombre: " + aBorrar.getNombreCliente();
            Optional<ButtonType> resul = alerta("Borrando datos...", "Atención, la siguiente información se borrará de manera permanente", datos);

            if (resul.isPresent() && resul.get() == ButtonType.OK) {
                db.deletePresentacion(aBorrar.getCodigoEvento());
                listaPresentaciones.remove(aBorrar);
            }
        } else if (model.isPuestaEnMarcha()) {
            aBorrar = tablaPuestaMarcha.getSelectionModel().getSelectedItem();
            String datos = "Código del evento: " + aBorrar.getCodigoEvento() + " DNI: " + aBorrar.getCodCliente() + " Nombre: " + aBorrar.getNombreCliente();
            Optional<ButtonType> resul = alerta("Borrando datos...", "Atención, la siguiente información se borrará de manera permanente", datos);
            if (resul.isPresent() && resul.get() == ButtonType.OK) {
                db.deletePuestaMarcha(aBorrar.getCodigoEvento());
                listaPuestasMarcha.remove(aBorrar);
            }
        }
    }

    private void actualizarTextArea() {
        if (model.isPresentacion())
            model.setObservaciones(tablaPresentaciones.getSelectionModel().getSelectedItem().getObservaciones());
    }

    private void onNuevoAction() {
        DialogoNuevaPyPM dialogo;
        if (model.isPresentacion()) {
            dialogo = new DialogoNuevaPyPM(listaClientes);
            Optional<PMyPresentacionesModel> resul = dialogo.showAndWait();
            if (resul.isPresent()) {
                db.insertPresentacion(resul.get());
                actualizarDatos();
            }

        } else if (model.isPuestaEnMarcha()) {
            dialogo = new DialogoNuevaPyPM(listaClientes);
            dialogo.setTitle("Añadir Puesta en Marcha");
            dialogo.getVenta().setVisible(false);
            Optional<PMyPresentacionesModel> resul = dialogo.showAndWait();
            if (resul.isPresent()) {
                db.insertPuestaEnMarcha(resul.get());
                actualizarDatos();
            }

        }
    }

    private void onBuscarAction() {
    }

    private void actualizarDatos() {
        //Actualizar presentaciones
            listaPresentaciones.clear();
            db.consultaTodasPresentaciones(listaPresentaciones);
        //Actualizar puestas en marcha
            listaPuestasMarcha.clear();
            db.consultaTodasPuestasMarcha(listaPuestasMarcha);
    }

    private void onExpChanged() {
        if (model.isPresentacion()) {
            setContent(tablaPresentaciones);
        } else if (model.isPuestaEnMarcha())
            setContent(tablaPuestaMarcha);
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

    private Optional<ButtonType> alerta(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initOwner(JoovaApp.getPrimaryStage());
        return alert.showAndWait();
    }
}
