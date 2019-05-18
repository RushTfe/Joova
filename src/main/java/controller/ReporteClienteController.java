package controller;

import database.HooverDataBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;
import model.NuevoProductoModel;
import util.ScreenResolutions;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ReporteClienteController extends BorderPane implements Initializable {
    private ReporteClienteModel model = new ReporteClienteModel();
    private Stage primaryStage;
    private HooverDataBase db;

    @FXML
    private TextField busquedaComprasTextField;

    @FXML
    private ImageView aspiradoraImageView;

    @FXML
    private BorderPane root;

    @FXML
    private VBox leftVbox;

    @FXML
    private VBox aspiradoraActualVbox;

    @FXML
    private HBox fechaPresentacionHbox;

    @FXML
    private VBox fechaVbox;

    @FXML
    private Label tituloFechaLabel;

    @FXML
    private Label fechaLabel;

    @FXML
    private Button verFechaPresentacionButton;

    @FXML
    private VBox puestaMarchaVbox;

    @FXML
    private Label tituloPuestaMarchaLabel;

    @FXML
    private ListView<PMyPresentacionesModel> puestasMarchaListView;

    @FXML
    private VBox accionEspecialVbox;

    @FXML
    private Label tituloAccionEspecialLabel;

    @FXML
    private ListView<AccionEspecialModel> accionesEspecialesListView;

    @FXML
    private VBox experienciaVbox;

    @FXML
    private Label tituloExperienciaLabel;

    @FXML
    private ListView<ExperienciaModel> experienciasListView;

    @FXML
    private VBox topVbox;

    @FXML
    private Label tituloResumenLabel;

    @FXML
    private Label nombreAspiradoraLabel;

    @FXML
    private VBox centerVbox;

    @FXML
    private HBox datosBasicosHbox;

    @FXML
    private VBox nombreApellidoVbox;

    @FXML
    private Label nombreClienteLabel;

    @FXML
    private Label apellidoClienteLabel;

    @FXML
    private HBox direccionHbox;

    @FXML
    private TextArea direccionTextArea;

    @FXML
    private VBox observacionesVbox;

    @FXML
    private Label tituloObservacionesLabel;

    @FXML
    private TextArea observacionesTextArea;

    @FXML
    private VBox comprasVbox;

    @FXML
    private Label tituloComprasLabel;

    @FXML
    private TableView<VentasModel> tablaCompras;

    @FXML
    private TableColumn<VentasModel, String> codigoColumn;

    @FXML
    private TableColumn<VentasModel, LocalDate> fechaColumn;

    @FXML
    private TableColumn<VentasModel, String> precioColumn;

    @FXML
    private VBox bottomVbox;

    @FXML
    private VBox rightVbox;

    @FXML
    private Label interesesTituloLabel;

    @FXML
    private HBox anadirProductoInteres;

    @FXML
    private ComboBox<NuevoProductoModel> productosComboBox;

    @FXML
    private Button anadirProductoInteresButton;

    @FXML
    private Button eliminarProductoInteresButton;

    @FXML
    private ListView<InteresesModel> interesesListView;

    public ReporteClienteController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.db = db;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReporteClienteView.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new ReporteClienteModel();
        int x = (int) (ScreenResolutions.getScreenResolutions().get(ScreenResolutions.getScreenResolutions().size()-1).getX()*0.8);
        int y = (int) (ScreenResolutions.getScreenResolutions().get(ScreenResolutions.getScreenResolutions().size()-1).getY()*0.8);

        setPadding(new Insets(5));
        setPrefWidth(x);
        setPrefHeight(y);

        precioColumn.setCellValueFactory(v -> v.getValue().precioAsString());
        codigoColumn.setCellValueFactory(v -> v.getValue().codContratoProperty());
        fechaColumn.setCellValueFactory(v -> v.getValue().fechaVentaProperty());

        /*****************************************
         *
         * Bindeos al modelo
         *
         *****************************************/

        puestasMarchaListView.itemsProperty().bind(model.puestaEnMarchaModelProperty());
        accionesEspecialesListView.itemsProperty().bind(model.accionEspecialModelProperty());
        experienciasListView.itemsProperty().bind(model.experienciaModelProperty());
        tablaCompras.itemsProperty().bind(model.ventasModelProperty());
        productosComboBox.itemsProperty().bind(model.listaProductosProperty());
        interesesListView.itemsProperty().bind(model.interesesModelProperty());



        // Organizando componentes dentro del BorderPane
        setTop(topVbox);
        setBottom(bottomVbox);
        setLeft(leftVbox);
        setRight(rightVbox);
        setCenter(centerVbox);
    }

    public ReporteClienteModel getModel() {
        return model;
    }

    public void setCliente(ClienteModel cliente) {
        nombreAspiradoraLabel.setText(cliente.getModeloAspiradora().getNombreProducto());
        nombreClienteLabel.setText(cliente.getNombre());
        apellidoClienteLabel.setText(cliente.getApellidos());
        direccionTextArea.setText(cliente.getDireccion());
        direccionTextArea.setWrapText(true);
        direccionTextArea.setEditable(false);
        aspiradoraImageView.setImage(new Image(cliente.getModeloAspiradora().getDireccionImagen()));
        observacionesTextArea.setText(cliente.getObservaciones());
        observacionesTextArea.setWrapText(true);
        observacionesTextArea.setEditable(false);
    }

    public Button getEliminarProductoInteresButton() {
        return eliminarProductoInteresButton;
    }

    public Button getAnadirProductoInteresButton() {
        return anadirProductoInteresButton;
    }

    public ComboBox<NuevoProductoModel> getProductosComboBox() {
        return productosComboBox;
    }

    public ListView<InteresesModel> getInteresesListView() {
        return interesesListView;
    }

    public void setFecha(String fecha) {
        fechaLabel.setText(fecha);
    }

    public TableView<VentasModel> getTablaCompras() {
        return tablaCompras;
    }

    public TextField getBusquedaComprasTextField() {
        return busquedaComprasTextField;
    }
}
