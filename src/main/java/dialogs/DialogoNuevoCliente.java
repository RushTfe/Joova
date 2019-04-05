package dialogs;

import NuevoCliente.NuevoClienteController;
import NuevoCliente.NuevoClienteModel;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;

public class DialogoNuevoCliente extends Dialog<NuevoClienteModel> {
    private NuevoClienteController controller;
    private NuevoClienteModel nuevoClienteModel;

    public DialogoNuevoCliente() {
        controller = new NuevoClienteController();
        nuevoClienteModel = controller.getModel();

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
