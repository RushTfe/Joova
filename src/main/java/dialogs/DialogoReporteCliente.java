package dialogs;

import controller.ReporteClienteController;
import database.HooverDataBase;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import model.*;

public class DialogoReporteCliente extends Dialog<ReporteClienteModel> {
    private ReporteClienteController root;
    private Stage primaryStage;
    private ListProperty<PMyPresentacionesModel> listaPuestasEnMarcha;
    private ListProperty<PMyPresentacionesModel> listaPresentaciones;
    private ListProperty<AccionEspecialModel> listaAccionesEspeciales;
    private ListProperty<ExperienciaModel> listaExperiencias;

    public DialogoReporteCliente(Stage primaryStage, HooverDataBase db, ClienteModel clienteModel) {
        root = new ReporteClienteController(primaryStage, clienteModel);
        root.setCliente(clienteModel);
        this.primaryStage = primaryStage;

        listaPuestasEnMarcha = new SimpleListProperty(this, "listaPuestasEnMarcha", FXCollections.observableArrayList());
        listaPresentaciones = new SimpleListProperty<>(this, "listaPresentaciones", FXCollections.observableArrayList());
        listaAccionesEspeciales = new SimpleListProperty<>(this, "listAccionesespeciales", FXCollections.observableArrayList());
        listaExperiencias = new SimpleListProperty<>(this, "listaExperiencias", FXCollections.observableArrayList());
        db.consultaTodasPuestasMarcha(listaPuestasEnMarcha);
        db.consultaTodasPresentaciones(listaPresentaciones);
        db.consultaTodasAccionesEspeciales(listaAccionesEspeciales);
        db.consultaTodasExperiencias(listaExperiencias);
        root.getModel().puestaEnMarchaModelProperty().bind(listaPuestasEnMarcha);
        root.getModel().presentacionesModelsProperty().bind(listaPresentaciones);
        root.getModel().accionEspecialModelProperty().bind(listaAccionesEspeciales);
        root.getModel().experienciaModelProperty().bind(listaExperiencias);

        try {
            root.setFecha(listaPuestasEnMarcha.get(0).getFechaEvento().toString());
        } catch (IndexOutOfBoundsException e) {
            root.setFecha("Cliente aún no tiene presentación");
        }
        initOwner(primaryStage);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK);

        getDialogPane().setContent(root);

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK)
                return root.getModel();
            return null;
        });
    }
}
