package ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

final class UiSupport {
    private UiSupport() { }
    static void info(String title, String message) { alert(Alert.AlertType.INFORMATION, title, message); }
    static void warning(String title, String message) { alert(Alert.AlertType.WARNING, title, message); }
    static void error(String title, String message) { alert(Alert.AlertType.ERROR, title, message); }
    static boolean confirm(String title, String message) { Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO); alert.setHeaderText(title); return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES; }
    static Integer positiveId(TextField field, String name) { try { int value = Integer.parseInt(field.getText().trim()); if (value > 0) return value; } catch (NumberFormatException ignored) { } warning("Invalid " + name, name + " must be a positive whole number."); return null; }
    static boolean textPresent(TextField... fields) { for (TextField field : fields) if (field.getText().trim().isEmpty()) { warning("Missing information", "Please complete every field."); return false; } return true; }
    private static void alert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setHeaderText(title);
        alert.showAndWait();
    }
}
