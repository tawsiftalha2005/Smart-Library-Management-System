package model;

public class BorrowRecord {

    private int recordId;
    private int bookId;
    private int memberId;
    private String borrowDate;
    private String returnDate;
    private boolean returned;

    // Constructor
    public BorrowRecord(int recordId, int bookId, int memberId,
                        String borrowDate, String returnDate, boolean returned) {
        this.recordId = recordId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.returned = returned;
    }

    // Getters
    public int getRecordId() {
        return recordId;
    }

    public int getBookId() {
        return bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return returned;
    }

    // Setters
    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    @Override
    public String toString() {
        return String.format(
                "%-5d %-5d %-5d %-15s %-15s %-10s",
                recordId,
                bookId,
                memberId,
                borrowDate,
                returnDate,
                returned ? "Returned" : "Borrowed"
        );
    }
}