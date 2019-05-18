package util;

import javafx.fxml.FXMLLoader;

import java.time.LocalDate;

public class JoovaUtil {
    public JoovaUtil() {
    }

    public static LocalDate stringToLocalDate(String fecha) {
        String [] tablaFecha = fecha.split("-");

        return LocalDate.of(Integer.valueOf(tablaFecha[0]), Integer.valueOf(tablaFecha[1]), Integer.valueOf(tablaFecha[2]));
    }
}
