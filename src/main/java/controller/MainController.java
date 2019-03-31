package controller;

import app.JoovaApp;
import database.HooverDataBase;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.MainModel;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // SOLO PARA TEST, SE IMPLEMENTARÁ DISTINTO CUANDO PASE LA PRIMERA FASE
    private static final String[] USUARIOS = {"Pedro", "user", "1234"};
    private static final String[] CONTRASENAS = {"1234", "user", "1234"};

    // Base de datos
    private HooverDataBase database;

    private MainModel model;

    //Controladores de las pestañas
    private ClienteController clienteController;
    private ProductoController productoController;

    @FXML
    private BorderPane root;

    @FXML
    private TabPane tabsRoot;

    @FXML
    private Tab clientesTab;

    @FXML
    private Tab ventasTab;

    @FXML
    private Tab accionesTab;

    @FXML
    private Tab productosTab;

    @FXML
    private Tab estadisticasTab;

    @FXML
    private GridPane logInRoot;

    @FXML
    private TextField userTextBox;

    @FXML
    private TextField passwordTextBox;

    @FXML
    private Button logInButton;

    @FXML
    private Button newAccountButton;

    public MainController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new MainModel();



        Bindings.bindBidirectional(userTextBox.textProperty(), model.nombreProperty());
        Bindings.bindBidirectional(passwordTextBox.textProperty(), model.passProperty());

        tabsRoot.setManaged(false);
        tabsRoot.setVisible(false);

        // Botones
        logInButton.setOnAction(e -> onLogInAction());
    }

    private void onLogInAction() {
        boolean existe = false;
        int i;
        for (i = 0; (i < USUARIOS.length) && (!USUARIOS[i].equals(model.getNombre())); i++);

        if ((i < USUARIOS.length) && model.getNombre().equals(USUARIOS[i])) {
            existe = true;
        }

        if (existe) {
            if (CONTRASENAS[i].equals(model.getPass())) {
                ocultarLogin();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error al iniciar sesión");
                alert.setHeaderText("Usuario o contraseña incorrectos");
                alert.setContentText("Pruebe de nuevo por favor");
                alert.show();
            }
        }
    }

    private void ocultarLogin() {
        // Ocultar el login
        logInRoot.setManaged(false);
        logInRoot.setVisible(false);

        root.setBottom(null);
        root.setCenter(tabsRoot);


        //Inicializar la base de datos
        database = new HooverDataBase(model.getNombre());

        //Controladores de las pestañas
        clienteController = new ClienteController(database);
        productoController = new ProductoController();
        //clienteController.setDb(database);

        //Añadir las pestañas al controlador principal
        setTabs();
        JoovaApp.getPrimaryStage().setMaximized(true);

        // Mostrar principal
        tabsRoot.setManaged(true);
        tabsRoot.setVisible(true);



    }

    private void setTabs() {
        clientesTab.setContent(clienteController.getRootClientes());
        productosTab.setContent(productoController.getRootProductos());
        }

    public BorderPane getRoot() {
        return root;
    }
}
