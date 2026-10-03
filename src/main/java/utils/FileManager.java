package utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileManager {

    public static final String BOOK_FILE = "books.txt";
    public static final String MEMBER_FILE = "members.txt";
    public static final String BORROW_FILE = "borrow_records.txt";

    public static Path getDataDirectory() {
        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        String home = System.getProperty("user.home");
        if (os.contains("win")) {
            String appData = System.getenv("APPDATA");
            return Paths.get(appData == null || appData.isBlank() ? home : appData, "SmartLibrary");
        }
        if (os.contains("mac")) return Paths.get(home, "Library", "Application Support", "SmartLibrary");
        return Paths.get(home, ".smartlibrary");
    }

    public static Path getFilePath(String fileName) { return getDataDirectory().resolve(fileName); }

    // Initialize files
    public static void initializeFiles() {

        try {

            Files.createDirectories(getDataDirectory());
            createFileIfNotExists(getFilePath(BOOK_FILE).toString());
            createFileIfNotExists(getFilePath(MEMBER_FILE).toString());
            createFileIfNotExists(getFilePath(BORROW_FILE).toString());

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

    public static List<String> readLines(String fileName) throws IOException {
        return Files.readAllLines(getFilePath(fileName));
    }

    public static void writeLines(String fileName, List<String> lines) throws IOException {
        Files.write(getFilePath(fileName), lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
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
