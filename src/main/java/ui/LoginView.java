package ui;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.util.prefs.Preferences;

/** Sign-in presentation shown before the library workspace. */
public final class LoginView extends BorderPane {
    private static final String ADMIN_USERNAME = "admin@smartlibrary.edu";
    private static final String ADMIN_PASSWORD = "admin123";
    private final Runnable onSuccess;
    private final Preferences preferences = Preferences.userNodeForPackage(MainLayout.class);
    private final StackPane card = new StackPane();
    private Canvas mascotCanvas;
    private StackPane mascotFigure;
    private StackPane mascotArea;
    private TranslateTransition idleBounce;
    private final TextField username = new TextField();
    private final PasswordField password = new PasswordField();
    private final TextField visiblePassword = new TextField();
    private final Button submit = new Button("Log In");
    private final Label speech = new Label();
    private boolean dark;
    private boolean busy;

    public LoginView(Runnable onSuccess) {
        this.onSuccess = onSuccess;
        dark = preferences.getBoolean("darkTheme", false);
        getStyleClass().add("login-screen");
        applyTheme();

        Button theme = new Button();
        theme.getStyleClass().add("login-theme-button");
        updateThemeText(theme);
        theme.setOnAction(e -> { dark = !dark; preferences.putBoolean("darkTheme", dark); applyTheme(); updateThemeText(theme); });
        StackPane top = new StackPane(theme);
        StackPane.setAlignment(theme, Pos.TOP_RIGHT);
        top.setPadding(new Insets(26, 30, 0, 30));
        setTop(top);

        buildCard();
        StackPane cardHolder = new StackPane(card);
        cardHolder.setAlignment(Pos.CENTER);
        ScrollPane scroll = new ScrollPane(cardHolder);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.getStyleClass().add("login-scroll");
        setCenter(scroll);

        Label footer = new Label("SECURE ACCESS FOR LIBRARY STAFF");
        footer.getStyleClass().add("login-footer");
        StackPane footerBox = new StackPane(footer);
        footerBox.setPadding(new Insets(0, 0, 17, 0));
        setBottom(footerBox);

        Button help = new Button("?");
        help.getStyleClass().add("login-help");
        help.setTooltip(new Tooltip("Library staff sign-in"));
        help.setOnAction(e -> UiSupport.info("Sign-in help", "Enter your library staff username and password."));
        StackPane overlay = new StackPane(help);
        StackPane.setAlignment(help, Pos.BOTTOM_RIGHT);
        overlay.setPadding(new Insets(0, 18, 12, 0));
        setRight(overlay);
    }

    private void buildCard() {
        card.getStyleClass().add("login-card");
        VBox content = new VBox(9);
        content.setPadding(new Insets(20, 30, 18, 30));
        content.setAlignment(Pos.TOP_CENTER);
        mascotFigure = mascot();
        HBox brand = new HBox(15, brandMark(), brandText());
        brand.setAlignment(Pos.CENTER);
        Label title = new Label("Welcome back"); title.getStyleClass().add("login-title");
        Label subtitle = new Label("Sign in to continue to your library workspace."); subtitle.getStyleClass().add("login-subtitle");
        VBox intro = new VBox(4, title, subtitle); intro.setAlignment(Pos.CENTER);

        VBox usernameBox = field("Email or Username", username);
        username.setPromptText("admin@smartlibrary.edu");
        username.getStyleClass().add("login-input");
        password.setPromptText("Enter your password"); password.getStyleClass().add("login-input");
        visiblePassword.setPromptText("Enter your password"); visiblePassword.getStyleClass().add("login-input");
        visiblePassword.setManaged(false); visiblePassword.setVisible(false);
        Button eye = new Button("◎"); eye.getStyleClass().add("password-eye"); eye.setTooltip(new Tooltip("Show password"));
        StackPane passwordStack = new StackPane(password, visiblePassword);
        HBox passwordRow = new HBox(passwordStack, eye); passwordRow.getStyleClass().add("password-row");
        HBox.setHgrow(passwordStack, Priority.ALWAYS);
        passwordStack.setMaxWidth(Double.MAX_VALUE);
        eye.setOnAction(e -> {
            if (password.isVisible()) { visiblePassword.setText(password.getText()); password.setVisible(false); password.setManaged(false); visiblePassword.setVisible(true); visiblePassword.setManaged(true); eye.setTooltip(new Tooltip("Hide password")); }
            else { password.setText(visiblePassword.getText()); visiblePassword.setVisible(false); visiblePassword.setManaged(false); password.setVisible(true); password.setManaged(true); eye.setTooltip(new Tooltip("Show password")); }
        });
        VBox passwordBox = new VBox(7, label("Password"), passwordRow);
        CheckBox remember = new CheckBox("Remember me"); remember.setSelected(true); remember.getStyleClass().add("login-remember");
        submit.getStyleClass().add("login-submit"); submit.setMaxWidth(Double.MAX_VALUE); submit.setOnAction(e -> signIn());
        Button forgot = new Button("Forgot password?"); forgot.getStyleClass().add("login-link");
        forgot.setOnAction(e -> UiSupport.info("Password help", "Contact your library administrator to reset your password."));

        speech.getStyleClass().add("mascot-speech"); speech.setVisible(false); speech.setManaged(false);
        mascotArea = new StackPane(mascotFigure, speech); mascotArea.setMinHeight(132);
        StackPane.setAlignment(speech, Pos.TOP_CENTER); StackPane.setMargin(speech, new Insets(0,0,0,0));
        content.getChildren().addAll(mascotArea, brand, intro, usernameBox, passwordBox, remember, submit, forgot);
        card.getChildren().add(content);
        card.setPrefWidth(460);
        card.setMaxWidth(480);
        card.setMinWidth(0);
        username.textProperty().addListener((o,a,b)->resetError());
        password.textProperty().addListener((o,a,b)->resetError());
        visiblePassword.textProperty().addListener((o,a,b)->resetError());
        idleBounce = new TranslateTransition(Duration.millis(1050), mascotFigure);
        idleBounce.setFromY(0); idleBounce.setToY(-5); idleBounce.setCycleCount(TranslateTransition.INDEFINITE); idleBounce.setAutoReverse(true);
        mascotFigure.sceneProperty().addListener((o,oldScene,newScene)->{if(newScene==null)idleBounce.stop();else if(!busy)idleBounce.playFromStart();});
    }

