package controller;

import app.JoovaApp;
import database.HooverDataBase;
import dialogs.DialogoNuevoUsuario;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.MainModel;
import signup.SignUpController;
import signup.SignUpModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
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
    private VentasController ventasController;
    private AccionesController accionesController;

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
        newAccountButton.setOnAction(e -> onNewUser());
    }

    private void onNewUser() {
        DialogoNuevoUsuario nuevoUser = new DialogoNuevoUsuario();
        Optional<SignUpModel> resul = nuevoUser.showAndWait();

        if (resul.isPresent()){
            File usuario = new File(System.getProperty("user.home") + "\\.Joova\\" + resul.get().getUsuario() + ".joo");
            if (!usuario.exists()) {
                try {
                    usuario.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            model.setNombre(resul.get().getUsuario());
            model.setPass(resul.get().getContrasena());
        }
    }

    private void onLogInAction() {

        File archivoUsuario = new File(System.getProperty("user.home") + "\\.Joova\\" + model.getNombre() + ".joo");

        if (!archivoUsuario.exists())
            alertaError("Usuario inexistente", "Este usuario aun no se ha registrado", "Por favor, registrese antes de proseguir");
        else {

            //TODO Todo lo que haya que hacer cuando el usuario escriba su user y pass correctamente

        }

        /****************************************
         *
         * Coodigo viejo pero funcional
         *
         */
        /*
        boolean existe = false;
        int i;
        for (i = 0; (i < USUARIOS.length) && (!USUARIOS[i].equals(model.getNombre())); i++);

        if ((i < USUARIOS.length) && model.getNombre().equals(USUARIOS[i])) {
            existe = true;
        } else {
            alertaError("Error al iniciar sesión", "Usuario o contraseña no existen", "Pruebe de nuevo por favor");
        }

        if (existe) {
            if (CONTRASENAS[i].equals(model.getPass())) {
                ocultarLogin();
            } else {
                alertaError("Error al iniciar sesión", "Usuario o contraseña incorrectos", "Pruebe de nuevo por favor");
            }
        }*/
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
        ventasController = new VentasController();
        accionesController = new AccionesController();


        //Bindeos necesarios DESPUES de crear los controladores
        model.listaClientesProperty().bind(clienteController.listaClientesProperty());
        model.listaProductosProperty().bind(productoController.getModel().listaProductosProperty());
        ventasController.getClientesComboBox().itemsProperty().bind(model.listaClientesProperty());


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
        ventasTab.setContent(ventasController.getRootVentas());
        accionesTab.setContent(accionesController.getRootAcciones());
        }

        public void alertaError(String titulo, String header, String content) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle(titulo);
            alerta.setHeaderText(header);
            alerta.setContentText(content);
            alerta.show();
        }

    public BorderPane getRoot() {
        return root;
    }
}
