package controller;

import app.JoovaApp;
import database.HooverDataBase;
import dialogs.DialogoNuevaAccionEspecial;
import dialogs.DialogoNuevaExperiencia;
import dialogs.DialogoNuevaPyPM;
import dialogs.JoovaAlert;
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
import model.*;

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

    /***************
     * Tabla de Experiencias
     ***************/
    private TableView<ExperienciaModel> tablaExperiencias;
    private TableColumn<ExperienciaModel, String> direccionExperienciaColumn;
    private TableColumn<ExperienciaModel, LocalDate> fechaExperienciaColumn;
    private ListProperty<ExperienciaModel> listaExperiencias;

    /****************
     * Tabla Acciones Especiales
     ****************/
    private TableView<AccionEspecialModel> tablaAccionesEspeciales;
    private TableColumn<AccionEspecialModel, String> nombreAccionColumn;
    private TableColumn<AccionEspecialModel, LocalDate> fechaAccionColumn;
    private TableColumn<AccionEspecialModel, TipoPagoyEventoModel> tipoAccionColumn;
    private TableColumn<AccionEspecialModel, String> direccionAccionColumn;
    private ListProperty<AccionEspecialModel> listaAccionesEspeciales;


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
         * Inicializacion experiencias
         *
         *************************************************************/

        tablaExperiencias = new TableView<>();
        direccionExperienciaColumn = new TableColumn<>();
        fechaExperienciaColumn = new TableColumn<>();
        listaExperiencias = new SimpleListProperty<>(this, "listaExperiencias", FXCollections.observableArrayList());

        tablaExperiencias.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tablaExperiencias.getColumns().add(direccionExperienciaColumn);
        tablaExperiencias.getColumns().add(fechaExperienciaColumn);

        direccionExperienciaColumn.setCellValueFactory(v -> v.getValue().direccionProperty());
        direccionExperienciaColumn.setText("Direccion");
        fechaExperienciaColumn.setCellValueFactory(v -> v.getValue().fechaExperienciaProperty());
        fechaExperienciaColumn.setText("Fecha");

        AnchorPane.setTopAnchor(tablaExperiencias, 0d);
        AnchorPane.setBottomAnchor(tablaExperiencias, 0d);
        AnchorPane.setLeftAnchor(tablaExperiencias, 0d);
        AnchorPane.setRightAnchor(tablaExperiencias, 0d);

        /*************************************************************
         *
         * Inicializacion Acciones Especiales
         *
         *************************************************************/
        tablaAccionesEspeciales = new TableView<>();
        nombreAccionColumn = new TableColumn<>();
        fechaAccionColumn = new TableColumn<>();
        tipoAccionColumn = new TableColumn<>();
        direccionAccionColumn = new TableColumn<>();
        listaAccionesEspeciales = new SimpleListProperty<>(this, "listaAccionesEspeciales", FXCollections.observableArrayList());

        tablaAccionesEspeciales.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tablaAccionesEspeciales.getColumns().add(nombreAccionColumn);
        tablaAccionesEspeciales.getColumns().add(fechaAccionColumn);
        tablaAccionesEspeciales.getColumns().add(tipoAccionColumn);
        tablaAccionesEspeciales.getColumns().add(direccionAccionColumn);

        nombreAccionColumn.setCellValueFactory(v -> v.getValue().nombreEventoProperty());
        nombreAccionColumn.setText("Nombre del evento");
        fechaAccionColumn.setCellValueFactory(v -> v.getValue().fechaEventoProperty());
        fechaAccionColumn.setText("Fecha");
        tipoAccionColumn.setCellValueFactory(v -> v.getValue().tipoEventoProperty());
        tipoAccionColumn.setText("Tipo de acción");
        direccionAccionColumn.setCellValueFactory(v -> v.getValue().direccionEventoProperty());
        direccionAccionColumn.setText("Dirección");

        AnchorPane.setTopAnchor(tablaAccionesEspeciales, 0d);
        AnchorPane.setBottomAnchor(tablaAccionesEspeciales, 0d);
        AnchorPane.setRightAnchor(tablaAccionesEspeciales, 0d);
        AnchorPane.setLeftAnchor(tablaAccionesEspeciales, 0d);

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
        // Tabla de Puestas en marcha
        tablaPuestaMarcha.itemsProperty().bind(listaPuestasMarcha);
        // Tabla de Experiencias
        tablaExperiencias.itemsProperty().bind(listaExperiencias);
        // Tabla de Acciones Especiales
        tablaAccionesEspeciales.itemsProperty().bind(listaAccionesEspeciales);

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
        observacionesTextArea.setWrapText(true);

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
        tablaAccionesEspeciales.getSelectionModel().selectedItemProperty().addListener(e -> onAEChanged());
        tablaExperiencias.getSelectionModel().selectedItemProperty().addListener(e -> onExpeChanged());
        tablaPuestaMarcha.getSelectionModel().selectedItemProperty().addListener(e -> onPMChanged());
        tablaPresentaciones.getSelectionModel().selectedItemProperty().addListener(e -> onPreChanged());

        actualizarDatos();

        // FIXME al cambiar de radioButton y volver, no funciona el listener
        // FIXME crea NullPointerException al eliminar un elemento de la lista
