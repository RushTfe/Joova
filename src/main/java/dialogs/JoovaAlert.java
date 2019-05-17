package dialogs;

import app.JoovaApp;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class JoovaAlert {
    public static void alertError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(JoovaApp.getPrimaryStage());
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(header);
        alert.show();
    }

    public static void alertInfo(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(JoovaApp.getPrimaryStage());
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(header);
        alert.show();
    }

    public static Optional<ButtonType> alertConf(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(JoovaApp.getPrimaryStage());
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(header);
        return alert.showAndWait();
    }
}
