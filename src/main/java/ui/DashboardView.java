package ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import model.Book;
import model.BorrowRecord;
import model.Member;
import service.BookService;
import service.BorrowService;
import service.MemberService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Live dashboard figures and activity are derived from the shared service state. */
public class DashboardView extends ScrollPane {
    private final BookService books;
    private final MemberService members;
    private final BorrowService borrows;

    public DashboardView(BookService books, MemberService members, BorrowService borrows,
                         Runnable openBooks, Runnable openMembers, Runnable openBorrow) {
        this.books = books;
        this.members = members;
        this.borrows = borrows;
        VBox body = new VBox(20);
        body.setPadding(new Insets(26, 30, 30, 30));
        body.getStyleClass().add("page-content");

        Label greeting = new Label("Good morning, Administrator");
        greeting.getStyleClass().add("page-title");
        Label intro = muted("Here's what's happening in your library today.");
        VBox welcome = new VBox(5, greeting, intro);

        HBox cards = new HBox(14,
                stat("▣", "Book titles", books.totalBooks(), books.availableBookCopies() + " total copies"),
                stat("♙", "Members", members.totalMembers(), "Registered members"),
                stat("↗", "Currently borrowed", active(), "Copies on active loan"),
                stat("!", "Overdue books", overdue(), "Past their due date"));
        cards.getChildren().forEach(node -> HBox.setHgrow(node, Priority.ALWAYS));

        HBox quick = new HBox(10, button("Manage books", openBooks), button("View members", openMembers), button("Borrow a book", openBorrow));
        VBox activity = new VBox(8, section("BORROWING ACTIVITY"), chart());
        activity.getStyleClass().add("panel");
        HBox lower = new HBox(16, catalog(), recent());
        HBox.setHgrow(lower.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(lower.getChildren().get(1), Priority.ALWAYS);
        body.getChildren().addAll(welcome, cards, quick, activity, lower, overduePanel());
        setContent(body);
        setFitToWidth(true);
        setHbarPolicy(ScrollBarPolicy.NEVER);
    }

    private VBox stat(String icon, String title, int value, String hint) {
        Label i = new Label(icon); i.getStyleClass().add("stat-icon");
        Label t = new Label(title); t.getStyleClass().add("stat-title");
        Label v = new Label(String.valueOf(value)); v.getStyleClass().add("stat-value");
        VBox card = new VBox(7, i, t, v, muted(hint));
        card.getStyleClass().add("stat-card");
        card.setMaxWidth(Double.MAX_VALUE);
        return card;
    }

    private Node chart() {
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();
        x.setLabel("Borrow date"); y.setLabel("Records");
        BarChart<String, Number> chart = new BarChart<>(x, y);
        chart.setLegendVisible(false); chart.setAnimated(false); chart.setPrefHeight(220);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter labelFormat = DateTimeFormatter.ofPattern("MMM d");
        for (int offset = 6; offset >= 0; offset--) {
            LocalDate day = today.minusDays(offset);
            int count = (int) borrows.getBorrowRecords().stream().filter(record -> parseDate(record.getBorrowDate()).map(day::equals).orElse(false)).count();
            series.getData().add(new XYChart.Data<>(day.format(labelFormat), count));
        }
        chart.getData().add(series);
        return chart;
    }

    private VBox catalog() {
        VBox box = new VBox(12, section("BOOK CATALOG")); box.getStyleClass().add("panel");
        if (books.getBooks().isEmpty()) box.getChildren().add(muted("No books in the catalog yet."));
        else books.getBooks().stream().limit(4).forEach(book -> {
            Label icon = new Label("▣"); icon.getStyleClass().add("book-cover");
            Label title = new Label(book.getTitle()); title.getStyleClass().add("row-title");
            Label detail = muted(book.getAuthor() + " · " + book.getQuantity() + " available");
            box.getChildren().add(new HBox(10, icon, new VBox(3, title, detail)));
        });
        return box;
    }

    private VBox recent() {
        VBox box = new VBox(12, section("RECENT BORROWING ACTIVITY")); box.getStyleClass().add("panel");
        List<BorrowRecord> records = borrows.getRecentBorrowRecords(4);
        if (records.isEmpty()) box.getChildren().add(muted("No borrowing activity yet."));
        else records.forEach(record -> {
            Label title = new Label(member(record.getMemberId()) + " · " + book(record.getBookId())); title.getStyleClass().add("row-title");
            box.getChildren().add(new VBox(3, title, muted("Borrowed " + record.getBorrowDate() + " · Due " + record.getReturnDate() + " · " + status(record))));
        });
        return box;
    }

    private VBox overduePanel() {
        VBox box = new VBox(12, section("OVERDUE BOOKS")); box.getStyleClass().add("panel");
        List<BorrowRecord> overdueRecords = borrows.getBorrowRecords().stream().filter(record -> "Overdue".equals(status(record))).toList();
        if (overdueRecords.isEmpty()) box.getChildren().add(muted("No overdue books right now."));
        else overdueRecords.forEach(record -> {
            long days = parseDate(record.getReturnDate()).map(due -> java.time.temporal.ChronoUnit.DAYS.between(due, LocalDate.now())).orElse(0L);
            Label row = new Label(member(record.getMemberId()) + " · " + book(record.getBookId()) + " · due " + record.getReturnDate() + " · " + days + " days overdue");
            row.getStyleClass().add("overdue-line"); box.getChildren().add(row);
        });
        return box;
    }

    private VBox section(String text) { Label label = new Label(text); label.getStyleClass().add("section-caption"); return new VBox(label); }
    private Label muted(String text) { Label label = new Label(text); label.getStyleClass().add("muted-text"); return label; }
    private Button button(String text, Runnable action) { Button button = new Button(text); button.setOnAction(event -> action.run()); return button; }
    private int active() { return (int) borrows.getBorrowRecords().stream().filter(record -> !record.isReturned()).count(); }
    private int overdue() { return (int) borrows.getBorrowRecords().stream().filter(record -> "Overdue".equals(status(record))).count(); }
    private String status(BorrowRecord record) { if (record.isReturned()) return "Returned"; return parseDate(record.getReturnDate()).filter(due -> due.isBefore(LocalDate.now())).isPresent() ? "Overdue" : "Borrowed"; }
    private java.util.Optional<LocalDate> parseDate(String text) { try { return java.util.Optional.of(LocalDate.parse(text)); } catch (RuntimeException ignored) { return java.util.Optional.empty(); } }
    private String book(int id) { Book book = books.searchBookById(id); return book == null ? "Book #" + id : book.getTitle(); }
    private String member(int id) { Member member = members.searchMemberById(id); return member == null ? "Member #" + id : member.getName(); }
}
