package controller;

import Joova.ProductoCardController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
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

    public ProductoController() {
        System.out.println(getClass().getResource("/ProductosView.fxml"));
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
        ProductoCardController card = new ProductoCardController();
        card.getModel().setNombreProducto("Aspiradora");
        card.getModel().setPrecio(1952.2);
        card.getModel().setTipoProducto("Tipo: My Ass");

        ProductoCardController card1 = new ProductoCardController();
        card1.getModel().setNombreProducto("Aspiradora2");
        card1.getModel().setPrecio(1952.2);
        card1.getModel().setTipoProducto("Tipo: My Ass");

        ProductoCardController card2 = new ProductoCardController();
        card2.getModel().setNombreProducto("Aspiradora3");
        card2.getModel().setPrecio(1952.2);
        card2.getModel().setTipoProducto("Tipo: My Ass");

        ProductoCardController card3 = new ProductoCardController();
        card3.getModel().setNombreProducto("Aspiradora3");
        card3.getModel().setPrecio(1952.2);
        card3.getModel().setTipoProducto("Tipo: My Ass");

        ProductoCardController card4 = new ProductoCardController();
        card4.getModel().setNombreProducto("Aspiradora3");
        card4.getModel().setPrecio(1952.2);
        card4.getModel().setTipoProducto("Tipo: My Ass");

        cajonProductosVbox.getChildren().addAll(card, card1, card2, card3, card4);

    }

    public BorderPane getRootProductos() {
        return rootProductos;
    }
}
