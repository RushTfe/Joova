package dialogs;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.ClienteModel;
import model.ExperienciaModel;
import model.Participante;


public class DialogoNuevaExperiencia extends Dialog<ExperienciaModel> {
    private ExperienciaModel model;
    private Stage primaryStage;
    private VBox leftBox;
    private HBox centerBox;
    private BorderPane root;
    private ComboBox<ClienteModel> listaClientes;
    private Button anadirParticipanteButton;
    private Button eliminarParticipante;
    private TextField direccion;
    private TextArea observaciones;
    private TableView<Participante> tablaParticipantes;
    private TableColumn<Participante, String> nombreParticipanteColumn;
    private TableColumn<Participante, Boolean> huboVentaColumn;
    private DatePicker fechaExperiencia;

    public DialogoNuevaExperiencia(ListProperty<ClienteModel> listaClientesCompleta, Stage primaryStage) {
        //Inicializando los componentes
        model = new ExperienciaModel();
        this.primaryStage = primaryStage;
        leftBox = new VBox();
        centerBox = new HBox();
        root = new BorderPane();
        listaClientes = new ComboBox<>();
        anadirParticipanteButton = new Button("Añadir Participante");
        eliminarParticipante = new Button("Eliminar Participante");
        direccion = new TextField();
        observaciones = new TextArea();
        tablaParticipantes = new TableView<>();
        nombreParticipanteColumn = new TableColumn<>();
        huboVentaColumn = new TableColumn<>();
        fechaExperiencia = new DatePicker();

        // Stage dueña
        initOwner(primaryStage);

        // Preparar Stage
        setTitle("Añadir nueva experiencia");
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(Bindings.when(
                tablaParticipantes.selectionModelProperty().isNull()
                        .or(fechaExperiencia.valueProperty().isNull())
                        .or(direccion.textProperty().isEmpty()))
                .then(true)
                .otherwise(false));

        //Preparando componentes
        observaciones.setPrefWidth(200);
        observaciones.setPrefHeight(400);
        observaciones.setMaxWidth(Double.MAX_VALUE);
        observaciones.setMaxHeight(Double.MAX_VALUE);
        observaciones.setPromptText("Observaciones a la experiencia... (Opcionales)");
        observaciones.setWrapText(true);

        direccion.setPrefWidth(300);
        direccion.setPromptText("Dirección de la experiencia...");

        listaClientes.setPromptText("Lista de Clientes");

        // Preparando el VBox de la izquierda
        leftBox.getChildren().add(listaClientes);
        leftBox.getChildren().add(anadirParticipanteButton);
        leftBox.getChildren().add(eliminarParticipante);
        leftBox.getChildren().add(fechaExperiencia);
        leftBox.getChildren().add(observaciones);
        leftBox.setSpacing(5d);
        leftBox.setAlignment(Pos.CENTER);
        leftBox.setMaxWidth(200);
        leftBox.setMaxWidth(400);
        leftBox.setPadding(new Insets(5));

        // Preparando el HBox del centro
        centerBox.getChildren().add(tablaParticipantes);
        centerBox.setPadding(new Insets(5));

        // Añadir las columnas a la tabla
        tablaParticipantes.getColumns().add(0, nombreParticipanteColumn);
        tablaParticipantes.getColumns().add(1, huboVentaColumn);

        // Preparar las columnas
        nombreParticipanteColumn.setCellValueFactory(v -> v.getValue().getCliente().nombreProperty());
        nombreParticipanteColumn.setText("Nombre del participante");
        huboVentaColumn.setCellValueFactory(v -> v.getValue().compraProperty());
        huboVentaColumn.setText("¿Compro productos?");
//        huboVentaColumn.setEditable(true);
        huboVentaColumn.setCellFactory(CheckBoxTableCell.forTableColumn(huboVentaColumn));

        // Preparar la tabla
        tablaParticipantes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaParticipantes.setPrefWidth(500);
        tablaParticipantes.setMaxWidth(500);
        tablaParticipantes.setEditable(true);

        root.setLeft(leftBox);
        root.setCenter(centerBox);
        root.setBottom(direccion);

        // Bindeos
        tablaParticipantes.itemsProperty().bindBidirectional(model.participantesProperty());
        model.fechaExperienciaProperty().bindBidirectional(fechaExperiencia.valueProperty());
        model.observacionesProperty().bindBidirectional(observaciones.textProperty());
        listaClientes.itemsProperty().bind(listaClientesCompleta);
        model.direccionProperty().bindBidirectional(direccion.textProperty());

        anadirParticipanteButton.setOnAction(e -> onAnadirAction());
        eliminarParticipante.setOnAction(e -> onEliminarAction());

        // Añadir el Root al contenido del dialogo
        getDialogPane().setContent(root);

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return model;
            }
            return null;
        });

    }

    private void onEliminarAction() {
        try {
            model.getParticipantes().remove(tablaParticipantes.getSelectionModel().getSelectedItem());
        } catch (NullPointerException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.initOwner(primaryStage);
            alerta.setTitle("Error");
            alerta.setHeaderText("No ha seleccionado ningún participante para eliminar");
            alerta.setContentText("Por favor, seleccione el participante que desea eliminar de la tabla");
            alerta.show();
        }
    }

    private void onAnadirAction() {
        if (listaClientes.getSelectionModel().getSelectedItem() != null) {
            ClienteModel cliente = listaClientes.getSelectionModel().getSelectedItem();
            boolean encontrado = false;

            for (int i = 0; i < model.getParticipantes().size() && !encontrado; i++) {
                if (model.getParticipantes().get(i).getCliente().equals(cliente))
                    encontrado = true;
            }

            if (!encontrado)
                model.getParticipantes().add(new Participante(cliente, false));
            else {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.initOwner(primaryStage);
                alerta.setTitle("Error");
                alerta.setHeaderText("El cliente ya ha sido añadido a la experiencia.");
                alerta.setContentText("¡¡Prueba a poner uno que no tengas ya!!");
                alerta.show();
            }
        } else {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.initOwner(primaryStage);
            alerta.setTitle("Error");
            alerta.setHeaderText("No ha seleccionado ningún participante para añadir");
            alerta.setContentText("Por favor, seleccione el participante que desea añadir del desplagable");
            alerta.show();
        }
    }

    public ExperienciaModel getModel() {
        return model;
    }

    public void setModel(ExperienciaModel model) {
        this.model = model;
    }
}
