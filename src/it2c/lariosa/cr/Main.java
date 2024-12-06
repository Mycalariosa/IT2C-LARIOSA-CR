package it2c.lariosa.cr;

import Customer.Customer;
import ClothingItem.ClothingItem;
import Rental.Rental;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String choice;
            Scanner sc =new Scanner (System.in);
            String ch=null;
            Scanner in = new Scanner (System.in);
            String another =null;

            do {
                displayMenu();

               int action = -1;  // Initialize with an invalid action
            boolean validInput = false;

            // Loop until a valid integer between 1 and 5 is entered
            while (!validInput) {
                System.out.print("Choose from 1-4: ");
                if (sc.hasNextInt()) {
                    action = sc.nextInt();
                    if (action >= 1 && action <= 4) {
                        validInput = true;
                    } else {
                        System.out.println("\nInvalid action. Please enter a number between 1 and 4.");
                    }
                } else {
                    System.out.println("\nInvalid input. Please enter a number.");
                    sc.next(); // Clear the invalid input
                }
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

               System.out.print("\nDo you still want to use Main menu? (Y/N): ");
            another = in.next().trim();
            
            while(!another.equals("Yes")&&!another.equals("yes")&&!another.equals("YES")&&!another.equals("y")&&!another.equals("Y")&&!another.equals("n")&&!another.equals("N")&&!another.equals("No")&&!another.equals("NO")&&!another.equals("no")){
                System.out.println("Enter again:");
                another=in.next().trim();
        }
            }while (another.equals("YES")||another.equals("yes")||another.equals("Yes")||another.equals("y")||another.equals("Y"));
                System.out.println("Thamkyou for using Clothing Rental Application");
    }
    }
    private static void displayMenu() {
        System.out.println("\n|------------------------------------|");
        System.out.println("|      WELCOME TO CLOTHING RENTAL    |");
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
