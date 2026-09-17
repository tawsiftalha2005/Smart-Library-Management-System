package model;

public class Book {

    private int id;
    private String title;
    private String author;
    private String category;
    private int quantity;

    // Constructor
    public Book(int id, String title, String author, String category, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.quantity = quantity;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format(
                "%-5d %-30s %-20s %-15s %-5d",
                id,
                title,
                author,
                category,
                quantity
        );
    }
}