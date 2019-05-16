package dialogs;

import NuevoCliente.NuevoClienteController;
import NuevoCliente.NuevoClienteModel;
import database.HooverDataBase;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import model.ClienteModel;
import nuevoproducto.NuevoProductoModel;

public class DialogoNuevoCliente extends Dialog<ClienteModel> {

    private ClienteModel model;

    private GridPane rootClienteNuevo;

    private TextField nombreField;

    private TextField apellidoField;

    private TextField dniField;

    private TextField tlfField;

    private TextField emailField;

    private DatePicker nacimientoField;

    private ComboBox<NuevoProductoModel> productosComboBox;

    private CheckBox huerfanoBox;

    private TextArea observacionesBox;

    private TextField direccionField;


    public DialogoNuevoCliente(Stage stage, ListProperty<NuevoProductoModel> listaProductosExt) {
        model = new ClienteModel();
        initOwner(stage);
        setTitle("Añadir un nuevo cliente");

        rootClienteNuevo = new GridPane();
        rootClienteNuevo.setPadding(new Insets(5));
        rootClienteNuevo.setHgap(10);
        rootClienteNuevo.setVgap(10);
        rootClienteNuevo.setPrefWidth(490);
        rootClienteNuevo.setMaxWidth(Double.MAX_VALUE);
        rootClienteNuevo.setMaxHeight(Double.MAX_VALUE);
        rootClienteNuevo.setAlignment(Pos.CENTER);

        ColumnConstraints columnConstraints = new ColumnConstraints(100);
        columnConstraints.setFillWidth(true);
        columnConstraints.setHgrow(Priority.ALWAYS);
        columnConstraints.setHalignment(HPos.CENTER);
        columnConstraints.setPercentWidth(-1);
        rootClienteNuevo.getColumnConstraints().add(0, columnConstraints);
        rootClienteNuevo.getColumnConstraints().add(1, columnConstraints);
        rootClienteNuevo.getColumnConstraints().add(2, columnConstraints);
        rootClienteNuevo.getColumnConstraints().add(3, columnConstraints);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(10);
        rowConstraints.setPrefHeight(30);
        rowConstraints.setPercentHeight(-1);
        rowConstraints.setFillHeight(true);
        rootClienteNuevo.getRowConstraints().add(0, rowConstraints);
        rootClienteNuevo.getRowConstraints().add(1, rowConstraints);
        rootClienteNuevo.getRowConstraints().add(2, rowConstraints);
        rootClienteNuevo.getRowConstraints().add(3, rowConstraints);
        rootClienteNuevo.getRowConstraints().add(4, rowConstraints);
        rootClienteNuevo.getRowConstraints().add(5, rowConstraints);
        rootClienteNuevo.getRowConstraints().add(6, rowConstraints);
        rootClienteNuevo.getRowConstraints().add(7, rowConstraints);

        nombreField = new TextField();
        nombreField.setPromptText("Nombre");
        nombreField.setMaxWidth(210);
        nombreField.setAlignment(Pos.CENTER_LEFT);

        apellidoField = new TextField();
        apellidoField.setPromptText("Apellidos");
        apellidoField.setMaxWidth(210);
        apellidoField.setAlignment(Pos.CENTER_LEFT);

        dniField = new TextField();
        dniField.setPromptText("DNI");
        dniField.setMaxWidth(210);
        dniField.setAlignment(Pos.CENTER_LEFT);

        tlfField = new TextField();
        tlfField.setPromptText("Teléfono");
        tlfField.setMaxWidth(210);
        tlfField.setMinWidth(210);
        tlfField.setAlignment(Pos.CENTER_LEFT);

        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(210);
        emailField.setMinWidth(210);
        emailField.setAlignment(Pos.CENTER_LEFT);

        nacimientoField = new DatePicker();
        nacimientoField.setPromptText("Fecha de Nacimiento");
        nacimientoField.setMaxWidth(210);
        nacimientoField.setMinWidth(210);

        productosComboBox = new ComboBox<>();
        productosComboBox.setPromptText("Productos");
        productosComboBox.setMaxWidth(Double.MAX_VALUE);
        productosComboBox.setMinWidth(210);

        huerfanoBox = new CheckBox();
        huerfanoBox.setText("¿Es el cliente huérfano?");
        huerfanoBox.setMaxWidth(Double.MAX_VALUE);
        huerfanoBox.setAlignment(Pos.CENTER);
        huerfanoBox.setMinWidth(145);

        observacionesBox = new TextArea();
        observacionesBox.setPromptText("Observaciones...");
        observacionesBox.setMaxWidth(Double.MAX_VALUE);
        observacionesBox.setWrapText(true);

        direccionField = new TextField();
        direccionField.setPromptText("Dirección");
        direccionField.setMaxWidth(Double.MAX_VALUE);
        direccionField.setAlignment(Pos.CENTER);

        model.nombreProperty().bindBidirectional(nombreField.textProperty());
        model.apellidosProperty().bindBidirectional(apellidoField.textProperty());
        model.dniProperty().bindBidirectional(dniField.textProperty());
        model.telefonoProperty().bindBidirectional(tlfField.textProperty());
        model.emailProperty().bindBidirectional(emailField.textProperty());
        model.fechaNacimientoProperty().bindBidirectional(nacimientoField.valueProperty());
        model.huerfanoProperty().bindBidirectional(huerfanoBox.selectedProperty());
        productosComboBox.itemsProperty().bind(listaProductosExt);
        model.modeloAspiradoraProperty().bind(productosComboBox.getSelectionModel().selectedItemProperty());
        model.observacionesProperty().bindBidirectional(observacionesBox.textProperty());
        model.direccionProperty().bindBidirectional(direccionField.textProperty());

        nacimientoField.setEditable(false);

        GridPane.setColumnSpan(nombreField, 2);
        GridPane.setColumnSpan(apellidoField, 2);
        GridPane.setColumnSpan(dniField, 2);
        GridPane.setColumnSpan(tlfField, 2);
        GridPane.setColumnSpan(emailField, 2);
        GridPane.setColumnSpan(nacimientoField, 2);
        GridPane.setColumnSpan(productosComboBox, 2);
        GridPane.setColumnSpan(direccionField, GridPane.REMAINING);
        GridPane.setColumnSpan(observacionesBox, GridPane.REMAINING);
        GridPane.setRowSpan(observacionesBox, 3);

        rootClienteNuevo.add(nombreField, 0, 0);
        rootClienteNuevo.add(apellidoField, 0, 1);
        rootClienteNuevo.add(dniField, 0, 2);
        rootClienteNuevo.add(tlfField, 3, 0);
        rootClienteNuevo.add(emailField, 3, 1);
        rootClienteNuevo.add(nacimientoField, 3, 2);
        rootClienteNuevo.add(productosComboBox, 3, 4);
        rootClienteNuevo.add(direccionField, 0, 3);
        rootClienteNuevo.add(huerfanoBox, 0, 4);
        rootClienteNuevo.add(observacionesBox, 0, 5);


        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().setContent(rootClienteNuevo);
// TODO        getDialogPane().getStylesheets().addAll("Hoja de estilos");


        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return model;
            }
            return null;
        });
    }

    public ClienteModel getModel() {
        return model;
    }

    public TextField getDNIField() {
        return dniField;
    }

    public ComboBox<NuevoProductoModel> getProductosComboBox() {
        return productosComboBox;
    }
}
