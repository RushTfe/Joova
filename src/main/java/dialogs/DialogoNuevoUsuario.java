package dialogs;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import signup.SignUpController;
import signup.SignUpModel;

public class DialogoNuevoUsuario extends Dialog<SignUpModel> {
    private SignUpController signup;
//    private ButtonType crearButtonType;

    public DialogoNuevoUsuario(Stage stage) {
        initOwner(stage);
        signup = new SignUpController();
//        crearButtonType = new ButtonType("Añadir", ButtonBar.ButtonData.OK_DONE);

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().setContent(signup);
// TODO        getDialogPane().getStylesheets().addAll("Hoja de estilos");
        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(signup.getModel().nuevoUserPermitidoProperty());
        setTitle("Nuevo usuario");
        setResultConverter(dialogButton -> {

            if (dialogButton == ButtonType.OK) {
                return signup.getModel();
            }
            return null;
        });
    }

}
