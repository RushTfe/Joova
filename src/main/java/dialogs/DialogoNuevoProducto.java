package dialogs;

import NuevoCliente.NuevoClienteModel;
import app.JoovaApp;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import nuevoproducto.NuevoProductoController;
import nuevoproducto.NuevoProductoModel;

public class DialogoNuevoProducto extends Dialog<NuevoProductoModel> {
    private NuevoProductoController controller;

    public DialogoNuevoProducto(Stage stage) {
        initOwner(stage);
        setTitle("Añadir un nuevo producto");
        controller = new NuevoProductoController(JoovaApp.getPrimaryStage());
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().setContent(controller);
        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(controller.getModel().listoParaInsertarProperty());
// TODO       getDialogPane().getStylesheets().addAll("Hoja de estilos");

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return controller.getModel();
            }
            return null;
        });
    }

    public NuevoProductoController getController() {
        return controller;
    }
}
