package dialogs;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import signup.SignUpController;
import signup.SignUpModel;

public class DialogoNuevoUsuario extends Dialog<SignUpModel> {
    private SignUpController signup;
    private ButtonType crearButtonType;

    public DialogoNuevoUsuario() {
        signup = new SignUpController();
        crearButtonType = new ButtonType("Añadir", ButtonBar.ButtonData.OK_DONE);

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().setContent(signup);
        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(signup.getModel().nuevoUserPermitidoProperty());

        setResultConverter(dialogButton -> {

            if (dialogButton == ButtonType.OK) {
                return signup.getModel();
            }
            return null;
        });
    }

}