//        tablaPresentaciones.getSelectionModel().selectedItemProperty().addListener(e -> actualizarTextArea());
    }

    private void onPreChanged() {
        observacionesTextArea.setText(tablaPresentaciones.getSelectionModel().getSelectedItem().getObservaciones());
    }

    private void onPMChanged() {
        observacionesTextArea.setText(tablaPuestaMarcha.getSelectionModel().getSelectedItem().getObservaciones());
    }

    private void onExpeChanged() {
        observacionesTextArea.setText(tablaExperiencias.getSelectionModel().getSelectedItem().getObservaciones());
    }

    private void onAEChanged() {
        observacionesTextArea.setText(tablaAccionesEspeciales.getSelectionModel().getSelectedItem().getObservacionesEvento());
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

        } else if (model.isExperiencia()) {
            try {
                ExperienciaModel experiencia = tablaExperiencias.getSelectionModel().getSelectedItem();
                ListProperty<Participante> listaParticipantes = new SimpleListProperty<>(this, "listaParticipantes", FXCollections.observableArrayList());
                DialogoNuevaExperiencia experienciaAModificar = new DialogoNuevaExperiencia(listaClientes, JoovaApp.getPrimaryStage());

                experienciaAModificar.getModel().setParticipantes(db.consultaExperienciaSegunCodigo(experiencia.getCodExperiencia()));
                experienciaAModificar.getModel().setObservaciones(experiencia.getObservaciones());
                experienciaAModificar.getModel().setFechaExperiencia(experiencia.getFechaExperiencia());
                experienciaAModificar.getModel().setDireccion(experiencia.getDireccion());
                experienciaAModificar.getModel().setCodExperiencia(experiencia.getCodExperiencia());
                Optional<ExperienciaModel> resul = experienciaAModificar.showAndWait();

                if (resul.isPresent()) {
                    for (int i = 0; i < experienciaAModificar.getModel().getParticipantes().size(); i++)
                        listaParticipantes.add(experienciaAModificar.getModel().getParticipantes().get(i));

                    resul.get().setCodExperiencia(experiencia.getCodExperiencia());
                    db.updateExperiencia(resul.get());
                    db.updateExperienciaCliente(listaParticipantes, experiencia.getCodExperiencia());
                    actualizarDatos();
                }

            } catch (NullPointerException e) {
                JoovaAlert.alertError("Error", "Por favor, seleccione una experiencia de la lista para modificarla", "");
            }
        } else {
            try {
                AccionEspecialModel accionModificar = tablaAccionesEspeciales.getSelectionModel().getSelectedItem();
                DialogoNuevaAccionEspecial dialogo = new DialogoNuevaAccionEspecial(listaClientes, JoovaApp.getPrimaryStage(), db);
                ListProperty<Participante> listaParticipantes = new SimpleListProperty<>(this, "listaParticipantes", FXCollections.observableArrayList());

                dialogo.getModel().setCodEvento(accionModificar.getCodEvento());
                dialogo.getModel().setNombreEvento(accionModificar.getNombreEvento());
                dialogo.getModel().setDireccionEvento(accionModificar.getDireccionEvento());
                dialogo.getEventosCombobox().getSelectionModel().select(accionModificar.getTipoEvento());
                dialogo.getModel().setFechaEvento(accionModificar.getFechaEvento());
                dialogo.getModel().setObservacionesEvento(accionModificar.getObservacionesEvento());
                dialogo.setListaParticipantes(db.consultaAccionesEspecialesClientesCodigo(accionModificar.getCodEvento()));

                Optional<AccionEspecialModel> resul = dialogo.showAndWait();
                if (resul.isPresent()) {
                    for (int i = 0; i < dialogo.getListaParticipantes().size(); i++)
                        listaParticipantes.add(dialogo.getListaParticipantes().get(i));

                    resul.get().setCodEvento(accionModificar.getCodEvento());
                    db.updateAccionEspecial(resul.get());
                    db.updateAccionEspecialCliente(listaParticipantes, accionModificar.getCodEvento());
                    actualizarDatos();
                }

            } catch (NullPointerException e) {
                JoovaAlert.alertError("Error", "No se ha seleccionado ningún objeto", "Por favor, elija un elemento de la tabla para modificar");
            }
        }
    }

    private void onEliminarAction() {
        PMyPresentacionesModel aBorrar;
        if (model.isPresentacion()) {
            aBorrar = tablaPresentaciones.getSelectionModel().getSelectedItem();
            String datos = "Código del evento: " + aBorrar.getCodigoEvento() + "\nDNI: " + aBorrar.getCodCliente() + "\nNombre: " + aBorrar.getNombreCliente();
            Optional<ButtonType> resul = JoovaAlert.alertConf("Borrando datos...", "Atención, la siguiente información se borrará de manera permanente", datos);

            if (resul.isPresent() && resul.get() == ButtonType.OK) {
                db.deletePresentacion(aBorrar.getCodigoEvento());
                listaPresentaciones.remove(aBorrar);
            }
        } else if (model.isPuestaEnMarcha()) {
            aBorrar = tablaPuestaMarcha.getSelectionModel().getSelectedItem();
            String datos = "Código del evento: " + aBorrar.getCodigoEvento() + "\nDNI: " + aBorrar.getCodCliente() + "\nNombre: " + aBorrar.getNombreCliente();
            Optional<ButtonType> resul = JoovaAlert.alertConf("Borrando datos...", "Atención, la siguiente información se borrará de manera permanente", datos);
            if (resul.isPresent() && resul.get() == ButtonType.OK) {
                db.deletePuestaMarcha(aBorrar.getCodigoEvento());
                listaPuestasMarcha.remove(aBorrar);
            }
        } else if (model.isExperiencia()) {
            ExperienciaModel expABorrar = tablaExperiencias.getSelectionModel().getSelectedItem();
            String datos = "Dirección: " + expABorrar.getDireccion() + "\nFecha: " + expABorrar.getFechaExperiencia().toString();
            Optional<ButtonType> resul = JoovaAlert.alertConf("Borrando datos...", "Atención, la siguiente información se borrará,\nY con ella todas las participaciones del evento", datos);
            if (resul.isPresent() && resul.get() == ButtonType.OK) {
                db.deleteExperiencia(expABorrar.getCodExperiencia());
                listaExperiencias.remove(expABorrar);
            }
        } else {
            AccionEspecialModel tipoABorrar = tablaAccionesEspeciales.getSelectionModel().getSelectedItem();
            String datos = "Nombre Accion Especial: " + tipoABorrar.getNombreEvento() + "\nFecha: " + tipoABorrar.getFechaEvento().toString() + "\nDirección: " + tipoABorrar.getDireccionEvento();
            Optional<ButtonType> resul = JoovaAlert.alertConf("Borrando datos...", "Atención, la siguiente información se borrará,\nY con ella todas las participaciones del evento", datos);
            if (resul.isPresent() && resul.get() == ButtonType.OK) {
                db.deleteAccionesEspeciales(tipoABorrar.getCodEvento());
                listaAccionesEspeciales.remove(tipoABorrar);
            }
        }
    }

    private void actualizarTextArea() {
        if (model.isPresentacion())
            model.setObservaciones(tablaPresentaciones.getSelectionModel().getSelectedItem().getObservaciones());
    }

    private void onNuevoAction() {
        DialogoNuevaPyPM dialogo;
        DialogoNuevaExperiencia dialogoExp;
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

        } else if (model.isExperiencia()) {
            dialogoExp = new DialogoNuevaExperiencia(listaClientes, JoovaApp.getPrimaryStage());
            Optional<ExperienciaModel> resul = dialogoExp.showAndWait();
            if (resul.isPresent()) {
                int codigo = db.insertExperiencias(resul.get());
                for (int i = 0; i < resul.get().getParticipantes().size(); i++) {
                    db.insertExperienciasClientes(codigo, resul.get().getParticipantes().get(i).getCliente().getDni(), resul.get().getParticipantes().get(i).isCompra());
                    actualizarDatos();
                }
            }
        } else {
            DialogoNuevaAccionEspecial dialogoNuevaAccionEspecial = new DialogoNuevaAccionEspecial(listaClientes, JoovaApp.getPrimaryStage(), db);
            Optional<AccionEspecialModel> resul = dialogoNuevaAccionEspecial.showAndWait();
            if (resul.isPresent()) {
                AccionEspecialModel model = resul.get();
                int codigo = db.insertAccionesEspeciales(model);
                for (int i = 0; i < dialogoNuevaAccionEspecial.getListaParticipantes().size(); i++) {
                    db.insertAccionesEspecialesClientes(codigo, dialogoNuevaAccionEspecial.getListaParticipantes().get(i));
                }
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

        //Actualizar Experiencias
        listaExperiencias.clear();
        db.consultaTodasExperiencias(listaExperiencias);

        //Actualizar Acciones Especiales
        listaAccionesEspeciales.clear();
        db.consultaTodasAccionesEspeciales(listaAccionesEspeciales);
    }

    private void onExpChanged() {
        if (model.isPresentacion())
            setContent(tablaPresentaciones);
        else if (model.isPuestaEnMarcha())
            setContent(tablaPuestaMarcha);
        else if (model.isExperiencia())
            setContent(tablaExperiencias);
        else
            setContent(tablaAccionesEspeciales);
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
