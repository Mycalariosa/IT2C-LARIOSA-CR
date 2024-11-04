
package Rental;

import ClothingItem.ClothingItem;
import it2c.lariosa.cr.CONFIG;
import java.util.Scanner;

public class Rental {

    public void rentalTransaction() {
        try (Scanner sc = new Scanner(System.in)) {
            String ch;
            
            do {
                System.out.println("\n|--------------------|");
                System.out.println("|   RENTAL MENU      |");
                System.out.println("|--------------------|");
                System.out.println("| 1. ADD RENTAL      |");
                System.out.println("| 2. VIEW RENTALS    |");
                System.out.println("| 3. UPDATE RENTAL    |");
                System.out.println("| 4. DELETE RENTAL    |");
                System.out.println("| 5. EXIT            |");
                System.out.println("|--------------------|");
                
                System.out.print("Choose from 1-5: ");
                int action = sc.nextInt();
                
                while (action < 1 || action > 5) {
                    System.out.print("\tInvalid action. Please enter a number between 1 and 5: ");
                    action = sc.nextInt();
                }
                
                switch (action) {
                    case 1:
                        startRentalProcess();
                        break;
                    case 2:
                        viewRentals();
                        break;
                    case 3:
                        updateRental();
                        break;
                    case 4:
                        deleteRental();
                        break;
                    case 5:
                        break;
                }
                
                System.out.print("\nDo you want to use another system? (Y/N): ");
                ch = sc.next();
            } while (ch.equalsIgnoreCase("Y"));
            
            System.out.println("\nThank you for using this application");
        }
    }

    public void startRentalProcess() {
        Scanner sc = new Scanner(System.in);

        System.out.print("\n------------------------------------\n");
        System.out.println("Welcome to Clothes Rental!");
        System.out.print("------------------------------------\n");
        System.out.println("Below is the list of items you can borrow:");

        ClothingItem clothingItem = new ClothingItem();
        clothingItem.viewClothingItem();

        System.out.println("\nPlease fill out the following rental form.");

        System.out.print("Customer Name: ");
        String customerName = sc.nextLine();

        System.out.print("Item to Rent (ID): ");
        int itemId = sc.nextInt();

        System.out.print("Details (Size, Color, etc.): ");
        sc.nextLine();
        String details = sc.nextLine();

        System.out.print("Price per Day: ");
        double pricePerDay = sc.nextDouble();

        System.out.print("Down Payment: ");
        double downPayment = sc.nextDouble();

        System.out.print("Rental Date (YYYY-MM-DD): ");
        String rentalDate = sc.next();

        System.out.print("Return Date (YYYY-MM-DD): ");
        String returnDate = sc.next();

        System.out.print("Number of Rental Days: ");
        int rentalDays = sc.nextInt();

        double totalCost = pricePerDay * rentalDays;
        double balanceLeft = totalCost - downPayment;

        System.out.println("\nSummary of your Rental:");
        System.out.println("Customer Name: " + customerName);
        System.out.println("Item to Rent: " + itemId + " (" + details + ")");
        System.out.println("Total Cost: " + totalCost);
        System.out.println("Down Payment: " + downPayment);
        System.out.println("Balance Left: " + balanceLeft);

    }

   private void viewRentals() {
    CONFIG conf = new CONFIG();

   
    String query = "SELECT customer_name, item_id, details, price_per_day, rental_date, return_date, down_payment, "
                 + "total_cost - down_payment AS remaining_balance, "
                 + "CASE WHEN return_date < CURRENT_DATE THEN 'Returned' ELSE 'Not Returned' END AS status "
                 + "FROM Rental";

   
    String[] headers = {
        "Customer Name", "Item ID", "Details", "Price/Day", "Rental Date",
        "Return Date", "Down Payment", "Remaining Balance", "Status"
    };
    
    
    String[] columns = {
        "customer_name", "item_id", "details", "price_per_day",
        "rental_date", "return_date", "down_payment", "remaining_balance", "status"
    };

   
    conf.viewRecords(query, headers, columns);
}
    private void updateRental() {
       
    }

    private void deleteRental() {
       
    }
}
