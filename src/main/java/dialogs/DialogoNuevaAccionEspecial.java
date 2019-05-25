package dialogs;

import app.JoovaApp;
import database.HooverDataBase;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;

import java.util.Optional;

public class DialogoNuevaAccionEspecial extends Dialog<AccionEspecialModel> {
    private AccionEspecialModel model;
    private Stage primaryStage;
    private HooverDataBase db;


    //Lista con los participantes
    private ListProperty<Participante> listaParticipantes;

    //Lista con los eventos
    private ListProperty<TipoPagoyEventoModel> listaEventos;

    //Boxes
    private BorderPane root;
    private VBox leftBox;
    private HBox leftButtons;
    private VBox rightBox;
    private HBox eventBox;
    private HBox topBox;
    private HBox centerBox;
    private HBox bottomBox;

    //Components
    private Label datosParticipante;
    private Label datosEvento;
    private ComboBox<ClienteModel> clientesCombobox;
    private TextArea observacionesParticipante;
    private CheckBox compra;
    private CheckBox regalo;
    private Button anadirParticipante;
    private Button eliminarParticipante;
    private TextField direccionEvento;
    private TextArea observacionesEvento;
    private ComboBox<TipoPagoyEventoModel> eventosCombobox;
    private Button nuevoEvento;
    private TextField nombreEvento;
    private DatePicker fechaEvento;

    //Table
    private TableView<Participante> tablaParticipantes;
    private TableColumn<Participante, String> codCliente;
    private TableColumn<Participante, Boolean> ventaColumn;
    private TableColumn<Participante, Boolean> regaloColumn;
    private TableColumn<Participante, String> observacionesParticipanteColumn;

