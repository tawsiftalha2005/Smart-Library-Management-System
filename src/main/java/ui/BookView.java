package ui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Book;
import service.BookService;

public class BookView extends BorderPane {
    private final BookService service; private final TableView<Book> table = new TableView<>();
    private final TextField id = new TextField(), title = new TextField(), author = new TextField(), category = new TextField(), quantity = new TextField(), search = new TextField();
    public BookView(BookService service, Runnable onChange) {
        this.service = service; setPadding(new Insets(28)); getStyleClass().add("content");
        Label heading = new Label("Book Management"); heading.getStyleClass().add("page-title");
        search.setPromptText("Search by ID, title, author, or category"); Button searchButton = new Button("Search"); searchButton.setOnAction(e -> refresh()); Button reset = new Button("Show all"); reset.setOnAction(e -> { search.clear(); refresh(); }); HBox searchBar = new HBox(8, search, searchButton, reset); HBox.setHgrow(search, Priority.ALWAYS);
        VBox top = new VBox(14, heading, searchBar); setTop(top); BorderPane.setMargin(top, new Insets(0, 0, 18, 0));
        configureTable(); table.setPlaceholder(new Label("No books found. Add your first book using the form.")); table.getSelectionModel().selectedItemProperty().addListener((o, old, book) -> { if (book != null) fill(book); }); setCenter(table);
        GridPane form = form(); Button add = new Button("Add"); add.setOnAction(e -> { if (save(false)) UiSupport.info("Book added", "The book was added successfully."); }); Button update = new Button("Update"); update.setOnAction(e -> { if (save(true)) UiSupport.info("Book updated", "The book was updated successfully."); }); Button delete = new Button("Delete"); delete.getStyleClass().add("danger-button"); delete.setOnAction(e -> delete()); Button clear = new Button("Clear"); clear.setOnAction(e -> clear()); HBox actions = new HBox(8, add, update, delete, clear); VBox right = new VBox(14, new Label("Book details"), form, actions); right.getStyleClass().add("form-panel"); right.setPrefWidth(320); setRight(right); BorderPane.setMargin(right, new Insets(0, 0, 0, 20)); refresh();
    }
    private void configureTable() { table.getColumns().addAll(col("ID", b -> b.getId()), col("Title", Book::getTitle), col("Author", Book::getAuthor), col("Category", Book::getCategory), col("Quantity", b -> b.getQuantity())); table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); }
    private <T> TableColumn<Book, T> col(String name, java.util.function.Function<Book, T> getter) { TableColumn<Book, T> column = new TableColumn<>(name); column.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(getter.apply(data.getValue()))); return column; }
    private GridPane form() { GridPane g = new GridPane(); g.setHgap(8); g.setVgap(9); add(g, "Book ID", id, 0); add(g, "Title", title, 1); add(g, "Author", author, 2); add(g, "Category", category, 3); add(g, "Quantity", quantity, 4); return g; }
    private void add(GridPane g, String text, TextField field, int row) { field.setMaxWidth(Double.MAX_VALUE); g.add(new Label(text), 0, row); g.add(field, 0, row + 1); }
    private boolean save(boolean update) { if (!UiSupport.textPresent(id, title, author, category, quantity)) return false; Integer bookId = UiSupport.positiveId(id, "Book ID"), qty = UiSupport.positiveId(quantity, "Quantity"); if (bookId == null || qty == null) return false; boolean okay = update ? service.updateBook(bookId, title.getText().trim(), author.getText().trim(), category.getText().trim(), qty) : service.addBook(new Book(bookId, title.getText().trim(), author.getText().trim(), category.getText().trim(), qty)); if (!okay) { UiSupport.warning(update ? "Book not found" : "Duplicate ID", update ? "Select an existing book or enter its valid ID." : "A book with that ID already exists."); return false; } refresh(); clear(); return true; }
    private void delete() { Integer bookId = UiSupport.positiveId(id, "Book ID"); if (bookId != null && UiSupport.confirm("Delete book", "Delete this book?")) { if (service.deleteBook(bookId)) { UiSupport.info("Book deleted", "The book was deleted."); refresh(); clear(); } else UiSupport.warning("Book not found", "No book has that ID."); } }
    private void refresh() { String q = search.getText().trim().toLowerCase(); table.setItems(FXCollections.observableArrayList(service.getBooks().stream().filter(b -> q.isEmpty() || String.valueOf(b.getId()).contains(q) || b.getTitle().toLowerCase().contains(q) || b.getAuthor().toLowerCase().contains(q) || b.getCategory().toLowerCase().contains(q)).toList())); }
    private void fill(Book b) { id.setText(String.valueOf(b.getId())); title.setText(b.getTitle()); author.setText(b.getAuthor()); category.setText(b.getCategory()); quantity.setText(String.valueOf(b.getQuantity())); }
    private void clear() { id.clear(); title.clear(); author.clear(); category.clear(); quantity.clear(); table.getSelectionModel().clearSelection(); }
}
