package service;

import model.Book;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import utils.FileManager;
import utils.CsvFormat;

public class BookService {

    private ArrayList<Book> books = new ArrayList<>();

    // Add Book
    public boolean addBook(Book book) {
        if (book == null || searchBookById(book.getId()) != null) {
            System.out.println("A book with this ID already exists.");
            return false;
        }
        books.add(book);
        saveToFile();
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
            saveToFile();
            return true;
        }

        return false;
    }

    // Update Quantity
    public boolean updateQuantity(int id, int quantity) {

        Book book = searchBookById(id);

        if (book != null) {
            book.setQuantity(quantity);
            saveToFile();
            return true;
        }

        return false;
    }

    public boolean updateBook(int id, String title, String author, String category, String isbn, int quantity) {
        Book book = searchBookById(id);
        if (book == null) return false;
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setIsbn(isbn);
        book.setQuantity(quantity);
        saveToFile();
        return true;
    }

    public boolean updateBook(int id, String title, String author, String category, int quantity) {
        return updateBook(id, title, author, category, "", quantity);
    }

    public void loadFromFile() {
        books.clear();
        try {
            for (String line : FileManager.readLines(FileManager.BOOK_FILE)) {
                if (line.isBlank()) continue;
                try {
                    List<String> f = CsvFormat.decode(line);
                    if (f.size() != 5 && f.size() != 6) throw new IllegalArgumentException("Expected 5 or 6 fields");
                    books.add(f.size() == 5
                            ? new Book(Integer.parseInt(f.get(0)), f.get(1), f.get(2), f.get(3), Integer.parseInt(f.get(4)))
                            : new Book(Integer.parseInt(f.get(0)), f.get(1), f.get(2), f.get(3), f.get(4), Integer.parseInt(f.get(5))));
                } catch (RuntimeException e) { System.err.println("Skipping invalid book row: " + e.getMessage()); }
            }
        } catch (IOException e) { System.err.println("Could not load books: " + e.getMessage()); }
    }

    public void saveToFile() {
        List<String> rows = new ArrayList<>();
        for (Book b : books) rows.add(CsvFormat.encode(String.valueOf(b.getId()), b.getTitle(), b.getAuthor(), b.getCategory(), b.getIsbn(), String.valueOf(b.getQuantity())));
        try { FileManager.writeLines(FileManager.BOOK_FILE, rows); }
        catch (IOException e) { System.err.println("Could not save books: " + e.getMessage()); }
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