    private VBox field(String name, TextField field) { field.getStyleClass().add("login-input"); return new VBox(7, label(name), field); }
    private Label label(String text) { Label l = new Label(text); l.getStyleClass().add("login-label"); return l; }
    private StackPane brandMark() { Label icon = new Label("▮▯"); icon.getStyleClass().add("login-brand-mark"); StackPane box = new StackPane(icon); box.setMinSize(60,60); box.setPrefSize(60,60); box.setMaxSize(60,60); return box; }
    private VBox brandText() { Label name = new Label("Smart Library"); name.getStyleClass().add("login-brand-name"); Label sub = new Label("Management System"); sub.getStyleClass().add("login-brand-subtitle"); return new VBox(2,name,sub); }

    private StackPane mascot() {
        mascotCanvas = new Canvas(180, 132);
        StackPane holder = new StackPane(mascotCanvas); holder.setMinHeight(132); holder.setMaxHeight(132);
        drawMascot(false, false);
        return holder;
    }

    private void drawMascot(boolean error, boolean success) {
        GraphicsContext g = mascotCanvas.getGraphicsContext2D();
        g.setTransform(1, 0, 0, 1, 0, 0);
        g.clearRect(0, 0, mascotCanvas.getWidth(), mascotCanvas.getHeight());
        g.translate(5, 1); g.scale(0.74, 0.74);
        Color purple = Color.web("#6861ed"), darkPurple = Color.web("#4945c8"), face = Color.web("#f5f5ff");
        javafx.scene.paint.LinearGradient shell = new javafx.scene.paint.LinearGradient(0,0,0,1,true,javafx.scene.paint.CycleMethod.NO_CYCLE,
                new javafx.scene.paint.Stop(0,Color.web("#827bfa")),new javafx.scene.paint.Stop(1,Color.web("#5e57df")));
        // antenna and rounded robot silhouette
        g.setStroke(purple); g.setLineWidth(9); g.strokeLine(110,17,110,35); g.setFill(purple); g.fillOval(102,5,16,16);
        g.setLineWidth(10); g.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        if (success) { g.strokeLine(61,93,39,67); g.strokeLine(39,67,29,48); g.strokeLine(159,93,181,67); g.strokeLine(181,67,191,48); }
        else { g.strokeLine(61,93,36,123); g.strokeLine(159,93,184,123); }
        g.strokeLine(78,147,75,164); g.strokeLine(142,147,145,164);
        g.setFill(Color.color(0.18,0.16,0.58,0.10)); g.fillOval(70,158,80,13);
        g.setFill(shell); g.fillRoundRect(56,32,108,92,34,34); g.setFill(face); g.fillRoundRect(67,43,86,68,25,25);
        g.setFill(darkPurple); g.fillOval(83,66,16,17); g.fillOval(121,66,16,17);
        g.setFill(Color.WHITE); g.fillOval(87,68,5,5); g.fillOval(125,68,5,5);
        g.setStroke(darkPurple); g.setLineWidth(4);
        if (error) { g.strokeLine(82,62,99,67); g.strokeLine(123,67,140,62); g.strokeArc(98,85,24,16,25,130, javafx.scene.shape.ArcType.OPEN); g.setFill(Color.web("#f2a0b1")); g.fillOval(71,89,15,7); g.fillOval(134,89,15,7); }
        else g.strokeArc(98,78,24,16,205,130, javafx.scene.shape.ArcType.OPEN);
        g.setFill(shell); g.fillRoundRect(77,116,66,40,16,16);
        g.setFill(face); g.fillRoundRect(98,127,25,13,5,5); g.setFill(purple); g.fillOval(105,130,6,7);
    }

