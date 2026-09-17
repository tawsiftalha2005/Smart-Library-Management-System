package ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.BorrowRecord;
import service.BookService;
import service.BorrowService;
import service.MemberService;

public class DashboardView extends ScrollPane {
    public DashboardView(BookService books, MemberService members, BorrowService borrows, Runnable openBooks, Runnable openMembers, Runnable openBorrow) {
        VBox content = new VBox(20); content.setPadding(new Insets(28)); content.getStyleClass().add("content");
        Label title = new Label("Dashboard Overview"); title.getStyleClass().add("page-title");
        Label intro = new Label("Welcome back. Here is a live view of your library."); intro.getStyleClass().add("muted-text");
        HBox cards = new HBox(16, card("Total Books", String.valueOf(books.totalBooks())), card("Total Members", String.valueOf(members.totalMembers())), card("Borrow Records", String.valueOf(borrows.totalBorrowRecords())), card("Available Copies", String.valueOf(books.availableBookCopies())));
        Label quickLabel = new Label("Quick actions"); quickLabel.getStyleClass().add("section-title");
        Button addBook = new Button("Manage books"); addBook.setOnAction(e -> openBooks.run()); Button addMember = new Button("Manage members"); addMember.setOnAction(e -> openMembers.run()); Button borrow = new Button("Borrow or return"); borrow.setOnAction(e -> openBorrow.run());
        HBox quick = new HBox(12, addBook, addMember, borrow); quick.getStyleClass().add("quick-actions");
        Label activityLabel = new Label("Recent borrow activity"); activityLabel.getStyleClass().add("section-title");
        VBox activity = new VBox(8); activity.getStyleClass().add("activity-panel");
        if (borrows.getRecentBorrowRecords(5).isEmpty()) activity.getChildren().add(new Label("No borrowing activity yet. Borrowed books will appear here."));
        else for (BorrowRecord record : borrows.getRecentBorrowRecords(5)) activity.getChildren().add(new Label("Record #" + record.getRecordId() + " · Book #" + record.getBookId() + " · " + (record.isReturned() ? "Returned" : "Borrowed")));
        content.getChildren().addAll(title, intro, cards, quickLabel, quick, activityLabel, activity); setContent(content); setFitToWidth(true); setFitToHeight(true); setHbarPolicy(ScrollBarPolicy.NEVER);
    }
    private VBox card(String label, String value) { Label number = new Label(value); number.getStyleClass().add("card-number"); Label caption = new Label(label); caption.getStyleClass().add("muted-text"); VBox box = new VBox(7, number, caption); box.getStyleClass().add("stat-card"); box.setPrefWidth(190); return box; }
}
