
package Rental;

import ClothingItem.ClothingItem;
import it2c.lariosa.cr.CONFIG;
import java.util.Scanner;

public class Rental {

    public void startRentalProcess() {
        Scanner sc = new Scanner(System.in);

        // Welcome Message
        System.out.println("Welcome to Clothes Rental!");
        System.out.println("Below is the list of items you can borrow:");

        ClothingItem clothingItem = new ClothingItem();
        clothingItem.viewClothingItem();  // Directly calling the method from ClothingItem class

        System.out.println("\nPlease fill out the following rental form.");

        // Rental Form
        System.out.print("Customer Name: ");
        String customerName = sc.nextLine();

        System.out.print("Item to Rent (ID): ");
        int itemId = sc.nextInt();

        System.out.print("Details (Size, Color, etc.): ");
        sc.nextLine();  // Consume newline
        String details = sc.nextLine();

        System.out.print("Price per Day: ");
        double pricePerDay = sc.nextDouble();

        System.out.print("Down Payment: ");
        double downPayment = sc.nextDouble();

        System.out.print("Rental Date (YYYY-MM-DD): ");
        String rentalDate = sc.next();

        System.out.print("Return Date (YYYY-MM-DD): ");
        String returnDate = sc.next();

        // Calculating Total Rental Days
        System.out.print("Number of Rental Days: ");
        int rentalDays = sc.nextInt();

        // Calculating Balance Left
        double totalCost = pricePerDay * rentalDays;
        double balanceLeft = totalCost - downPayment;

        System.out.println("\nSummary of your Rental:");
        System.out.println("Customer Name: " + customerName);
        System.out.println("Item to Rent: " + itemId + " (" + details + ")");
        System.out.println("Total Cost: " + totalCost);
        System.out.println("Down Payment: " + downPayment);
        System.out.println("Balance Left: " + balanceLeft);

        // Saving the Rental Record
        saveRentalRecord(customerName, itemId, details, pricePerDay, downPayment, rentalDate, returnDate, totalCost, balanceLeft);
    }

    private void saveRentalRecord(String customerName, int itemId, String details, double pricePerDay, double downPayment,
                                  String rentalDate, String returnDate, double totalCost, double balanceLeft) {
        CONFIG conf = new CONFIG();

        String sql = "INSERT INTO Rental (customer_name, item_id, details, price_per_day, down_payment, rental_date, return_date, total_cost, balance_left) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        conf.addRecord(sql, customerName, itemId, details, pricePerDay, downPayment, rentalDate, returnDate, totalCost, balanceLeft);

        System.out.println("\nThank you! Your rental has been recorded.");
    }
}

