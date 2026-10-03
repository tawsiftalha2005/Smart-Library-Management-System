package utils;

import java.util.Scanner;

public class InputValidator {

    private static final Scanner scanner = new Scanner(System.in);

    // Integer Input
    public static int getInt(String message) {

        while (true) {

            System.out.print(message);

            try {
                return Integer.parseInt(scanner.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    // String Input
    public static String getString(String message) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("Input cannot be empty!");
        }
    }
}