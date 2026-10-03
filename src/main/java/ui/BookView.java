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
    private final TextField id = new TextField(), title = new TextField(), author = new TextField(), category = new TextField(), isbn = new TextField(), quantity = new TextField(), search = new TextField();
    public BookView(BookService service, Runnable onChange) {
        this.service = service; setPadding(new Insets(22,28,28,28)); getStyleClass().add("content");
        Button addBook=new Button("＋  Add Book"); addBook.setOnAction(e->editDialog(false));
        search.setPromptText("⌕   Search by title, author, category, or ID..."); search.getStyleClass().add("search-field"); search.textProperty().addListener((o,a,b)->refresh());
        HBox toolbar=new HBox(12,search,addBook); HBox.setHgrow(search,Priority.ALWAYS); setTop(toolbar); BorderPane.setMargin(toolbar,new Insets(0,0,18,0));
        configureTable(); table.setPlaceholder(new Label("No books found. Add a book to your collection.")); table.getSelectionModel().selectedItemProperty().addListener((o, old, book) -> { if (book != null) fill(book); }); setCenter(table);
        refresh();
    }
    private void configureTable() { table.getColumns().addAll(col("BOOK", Book::getTitle), col("BOOK ID", b -> String.format("BK-%03d",b.getId())), col("CATEGORY", Book::getCategory), col("ISBN", Book::getIsbn), col("AVAILABILITY", b -> b.getQuantity()>0?"Available":"Unavailable"), col("QTY", b -> b.getQuantity()), actionColumn()); table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); }
    private TableColumn<Book,Book> actionColumn(){TableColumn<Book,Book> c=new TableColumn<>("ACTIONS");c.setCellValueFactory(v->new javafx.beans.property.SimpleObjectProperty<>(v.getValue()));c.setCellFactory(col->new TableCell<>(){private final Button edit=new Button("✎"),del=new Button("⌫");private final HBox box=new HBox(5,edit,del);{edit.getStyleClass().add("icon-button");del.getStyleClass().addAll("icon-button","danger-button");edit.setOnAction(e->{Book b=getItem();if(b!=null){fill(b);editDialog(true);}});del.setOnAction(e->{Book b=getItem();if(b!=null){id.setText(String.valueOf(b.getId()));delete();}});}protected void updateItem(Book b,boolean empty){super.updateItem(b,empty);setGraphic(empty?null:box);}});return c;}
    private void editDialog(boolean update){if(!update)clear();Dialog<ButtonType>d=new Dialog<>();d.setTitle(update?"Edit book":"Add book");d.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);d.getDialogPane().setContent(form());ButtonType okay=ButtonType.OK;d.showAndWait().ifPresent(result->{if(result==okay&&save(update)){UiSupport.info(update?"Book updated":"Book added",update?"The book was updated successfully.":"The book was added successfully.");clear();}});}
    private <T> TableColumn<Book, T> col(String name, java.util.function.Function<Book, T> getter) { TableColumn<Book, T> column = new TableColumn<>(name); column.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(getter.apply(data.getValue()))); return column; }
    private GridPane form() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(6);
        grid.setPadding(new Insets(12));
        addField(grid, "Book ID", id, 0);
        addField(grid, "Title", title, 1);
        addField(grid, "Author", author, 2);
        addField(grid, "Category", category, 3);
        addField(grid, "ISBN", isbn, 4);
        addField(grid, "Quantity", quantity, 5);
        return grid;
    }

    private void addField(GridPane grid, String label, TextField field, int index) {
        field.setPromptText(label);
        field.setMaxWidth(Double.MAX_VALUE);
        int labelRow = index * 2;
        grid.add(new Label(label), 0, labelRow);
        grid.add(field, 0, labelRow + 1);
    }
    private boolean save(boolean update) { if (!UiSupport.textPresent(id, title, author, category, isbn, quantity)) return false; Integer bookId = UiSupport.positiveId(id, "Book ID"), qty = UiSupport.positiveId(quantity, "Quantity"); if (bookId == null || qty == null) return false; boolean okay = update ? service.updateBook(bookId, title.getText().trim(), author.getText().trim(), category.getText().trim(), isbn.getText().trim(), qty) : service.addBook(new Book(bookId, title.getText().trim(), author.getText().trim(), category.getText().trim(), isbn.getText().trim(), qty)); if (!okay) { UiSupport.warning(update ? "Book not found" : "Duplicate ID", update ? "Select an existing book or enter its valid ID." : "A book with that ID already exists."); return false; } refresh(); clear(); return true; }
    private void delete() { Integer bookId = UiSupport.positiveId(id, "Book ID"); if (bookId != null && UiSupport.confirm("Delete book", "Delete this book?")) { if (service.deleteBook(bookId)) { UiSupport.info("Book deleted", "The book was deleted."); refresh(); clear(); } else UiSupport.warning("Book not found", "No book has that ID."); } }
    private void refresh() { String q = search.getText().trim().toLowerCase(); table.setItems(FXCollections.observableArrayList(service.getBooks().stream().filter(b -> q.isEmpty() || String.valueOf(b.getId()).contains(q) || b.getTitle().toLowerCase().contains(q) || b.getAuthor().toLowerCase().contains(q) || b.getCategory().toLowerCase().contains(q)).toList())); }
    private void fill(Book b) { id.setText(String.valueOf(b.getId())); title.setText(b.getTitle()); author.setText(b.getAuthor()); category.setText(b.getCategory()); isbn.setText(b.getIsbn()); quantity.setText(String.valueOf(b.getQuantity())); }
    private void clear() { id.clear(); title.clear(); author.clear(); category.clear(); isbn.clear(); quantity.clear(); table.getSelectionModel().clearSelection(); }
}
