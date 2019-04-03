package dialogs;

import NuevoCliente.NuevoClienteController;
import NuevoCliente.NuevoClienteModel;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class DialogoNuevoCliente extends Dialog<NuevoClienteModel> {
    NuevoClienteController controller = new NuevoClienteController();

    public DialogoNuevoCliente() {
        controller = new NuevoClienteController();
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().setContent(controller);


        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return controller.getModel();
            }
            return null;
        });
    }


}
