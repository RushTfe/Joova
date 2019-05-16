package controller;

import app.JoovaApp;
import database.HooverDataBase;
import dialogs.DialogoNuevoProducto;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import model.NuevoProductoModel;
import model.PrecioModel;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductoController implements Initializable {

    private ListProperty<NuevoProductoModel> listaProductos = new SimpleListProperty<>(this, "listaProductos", FXCollections.observableArrayList());


    @FXML
    private BorderPane rootProductos;

    @FXML
    private Button anadirProductoButton;

    @FXML
    private Button eliminarProductoButton;

    @FXML
    private Button modificarProductoButton;

    @FXML
    private TextField busquedaTextField;

    @FXML
    private TableView<NuevoProductoModel> tablaProductos;

    @FXML
    private TableColumn<NuevoProductoModel, ImageView> imagenColumn;

    @FXML
    private TableColumn<NuevoProductoModel, String> nombreProductoColumn;

    @FXML
    private TableColumn<NuevoProductoModel, String> tipoProductoColumn;

    @FXML
    private TableColumn<NuevoProductoModel, Number> preciosColumn;

    private NuevoProductoModel model;

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
        model = new NuevoProductoModel();
        db.consultaTodosProductos(listaProductos);
        tablaProductos.itemsProperty().bind(listaProductos);
        imagenColumn.setCellValueFactory(new PropertyValueFactory<NuevoProductoModel, ImageView>("imagen"));
        nombreProductoColumn.setCellValueFactory(v -> v.getValue().nombreProductoProperty());
        preciosColumn.setCellValueFactory(v -> v.getValue().precioProductoProperty());
        tipoProductoColumn.setCellValueFactory(v -> v.getValue().tipoProductoProperty());

        // Listeners

        anadirProductoButton.setOnAction(e -> onAnadirProducto());
        eliminarProductoButton.setOnAction(e -> onEliminarProducto());
        modificarProductoButton.setOnAction(e -> onModificarProducto());
        busquedaTextField.textProperty().addListener(e -> onBusquedaProducto());
    }

    private void onBusquedaProducto() {
        listaProductos.clear();
        db.consultaProductosWhere(listaProductos, busquedaTextField.getText());
    }

    private void onModificarProducto() {
        DialogoNuevoProducto dialogoProductoModificado = new DialogoNuevoProducto(JoovaApp.getPrimaryStage());
        NuevoProductoModel aModificar = tablaProductos.getSelectionModel().getSelectedItem();
        dialogoProductoModificado.getController().getModel().setNombreProducto(aModificar.getNombreProducto());
        dialogoProductoModificado.getController().getModel().setDescripcionProducto(aModificar.getDescripcionProducto());

        if (aModificar.getTipoProducto().equals("Aspiradora"))
            dialogoProductoModificado.getController().getAspiradoraRB().setSelected(true);
        else if (aModificar.getTipoProducto().equals("Modulo"))
            dialogoProductoModificado.getController().getModuloRB().setSelected(true);
        else
            dialogoProductoModificado.getController().getOtrosRB().setSelected(true);

        dialogoProductoModificado.getController().getModel().setDireccionImagen(aModificar.getDireccionImagen());
        dialogoProductoModificado.getController().getImagenProducto().setImage(new Image(aModificar.getDireccionImagen()));
        dialogoProductoModificado.getController().getModel().setPrecioProducto(aModificar.getPrecioProducto());

        Optional<nuevoproducto.NuevoProductoModel> resul = dialogoProductoModificado.showAndWait();
        if (resul.isPresent()) {

            aModificar.setNombreProducto(dialogoProductoModificado.getController().getModel().getNombreProducto());
            aModificar.setDescripcionProducto(dialogoProductoModificado.getController().getModel().getDescripcionProducto());
            aModificar.setTipoProducto(dialogoProductoModificado.getController().getModel().getTipoProducto());
            aModificar.setPrecioProducto(dialogoProductoModificado.getController().getModel().getPrecioProducto());
            aModificar.setDireccionImagen(dialogoProductoModificado.getController().getModel().getDireccionImagen());
            db.updateProducto(aModificar);

            PrecioModel precioModel = new PrecioModel();
            precioModel.setCodArticulo(aModificar.getCodArticulo());
            precioModel.setPrecioArticulo(aModificar.getPrecioProducto());
            precioModel.setFechaCambio(LocalDate.now());
            try {
                db.insertPrecio(precioModel);
                actualizarProductos();
            } catch (SQLException e) {
                String error = "[SQLITE_CONSTRAINT]  Abort due to constraint violation (UNIQUE constraint failed: Historico_Precios.Cod_Articulo, Historico_Precios.Precio, Historico_Precios.Fecha)";
                if (!e.getMessage().equals(error))
                    e.printStackTrace();
            }
        }
    }

    private void onAnadirProducto() {
        DialogoNuevoProducto nuevoProducto = new DialogoNuevoProducto(JoovaApp.getPrimaryStage());
        Optional<nuevoproducto.NuevoProductoModel> resul = nuevoProducto.showAndWait();
        if (resul.isPresent()) {
            NuevoProductoModel productoInsertado = new NuevoProductoModel();
            productoInsertado.setNombreProducto(resul.get().getNombreProducto());
            productoInsertado.setDescripcionProducto(resul.get().getDescripcionProducto());
            productoInsertado.setTipoProducto(resul.get().getTipoProducto());
            productoInsertado.setDireccionImagen(resul.get().getDireccionImagen());
            db.insertProduct(productoInsertado);


            int codProducto = listaProductos.get(listaProductos.getSize() - 1).getCodArticulo();
            PrecioModel precioModel = new PrecioModel();
            precioModel.setCodArticulo(codProducto);
            precioModel.setPrecioArticulo(resul.get().getPrecioProducto());
            precioModel.setFechaCambio(LocalDate.now());
            actualizarProductos();
            try {
                db.insertPrecio(precioModel);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private void onEliminarProducto() {
        db.deleteArticulo(tablaProductos.getSelectionModel().getSelectedItem().getCodArticulo());
        actualizarProductos();
    }

    private void actualizarProductos() {
        listaProductos.clear();
        db.consultaTodosProductos(listaProductos);
    }

    public NuevoProductoModel getModel() {
        return model;
    }

    public ListProperty<NuevoProductoModel> listaProductosProperty() {
        return listaProductos;
    }

    public BorderPane getRootProductos() {
        return rootProductos;
    }
}
