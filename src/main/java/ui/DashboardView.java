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

        HBox cards = new HBox(16,
                stat("▣", "Total Books", books.totalBooks(), books.availableBookCopies() + " copies available"),
                stat("♙", "Total Members", members.totalMembers(), "Registered members"),
                stat("↔", "Currently Borrowed", active(), "Copies on active loan"),
                stat("⚠", "Overdue Books", overdue(), "Requires attention"));
        cards.getChildren().forEach(node -> { HBox.setHgrow(node, Priority.ALWAYS); ((Region) node).setMinWidth(0); });

        VBox activity = new VBox(8, new Label("Borrowing Activity"), muted("Last 7 days"), chart());
        activity.getStyleClass().add("panel");
        VBox popular = popular();
        HBox middle = new HBox(16, activity, popular);
        HBox.setHgrow(activity, Priority.ALWAYS); HBox.setHgrow(popular, Priority.ALWAYS);
        activity.setMinWidth(0); popular.setMinWidth(0);
        HBox lower = new HBox(16, recent(), overduePanel());
        HBox.setHgrow(lower.getChildren().get(0), Priority.ALWAYS); HBox.setHgrow(lower.getChildren().get(1), Priority.ALWAYS);
        body.getChildren().addAll(welcome, cards, middle, lower);
        setContent(body);
        setFitToWidth(true);
        setHbarPolicy(ScrollBarPolicy.NEVER);
    }

    private VBox stat(String icon, String title, int value, String hint) {
        Label i = new Label(icon); i.getStyleClass().add("stat-icon");
        Label t = new Label(title); t.getStyleClass().add("stat-title");
        Label v = new Label(String.valueOf(value)); v.getStyleClass().add("stat-value");
        VBox card = new VBox(9, i, v, t, new Separator(), muted(hint));
        card.getStyleClass().add("stat-card");
        card.setMaxWidth(Double.MAX_VALUE);
        return card;
    }

    private Node chart() {
        CategoryAxis x = new CategoryAxis(); NumberAxis y = new NumberAxis();
        LineChart<String, Number> chart = new LineChart<>(x, y);
        chart.setCreateSymbols(true); chart.setAnimated(false); chart.setPrefHeight(260); chart.setMinHeight(220);
        XYChart.Series<String, Number> borrowed = new XYChart.Series<>(), returned = new XYChart.Series<>();
        borrowed.setName("Borrowed"); returned.setName("Returned");
        LocalDate today = LocalDate.now();
        DateTimeFormatter labelFormat = DateTimeFormatter.ofPattern("EEE");
        for (int offset = 6; offset >= 0; offset--) {
            LocalDate day = today.minusDays(offset);
            int countBorrowed = (int) borrows.getBorrowRecords().stream().filter(record -> parseDate(record.getBorrowDate()).map(day::equals).orElse(false)).count();
            int countReturned = (int) borrows.getBorrowRecords().stream().filter(record -> record.isReturned() && parseDate(record.getReturnDate()).map(day::equals).orElse(false)).count();
            borrowed.getData().add(new XYChart.Data<>(day.format(labelFormat), countBorrowed));
            returned.getData().add(new XYChart.Data<>(day.format(labelFormat), countReturned));
        }
        chart.getData().addAll(borrowed, returned);
        return chart;
    }

    private VBox popular() {
        VBox box = new VBox(0); box.getStyleClass().add("panel");
        Label title = new Label("Popular Books"); title.getStyleClass().add("section-title");
        box.getChildren().addAll(title, muted("Most borrowed in your library"), new Separator());
        var counts = new java.util.HashMap<Integer, Long>();
        borrows.getBorrowRecords().forEach(r -> counts.merge(r.getBookId(), 1L, Long::sum));
        var ranked = books.getBooks().stream().sorted((a,b) -> Long.compare(counts.getOrDefault(b.getId(),0L),counts.getOrDefault(a.getId(),0L))).limit(5).toList();
        if (ranked.isEmpty()) box.getChildren().add(muted("No books in the catalog yet."));
        else for (Book book : ranked) {
            Label cover = new Label(book.getTitle().isBlank()?"?":book.getTitle().substring(0,1).toUpperCase()); cover.getStyleClass().add("book-cover"); cover.setMinWidth(40);
            Label name = new Label(book.getTitle()); name.setWrapText(true); name.getStyleClass().add("row-title");
            Label author = muted(book.getAuthor()); VBox details = new VBox(4,name,author); HBox row = new HBox(12,cover,details); row.setAlignment(javafx.geometry.Pos.CENTER_LEFT); row.setPadding(new Insets(13,0,13,0));
            Label count = new Label(counts.getOrDefault(book.getId(),0L)+"×"); count.getStyleClass().add("stat-icon"); Region gap = new Region(); HBox.setHgrow(gap,Priority.ALWAYS); row.getChildren().addAll(gap,count); box.getChildren().addAll(new Separator(),row);
        }
        return box;
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