    private void signIn() {
        if (busy) return;
        String enteredUsername = username.getText().trim();
        if (!(ADMIN_USERNAME.equalsIgnoreCase(enteredUsername) || "admin".equalsIgnoreCase(enteredUsername))
                || !ADMIN_PASSWORD.equals(currentPassword())) {
            busy = true; idleBounce.stop(); speech.setText("Oops, that's not right!"); speech.setVisible(true); speech.setManaged(true); card.getStyleClass().add("login-error"); drawMascot(true, false);
            Timeline shake = new Timeline(new KeyFrame(Duration.ZERO,new KeyValue(card.translateXProperty(),0)),new KeyFrame(Duration.millis(80),new KeyValue(card.translateXProperty(),-9)),new KeyFrame(Duration.millis(160),new KeyValue(card.translateXProperty(),9)),new KeyFrame(Duration.millis(240),new KeyValue(card.translateXProperty(),-6)),new KeyFrame(Duration.millis(320),new KeyValue(card.translateXProperty(),6)),new KeyFrame(Duration.millis(400),new KeyValue(card.translateXProperty(),0)));
            shake.setOnFinished(e->busy=false);shake.play();return;
        }
        busy = true; idleBounce.stop(); speech.setText("Welcome back!"); speech.setVisible(true); speech.setManaged(true);
        drawMascot(false, true);
        playSuccessConfetti();
        card.getStyleClass().remove("login-error"); card.getStyleClass().add("login-success");
        submit.setText("✓   Signing you in"); submit.setDisable(true);
        PauseTransition pause = new PauseTransition(Duration.millis(1500)); pause.setOnFinished(e -> onSuccess.run()); pause.play();
    }
    private void resetError(){if(!card.getStyleClass().contains("login-error")||busy)return;card.getStyleClass().remove("login-error");speech.setVisible(false);speech.setManaged(false);drawMascot(false,false);if(idleBounce!=null&&mascotFigure.getScene()!=null)idleBounce.playFromStart();}
    private void playSuccessConfetti(){java.util.Random random=new java.util.Random(7);ParallelTransition burst=new ParallelTransition();java.util.List<Circle> pieces=new java.util.ArrayList<>();Color[] colors={Color.web("#5142d4"),Color.web("#8b83ff"),Color.web("#f0b94e"),Color.web("#42b89c")};for(int i=0;i<18;i++){Circle piece=new Circle(3+random.nextDouble()*2,colors[i%colors.length]);pieces.add(piece);mascotArea.getChildren().add(piece);StackPane.setAlignment(piece,Pos.CENTER);piece.setTranslateX(random.nextDouble()*190-95);piece.setTranslateY(-15-random.nextDouble()*38);TranslateTransition fall=new TranslateTransition(Duration.millis(850+random.nextDouble()*450),piece);fall.setByY(75+random.nextDouble()*40);fall.setByX(random.nextDouble()*70-35);FadeTransition fade=new FadeTransition(Duration.millis(1000),piece);fade.setFromValue(1);fade.setToValue(0);burst.getChildren().add(new ParallelTransition(fall,fade));}TranslateTransition cheer=new TranslateTransition(Duration.millis(240),mascotFigure);cheer.setByY(-12);cheer.setAutoReverse(true);cheer.setCycleCount(2);burst.getChildren().add(cheer);burst.setOnFinished(e->mascotArea.getChildren().removeAll(pieces));burst.play();}
    private String currentPassword() { return password.isVisible() ? password.getText() : visiblePassword.getText(); }
    private void applyTheme() { getStyleClass().removeAll("login-light", "login-dark"); getStyleClass().add(dark ? "login-dark" : "login-light"); }
    private void updateThemeText(Button button) { button.setText(dark ? "☾  Dark" : "☼  Light"); }
}
