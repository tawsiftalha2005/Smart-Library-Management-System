package ui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Member;
import service.MemberService;

public class MemberView extends BorderPane {
    private final MemberService service; private final TableView<Member> table = new TableView<>();
    private final TextField id = new TextField(), name = new TextField(), email = new TextField(), phone = new TextField(), search = new TextField();
    public MemberView(MemberService service, Runnable onChange) {
        this.service = service; setPadding(new Insets(28)); getStyleClass().add("content"); Label heading = new Label("Member Management"); heading.getStyleClass().add("page-title"); search.setPromptText("Search by ID, name, email, or phone"); Button go = new Button("Search"); go.setOnAction(e -> refresh()); Button all = new Button("Show all"); all.setOnAction(e -> { search.clear(); refresh(); }); HBox searchBar = new HBox(8, search, go, all); HBox.setHgrow(search, Priority.ALWAYS); VBox top = new VBox(14, heading, searchBar); setTop(top); BorderPane.setMargin(top, new Insets(0,0,18,0));
        table.getColumns().addAll(column("ID", m -> m.getId()), column("Name", Member::getName), column("Email", Member::getEmail), column("Phone", Member::getPhone)); table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); table.setPlaceholder(new Label("No members found. Add the first member using the form.")); table.getSelectionModel().selectedItemProperty().addListener((o, old, m) -> { if (m != null) fill(m); }); setCenter(table);
        GridPane g = new GridPane(); g.setHgap(8); g.setVgap(9); field(g,"Member ID",id,0); field(g,"Name",name,1); field(g,"Email",email,2); field(g,"Phone",phone,3); Button add = new Button("Add"); add.setOnAction(e -> { if (save(false)) UiSupport.info("Member added", "The member was added successfully."); }); Button update = new Button("Update"); update.setOnAction(e -> { if (save(true)) UiSupport.info("Member updated", "The member was updated successfully."); }); Button delete = new Button("Delete"); delete.getStyleClass().add("danger-button"); delete.setOnAction(e -> delete()); Button clear = new Button("Clear"); clear.setOnAction(e -> clear()); VBox panel = new VBox(14, new Label("Member details"), g, new HBox(8,add,update,delete,clear)); panel.getStyleClass().add("form-panel"); panel.setPrefWidth(320); setRight(panel); BorderPane.setMargin(panel,new Insets(0,0,0,20)); refresh();
    }
    private <T> TableColumn<Member,T> column(String text, java.util.function.Function<Member,T> f) { TableColumn<Member,T> c = new TableColumn<>(text); c.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(f.apply(d.getValue()))); return c; }
    private void field(GridPane g,String label,TextField f,int r){g.add(new Label(label),0,r*2);g.add(f,0,r*2+1);}
    private boolean save(boolean update) { if(!UiSupport.textPresent(id,name,email,phone))return false; Integer memberId=UiSupport.positiveId(id,"Member ID"); if(memberId==null)return false; if(!email.getText().contains("@")){UiSupport.warning("Invalid email","Enter a valid email address.");return false;} boolean ok=update?service.updateMember(memberId,name.getText().trim(),email.getText().trim(),phone.getText().trim()):service.addMember(new Member(memberId,name.getText().trim(),email.getText().trim(),phone.getText().trim())); if(!ok){UiSupport.warning(update?"Member not found":"Duplicate ID",update?"Select an existing member or enter its valid ID.":"A member with that ID already exists.");return false;} refresh();clear();return true; }
    private void delete(){Integer memberId=UiSupport.positiveId(id,"Member ID");if(memberId!=null&&UiSupport.confirm("Delete member","Delete this member?")){if(service.deleteMember(memberId)){UiSupport.info("Member deleted","The member was deleted.");refresh();clear();}else UiSupport.warning("Member not found","No member has that ID.");}}
    private void refresh(){String q=search.getText().trim().toLowerCase();table.setItems(FXCollections.observableArrayList(service.getMembers().stream().filter(m->q.isEmpty()||String.valueOf(m.getId()).contains(q)||m.getName().toLowerCase().contains(q)||m.getEmail().toLowerCase().contains(q)||m.getPhone().toLowerCase().contains(q)).toList()));}
    private void fill(Member m){id.setText(String.valueOf(m.getId()));name.setText(m.getName());email.setText(m.getEmail());phone.setText(m.getPhone());}
    private void clear(){id.clear();name.clear();email.clear();phone.clear();table.getSelectionModel().clearSelection();}
}
