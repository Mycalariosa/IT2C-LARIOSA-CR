package Rental;

import ClothingItem.ClothingItem;
import it2c.lariosa.cr.CONFIG;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Rental {

    public void rentalTransaction() {
        Scanner sc = new Scanner(System.in);
        String ch;

        do {
            System.out.println("\n|-------------------|");
            System.out.println("|     RENTAL MENU   |");
            System.out.println("|-------------------|");
            System.out.println("| 1. ADD RENTAL     |");
            System.out.println("| 2. VIEW RENTALS   |");
            System.out.println("| 3. UPDATE RENTAL  |");
            System.out.println("| 4. DELETE RENTAL  |");
            System.out.println("|-------------------|");

            System.out.print("Choose from 1-4: ");
            int action = sc.nextInt();

            while (action < 1 || action > 4) {
                System.out.print("\tInvalid action. Please enter a number between 1 and 4: ");
                action = sc.nextInt();
            }

            switch (action) {
                case 1:
                    addRental(sc);
                    break;
                case 2:
                    viewRentals();
                    break;
                case 3:
                    updateRental(sc);
                    break;
                case 4:
                    deleteRental(sc);
                    break;
            }

            System.out.print("\nDo you want to use Rental menu? (Y/N): ");
            ch = sc.next();
        } while (ch.equalsIgnoreCase("Y"));

        System.out.println("\nThank you for using the Rental application");
    }

    public void addRental(Scanner sc) {
        CONFIG conf = new CONFIG();

        System.out.print("\n------------------------------------\n");
        System.out.println("Welcome to Clothing Rental!");
        System.out.print("------------------------------------\n");
        System.out.println("Below is the list of items you can borrow:");

        ClothingItem clothingItem = new ClothingItem();
        clothingItem.viewClothingItem();

        System.out.println("\nPlease fill out the following rental form.");

        int customerId;
        while (true) {
            System.out.print("Customer ID: ");
            customerId = sc.nextInt();

            // Validate if the customer ID exists
            String query = "SELECT COUNT(1) FROM Customer WHERE c_id=?";
            int exists = conf.checkExistence(query, customerId);

            if (exists > 0) {
                break;
            } else {
                System.out.println("Invalid Customer ID. Please enter an existing one.");
            }
        }

        int clothingItemId;
        while (true) {
            System.out.print("Clothing Item ID: ");
            clothingItemId = sc.nextInt();

            // Check if the clothing item ID exists in the ClothingItem table
            String queryClothing = "SELECT COUNT(1) FROM ClothingItem WHERE clothing_ID=?";
            int clothingExists = conf.checkExistence(queryClothing, clothingItemId);

            if (clothingExists > 0) {
                break;
            } else {
                System.out.println("Invalid Clothing Item ID. Please enter a valid ID.");
            }
        }

        // Input additional item details
        sc.nextLine(); // Consume newline left-over
        System.out.print("Item Detail: ");
        String itemDetail = sc.nextLine();
        System.out.print("------------------------------------\n");

        System.out.print("Rental Start Date (YYYY-MM-DD): ");
        String startDateStr = sc.next();

        System.out.print("Rental End Date (YYYY-MM-DD): ");
        String endDateStr = sc.next();

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        System.out.print("------------------------------------\n");

        // Calculate the number of rental days
        long numDays = ChronoUnit.DAYS.between(startDate, endDate);
        System.out.println("Number of Days: " + numDays);

        System.out.print("------------------------------------\n");

        // Input the daily price
        System.out.print("Price per Day: ");
        double dailyPrice = sc.nextDouble();

        // Calculate total price
        double totalPrice = numDays * dailyPrice;
        System.out.printf("Total Price: %.2f\n", totalPrice);

        System.out.print("Down Payment: ");
        double downPayment = sc.nextDouble();
        System.out.print("------------------------------------\n");

        double remainingBalance = totalPrice - downPayment;
        System.out.printf("Remaining Balance: %.2f\n", remainingBalance);

        // Insert rental record with r_price and item_detail
        String sql = "INSERT INTO Rental (customer_id, clothing_item_id, item_detail, rental_start_date, rental_end_date, num_days, r_price, total_price, down_payment, remaining_balance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        conf.addRecord(sql, customerId, clothingItemId, itemDetail, startDateStr, endDateStr, numDays, dailyPrice, totalPrice, downPayment, remainingBalance);
        System.out.println("Rental added successfully.");
    }

    private void viewRentals() {
        CONFIG con = new CONFIG();

        String query = "SELECT * FROM Rental";
        String[] headers = {"Rental ID", "Customer ID", "Clothing Item ID", "Item Detail", "Start Date", "End Date", "Number of Days", "Price per Day", "Total Price", "Down Payment", "Remaining Balance"};
        String[] columns = {"rental_id", "customer_id", "clothing_item_id", "item_detail", "rental_start_date", "rental_end_date", "num_days", "r_price", "total_price", "down_payment", "remaining_balance"};

        con.viewRecords(query, headers, columns);
    }

    private void updateRental(Scanner sc) {
        viewRentals();

        System.out.print("Enter Rental ID to update: ");
        int rentalId = sc.nextInt();

        System.out.print("New Customer ID: ");
        int newCustomerId = sc.nextInt();

        System.out.print("New Clothing Item ID: ");
        int newClothingItemId = sc.nextInt();

        sc.nextLine(); // Consume newline
        System.out.print("New Item Detail: ");
        String newItemDetail = sc.nextLine();

        System.out.print("New Rental Start Date (YYYY-MM-DD): ");
        String newStartDateStr = sc.next();

        System.out.print("New Rental End Date (YYYY-MM-DD): ");
        String newEndDateStr = sc.next();

        LocalDate newStartDate = LocalDate.parse(newStartDateStr);
        LocalDate newEndDate = LocalDate.parse(newEndDateStr);

        long newNumDays = ChronoUnit.DAYS.between(newStartDate, newEndDate);
        System.out.println("Number of Days (computed automatically): " + newNumDays);

        // Input the new daily price
        System.out.print("New Price per Day: ");
        double newDailyPrice = sc.nextDouble();

        // Calculate total price
        double newTotalPrice = newNumDays * newDailyPrice;
        System.out.printf("New Total Price: %.2f\n", newTotalPrice);

        System.out.print("New Down Payment: ");
        double newDownPayment = sc.nextDouble();

        double newRemainingBalance = newTotalPrice - newDownPayment;
        System.out.printf("New Remaining Balance: %.2f\n", newRemainingBalance);

        // Update rental record with new r_price and item_detail
        String qry = "UPDATE Rental SET customer_id=?, clothing_item_id=?, item_detail=?, rental_start_date=?, rental_end_date=?, num_days=?, r_price=?, total_price=?, down_payment=?, remaining_balance=? WHERE rental_id=?";
        CONFIG con = new CONFIG();
        con.updateRecord(qry, newCustomerId, newClothingItemId, newItemDetail, newStartDateStr, newEndDateStr, newNumDays, newDailyPrice, newTotalPrice, newDownPayment, newRemainingBalance, rentalId);
        System.out.println("Rental updated successfully.");
    }

    private void deleteRental(Scanner sc) {
        viewRentals();

        System.out.print("Enter Rental ID to delete: ");
        int rentalId = sc.nextInt();

        String sqlDelete = "DELETE FROM Rental WHERE rental_id=?";
        CONFIG con = new CONFIG();
        con.deleteRecord(sqlDelete, rentalId);
        System.out.println("Rental deleted successfully.");
    }
}
