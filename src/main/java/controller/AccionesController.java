package controller;

import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.AccionesModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AccionesController implements Initializable {

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

    public AccionesController() {
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

        /*************************************************************
         *
         * Bindings
         *
         *************************************************************/

        model.observacionesProperty().bind(observacionesTextArea.textProperty());
        model.presentacionProperty().bind(presentacionRB.selectedProperty());
        model.puestaEnMarchaProperty().bind(puestaEnMarchaRB.selectedProperty());
        model.experienciaProperty().bind(experienciasRB.selectedProperty());
        model.accionEspecialProperty().bind(accionesEspecialesRB.selectedProperty());

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
         * Listeners
         *
         *********************************************/
        model.presentacionProperty().addListener(e -> onExpChanged());
    }

    private void onExpChanged() {
//        setContent();
    }

    public void setContent(Node node) {
        contentAnchorPane.getChildren().clear();
        contentAnchorPane.getChildren().add(node);
    }

    public BorderPane getRootAcciones() {
        return rootAcciones;
    }
}
