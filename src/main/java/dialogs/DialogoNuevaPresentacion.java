package dialogs;

import app.JoovaApp;
import javafx.beans.property.ListProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.ClienteModel;
import model.PresentacionesModel;

public class DialogoNuevaPresentacion extends Dialog<PresentacionesModel> {
    private PresentacionesModel model;
    private GridPane root;
    private ComboBox<ClienteModel> clientesComboBox;
    private DatePicker fecha;
    private CheckBox venta;
    private TextArea observaciones;

    public DialogoNuevaPresentacion(ListProperty<ClienteModel> listaClientes) {
        // Inicializar todos los objetos
        model = new PresentacionesModel();
        root = new GridPane();
        clientesComboBox = new ComboBox<>();
        fecha = new DatePicker();
        venta = new CheckBox();
        observaciones = new TextArea();

        // Bindeo del ComboBox
        clientesComboBox.itemsProperty().bind(listaClientes);

        // Añadir componentes a root
        root.add(clientesComboBox, 0, 0);
        root.add(venta, 1,0);
        root.add(fecha, 0,1);
        root.add(observaciones, 0, 2);

        // Poniendolo bonito
        root.setAlignment(Pos.CENTER);
        root.setHgap(5d);
        root.setVgap(5d);
        root.setPadding(new Insets(5));
        GridPane.setColumnSpan(observaciones, GridPane.REMAINING);

        clientesComboBox.setPromptText("Clientes");
        fecha.setPromptText("Fecha");
        venta.setText("Hubo venta en la sesión?");
        observaciones.setPromptText("Observaciones.... (Opcionales)");


        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().setContent(root);

        initOwner(JoovaApp.getPrimaryStage());
        setTitle("Añadir una nueva presentacion");

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                model.setCodCliente(clientesComboBox.getSelectionModel().getSelectedItem().getDni());
                model.setNombreCliente(clientesComboBox.getSelectionModel().getSelectedItem().getNombre());
                model.setDireccionCliente(clientesComboBox.getSelectionModel().getSelectedItem().getDireccion());
                model.setObservaciones(observaciones.getText());
                model.setVentaRealizada(venta.isSelected());
                model.setFechaPresentacion(fecha.getValue());
                return model;
            }
            return null;
        });

    }

    public ComboBox<ClienteModel> getClientesComboBox() {
        return clientesComboBox;
    }

    public DatePicker getFecha() {
        return fecha;
    }

    public CheckBox getVenta() {
        return venta;
    }

    public TextArea getObservaciones() {
        return observaciones;
    }
}
