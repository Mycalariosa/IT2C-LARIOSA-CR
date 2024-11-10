package it2c.lariosa.cr;

import Customer.Customer;
import ClothingItem.ClothingItem;
import Rental.Rental;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String choice;

            do {
                displayMenu();

                System.out.print("Please choose a transaction (1-4): ");
                int action = scanner.nextInt();

                while (action < 1 || action > 4) {
                    System.out.print("\tInvalid selection. Please enter a number between 1 and 4: ");
                    action = scanner.nextInt();
                }

                switch (action) {
                    case 1:
                        handleCustomerTransaction(scanner);
                        break;
                    case 2:
                        handleClothingTransaction(scanner);
                        break;
                    case 3:
                        handleRentalTransaction();
                        break;
                    case 4:
                        System.out.println("\nExiting the application. Thank you for choosing Clothing Rental!");
                        return;
                }

                System.out.print("\nWould you like to return to the Main Menu? (Y/N): ");
                choice = scanner.next();

            } while (choice.equalsIgnoreCase("Y"));

            System.out.println("\nThank you for using the Clothing Rental application. Goodbye!");
        }
    }

    private static void displayMenu() {
        System.out.println("\n|------------------------------------|");
        System.out.println("|        WELCOME TO CLOTHING RENTAL  |");
        System.out.println("|------------------------------------|");
        System.out.println("| 1. Customer Information            |");
        System.out.println("| 2. Clothing Items                  |");
        System.out.println("| 3. Rental                          |");
        System.out.println("| 4. Exit                            |");
        System.out.println("|------------------------------------|");
    }

    private static void handleCustomerTransaction(Scanner scanner) {
        Customer customer = new Customer();
        customer.customerTransaction(scanner);
    }

    private static void handleClothingTransaction(Scanner scanner) {
        ClothingItem clothingItem = new ClothingItem();
        clothingItem.clothingTransaction(scanner);
    }

    private static void handleRentalTransaction() {
        Rental rental = new Rental();
        rental.rentalTransaction();
    }
}
