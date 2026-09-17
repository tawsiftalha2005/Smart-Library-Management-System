package service;

import model.BorrowRecord;
import model.Book;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BorrowService {

    private ArrayList<BorrowRecord> borrowRecords = new ArrayList<>();


    public void borrowBook(BorrowRecord record) {
        borrowRecords.add(record);
        System.out.println("Book borrowed successfully!");
    }

    /** Borrow using the shared services, enforcing the rules required by the GUI. */
    public boolean borrowBook(BorrowRecord record, BookService bookService, MemberService memberService) {
        if (record == null || searchRecord(record.getRecordId()) != null) return false;
        Book book = bookService.searchBookById(record.getBookId());
        if (book == null || memberService.searchMemberById(record.getMemberId()) == null || book.getQuantity() < 1) return false;
        book.setQuantity(book.getQuantity() - 1);
        borrowRecords.add(record);
        return true;
    }


    public boolean returnBook(int recordId) {

        for (BorrowRecord record : borrowRecords) {

            if (record.getRecordId() == recordId) {

                if (!record.isReturned()) {
                    record.setReturned(true);
                    System.out.println("Book returned successfully!");
                    return true;
                } else {
                    System.out.println("This book has already been returned.");
                    return false;
                }
            }
        }

        System.out.println("Borrow record not found.");
        return false;
    }

    public boolean returnBook(int recordId, BookService bookService) {
        BorrowRecord record = searchRecord(recordId);
        if (record == null || record.isReturned()) return false;
        Book book = bookService.searchBookById(record.getBookId());
        if (book == null) return false;
        record.setReturned(true);
        book.setQuantity(book.getQuantity() + 1);
        return true;
    }


    public void viewBorrowRecords() {

        if (borrowRecords.isEmpty()) {
            System.out.println("No borrow records available.");
            return;
        }

        System.out.println("---------------------------------------------------------------------------------------");
        System.out.printf("%-5s %-5s %-5s %-15s %-15s %-10s%n",
                "RID", "BID", "MID", "Borrow Date", "Return Date", "Status");
        System.out.println("---------------------------------------------------------------------------------------");

        for (BorrowRecord record : borrowRecords) {
            System.out.println(record);
        }

        System.out.println("---------------------------------------------------------------------------------------");
    }


    public BorrowRecord searchRecord(int recordId) {

        for (BorrowRecord record : borrowRecords) {
            if (record.getRecordId() == recordId) {
                return record;
            }
        }

        return null;
    }


    public int totalBorrowRecords() {
        return borrowRecords.size();
    }

    public List<BorrowRecord> getBorrowRecords() {
        return Collections.unmodifiableList(borrowRecords);
    }

    public List<BorrowRecord> getRecentBorrowRecords(int limit) {
        List<BorrowRecord> recent = new ArrayList<>(borrowRecords);
        Collections.reverse(recent);
        return recent.subList(0, Math.min(limit, recent.size()));
    }
}
