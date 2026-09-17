package utils;

import java.io.*;

public class FileManager {

    public static final String BOOK_FILE = "data/books.txt";
    public static final String MEMBER_FILE = "data/members.txt";
    public static final String BORROW_FILE = "data/borrow_records.txt";

    // Initialize files
    public static void initializeFiles() {

        try {

            File dataFolder = new File("data");

            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }

            createFileIfNotExists(BOOK_FILE);
            createFileIfNotExists(MEMBER_FILE);
            createFileIfNotExists(BORROW_FILE);

        } catch (Exception e) {
            System.out.println("Error initializing files.");
        }
    }

    // Create file if it doesn't exist
    private static void createFileIfNotExists(String path) throws IOException {

        File file = new File(path);

        if (!file.exists()) {
            file.createNewFile();
        }
    }

    // Write to file
    public static void write(String path, String text) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {

            writer.write(text);
            writer.newLine();

        } catch (IOException e) {
            System.out.println("Write Error!");
        }
    }

    // Read from file
    public static void read(String path) {

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            System.out.println("Read Error!");
        }
    }

    // Clear file
    public static void clear(String path) {

        try (PrintWriter writer = new PrintWriter(path)) {
            writer.print("");

        } catch (IOException e) {
            System.out.println("Clear Error!");
        }
    }
}