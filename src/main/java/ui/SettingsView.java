package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.function.BooleanSupplier;

/** Workspace and library preferences. Theme choice is applied immediately. */
public class SettingsView extends ScrollPane {
    public SettingsView(Runnable toggleTheme, BooleanSupplier isDark) {
        VBox body = new VBox(18); body.setPadding(new Insets(28)); body.getStyleClass().add("page-content");
        Button save = new Button("Save Changes"); save.setOnAction(e -> UiSupport.info("Settings saved", "Your preferences have been saved."));
        Label heading = new Label("Settings"); heading.getStyleClass().add("page-title");
        Label subtitle = muted("System preferences and configuration");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox title = new HBox(heading, spacer, save); title.setAlignment(Pos.CENTER_LEFT);

        ToggleGroup themes = new ToggleGroup();
        ToggleButton light = themeChoice("☼", "Light"), dark = themeChoice("☾", "Dark"), system = themeChoice("⚙", "System");
        light.setToggleGroup(themes); dark.setToggleGroup(themes); system.setToggleGroup(themes); (isDark.getAsBoolean()?dark:light).setSelected(true);
        themes.selectedToggleProperty().addListener((obs, old, selected) -> { if (selected != null && ((selected == dark) != isDark.getAsBoolean())) toggleTheme.run(); });
        HBox choices = new HBox(10, light, dark, system); choices.getChildren().forEach(n -> HBox.setHgrow(n, Priority.ALWAYS));
        VBox appearance = card("Appearance", "Choose how the interface looks", choices);

        TextField displayName = new TextField("Library Administrator"), email = new TextField("admin@smartlibrary.edu");
        VBox accountFields = new VBox(10, labeled("DISPLAY NAME", displayName), labeled("EMAIL", email));
        VBox account = card("Account", "Manage your profile", accountFields);
        HBox upper = new HBox(18, appearance, account); HBox.setHgrow(appearance, Priority.ALWAYS); HBox.setHgrow(account, Priority.ALWAYS);

        VBox libraryRows = new VBox(
                settingRow("Library Name", "Official name shown across the system", new TextField("Smart University Library")),
                settingRow("Default Borrow Duration", "Number of days before a book is due", new TextField("14 days")),
                settingRow("Max Books per Member", "Borrowing limit per member at one time", new TextField("5")));
        VBox library = card("Library", "Configure borrowing rules and system defaults", libraryRows);
        VBox notifications = card("Notifications", "Control automated email and reminder settings",
                new VBox(notificationRow("Email Notifications", "Confirmations for borrows and returns", true),
                        notificationRow("Overdue Reminders", "Automatic reminders for overdue books", true),
                        notificationRow("Daily Digest", "Summary of library activity each morning", false)));
        body.getChildren().addAll(title, subtitle, upper, library, notifications);
        setContent(body); setFitToWidth(true); setHbarPolicy(ScrollBarPolicy.NEVER);
    }

    private ToggleButton themeChoice(String icon, String text) { ToggleButton b = new ToggleButton(icon + "\n" + text); b.setMaxWidth(Double.MAX_VALUE); b.setPrefHeight(82); return b; }
    private VBox labeled(String label, TextField field) { Label l = new Label(label); l.getStyleClass().add("section-caption"); l.setPadding(new Insets(0)); field.setMaxWidth(Double.MAX_VALUE); return new VBox(7,l,field); }
    private HBox settingRow(String title, String description, TextField field) {
        Label h = new Label(title); h.getStyleClass().add("row-title"); Label sub = muted(description); VBox labels = new VBox(5,h,sub); Region gap = new Region(); HBox.setHgrow(gap,Priority.ALWAYS); field.setPrefWidth(245); HBox row = new HBox(16,labels,gap,field); row.getStyleClass().add("setting-row"); row.setAlignment(Pos.CENTER_LEFT); row.setPadding(new Insets(16,4,16,4)); return row;
    }
    private HBox notificationRow(String title, String description, boolean enabled) { Label h=new Label(title);h.getStyleClass().add("row-title");Label sub=muted(description);VBox labels=new VBox(5,h,sub);Region gap=new Region();HBox.setHgrow(gap,Priority.ALWAYS);CheckBox toggle=new CheckBox();toggle.setSelected(enabled);HBox row=new HBox(16,labels,gap,toggle);row.getStyleClass().add("setting-row");row.setAlignment(Pos.CENTER_LEFT);row.setPadding(new Insets(16,4,16,4));return row; }
    private VBox card(String title, String description, javafx.scene.Node content) { Label h = new Label(title); h.getStyleClass().add("section-title"); VBox head = new VBox(5,h,muted(description)); VBox box = new VBox(18,head,new Separator(),content); box.getStyleClass().add("settings-card"); return box; }
    private Label muted(String text) { Label l = new Label(text); l.getStyleClass().add("muted-text"); return l; }
}
