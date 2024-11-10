package Rental;

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
        System.out.println("Below is the list that you can borrow:");

        System.out.println("-----------------------------Customers--------------------------------------");
        displayCustomerDetails();
        System.out.println("--------------------------Clothing Item-------------------------------------");
        displayAvailableClothingItems();

        System.out.println("\nPlease fill out the following rental form.");

        int customerId;
        while (true) {
            System.out.print("Customer ID: ");
            customerId = sc.nextInt();

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

            String queryClothing = "SELECT COUNT(1) FROM ClothingItem WHERE clothing_ID=?";
            int clothingExists = conf.checkExistence(queryClothing, clothingItemId);

            if (clothingExists > 0) {
                break;
            } else {
                System.out.println("Invalid Clothing Item ID. Please enter a valid ID.");
            }
        }

        sc.nextLine(); 
        System.out.print("------------------------------------\n");

        System.out.print("Rental Start Date (YYYY-MM-DD): ");
        String startDateStr = sc.next();

        System.out.print("Rental End Date (YYYY-MM-DD): ");
        String endDateStr = sc.next();

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        System.out.print("------------------------------------\n");

        long numDays = ChronoUnit.DAYS.between(startDate, endDate);
        System.out.println("Number of Days: " + numDays);

        System.out.print("------------------------------------\n");

        System.out.print("Price per Day: ");
        double dailyPrice = sc.nextDouble();

        double totalPrice = numDays * dailyPrice;
        System.out.printf("Total Price: %.2f\n", totalPrice);

        double payment = 0;
        while (payment < totalPrice) {
            System.out.print("Payment: ");
            payment = sc.nextDouble();
            if (payment < totalPrice) {
                System.out.println("Not enough payment. Please input a new amount.");
            }
        }
        System.out.print("------------------------------------\n");

        double change = payment - totalPrice;
        System.out.printf("Change: %.2f\n", change);

        String sql = "INSERT INTO Rental (customer_id, clothing_item_id, rental_start_date, rental_end_date, num_days, r_price, total_price, payment, change) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        conf.addRecord(sql, customerId, clothingItemId, startDateStr, endDateStr, numDays, dailyPrice, totalPrice, payment, change);
        
        String updateAvailabilitySql = "UPDATE ClothingItem SET c_availability='unavailable' WHERE clothing_ID=?";
        conf.updateRecord(updateAvailabilitySql, clothingItemId);

        System.out.println("Rental added successfully and item marked as unavailable.");
    }

    private void deleteRental(Scanner sc) {
        viewRentals();

        System.out.print("Enter Rental ID to delete: ");
        int rentalId = sc.nextInt();

        CONFIG conf = new CONFIG();

        String getClothingItemIdQuery = "SELECT clothing_item_id FROM Rental WHERE rental_id=?";
        int clothingItemId = conf.getSingleIntResult(getClothingItemIdQuery, rentalId);

        String sqlDelete = "DELETE FROM Rental WHERE rental_id=?";
        conf.deleteRecord(sqlDelete, rentalId);

        String checkRentalQuery = "SELECT COUNT(1) FROM Rental WHERE clothing_item_id = ? AND rental_end_date >= CURDATE()";
        int rentedCount = conf.checkExistence(checkRentalQuery, clothingItemId);

        if (rentedCount == 0) {
            String updateAvailabilitySql = "UPDATE ClothingItem SET c_availability='available' WHERE clothing_ID=?";
            conf.updateRecord(updateAvailabilitySql, clothingItemId);
            System.out.println("Rental deleted successfully and item marked as available.");
        } else {
            System.out.println("Rental deleted successfully.");
        }
    }

    private void displayCustomerDetails() {
        CONFIG conf = new CONFIG();
        String query = "SELECT c_id, c_fname, c_lname FROM Customer";
        String[] headers = {"ID", "First Name", "Last Name"};
        String[] columns = {"c_id", "c_fname", "c_lname"};
        conf.viewRecords(query, headers, columns);
    }

    private void displayAvailableClothingItems() {
        CONFIG conf = new CONFIG();
        String query = "SELECT clothing_ID, c_name, c_price FROM ClothingItem WHERE c_availability='available'";
        String[] headers = {"ID", "Name", "Price"};
        String[] columns = {"clothing_ID", "c_name","c_price"};
        conf.viewRecords(query, headers, columns);
    }

    private void viewRentals() {
        CONFIG con = new CONFIG();
        String query = "SELECT rental_id, customer_id, clothing_item_id FROM Rental";
        String[] headers = {"Rental ID", "Customer ID", "Clothing Item ID"};
        String[] columns = {"rental_id", "customer_id", "clothing_item_id"};
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

        sc.nextLine();

        System.out.print("New Rental Start Date (YYYY-MM-DD): ");
        String newStartDateStr = sc.next();

        System.out.print("New Rental End Date (YYYY-MM-DD): ");
        String newEndDateStr = sc.next();

        LocalDate newStartDate = LocalDate.parse(newStartDateStr);
        LocalDate newEndDate = LocalDate.parse(newEndDateStr);

        long newNumDays = ChronoUnit.DAYS.between(newStartDate, newEndDate);
        System.out.println("Number of Days (computed automatically): " + newNumDays);

        System.out.print("New Price per Day: ");
        double newDailyPrice = sc.nextDouble();

        double newTotalPrice = newNumDays * newDailyPrice;
        System.out.printf("New Total Price: %.2f\n", newTotalPrice);

        double newPayment = 0;
        while (newPayment < newTotalPrice) {
            System.out.print("New Payment: ");
            newPayment = sc.nextDouble();
            if (newPayment < newTotalPrice) {
                System.out.println("Not enough payment. Please input a new amount.");
            }
        }

        double newChange = newPayment - newTotalPrice;
        System.out.printf("New Remaining Balance: %.2f\n", newChange);

        String qry = "UPDATE Rental SET customer_id=?, clothing_item_id=?, rental_start_date=?, rental_end_date=?, num_days=?, r_price=?, total_price=?, payment=?, change=? WHERE rental_id=?";
        CONFIG con = new CONFIG();
        con.updateRecord(qry, newCustomerId, newClothingItemId, newStartDateStr, newEndDateStr, newNumDays, newDailyPrice, newTotalPrice, newPayment, newChange, rentalId);
        System.out.println("Rental updated successfully.");
    }
}