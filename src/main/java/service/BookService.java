package service;

import model.Book;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookService {

    private ArrayList<Book> books = new ArrayList<>();

    // Add Book
    public boolean addBook(Book book) {
        if (book == null || searchBookById(book.getId()) != null) {
            System.out.println("A book with this ID already exists.");
            return false;
        }
        books.add(book);
        System.out.println("Book added successfully!");
        return true;
    }

    // View Books
    public void viewBooks() {

        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("%-5s %-30s %-20s %-15s %-5s%n",
                "ID", "Title", "Author", "Category", "Qty");
        System.out.println("----------------------------------------------------------------------------");

        for (Book book : books) {
            System.out.println(book);
        }

        System.out.println("----------------------------------------------------------------------------");
    }

    // Search Book
    public Book searchBookById(int id) {

        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }

        return null;
    }

    // Delete Book
    public boolean deleteBook(int id) {

        Book book = searchBookById(id);

        if (book != null) {
            books.remove(book);
            return true;
        }

        return false;
    }

    // Update Quantity
    public boolean updateQuantity(int id, int quantity) {

        Book book = searchBookById(id);

        if (book != null) {
            book.setQuantity(quantity);
            return true;
        }

        return false;
    }

    public boolean updateBook(int id, String title, String author, String category, int quantity) {
        Book book = searchBookById(id);
        if (book == null) return false;
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setQuantity(quantity);
        return true;
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    public int availableBookCopies() {
        return books.stream().mapToInt(Book::getQuantity).sum();
    }

    // Total Books
    public int totalBooks() {
        return books.size();
    }
}
