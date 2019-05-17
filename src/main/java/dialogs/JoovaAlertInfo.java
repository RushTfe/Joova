package dialogs;

import app.JoovaApp;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class JoovaAlertInfo {
    private Alert alert;

    public JoovaAlertInfo(String title, String header, String content) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initOwner(JoovaApp.getPrimaryStage());
    }

    public void show() {
        alert.show();
    }

    public Optional<ButtonType> showAndWait() {
        return alert.showAndWait();
    }
}