    public DialogoNuevaAccionEspecial(ListProperty<ClienteModel> listaClientes, Stage primaryStage, HooverDataBase db) {
        getDialogPane().getStylesheets().add("css/nuevaExperiencia.css");
        this.primaryStage = primaryStage;
        model = new AccionEspecialModel();
        this.db = db;


        listaParticipantes = new SimpleListProperty<>(this, "listaParticipantes", FXCollections.observableArrayList());
        listaEventos = new SimpleListProperty<>(this, "listaEventos", FXCollections.observableArrayList());

        // Boxes
        root = new BorderPane();
        leftBox = new VBox();
        rightBox = new VBox();
        topBox = new HBox();
        centerBox = new HBox();
        eventBox = new HBox();
        bottomBox = new HBox();

        // Components
        datosParticipante = new Label("Datos del Participante");
        datosEvento = new Label("Datos del evento");
        clientesCombobox = new ComboBox<>();
        observacionesParticipante = new TextArea();
        compra = new CheckBox();
        regalo = new CheckBox();
        anadirParticipante = new Button();
        anadirParticipante.getStyleClass().add("anadirParticipanteButton");
        eliminarParticipante = new Button();
        eliminarParticipante.getStyleClass().add("eliminarParticipante");
        leftButtons = new HBox(anadirParticipante, eliminarParticipante);
        direccionEvento = new TextField();
        observacionesEvento = new TextArea();
        eventosCombobox = new ComboBox<>();
        nuevoEvento = new Button();
        nuevoEvento.getStyleClass().add("anadirParticipanteButton");
        nombreEvento = new TextField();
        fechaEvento = new DatePicker();
        fechaEvento.setEditable(false);

        // Table
        tablaParticipantes = new TableView<>();
        codCliente = new TableColumn<>();
        ventaColumn = new TableColumn<>();
        regaloColumn = new TableColumn<>();
        observacionesParticipanteColumn = new TableColumn<>();

        tablaParticipantes.getColumns().add(0, codCliente);
        tablaParticipantes.getColumns().add(1, ventaColumn);
        tablaParticipantes.getColumns().add(2, regaloColumn);
        tablaParticipantes.getColumns().add(3, observacionesParticipanteColumn);

        tablaParticipantes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        codCliente.setCellValueFactory(v -> v.getValue().getCliente().nombreProperty().concat(" ").concat(v.getValue().getCliente().apellidosProperty()));
        codCliente.setText("Participante");
        ventaColumn.setCellValueFactory(v -> v.getValue().compraProperty());
        ventaColumn.setText("Compra?");
        observacionesParticipanteColumn.setCellValueFactory(v -> v.getValue().observacionParticipanteProperty());
        observacionesParticipanteColumn.setText("Observaciones");
        regaloColumn.setCellValueFactory(v -> v.getValue().regaloProperty());
        regaloColumn.setText("Regalo?");

        // Valor a componentes
        clientesCombobox.setPromptText("Seleccionar Cliente");
        observacionesParticipante.setPromptText("Añadir observaciones al cliente");
        observacionesParticipante.setWrapText(true);
        compra.setText("Compra?");
        regalo.setText("Regalo? (añadir a observacion)");
        direccionEvento.setPromptText("Dirección del evento");
        observacionesEvento.setPromptText("Añadir observaciones al evento");
        observacionesEvento.setWrapText(true);
        eventosCombobox.setPromptText("Seleccionar Tipo Evento");
        nombreEvento.setPromptText("Nombre del evento");
        fechaEvento.setPromptText("Fecha del evento");


        // Bindings
        model.direccionEventoProperty().bindBidirectional(direccionEvento.textProperty());
        model.fechaEventoProperty().bindBidirectional(fechaEvento.valueProperty());
        model.nombreEventoProperty().bindBidirectional(nombreEvento.textProperty());
        model.observacionesEventoProperty().bindBidirectional(observacionesEvento.textProperty());
        model.tipoEventoProperty().bind(eventosCombobox.getSelectionModel().selectedItemProperty());
        tablaParticipantes.itemsProperty().bind(listaParticipantes);
        clientesCombobox.itemsProperty().bind(listaClientes);
        eventosCombobox.itemsProperty().bind(listaEventos);

        // Arreglar dentro de los cajones
        //Left Box
        leftButtons.setAlignment(Pos.CENTER);
        leftButtons.setSpacing(10);
        leftBox.getChildren().add(datosParticipante);
        leftBox.getChildren().add(clientesCombobox);
        leftBox.getChildren().add(observacionesParticipante);
        leftBox.getChildren().add(compra);
        leftBox.getChildren().add(regalo);
        leftBox.getChildren().add(leftButtons);
        leftBox.setSpacing(5);
        leftBox.setAlignment(Pos.TOP_CENTER);
        leftBox.setMaxWidth(200);
        leftBox.setMaxHeight(400);
        root.setLeft(leftBox);

        //CenterBox
        centerBox.getChildren().add(tablaParticipantes);
        tablaParticipantes.setPrefWidth(600);
        centerBox.setPadding(new Insets(5));
        centerBox.setPrefWidth(600);
        root.setCenter(centerBox);

        //RighBox
        eventBox.getChildren().add(eventosCombobox);
        eventBox.getChildren().add(nuevoEvento);
        eventBox.setSpacing(5);
        eventBox.setAlignment(Pos.CENTER);
        rightBox.setSpacing(5);
        rightBox.getChildren().add(datosEvento);
        rightBox.getChildren().add(eventBox);
        rightBox.getChildren().add(nombreEvento);
        rightBox.getChildren().add(fechaEvento);
        rightBox.getChildren().add(observacionesEvento);
        rightBox.setAlignment(Pos.TOP_CENTER);
        rightBox.setMaxWidth(220);
        rightBox.setMaxHeight(400);
        root.setRight(rightBox);

        //TopBox
        topBox.getChildren().add(direccionEvento);
        direccionEvento.setPrefWidth(500);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPrefWidth(500);
        root.setTop(topBox);

        // Listeners
        anadirParticipante.setOnAction(e -> onAnadirButton());
        eliminarParticipante.setOnAction(e -> onEliminarButton());
        nuevoEvento.setOnAction(e -> onAnadirEvento());


        // Inherentes al Dialogo
        getDialogPane().setContent(root);

        // Dueño
        initOwner(primaryStage);
        //Titulo
        setTitle("Añadir Accion Especial");

        actualizarListaEventos();

        // Añadir botones
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        //Bindear aceptar a que esté relleno
        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(Bindings.when(
                tablaParticipantes.selectionModelProperty().isNull()
                        .or(eventosCombobox.selectionModelProperty().isNull()
                                .or(nombreEvento.textProperty().isEmpty()
                                        .or(clientesCombobox.selectionModelProperty().isNull()
                                                .or(fechaEvento.valueProperty().isNull())))))
                .then(true)
                .otherwise(false));

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return model;
            }
            return null;
        });

    }

    private void onAnadirEvento() {
        DialogoNuevoTipoPagoyEvento nuevo = new DialogoNuevoTipoPagoyEvento(JoovaApp.getPrimaryStage());
        nuevo.setTitle("Nuevo tipo de evento");
        nuevo.getDescripcionTipo().setVisible(false);
        nuevo.getDescripcionTipo().setManaged(false);
        nuevo.getNombreTipo().setPromptText("Tipo de evento...");
        nuevo.getNombreTipo().setPrefWidth(300);
        Optional<TipoPagoyEventoModel> resul = nuevo.showAndWait();
        if (resul.isPresent()) {
            db.insertTipoEvento(resul.get().getNombreTipoPago());
            actualizarListaEventos();
        }

    }

    private void onAnadirButton() {
        if (null != clientesCombobox.getSelectionModel().getSelectedItem()) {
            ClienteModel cliente = clientesCombobox.getSelectionModel().getSelectedItem();
            Participante nuevoParticipante = new Participante();
            boolean encontrado = false;
            for (int i = 0; i < listaParticipantes.size() && !encontrado; i++) {
                if (listaParticipantes.get(i).getCliente().equals(cliente)) {
                    encontrado = true;
                }
            }

            if (!encontrado) {
                nuevoParticipante.setCompra(compra.isSelected());
                nuevoParticipante.setRegalo(regalo.isSelected());
                nuevoParticipante.setCliente(cliente);
                nuevoParticipante.setObservacionParticipante(observacionesParticipante.getText());
                listaParticipantes.add(nuevoParticipante);
                compra.setSelected(false);
                regalo.setSelected(false);
                observacionesParticipante.setText("");

            } else {
                JoovaAlert.alertInfo("Info", "El cliente ya ha sido añadido a la Accion Especial.", "¡¡Prueba a poner uno que no tengas ya!!");
            }

        } else {
            JoovaAlert.alertError("Error", "No se ha seleccionado ningún cliente", "Por favor, seleccione uno");
        }
    }

    public void onEliminarButton() {
        try {
            listaParticipantes.remove(tablaParticipantes.getSelectionModel().getSelectedItem());
        } catch (NullPointerException e) {
            JoovaAlert.alertError("Error", "No se ha podido eliminar el participante", "Por favor, elija uno de la tabla antes de proceder a eliminarlo");
        }
    }

    public void actualizarListaEventos() {
        listaEventos.clear();
        db.constulaTodosEventos(listaEventos);
    }

    public ObservableList<Participante> getListaParticipantes() {
        return listaParticipantes.get();
    }

    public void setListaParticipantes(ObservableList<Participante> listaParticipantes) {
        this.listaParticipantes.set(listaParticipantes);
    }

    public AccionEspecialModel getModel() {
        return model;
    }

    public ComboBox<TipoPagoyEventoModel> getEventosCombobox() {
        return eventosCombobox;
    }
}
