package ui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Book;
import model.BorrowRecord;
import model.Member;
import service.BookService;
import service.BorrowService;
import service.MemberService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BorrowView extends BorderPane {
    private final BookService books; private final MemberService members; private final BorrowService service; private final TableView<BorrowRecord> table=new TableView<>();
    private final TextField recordId=new TextField(), search=new TextField(); private final ComboBox<Book> book=new ComboBox<>(); private final ComboBox<Member> member=new ComboBox<>(); private final DatePicker borrowDate=new DatePicker(LocalDate.now()), returnDate=new DatePicker();
    public BorrowView(BookService books, MemberService members, BorrowService service, boolean recordsOnly, Runnable onChange) {
        this.books=books;this.members=members;this.service=service;setPadding(new Insets(28));getStyleClass().add("content"); Label heading=new Label(recordsOnly?"Borrow Records":"Borrow / Return");heading.getStyleClass().add("page-title"); search.setPromptText("Search by record, book, or member ID");Button go=new Button("Search");go.setOnAction(e->refresh());Button all=new Button("Show all");all.setOnAction(e->{search.clear();refresh();});HBox searchBar=new HBox(8,search,go,all);HBox.setHgrow(search,Priority.ALWAYS);setTop(new VBox(14,heading,searchBar));BorderPane.setMargin(getTop(),new Insets(0,0,18,0));
        table.getColumns().addAll(column("Record ID",BorrowRecord::getRecordId),column("Book ID",BorrowRecord::getBookId),column("Member ID",BorrowRecord::getMemberId),column("Borrow date",BorrowRecord::getBorrowDate),column("Return date",BorrowRecord::getReturnDate),column("Status",r->r.isReturned()?"Returned":"Borrowed"));table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);table.setPlaceholder(new Label("No borrow records yet."));table.getSelectionModel().selectedItemProperty().addListener((o,old,r)->{if(r!=null)recordId.setText(String.valueOf(r.getRecordId()));});setCenter(table);
        if(!recordsOnly) setRight(form(onChange)); refresh();
    }
    private VBox form(Runnable onChange) { book.setItems(FXCollections.observableArrayList(books.getBooks()));member.setItems(FXCollections.observableArrayList(members.getMembers()));book.setPromptText("Select an available book");member.setPromptText("Select a member");book.setConverter(new javafx.util.StringConverter<>(){public String toString(Book b){return b==null?"":b.getId()+" — "+b.getTitle()+" ("+b.getQuantity()+" available)";}public Book fromString(String s){return null;}});member.setConverter(new javafx.util.StringConverter<>(){public String toString(Member m){return m==null?"":m.getId()+" — "+m.getName();}public Member fromString(String s){return null;}});GridPane g=new GridPane();g.setHgap(8);g.setVgap(9);field(g,"Record ID",recordId,0);combo(g,"Book",book,1);combo(g,"Member",member,2);date(g,"Borrow date",borrowDate,3);date(g,"Expected return",returnDate,4);Button borrow=new Button("Borrow book");borrow.setOnAction(e->{if(borrow()){UiSupport.info("Book borrowed","Borrow record created successfully.");}});Button returned=new Button("Return selected");returned.setOnAction(e->{Integer id=UiSupport.positiveId(recordId,"Record ID");if(id!=null){if(service.returnBook(id,books)){UiSupport.info("Book returned","The book has been returned.");refresh();}else UiSupport.warning("Unable to return","Use an active record ID; the book and record must exist.");}});Button clear=new Button("Clear");clear.setOnAction(e->clear());VBox panel=new VBox(14,new Label("Borrow details"),g,new HBox(8,borrow,returned,clear));panel.getStyleClass().add("form-panel");panel.setPrefWidth(330);BorderPane.setMargin(panel,new Insets(0,0,0,20));return panel; }
    private boolean borrow(){if(!UiSupport.textPresent(recordId)||book.getValue()==null||member.getValue()==null||borrowDate.getValue()==null||returnDate.getValue()==null){UiSupport.warning("Missing information","Complete the record, book, member, and date fields.");return false;}Integer id=UiSupport.positiveId(recordId,"Record ID");if(id==null)return false;if(returnDate.getValue().isBefore(borrowDate.getValue())){UiSupport.warning("Invalid dates","Expected return must be on or after the borrow date.");return false;}BorrowRecord r=new BorrowRecord(id,book.getValue().getId(),member.getValue().getId(),borrowDate.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE),returnDate.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE),false);if(!service.borrowBook(r,books,members)){UiSupport.warning("Unable to borrow","Check that the record ID is new and the selected book is available.");return false;}refresh();clear();return true;}
    private <T> TableColumn<BorrowRecord,T> column(String name,java.util.function.Function<BorrowRecord,T> f){TableColumn<BorrowRecord,T> c=new TableColumn<>(name);c.setCellValueFactory(d->new javafx.beans.property.SimpleObjectProperty<>(f.apply(d.getValue())));return c;}
    private void field(GridPane g,String s,TextField f,int r){g.add(new Label(s),0,r*2);g.add(f,0,r*2+1);}private void combo(GridPane g,String s,ComboBox<?> c,int r){g.add(new Label(s),0,r*2);g.add(c,0,r*2+1);}private void date(GridPane g,String s,DatePicker d,int r){g.add(new Label(s),0,r*2);g.add(d,0,r*2+1);}
    private void refresh(){String q=search.getText().trim();table.setItems(FXCollections.observableArrayList(service.getBorrowRecords().stream().filter(r->q.isEmpty()||String.valueOf(r.getRecordId()).contains(q)||String.valueOf(r.getBookId()).contains(q)||String.valueOf(r.getMemberId()).contains(q)).toList()));}
    private void clear(){recordId.clear();book.setValue(null);member.setValue(null);borrowDate.setValue(LocalDate.now());returnDate.setValue(null);table.getSelectionModel().clearSelection();}
}
