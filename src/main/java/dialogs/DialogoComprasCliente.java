package dialogs;

import app.JoovaApp;
import javafx.beans.property.ListProperty;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.NuevoProductoModel;

import javax.swing.text.html.ImageView;

public class DialogoComprasCliente extends Dialog {
    private TableView<NuevoProductoModel> tablaProductos;
    private TableColumn<NuevoProductoModel, ImageView> imagenColumn;
    private TableColumn<NuevoProductoModel, String> nombreArticuloColumn;
    private TableColumn<NuevoProductoModel, String> descripcionArticuloColumn;
    private TableColumn<NuevoProductoModel, String> tipoArticuloColumn;

    public DialogoComprasCliente(ListProperty<NuevoProductoModel> listaProductos) {
        tablaProductos = new TableView<>();
        getDialogPane().getStylesheets().addAll("css/joovaAlert.css", "css/productosView.css");
        imagenColumn = new TableColumn<>();
        nombreArticuloColumn = new TableColumn<>("Nombre");
        descripcionArticuloColumn = new TableColumn<>("Descripción");
        tipoArticuloColumn = new TableColumn<>("Tipo");

        tablaProductos.getColumns().add(0, imagenColumn);
        tablaProductos.getColumns().add(1, nombreArticuloColumn);
        tablaProductos.getColumns().add(2, descripcionArticuloColumn);
        tablaProductos.getColumns().add(3, tipoArticuloColumn);

        tablaProductos.itemsProperty().bind(listaProductos);
        tablaProductos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaProductos.setPrefWidth(900);
        tablaProductos.setPrefHeight(600);

        nombreArticuloColumn.setCellValueFactory(v -> v.getValue().nombreProductoProperty());
        tipoArticuloColumn.setCellValueFactory(v -> v.getValue().tipoProductoProperty());
        descripcionArticuloColumn.setCellValueFactory(v -> v.getValue().descripcionProductoProperty());
        imagenColumn.setCellValueFactory(new PropertyValueFactory<NuevoProductoModel, ImageView>("imagen"));

        tablaProductos.setPadding(new Insets(5));

        initOwner(JoovaApp.getPrimaryStage());

        getDialogPane().getButtonTypes().addAll(ButtonType.OK);

        getDialogPane().setContent(tablaProductos);
    }
}
