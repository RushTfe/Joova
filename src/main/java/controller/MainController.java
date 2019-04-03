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

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // SOLO PARA TEST, SE IMPLEMENTARÁ DISTINTO CUANDO PASE LA PRIMERA FASE
    private static final String[] USUARIOS = {"Pedro", "user", "1234"};
    private static final String[] CONTRASENAS = {"1234", "user", "1234"};

    // Tamaño de los registros guardados en el archivo
    private static final int TAMANO_CELDA = 60;

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

        if (resul.isPresent()) {
            File usuario = new File(System.getProperty("user.home") + "\\.Joova\\" + resul.get().getUsuario() + ".joo");
            if (!usuario.exists()) {
                try {
                    usuario.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            guardarBinario(usuario, resul.get());

            model.setNombre(resul.get().getUsuario());
            model.setPass(resul.get().getContrasena());
        }
    }

    private void onLogInAction() {
        File archivoUsuario = new File(System.getProperty("user.home") + "\\.Joova\\" + model.getNombre() + ".joo");

        if (!archivoUsuario.exists())
            alertaError("Usuario inexistente", "Este usuario aun no se ha registrado", "Por favor, registrese antes de proseguir");
        else {
            String[] tablaDatos = cargarBinario(archivoUsuario);
            if (model.getNombre().equals(tablaDatos[0]) && model.getPass().equals(tablaDatos[1])) {
                ocultarLogin();
            } else {
                alertaError("Usuario inexistente", "Este usuario aun no se ha registrado", "Por favor, registrese antes de proseguir");
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

    public static void alertaError(String titulo, String header, String content) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
//        alerta.getDialogPane().getStylesheets().addAll("Ruta");
        alerta.setTitle(titulo);
        alerta.setHeaderText(header);
        alerta.setContentText(content);
        alerta.initOwner(JoovaApp.getPrimaryStage());
        alerta.show();
    }

    public static Optional<ButtonType> alertaConfirmation(String titulo, String header, String content) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
//        alerta.getDialogPane().getStylesheets().addAll("Ruta");
        alerta.setTitle(titulo);
        alerta.setHeaderText(header);
        alerta.setContentText(content);
        alerta.initOwner(JoovaApp.getPrimaryStage());
        return alerta.showAndWait();
    }


    /**
     * Funcion que guarda un archivo binario con los datos del usuario
     *
     * @param file  el archivo que representa el lugar donde se encuentra el archivo fisico donde se guardarán lso datos.
     * @param model el modelo de SignUp con los datos de la creación del usuario.
     */
    public void guardarBinario(File file, SignUpModel model) {
        // TODO escribir de manera que el archivo no se vea
        char[] nombre = model.getUsuario().toCharArray();
        char[] pass = model.getContrasena().toCharArray();
        char[] email = model.getEmail().toCharArray();
        char[] respuesta = model.getRespuesta().toCharArray();

        if (file.isFile()) {
            try {
                int i;
                RandomAccessFile archivo = new RandomAccessFile(file, "rw");
                // Celda de nombre
                for (i = 0; i < nombre.length; i++)
                    archivo.writeChar(nombre[i]);
                archivo.writeChar('+');

                // Celda de Password
                for (i = 0; i < pass.length; i++)
                    archivo.writeChar(pass[i]);
                archivo.writeChar('+');

                // Celda de email
                for (i = 0; i < email.length; i++)
                    archivo.writeChar(email[i]);
                archivo.writeChar('+');

                // Celda de respuesta
                for (i = 0; i < respuesta.length; i++)
                    archivo.writeChar(respuesta[i]);
                archivo.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] cargarBinario(File file) {
        String[] tablaDatos = null;
        try {
            RandomAccessFile archivo = new RandomAccessFile(file, "r");

            int i = 0;
            String pegote = "";

            try {
                while (true) {
                    pegote += archivo.readChar();
                }
            } catch (EOFException e) {
                tablaDatos = pegote.split("[+]");
                return tablaDatos;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tablaDatos;
    }

    public BorderPane getRoot() {
        return root;
    }
}
