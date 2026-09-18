import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.BookService;
import service.BorrowService;
import service.MemberService;
import ui.MainLayout;
import utils.FileManager;

/** JavaFX entry point. Business data is shared by every GUI screen. */
public class Main extends Application {
    @Override public void start(Stage stage) {
        FileManager.initializeFiles();
        Scene scene = new Scene(new MainLayout(new BookService(), new MemberService(), new BorrowService()), 1440, 900);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setTitle("Smart Library Management System");
        stage.setMinWidth(1024); stage.setMinHeight(700); stage.setScene(scene); stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
