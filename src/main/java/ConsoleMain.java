import model.Book;
import model.Member;
import service.BookService;
import service.MemberService;
import service.BorrowService;
import utils.InputValidator;
import utils.FileManager;

/** Entry point for the original menu-driven interface. */
public class ConsoleMain {

    static BookService bookService = new BookService();
    static MemberService memberService = new MemberService();
    static BorrowService borrowService = new BorrowService();

    public static void main(String[] args) {

        FileManager.initializeFiles();

        while (true) {

            System.out.println("\n=========================================");
            System.out.println("     SMART LIBRARY MANAGEMENT SYSTEM");
            System.out.println("=========================================");
            System.out.println("1. Book Management");
            System.out.println("2. Member Management");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. View Borrow Records");
            System.out.println("6. Exit");
            System.out.println("=========================================");

            int choice = InputValidator.getInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    bookMenu();
                    break;

                case 2:
                    memberMenu();
                    break;

                case 3:
                    borrowBook();
                    break;

                case 4:
                    returnBook();
                    break;

                case 5:
                    borrowService.viewBorrowRecords();
                    break;

                case 6:
                    System.out.println("Thank You!");
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice!");
            }

        }

    }

    //================ BOOK MENU =====================

    public static void bookMenu() {

        while (true) {

            System.out.println("\n========== BOOK MENU ==========");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Search Book");
            System.out.println("4. Delete Book");
            System.out.println("5. Update Quantity");
            System.out.println("6. Back");

            int choice = InputValidator.getInt("Enter Choice : ");

            switch (choice) {

                case 1:

                    int id = InputValidator.getInt("Book ID : ");
                    String title = InputValidator.getString("Title : ");
                    String author = InputValidator.getString("Author : ");
                    String category = InputValidator.getString("Category : ");
                    int quantity = InputValidator.getInt("Quantity : ");

                    Book book = new Book(
                            id,
                            title,
                            author,
                            category,
                            quantity
                    );

                    bookService.addBook(book);

                    break;

                case 2:

                    bookService.viewBooks();

                    break;

                case 3:

                    int searchId =
                            InputValidator.getInt("Enter Book ID : ");

                    Book found =
                            bookService.searchBookById(searchId);

                    if (found != null) {

                        System.out.println(found);

                    } else {

                        System.out.println("Book Not Found.");

                    }

                    break;

                case 4:

                    int deleteId =
                            InputValidator.getInt("Book ID : ");

                    if (bookService.deleteBook(deleteId)) {

                        System.out.println("Deleted Successfully.");

                    } else {

                        System.out.println("Book Not Found.");

                    }

                    break;

                case 5:

                    int updateId =
                            InputValidator.getInt("Book ID : ");

                    int qty =
                            InputValidator.getInt("New Quantity : ");

                    if (bookService.updateQuantity(updateId, qty)) {

                        System.out.println("Updated Successfully.");

                    } else {

                        System.out.println("Book Not Found.");

                    }

                    break;

                case 6:

                    return;

                default:

                    System.out.println("Invalid Choice.");

            }

        }

    }
    //================ MEMBER MENU =====================

    public static void memberMenu() {

        while (true) {

            System.out.println("\n========== MEMBER MENU ==========");
            System.out.println("1. Add Member");
            System.out.println("2. View Members");
            System.out.println("3. Search Member");
            System.out.println("4. Delete Member");
            System.out.println("5. Back");

            int choice = InputValidator.getInt("Enter Choice : ");

            switch (choice) {

                case 1:

                    int id = InputValidator.getInt("Member ID : ");
                    String name = InputValidator.getString("Name : ");
                    String email = InputValidator.getString("Email : ");
                    String phone = InputValidator.getString("Phone : ");

                    Member member = new Member(id, name, email, phone);

                    memberService.addMember(member);

                    break;

                case 2:

                    memberService.viewMembers();

                    break;

                case 3:

                    int searchId = InputValidator.getInt("Enter Member ID : ");

                    Member found = memberService.searchMemberById(searchId);

                    if (found != null) {

                        System.out.println(found);

                    } else {

                        System.out.println("Member Not Found.");

                    }

                    break;

                case 4:

                    int deleteId = InputValidator.getInt("Member ID : ");

                    if (memberService.deleteMember(deleteId)) {

                        System.out.println("Member Deleted Successfully.");

                    } else {

                        System.out.println("Member Not Found.");

                    }

                    break;

                case 5:

                    return;

                default:

                    System.out.println("Invalid Choice.");

            }

        }

    }

    //================ BORROW BOOK =====================

    public static void borrowBook() {

        int recordId = InputValidator.getInt("Record ID : ");
        int bookId = InputValidator.getInt("Book ID : ");
        int memberId = InputValidator.getInt("Member ID : ");

        String borrowDate = InputValidator.getString("Borrow Date : ");
        String returnDate = InputValidator.getString("Return Date : ");

        borrowService.borrowBook(
                new model.BorrowRecord(
                        recordId,
                        bookId,
                        memberId,
                        borrowDate,
                        returnDate,
                        false
                )
        );

    }

    //================ RETURN BOOK =====================

    public static void returnBook() {

        int recordId = InputValidator.getInt("Record ID : ");

        borrowService.returnBook(recordId);

    }

}
