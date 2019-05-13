package dialogs;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.TipoPagoyEventoModel;

public class DialogoNuevoTipoPagoyEvento extends Dialog<TipoPagoyEventoModel> {
    private TipoPagoyEventoModel model;
    private TextField nombreTipoPago;
    private TextArea descripcionTipoPago;
    private VBox cajaTextos;

    public DialogoNuevoTipoPagoyEvento(Stage stage) {
// TODO     getDialogPane().getStylesheets().addAll("Direccion.css");

        // Modelo con la informacion a devolver
        model = new TipoPagoyEventoModel();

        // titulo de la ventana
        setTitle("Añadir nuevo tipo de pago");
        initOwner(stage);

        // Objetos dentro del dialogo
        nombreTipoPago = new TextField();
        descripcionTipoPago = new TextArea();
        cajaTextos = new VBox();

        cajaTextos.setSpacing(5d);
        cajaTextos.setPadding(new Insets(5));

        // Bindeos de la vista con el modelo
        model.nombreTipoPagoProperty().bind(nombreTipoPago.textProperty());
        model.descripcionTipoPagoProperty().bind(descripcionTipoPago.textProperty());

        // Anadir los botones a usar.
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(Bindings.when(model.nombreTipoPagoProperty().isEqualTo("")).then(true).otherwise(false));

        // Textos descriptivos
        nombreTipoPago.setPromptText("Tipo de Pago");
        descripcionTipoPago.setPromptText("Descripcion sobre el tipo de pago..... (Opcional)");

        // Anadis los objetos a el cajon
        cajaTextos.getChildren().add(nombreTipoPago);
        cajaTextos.getChildren().add(descripcionTipoPago);

        // Anadir el cajon al cuadro de Dialogo
        getDialogPane().setContent(cajaTextos);


        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return model;
            }
            return null;
        });
    }

    public TextField getNombreTipo() {
        return nombreTipoPago;
    }

    public TextArea getDescripcionTipo() {
        return descripcionTipoPago;
    }
}
