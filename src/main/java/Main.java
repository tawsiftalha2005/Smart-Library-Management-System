import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import service.BookService;
import service.BorrowService;
import service.MemberService;
import ui.BookView;
import ui.BorrowView;
import ui.DashboardView;
import ui.MemberView;
import utils.FileManager;

/** JavaFX entry point. Business data is shared by every GUI screen. */
public class Main extends Application {
    private final BookService bookService = new BookService();
    private final MemberService memberService = new MemberService();
    private final BorrowService borrowService = new BorrowService();
    private BorderPane root;
    private DashboardView dashboard;

    @Override public void start(Stage stage) {
        FileManager.initializeFiles();
        root = new BorderPane();
        root.setTop(createHeader());
        root.setLeft(createSidebar(stage));
        showDashboard();
        Scene scene = new Scene(root, 1280, 780);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setTitle("Smart Library Management System");
        stage.setMinWidth(1000); stage.setMinHeight(650); stage.setScene(scene); stage.show();
    }

    private HBox createHeader() {
        Label title = new Label("Smart Library Management System"); title.getStyleClass().add("app-title");
        Label subtitle = new Label("Library operations dashboard"); subtitle.getStyleClass().add("header-subtitle");
        VBox labels = new VBox(2, title, subtitle);
        HBox header = new HBox(labels); header.setAlignment(Pos.CENTER_LEFT); header.setPadding(new Insets(18, 28, 18, 28));
        header.getStyleClass().add("header"); return header;
    }

    private VBox createSidebar(Stage stage) {
        VBox sidebar = new VBox(8); sidebar.setPadding(new Insets(20, 14, 20, 14)); sidebar.getStyleClass().add("sidebar");
        Label navigation = new Label("NAVIGATION"); navigation.getStyleClass().add("nav-caption");
        Button dashboardButton = navButton("Dashboard", this::showDashboard);
        Button booksButton = navButton("Books", () -> root.setCenter(new BookView(bookService, this::showDashboard)));
        Button membersButton = navButton("Members", () -> root.setCenter(new MemberView(memberService, this::showDashboard)));
        Button borrowButton = navButton("Borrow / Return", () -> root.setCenter(new BorrowView(bookService, memberService, borrowService, false, this::showDashboard)));
        Button recordsButton = navButton("Borrow Records", () -> root.setCenter(new BorrowView(bookService, memberService, borrowService, true, this::showDashboard)));
        Region spacer = new Region(); VBox.setVgrow(spacer, Priority.ALWAYS);
        Button exit = navButton("Exit", () -> confirmExit(stage)); exit.getStyleClass().add("exit-button");
        sidebar.getChildren().addAll(navigation, dashboardButton, booksButton, membersButton, borrowButton, recordsButton, spacer, exit);
        return sidebar;
    }
    private Button navButton(String text, Runnable action) { Button button = new Button(text); button.setMaxWidth(Double.MAX_VALUE); button.setOnAction(e -> action.run()); button.getStyleClass().add("nav-button"); return button; }
    private void showDashboard() { dashboard = new DashboardView(bookService, memberService, borrowService, () -> root.setCenter(new BookView(bookService, this::showDashboard)), () -> root.setCenter(new MemberView(memberService, this::showDashboard)), () -> root.setCenter(new BorrowView(bookService, memberService, borrowService, false, this::showDashboard))); root.setCenter(dashboard); }
    private void confirmExit(Stage stage) { Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Close the application?", ButtonType.YES, ButtonType.NO); alert.setHeaderText("Exit Smart Library"); if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) stage.close(); }
    public static void main(String[] args) { launch(args); }
}
