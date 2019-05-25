package dialogs;

import app.JoovaApp;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;

import java.util.Optional;

public class JoovaAlert {
    public static void alertError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.getDialogPane().getStylesheets().addAll("css/joovaAlert.css");
        alert.getDialogPane().setGraphic(new ImageView("css/icons/delete64.png"));
        alert.initOwner(JoovaApp.getPrimaryStage());
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(header);
        alert.show();
    }

    public static void alertInfo(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().getStylesheets().addAll("css/joovaAlert.css");
        alert.getDialogPane().setGraphic(new ImageView("css/icons/info.png"));
        alert.initOwner(JoovaApp.getPrimaryStage());
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(header);
        alert.show();
    }

    public static Optional<ButtonType> alertConf(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().addAll("css/joovaAlert.css");
        alert.getDialogPane().setGraphic(new ImageView("css/icons/info.png"));
        alert.initOwner(JoovaApp.getPrimaryStage());
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(header);
        return alert.showAndWait();
    }
}
