package Rental;

import it2c.lariosa.cr.CONFIG;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import ClothingItem.ClothingItem;

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

            System.out.print("Choose from 1-5: ");
            int action = sc.nextInt();

            while (action < 1 || action > 5) {
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
        ClothingItem clothingItem = new ClothingItem();

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

        int clothingItemId = 0;
       System.out.print("Enter Clothing Item ID to rent: ");
        int id = sc.nextInt();
        
        // Check if the item is available
        String availabilityQuery = "SELECT c_availability FROM ClothingItem WHERE clothing_ID = ?";
        String currentAvailability = conf.getRecord(availabilityQuery, id);
        
        if (currentAvailability != null && currentAvailability.equalsIgnoreCase("available")) {
            // Proceed with rental process
            String rentalQuery = "INSERT INTO Rental (clothing_item_id, rental_start_date, rental_end_date) VALUES (?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY))";
            conf.addRecord(rentalQuery, id);
            
            // Update clothing item status to unavailable
            clothingItem.updateAvailability(id, "unavailable");
            System.out.println("Rental added successfully and clothing item marked as unavailable.");
        } else {
            System.out.println("Clothing item is not available for rent.");
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
     private void viewRentals() {
    CONFIG con = new CONFIG();
    // SQL query to fetch rental data including status from the Rental table
    String query = "SELECT rental_id, customer_id, clothing_item_id, r_status FROM Rental";
    
    // Define headers and columns for the table display
    String[] headers = {"Rental ID", "Customer ID", "Clothing Item ID", "Status"};
    
    // Get the records using getRecords method
    ResultSet rs = null;
    try {
        rs = con.getRecords(query, new String[]{"rental_id", "customer_id", "clothing_item_id", "r_status"});
        
        // Check if result set is empty
        if (rs != null && !rs.isBeforeFirst()) {
            System.out.println("No rental records found.");
            return;
        }
        
        // Print the table header
        System.out.println("\n|----------------------------------------------------|");
        System.out.println("|                   RENTAL LIST                    |");
        System.out.println("|----------------------------------------------------|");
        System.out.printf("| %-15s %-15s %-20s %-10s |\n", headers[0], headers[1], headers[2], headers[3]);
        System.out.println("|----------------------------------------------------|");
        
        // Process and display the records
        while (rs.next()) {
            int rentalId = rs.getInt("rental_id");
            int customerId = rs.getInt("customer_id");
            int clothingItemId = rs.getInt("clothing_item_id");
            String rStatus = rs.getString("r_status");
            
            if (null == rStatus) {
                rStatus = "rented";  // Default status if null
            } else // Check if rStatus is null and assign default value
            switch (rStatus) {
                case "R":
                    rStatus = "rented";  // If "R" is stored in the database, show as "rented"
                    break;
                case "C":
                    rStatus = "returned";  // If "C" is stored in the database, show as "returned"
                    break;
                default:
                    break;
            }
            
            // Print out each record with proper formatting
            System.out.printf("| %-15d %-15d %-20d %-10s |\n", rentalId, customerId, clothingItemId, rStatus);
        }
        
        System.out.println("|----------------------------------------------------|");

        // Prompt user if they want to view individual rental report
        Scanner sc = new Scanner(System.in);
        System.out.print("\nWould you like to view an individual rental report? (Y/N): ");
        String choice = sc.nextLine();
        if (choice.equalsIgnoreCase("Y")) {
            indivRentalReport();
        }

    } catch (SQLException e) {
        System.out.println("Error displaying rental records: " + e.getMessage());
    } finally {
        // Ensure the ResultSet is closed to prevent resource leaks
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing ResultSet: " + e.getMessage());
        }
    }
}

public void ReturnRental(Scanner sc) {
    CONFIG conf = new CONFIG();
    ClothingItem clothingItem = new ClothingItem(); // Assuming ClothingItem class exists

    System.out.println("\n---------------------------- Current Rented Items ----------------------------");
    displayRentedItems(); // Display all rented items before prompting

    int rentalId;
    boolean validId = false;

    do {
        System.out.print("Enter Rental ID you want to return: ");
        rentalId = sc.nextInt();

        // Validate if the rental ID exists
        String queryExistence = "SELECT COUNT(1) FROM Rental WHERE rental_id=?";
        int exists = conf.checkExistence(queryExistence, rentalId);

        if (exists == 0) {
            System.out.println("\tERROR: Rental ID doesn't exist.");
            System.out.print("Would you like to try again? (Y/N): ");
            String retry = sc.next();
            if (!retry.equalsIgnoreCase("Y")) {
                System.out.println("Exiting return process.");
                return;
            }
        } else {
            validId = true; // ID is valid, exit the loop
        }
    } while (!validId);

    String getEndDateQuery = "SELECT rental_end_date, r_price FROM Rental WHERE rental_id=?";
    LocalDate rentalEndDate;
    double dailyRate;

    try (Connection con = conf.connectDB();
         PreparedStatement pst = con.prepareStatement(getEndDateQuery)) {
        pst.setInt(1, rentalId);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            rentalEndDate = LocalDate.parse(rs.getString("rental_end_date"));
            dailyRate = rs.getDouble("r_price");

            System.out.println("\nRental End Date (must be returned by): " + rentalEndDate);
        } else {
            System.out.println("Rental end date not found. Please check the Rental ID.");
            return;
        }
    } catch (SQLException e) {
        System.out.println("Error retrieving rental end date: " + e.getMessage());
        return;
    }

    System.out.print("Enter today's date (YYYY-MM-DD): ");
    String todayDateStr = sc.next();
    LocalDate todayDate = LocalDate.parse(todayDateStr);

    int damageFee = 0;
    if (todayDate.isAfter(rentalEndDate)) {
        long lateDays = ChronoUnit.DAYS.between(rentalEndDate, todayDate);
        double lateFee = lateDays * dailyRate;
        System.out.printf("Late return fee for %d day(s): PHP %.2f%n", lateDays, lateFee);
        damageFee += lateFee;
    }

    System.out.println("\n---------------------------------------");
    System.out.println("|       Damage Type       |   Charge   |");
    System.out.println("---------------------------------------");
    System.out.println("| 1. Minimal damage        |   ₱200     |");
    System.out.println("| 2. Moderate damage       |   ₱500     |");
    System.out.println("| 3. Severe damage         |   ₱1000    |");
    System.out.println("---------------------------------------");
    System.out.println("\nNote: Additional charges apply for late returns.");

    sc.nextLine(); // Consume newline left-over
    System.out.print("\nIs there any damage to the rental item? (yes/no): ");
    String damageResponse = sc.nextLine().trim().toLowerCase();

    if (damageResponse.equals("yes") || damageResponse.equals("y")) {
        System.out.print("Enter the number corresponding to the damage level: ");
        int damageLevel = sc.nextInt();
        sc.nextLine(); // Consume newline left-over

        switch (damageLevel) {
            case 1:
                damageFee += 200;
                break;
            case 2:
                damageFee += 500;
                break;
            case 3:
                damageFee += 1000;
                break;
            default:
                System.out.println("Invalid option. Returning rental canceled.");
                return;
        }
    }

    System.out.println("\nTotal additional charge (including late and damage fees): PHP " + damageFee);
    System.out.print("Proceed with payment? (yes/no): ");
    String paymentResponse = sc.nextLine().trim().toLowerCase();

    if (paymentResponse.equals("yes") || paymentResponse.equals("y")) {
        processPayment(damageFee);

        // Update the r_status to "returned"
        String updateStatusQuery = "UPDATE Rental SET r_status = 'returned' WHERE rental_id = ?";
        try (Connection con = conf.connectDB();
             PreparedStatement pst = con.prepareStatement(updateStatusQuery)) {
            pst.setInt(1, rentalId);
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Rental status updated to 'returned'.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating rental status: " + e.getMessage());
        }

        System.out.println("Rental returned successfully. Payment processed.");
    } else {
        System.out.println("Payment canceled. Rental return not completed.");
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
            // Check if there are any other active rentals for this clothing item
            String checkRentalQuery = "SELECT COUNT(1) FROM Rental WHERE clothing_item_id = ? AND rental_end_date >= CURDATE()";
            int rentedCount = conf.checkExistence(checkRentalQuery, clothingItemId);

            if (rentedCount == 0) {
                // If no active rentals, update availability of the clothing item
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
        // Provide more specific error handling
        System.out.println("Error deleting rental: " + e.getMessage());
    }
}


   private void displayRentedItems() {
    CONFIG con = new CONFIG();
    String query = "SELECT rental_id, customer_id, clothing_item_id FROM Rental";
    String[] headers = {"Rental ID", "Customer ID", "Clothing Item ID"};
    String[] columns = {"rental_id", "customer_id", "clothing_item_id"};
    con.viewRecords(query, headers, columns);
}


private void processPayment(int requiredAmount) {
    Scanner sc = new Scanner(System.in);
    double payment = 0;

    // Continuously prompt until payment meets or exceeds the required amount
    while (payment < requiredAmount) {
        System.out.print("Enter payment amount: ");
        double amountEntered = sc.nextDouble();

        payment += amountEntered;

        if (payment < requiredAmount) {
            System.out.printf("Insufficient amount. You still owe: PHP %.2f%n", (requiredAmount - payment));
        }
    }

    // Calculate and display change, if any
    double change = payment - requiredAmount;
    System.out.printf("Payment successful. Change: PHP %.2f%n", change);
}}
