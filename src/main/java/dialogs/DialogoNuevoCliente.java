package dialogs;

import NuevoCliente.NuevoClienteController;
import NuevoCliente.NuevoClienteModel;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DialogoNuevoCliente extends Dialog<NuevoClienteModel> {
    private NuevoClienteController controller;
    private NuevoClienteModel nuevoClienteModel;

    public DialogoNuevoCliente(Stage stage) {
        controller = new NuevoClienteController();
        nuevoClienteModel = controller.getModel();
        initOwner(stage);
        setTitle("Añadir un nuevo cliente");

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().setContent(controller);
// TODO        getDialogPane().getStylesheets().addAll("Hoja de estilos");


        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return controller.getModel();
            }
            return null;
        });
    }

    public TextField getDNIField() {
        return controller.getDniField();
    }

    public NuevoClienteModel getNuevoClienteModel() {
        return nuevoClienteModel;
    }

    public NuevoClienteController getController() {
        return controller;
    }
}
