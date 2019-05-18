package controller;

import app.JoovaApp;
import database.HooverDataBase;
import dialogs.DialogoNuevoUsuario;
import dialogs.JoovaAlert;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.MainModel;
import signup.SignUpModel;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
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
    private PasswordField passwordTextBox;

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

        // Bindeo de los campos Usuario y Contraseña del Login a su respectivo modelo
        Bindings.bindBidirectional(userTextBox.textProperty(), model.nombreProperty());
        Bindings.bindBidirectional(passwordTextBox.textProperty(), model.passProperty());

        // Preparamos las pestañas previamente para que no se vean al principio de la ejecución
        tabsRoot.setManaged(false);
        tabsRoot.setVisible(false);

        // Botones
        logInButton.setOnAction(e -> onLogInAction());
        newAccountButton.setOnAction(e -> onNewUser());
    }

    private void onNewUser() {
        DialogoNuevoUsuario nuevoUser = new DialogoNuevoUsuario(JoovaApp.getPrimaryStage());
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
        // Creamos la ruta hacia el archivo donde se encontrarán los datos del usuario.
        File archivoUsuario = new File(System.getProperty("user.home") + "\\.Joova\\" + model.getNombre() + ".joo");

        /**
         * Si el archivo no existe, lanzamos un error diciendo que el usuario no existe.
         *
         * De lo contrario, cargamos los datos del archivo en el modelo, y procedemos a comprobar si los datos de usuario y
         * contraseña coinciden. Si es así, procedemos a ocultar la ventana de login para mostrar el programa principal.
         *
         * Si no coinciden, se lanza otro error pidiendo los datos correctos.
         */
        if (!archivoUsuario.exists())
            JoovaAlert.alertError("Usuario inexistente", "Este usuario aun no se ha registrado", "Por favor, registrese antes de proseguir");
        else {
            String[] tablaDatos = cargarBinario(archivoUsuario);
            if (model.getNombre().equals(tablaDatos[0]) && model.getPass().equals(tablaDatos[1])) {
                ocultarLogin();
            } else {
                JoovaAlert.alertError("Usuario inexistente", "Este usuario aun no se ha registrado", "Por favor, registrese antes de proseguir");
            }
        }
    }

    private void ocultarLogin() {
        // Ocultar el login
        logInRoot.setManaged(false);
        logInRoot.setVisible(false);
        // Eliminar la vista en el nodo central, y colocar el panel de pestañas.
        root.setBottom(null);
        root.setCenter(tabsRoot);


        //Inicializar la base de datos
        database = new HooverDataBase(model.getNombre());

        //Controladores de las pestañas
        clienteController = new ClienteController(database);
        productoController = new ProductoController(database);
        ventasController = new VentasController(database);
        accionesController = new AccionesController(database);

        //Bindeos necesarios DESPUES de crear los controladores
        model.listaClientesProperty().bind(clienteController.listaClientesProperty());
        model.listaProductosProperty().bind(productoController.listaProductosProperty());
        ventasController.getClientesComboBox().itemsProperty().bind(model.listaClientesProperty());
        ventasController.listaProductosDisponiblesProperty().bind(productoController.listaProductosProperty());
        accionesController.listaClientesProperty().bind(model.listaClientesProperty());
        clienteController.listaProductosProperty().bind(model.listaProductosProperty());


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
