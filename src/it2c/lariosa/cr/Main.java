
package it2c.lariosa.cr;

import Customer.Customer;
import java.util.Scanner;
import ClothingItem.ClothingItem;
import Rental.Rental;

public class Main {

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {
            String ch;
            
            do {
                System.out.println("\n|------------------------------------|");
                System.out.println("|    WELCOME TO CLOTHING RENTAL      |");
                System.out.println("|------------------------------------|");
                System.out.println("| 1. CUSTOMER INFORMATION            |");
                System.out.println("| 2. CLOTHING ITEMS                  |");
                System.out.println("| 3. RENTAL                          |");
                System.out.println("| 4. EXIT                            |");
                System.out.println("|------------------------------------|");
                
                System.out.print("Please Choose a transaction (1-4): ");
                int action = sc.nextInt();
                
                
                while (action < 1 || action > 4) {
                    System.out.print("\tInvalid action. Please enter a number between 1 and 4: ");
                    action = sc.nextInt();
                }
          
                Rental rn = new Rental();
                
                switch (action) {
                    case 1:
                        Customer cr = new Customer();
                        cr.customerTransaction(sc);
                        break;
                    case 2:
                        ClothingItem ci = new ClothingItem();
                        ci.clothingTransaction(sc);
                        break;
                    case 3:
                        rn.rentalTransaction();
                        break;
                    case 4:
                        System.out.println("Exiting the application. Thank you!");
                        sc.close();
                        return;
                }
                
                System.out.print("\nDo you want to use another system? (Y/N): ");
                ch = sc.next();
            } while (ch.equalsIgnoreCase("Y"));
            
            System.out.println("\nThank you for using this application");
        }
    }
}
