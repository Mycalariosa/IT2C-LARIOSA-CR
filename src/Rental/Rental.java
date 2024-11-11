package Rental;

import it2c.lariosa.cr.CONFIG;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            System.out.println("| 3. RETURN RENTAL  |");
            System.out.println("| 4. UPDATE RENTAL  |");
            System.out.println("| 5. DELETE RENTAL  |");
            System.out.println("|-------------------|");

            System.out.print("Choose from 1-4: ");
            int action = sc.nextInt();

            while (action < 1 || action > 4) {
                System.out.print("\tInvalid action. Please enter a number between 1 and 5: ");
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
                   ReturnRental(sc);
                    break;
                case 4:
                    updateRental(sc);
                    break;
                case 5:
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
    
public void ReturnRental(Scanner sc) {
    int rentalID;

    // Prompt the user to enter a valid rental ID
    while (true) {
        System.out.print("Enter your Rental ID: ");
        if (sc.hasNextInt()) {
            rentalID = sc.nextInt();
            sc.nextLine(); // Consume the newline character after the integer

            // Check if the rental ID is valid
            if (rentalExists(rentalID)) {
                break; // Valid rental ID, proceed with return process
            } else {
                System.out.println("Invalid Rental ID. Please try again.");
            }
        } else {
            System.out.println("Invalid input. Please enter a numeric Rental ID.");
            sc.next(); // Clear the invalid input
        }
    }

    // Ask about damages if rental ID is valid
    System.out.print("Is there any damage to the rental item? (yes/no): ");
    String damageResponse = sc.nextLine().trim().toLowerCase();

    int damageFee = 0;

    if (damageResponse.equals("yes") || damageResponse.equals("y") || damageResponse.equals("yes") || damageResponse.equals("YES")) {
        System.out.println("Please select the type of damage:");
        System.out.println("1. Minimal damage - ₱200");
        System.out.println("2. Moderate damage - ₱500");
        System.out.println("3. Severe damage - ₱1000");

        System.out.print("Enter the number corresponding to the damage level: ");
        int damageLevel = sc.nextInt();
        sc.nextLine(); // Consume newline left-over

        switch (damageLevel) {
            case 1:
                damageFee = 200;
                break;
            case 2:
                damageFee = 500;
                break;
            case 3:
                damageFee = 1000;
                break;
            default:
                System.out.println("Invalid option. Returning rental canceled.");
                return;
        }
    }

    // Process payment
    System.out.println("The damage fee is: ₱" + damageFee);
    System.out.print("Proceed with payment? (yes/no): ");
    String paymentResponse = sc.nextLine().trim().toLowerCase();

    if (paymentResponse.equals("yes") || paymentResponse.equals("y") || paymentResponse.equals("YES") || paymentResponse.equals("Y")) {
        processPayment(damageFee);
        System.out.println("Rental returned successfully. Payment processed.");
    } else {
        System.out.println("Payment canceled. Rental return not completed.");
    }
}

// Modify this method to check if the rental ID exists as an integer
private boolean rentalExists(int rentalID) {
    // Logic to check if rental ID exists in the system
    return true; // Replace with actual check
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

        Scanner sc = new Scanner(System.in);
        System.out.print("\nWould you like to view an individual rental report? (Y/N): ");
        String choice = sc.nextLine();
        if (choice.equalsIgnoreCase("Y")) {
            indivRentalReport();
        }
    }

    public void indivRentalReport() {
        Scanner sc = new Scanner(System.in);
        CONFIG conf = new CONFIG();

        System.out.print("Enter Rental ID you want to view: ");
        int rentalId = sc.nextInt();

        // Validate if the rental ID exists
        String queryExistence = "SELECT COUNT(1) FROM Rental WHERE rental_id=?";
        int exists = conf.checkExistence(queryExistence, rentalId);

        if (exists == 0) {
            System.out.println("\tERROR: Rental ID doesn't exist.");
            return;
        }

        // Fetch rental and customer details
        String query = "SELECT r.*, c.c_fname, c.c_lname, c.c_contact, c.c_email "
                     + "FROM Rental r "
                     + "JOIN Customer c ON r.customer_id = c.c_id "
                     + "WHERE r.rental_id = ?";

        try (Connection con = conf.connectDB();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, rentalId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("c_fname");
                String lastName = rs.getString("c_lname");
                String customerId = rs.getString("customer_id");
                String contact = rs.getString("c_contact");
                String email = rs.getString("c_email");
                String rentalStartDate = rs.getString("rental_start_date");
                String rentalEndDate = rs.getString("rental_end_date");
                double rentalFee = rs.getDouble("r_price");
                double totalAmount = rs.getDouble("total_price");
                double payment = rs.getDouble("payment");
               int rentalDays = rs.getInt("num_days");

                System.out.println("\n***************************************************************************");
                System.out.println("                          RENTAL REPORT STATEMENT                          ");
                System.out.println("***************************************************************************");
                System.out.printf("%-20s: %-30s%n", "Rental ID", rentalId);
                System.out.printf("%-20s: %-30s%n", "Customer Name", firstName + " " + lastName);
                System.out.printf("%-20s: %-30s%n", "Customer ID", customerId);
                System.out.printf("%-20s: %-30s%n", "Contact", contact);
                System.out.printf("%-20s: %-30s%n", "Email", email);
                System.out.printf("%-20s: %-30s%n", "Rental Start Date", rentalStartDate);
                System.out.printf("%-20s: %-30s%n", "Rental End Date", rentalEndDate);
                System.out.println("---------------------------------------------------------------------------");
                System.out.printf("%-20s: %-30s PHP %.2f%n", "Rental Fee per Day", "", rentalFee);
                System.out.printf("%-20s: %-30s %d days%n", "Number of Rental Days", "", rentalDays);
                System.out.printf("%-20s: %-30s PHP %.2f%n", "Total Amount to pay", "", totalAmount);
                System.out.printf("%-20s: %-30s PHP %.2f%n", "Total Payments made", "", payment);
                System.out.println("***************************************************************************");

            } else {
                System.out.println("No rental report found for the given Rental ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving rental report: " + e.getMessage());
        }
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
    private void deleteRental(Scanner sc) {
    viewRentals();

    System.out.print("Enter Rental ID to delete: ");
    int rentalId = sc.nextInt();

    System.out.print("Are you sure you want to delete this rental? (yes/no): ");
    String confirmation = sc.next().toLowerCase();

    if (!confirmation.equals("yes")) {
        System.out.println("Deletion cancelled.");
        return;
    }

    CONFIG conf = new CONFIG();

    try {
        // Retrieve the clothing item ID associated with this rental
        String getClothingItemIdQuery = "SELECT clothing_item_id FROM Rental WHERE rental_id=?";
        int clothingItemId = conf.getSingleIntResult(getClothingItemIdQuery, rentalId);

        // Delete the rental record
        String sqlDelete = "DELETE FROM Rental WHERE rental_id=?";
        boolean isDeleted = conf.deleteRecord(sqlDelete, rentalId);

        if (isDeleted) {
            // Check if any other active rentals exist for this clothing item
            String checkRentalQuery = "SELECT COUNT(1) FROM Rental WHERE clothing_item_id = ? AND rental_end_date >= CURDATE()";
            int rentedCount = conf.checkExistence(checkRentalQuery, clothingItemId);

            if (rentedCount == 0) {
                // Update availability if no active rentals are found
                String updateAvailabilitySql = "UPDATE ClothingItem SET c_availability='available' WHERE clothing_ID=?";
                conf.updateRecord(updateAvailabilitySql, clothingItemId);
                System.out.println("Rental deleted successfully and item marked as available.");
            } else {
                System.out.println("Rental deleted successfully.");
            }
        } else {
            System.out.println("Rental not found or could not be deleted.");
        }
    } catch (Exception e) {
        System.out.println("Error deleting rental: " + e.getMessage());
    }
}
}