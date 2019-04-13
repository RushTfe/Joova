package controller;

import Joova.ProductoCardController;
import Joova.ProductoCardModel;
import database.HooverDataBase;
import dialogs.DialogoNuevoProducto;
import javafx.beans.property.ListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.PrecioModel;
import model.ProductoModel;
import nuevoproducto.NuevoProductoModel;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductoController implements Initializable {

    @FXML
    private BorderPane rootProductos;

    @FXML
    private ScrollPane cajonProductosScroll;

    @FXML
    private ListView<NuevoProductoModel> cajonProductosListView;

    @FXML
    private Button anadirProductoButton;

    @FXML
    private Button anadirFiltroButton;

    @FXML
    private Button eliminarProductoButton;

    @FXML
    private Button modificarProductoButton;

    private ProductoModel model;

    //Conexion a la base de datos
    private HooverDataBase db;

    public ProductoController(HooverDataBase db) {
        this.db = db;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductosView.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new ProductoModel();


        // Ponemos el ListView del tipo ProductoCardModel y lo preparamos para que guarde objetos de tipo ProductoCardModel
        // y muestre ProductoCards
        cajonProductosListView.setCellFactory(new Callback<ListView<NuevoProductoModel>, ListCell<NuevoProductoModel>>() {
            @Override
            public ListCell<NuevoProductoModel> call(ListView<NuevoProductoModel> param) {
                return new ListCell<NuevoProductoModel>() {

                    ProductoCardController card = new ProductoCardController();

                    @Override
                    protected void updateItem(NuevoProductoModel item, boolean empty) {
                        if (item == null || empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            card.getModel().setNombreProducto(item.getNombreProducto());
                            card.getModel().setPrecio(item.getPrecioProducto());
                            card.getModel().setRutaImagen(new Image(item.getDireccionImagen()));
                            card.getModel().setDescripcion(item.getDescripcionProducto());
                            card.getModel().setCodProducto(item.getCodArticulo());
//                            item.aModificarProperty().bind(card.getModel().aModificarProperty());
                            setText(null);
                            setGraphic(card);
                        }
                    }

                };
            }
        });

        //Bindeo de la lista de productos con el listView
        cajonProductosListView.itemsProperty().bind(model.listaProductosProperty());

        actualizarProductos();

        // BOTONES
        anadirProductoButton.setOnAction(e -> onAnadirProductoAction());
        eliminarProductoButton.setOnAction(e -> onEliminarProductoAction());
    }

    private void onEliminarProductoAction() {
        int i = model.getListaProductos().size() - 1;
        int codABorrar = 0;
        while (i >= 0) {
            if (model.getListaProductos().get(i).isaModificar()) {
                codABorrar = model.getListaProductos().get(i).getCodArticulo();
                db.deleteArticulo(codABorrar);
            }

            i--;
        }


//        actualizarProductos();
    }

    /**
     * Muestra la lista de cartas en el listView.
     */
    private void actualizarProductos() {
        model.getListaProductos().clear();
        db.consultaTodosProductos(model.listaProductosProperty());
    }

    /**
     * Añade un producto a la base de datos y al modelo, para que aparezca en la vista.
     */
    private void onAnadirProductoAction() {


        NuevoProductoModel cardModel = new NuevoProductoModel();
        DialogoNuevoProducto nuevoProducto = new DialogoNuevoProducto();
        Optional<NuevoProductoModel> resul = nuevoProducto.showAndWait();
        if (resul.isPresent()) {

            //TODO Copiar la imagen a la carpeta de recursos, y usarla desde ahi, de modo que el usuario, si tira/mueve la imagen no la pierde.
            File imagen = new File(resul.get().getDireccionImagen().substring(6, resul.get().getDireccionImagen().length()).replaceAll("%20", " "));
            String nombreImagen = imagen.getName();
            File imagenDestino = new File(System.getProperty("user.home") + "\\.Joova\\" + nombreImagen);

            InputStream is = null;
            OutputStream os = null;

            try {
                is = new FileInputStream(Paths.get(imagen.toURI()).toFile());
                os = new FileOutputStream(Paths.get(imagenDestino.toURI()).toFile());

                byte[] buffer = new byte[1024];
                int length;

                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }

                String direccionFinal = imagenDestino.toURI().toString();
                resul.get().setDireccionImagen(direccionFinal);


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // INSERTO EL PRODUCTO EN LA BASE DE DATOS
            int codArticuloInsertado = db.insertProduct(resul.get());

            // UNA VEZ DENTRO, YA HAY UN CODIGO DE ARTICULO QUE ASIGNAR AL PRECIO
            PrecioModel precioModel = new PrecioModel();
            precioModel.setCodArticulo(codArticuloInsertado);
            precioModel.setPrecioArticulo(resul.get().getPrecioProducto());

            // HE INSERTAMOS EL PRECIO EN LA BD
            db.insertPrecio(precioModel);
            cardModel.setNombreProducto(resul.get().getNombreProducto());
            cardModel.setTipoProducto(resul.get().getTipoProducto());
            cardModel.setPrecioProducto(precioModel.getPrecioArticulo());
            cardModel.setDireccionImagen(resul.get().getDireccionImagen());
            //Al estar el precio separado del articulo para poder llevar un historico de precios, es necesario
            // insertar primero el articulo, y luego crear el precio en su entidad

            // Y LA ANADIMOS A LA VISTA
            model.getListaProductos().add(cardModel);
        }
    }

    public ProductoModel getModel() {
        return model;
    }

    public BorderPane getRootProductos() {
        return rootProductos;
    }
}
