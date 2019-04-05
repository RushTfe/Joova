package controller;

import Joova.ProductoCardController;
import database.HooverDataBase;
import dialogs.DialogoNuevoProducto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.PrecioModel;
import model.ProductoModel;
import nuevoproducto.NuevoProductoModel;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductoController implements Initializable {

    @FXML
    private BorderPane rootProductos;

    @FXML
    private ScrollPane cajonProductosScroll;

    @FXML
    private VBox cajonProductosVbox;

    @FXML
    private Button anadirProductoButton;

    @FXML
    private Button anadirFiltroButton;

    private ProductoModel model;

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
/*
        ProductoCardController card = new ProductoCardController();
        card.getModel().setNombreProducto("Aspiradora");
        card.getModel().setPrecio(1952.2);
        card.getModel().setTipoProducto("Tipo: Aspiradora");

        ProductoCardController card1 = new ProductoCardController();
        card1.getModel().setNombreProducto("Aspiradora2");
        card1.getModel().setPrecio(1952.2);
        card1.getModel().setTipoProducto("Tipo: Aspiradora");

        ProductoCardController card2 = new ProductoCardController();
        card2.getModel().setNombreProducto("Aspiradora3");
        card2.getModel().setPrecio(1952.2);
        card2.getModel().setTipoProducto("Tipo: Modulo");

        ProductoCardController card3 = new ProductoCardController();
        card3.getModel().setNombreProducto("Aspiradora3");
        card3.getModel().setPrecio(1952.2);
        card3.getModel().setTipoProducto("Tipo: Otro");

        ProductoCardController card4 = new ProductoCardController();
        card4.getModel().setNombreProducto("Aspiradora3");
        card4.getModel().setPrecio(1952.2);
        card4.getModel().setTipoProducto("Tipo: Otro");

        cajonProductosVbox.getChildren().addAll(card, card1, card2, card3, card4);
*/
        anadirProductoButton.setOnAction(e -> onAnadirProductoAction());

    }

    private void onAnadirProductoAction() {
        ProductoCardController card = new ProductoCardController();
        DialogoNuevoProducto nuevoProducto = new DialogoNuevoProducto();
        Optional<NuevoProductoModel> resul = nuevoProducto.showAndWait();
        if (resul.isPresent()) {

            // INSERTO EL PRODUCTO EN LA BASE DE DATOS
            int codArticuloInsertado = db.insertProduct(resul.get());

            // UNA VEZ DENTRO, YA HAY UN CODIGO DE ARTICULO QUE ASIGNAR AL PRECIO
            PrecioModel precioModel = new PrecioModel();
            precioModel.setCodArticulo(codArticuloInsertado);
            precioModel.setPrecioArticulo(resul.get().getPrecioProducto());

            // HE INSERTAMOS EL PRECIO EN LA BD
            db.insertPrecio(precioModel);

            //DAMOS VALOR A LA TARJETA QUE SE MOSTRARA
            card.getModel().setNombreProducto(resul.get().getNombreProducto());
            card.getModel().setTipoProducto(resul.get().getTipoProducto());
            card.getModel().setRutaImagen(new Image(resul.get().getDireccionImagen()));
            card.getModel().setPrecio(precioModel.getPrecioArticulo());

            // Y LA ANADIMOS A LA VISTA
            cajonProductosVbox.getChildren().add(card);
        }
    }

    public ProductoModel getModel() {
        return model;
    }

    public BorderPane getRootProductos() {
        return rootProductos;
    }
}
