package dialogs;

import controller.NuevoClienteController;
import javafx.beans.property.ListProperty;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.ClienteModel;
import model.NuevoProductoModel;

public class DialogoNuevoCliente extends Dialog<ClienteModel> {
    private NuevoClienteController root;
    private ClienteModel model;

    public DialogoNuevoCliente(Stage stage, ListProperty<NuevoProductoModel> listaProductosExt) {
        root = new NuevoClienteController(listaProductosExt);
        model = root.getModel();
        initOwner(stage);
        setTitle("Añadir un nuevo cliente");

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().setContent(root.getRootClienteNuevo());
// TODO        getDialogPane().getStylesheets().addAll("Hoja de estilos");


        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return model;
            }
            return null;
        });
    }

    public ClienteModel getModel() {
        return model;
    }

    public TextField getDNIField() {
        return root.getDniField();
    }

    public ComboBox<NuevoProductoModel> getProductosComboBox() {
        return root.getProductosCombobox();
    }
}
