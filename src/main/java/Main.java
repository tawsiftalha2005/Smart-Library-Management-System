import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import service.BookService;
import service.BorrowService;
import service.MemberService;
import ui.MainLayout;
import ui.LoginView;
import utils.FileManager;

/** JavaFX entry point. Business data is shared by every GUI screen. */
public class Main extends Application {
    @Override public void start(Stage stage) {
        FileManager.initializeFiles();
        BookService books = new BookService();
        MemberService members = new MemberService();
        BorrowService borrows = new BorrowService();
        books.loadFromFile(); members.loadFromFile(); borrows.loadFromFile();
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(new LoginView(() -> sceneRoot(stage, new MainLayout(books, members, borrows,
                        () -> showLogin(stage, books, members, borrows)))),
                Math.min(1440, bounds.getWidth()), Math.min(900, bounds.getHeight()));
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/css/login.css").toExternalForm());
        stage.setTitle("Smart Library Management System");
        stage.setMinWidth(1024); stage.setMinHeight(700); stage.setScene(scene);
        stage.setX(bounds.getMinX()); stage.setY(bounds.getMinY());
        stage.show();
        stage.setMaximized(true);
    }
    private void sceneRoot(Stage stage, MainLayout layout) {
        stage.getScene().setRoot(layout);
    }
    private void showLogin(Stage stage, BookService books, MemberService members, BorrowService borrows) {
        stage.getScene().setRoot(new LoginView(() -> sceneRoot(stage, new MainLayout(books, members, borrows,
                () -> showLogin(stage, books, members, borrows)))));
    }
    public static void main(String[] args) {
        if (System.getProperty("os.name", "").toLowerCase(java.util.Locale.ROOT).contains("win")
                && System.getProperty("glass.win.uiScale") == null) {
            System.setProperty("glass.win.uiScale", "90%");
        }
        launch(args);
    }
}
