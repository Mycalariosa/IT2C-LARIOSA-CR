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
import java.sql.Date;

public class Rental {

    public void rentalTransaction() {
        Scanner sc = new Scanner(System.in);
        String ch;
        Scanner in = new Scanner (System.in);
        String another =null;

        do {
            System.out.println("\n|-------------------|");
            System.out.println("|     RENTAL MENU   |");
            System.out.println("|-------------------|");
            System.out.println("| 1. ADD RENTAL     |");
            System.out.println("| 2. VIEW RENTALS   |");
            System.out.println("| 3. RETURN RENTAL  |");
            System.out.println("| 4. UPDATE RENTAL  |");
            System.out.println("| 5. DELETE RENTAL  |");
            System.out.println("| 6. EXIT           |");
            System.out.println("|-------------------|");

            int action = -1;  // Initialize with an invalid action
            boolean validInput = false;

            // Loop until a valid integer between 1 and 5 is entered
            while (!validInput) {
                System.out.print("Choose from 1-6: ");
                if (sc.hasNextInt()) {
                    action = sc.nextInt();
                    if (action >= 1 && action <= 6) {
                        validInput = true;
                    } else {
                        System.out.println("\nInvalid action. Please enter a number between 1 and 6.");
                    }
                } else {
                    System.out.println("\nInvalid input. Please enter a number.");
                    sc.next(); // Clear the invalid input
                }
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
                case 6:
                    System.out.println("Thankyou for using Rental Menu!");
                    return;
            }

            
            System.out.print("\nDo you still want to use Rental menu? (Y/N): ");
            another = in.next().trim();
            
            while(!another.equals("Yes")&&!another.equals("yes")&&!another.equals("YES")&&!another.equals("y")&&!another.equals("Y")&&!another.equals("n")&&!another.equals("N")&&!another.equals("No")&&!another.equals("NO")&&!another.equals("no")){
                System.out.println("Enter again:");
                another=in.next().trim();
        }
            }while (another.equals("YES")||another.equals("yes")||another.equals("Yes")||another.equals("y")||another.equals("Y"));
                System.out.println("Thamkyou for using Rental Application");
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

    int clothingItemId;
    while (true) {
        System.out.print("Enter Clothing Item ID to rent: ");
        clothingItemId = sc.nextInt();

        // Check if the item is available
        String availabilityQuery = "SELECT c_availability FROM ClothingItem WHERE clothing_ID = ?";
        String currentAvailability = conf.getRecord(availabilityQuery, clothingItemId);

        if (currentAvailability != null && currentAvailability.equalsIgnoreCase("available")) {
            // Proceed with rental process
            break;
        } else {
            System.out.println("Clothing item is not available for rent. Please try another item.");
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

    // Insert rental record into the database with r_status as "rented"
    String sql = "INSERT INTO Rental (customer_id, clothing_item_id, rental_start_date, rental_end_date, num_days, r_price, total_price, payment, change, r_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    conf.addRecord(sql, customerId, clothingItemId, startDateStr, endDateStr, numDays, dailyPrice, totalPrice, payment, change, "rented");

    // Update clothing item availability to "unavailable"
    String updateAvailabilitySql = "UPDATE ClothingItem SET c_availability='unavailable' WHERE clothing_ID=?";
    conf.updateRecord(updateAvailabilitySql, clothingItemId);

    System.out.println("Rental added successfully and item marked as unavailable.");
}


   private void viewRentals() {
    CONFIG con = new CONFIG();
    
    // SQL query to fetch rental data from relevant tables
    String query = "SELECT r.rental_id, c.c_lname, c.c_fname, ci.c_name, r.r_status " +
                   "FROM Rental r " +
                   "JOIN Customer c ON r.customer_id = c.c_ID " +
                   "JOIN ClothingItem ci ON r.clothing_item_id = ci.Clothing_ID";

    // Define headers for the table display
    String[] headers = {"Rental ID", "Customer Name", "Clothing Name", "Status"};
    ResultSet rs = null;

    try {
        rs = con.getRecords(query, null);
        
        // Check if result set is empty
        if (rs == null || !rs.isBeforeFirst()) {
            System.out.println("No rental records found.");
            return;
        }
        
        // Print the table header
        System.out.println("|-------------------------------------------------------------------------------------------|");
        System.out.println("|                                        RENTAL LIST                                        |");
        System.out.println("|-------------------------------------------------------------------------------------------|");
        System.out.printf("| %-15s | %-25s | %-25s | %-15s |\n", headers[0], headers[1], headers[2], headers[3]);
        System.out.println("|-------------------------------------------------------------------------------------------|");

        // Process and display the records
        while (rs.next()) {
            int rentalId = rs.getInt("rental_id");
            String customerName = rs.getString("c_lname") + ", " + rs.getString("c_fname");
            String clothingName = rs.getString("c_name");
            String rStatus = rs.getString("r_status");

            // Default status if null or map to descriptive status
            if (rStatus == null) {
                rStatus = "Rented";
            } else {
                switch (rStatus) {
                    case "R":
                        rStatus = "Rented";
                        break;
                    case "C":
                        rStatus = "Returned";
                        break;
                    default:
                }
            }

            // Print out each record with proper formatting
            System.out.printf("| %-15d | %-25s | %-25s | %-15s |\n", rentalId, customerName, clothingName, rStatus);
        }

        System.out.println("|-------------------------------------------------------------------------------------------|");

        // Ask the user if they want to view an individual rental report
        Scanner sc = new Scanner(System.in);
        String choice;
        do {
            System.out.print("\nWould you like to view an individual rental report? (Y/N): ");
            choice = sc.nextLine().trim();

            if (!choice.equalsIgnoreCase("Y") && !choice.equalsIgnoreCase("N")) {
                System.out.println("Invalid input. Please enter Y for Yes or N for No.");
            }
        } while (!choice.equalsIgnoreCase("Y") && !choice.equalsIgnoreCase("N"));

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

    // Display active rentals before prompting for return
    System.out.println("\n---------------------------- Current Active Rentals ----------------------------");
    displayActiveRentals(); // Display only active rentals

    int rentalId;
    boolean validId = false;

    do {
        System.out.print("Enter Rental ID you want to return: ");
        rentalId = sc.nextInt();

        // Validate if the rental ID exists and is active
        String queryExistence = "SELECT COUNT(1) FROM Rental WHERE rental_id=? AND r_status='rented'";
        int exists = conf.checkExistence(queryExistence, rentalId);

        if (exists == 0) {
            System.out.println("\tERROR: Rental ID doesn't exist or is not active.");
            System.out.print("Would you like to try again? (Y/N): ");
            String retry = sc.next();
            if (!retry.equalsIgnoreCase("Y")) {
                System.out.println("Exiting return process.");
                return;
            }
        } else {
            validId = true; // ID is valid and active, exit the loop
        }
    } while (!validId);

    // Retrieve rental details
    String getEndDateQuery = "SELECT rental_end_date, r_price, clothing_id FROM Rental WHERE rental_id=?";
    LocalDate rentalEndDate;
    double dailyRate;
    int clothingId;
    double lateFee = 0.0;

    try (Connection con = conf.connectDB();
         PreparedStatement pst = con.prepareStatement(getEndDateQuery)) {
        pst.setInt(1, rentalId);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            rentalEndDate = LocalDate.parse(rs.getString("rental_end_date"));
            dailyRate = rs.getDouble("r_price");
            clothingId = rs.getInt("clothing_id"); // Retrieve associated clothing item ID

            System.out.println("\nRental End Date (must be returned by): " + rentalEndDate);
        } else {
            System.out.println("Rental end date not found. Please check the Rental ID.");
            return;
        }
    } catch (SQLException e) {
        System.out.println("Error retrieving rental details: " + e.getMessage());
        return;
    }

    // Process return date and calculate late fee
    System.out.print("Enter today's date (YYYY-MM-DD): ");
    String todayDateStr = sc.next();
    LocalDate todayDate = LocalDate.parse(todayDateStr);

    int damageFee = 0;
    if (todayDate.isAfter(rentalEndDate)) {
        long lateDays = ChronoUnit.DAYS.between(rentalEndDate, todayDate);
        lateFee = lateDays * (dailyRate + 200); // Modified calculation: daily rate plus 200 pesos per day
        System.out.printf("Late return fee for %d day(s): PHP %.2f%n", lateDays, lateFee);
        damageFee += lateFee;
    }

    // Handle damage fees
    System.out.println("\n---------------------------------------");
    System.out.println("|       Damage Type       |   Charge   |");
    System.out.println("---------------------------------------");
    System.out.println("| 1. Minimal damage        |   ₱200     |");
    System.out.println("| 2. Moderate damage       |   ₱500     |");
    System.out.println("| 3. Severe damage         |   ₱1000    |");
    System.out.println("---------------------------------------");
    System.out.println("\nNote: Additional charges apply for late returns.(dailyprice+200)");

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

        // Update the rental and clothing item status
        String updateRentalStatusQuery = "UPDATE Rental SET r_status = 'returned', damage_charge = ?, late_return_charge = ?, current_date = ? WHERE rental_id = ?";
        String updateClothingAvailabilityQuery = "UPDATE ClothingItem SET c_availability = 'available' WHERE clothing_ID = ?";

        try (Connection con = conf.connectDB();
             PreparedStatement rentalPst = con.prepareStatement(updateRentalStatusQuery);
             PreparedStatement clothingPst = con.prepareStatement(updateClothingAvailabilityQuery)) {

            // Update rental status
            rentalPst.setDouble(1, damageFee - lateFee); // Only the damage fee
            rentalPst.setDouble(2, lateFee); // Late return charge
            rentalPst.setDate(3, java.sql.Date.valueOf(todayDate)); // Setting current date as return date
            rentalPst.setInt(4, rentalId);
            rentalPst.executeUpdate();

            // Update clothing item availability
            clothingPst.setInt(1, clothingId);
            clothingPst.executeUpdate();

            System.out.println("Rental returned successfully. Clothing item marked as 'available'.");
        } catch (SQLException e) {
            System.out.println("Error updating rental or clothing item status: " + e.getMessage());
        }
    } else {
        System.out.println("Payment canceled. Rental return not completed.");
    }
}


// Method to display active rentals
private void displayActiveRentals() {
    String activeRentalsQuery = "SELECT r.rental_id, c.c_lname, c.c_fname, ci.c_name, r.r_status, " +
                                "r.rental_start_date, r.rental_end_date " +
                                "FROM Rental r " +
                                "JOIN Customer c ON r.customer_id = c.c_ID " +
                                "JOIN ClothingItem ci ON r.clothing_item_id = ci.Clothing_ID " +
                                "WHERE r.r_status = 'rented'";  // Only rented rentals are shown

    String[] headers = {"Rental ID", "Customer Name", "Clothing Name", "Status", "Start Date", "End Date"};

    try (Connection con = CONFIG.connectDB();
         PreparedStatement pst = con.prepareStatement(activeRentalsQuery);
         ResultSet rs = pst.executeQuery()) {

        // Check if result set is empty
        if (!rs.isBeforeFirst()) {
            System.out.println("No active rental records found.");
            return;
        }

        // Print table headers
        System.out.println("|----------------------------------------------------------------------------------------------------------|");
        System.out.println("|                                         ACTIVE RENTALS                                                   |");
        System.out.println("|----------------------------------------------------------------------------------------------------------|");
        System.out.printf("| %-10s | %-25s | %-20s | %-10s | %-12s | %-12s |%n",
                (Object[]) headers);
        System.out.println("|----------------------------------------------------------------------------------------------------------|");

        // Iterate through and display records
        while (rs.next()) {
            int rentalId = rs.getInt("rental_id");
            String customerName = rs.getString("c_lname") + ", " + rs.getString("c_fname");
            String clothingName = rs.getString("c_name");
            String status = rs.getString("r_status");
            String startDate = rs.getString("rental_start_date");
            String endDate = rs.getString("rental_end_date");

            System.out.printf("| %-10d | %-25s | %-20s | %-10s | %-12s | %-12s |%n",
                    rentalId, customerName, clothingName, status, startDate, endDate);
        }

        System.out.println("|----------------------------------------------------------------------------------------------------------|");

    } catch (SQLException e) {
        System.out.println("Error retrieving active rentals: " + e.getMessage());
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
    String query = "SELECT clothing_ID, c_name, c_price, c_availability FROM ClothingItem";
    String[] headers = {"ID", "Name", "Price", "Availability"};
    String[] columns = {"clothing_ID", "c_name", "c_price", "c_availability"};

    // Call the method to display records with the updated columns
    conf.viewRecords(query, headers, columns);
}

public void indivRentalReport() {
    Scanner sc = new Scanner(System.in);
    CONFIG conf = new CONFIG();
    boolean validId = false;
    int rentalId = 0; // Declare rentalId here

    do {
        System.out.print("Enter Rental ID you want to view: ");
        rentalId = sc.nextInt();

        // Validate if the rental ID exists
        String queryExistence = "SELECT COUNT(1) FROM Rental WHERE rental_id=?";
        int exists = conf.checkExistence(queryExistence, rentalId);

        if (exists == 0) {
            System.out.println("\tERROR: Rental ID doesn't exist.");
            System.out.print("Would you like to try again? (Y/N): ");
            String retry = sc.next();
            if (!retry.equalsIgnoreCase("Y")) {
                System.out.println("Exiting view rental process.");
                return;
            }
        } else {
            validId = true; // ID is valid, exit the loop
            System.out.println("Rental ID is valid. Displaying rental summary...");
        }
    } while (!validId);

    // Query to fetch detailed rental information
    String query = "SELECT r.*, c.c_fname, c.c_lname, c.c_contact, c.c_email, ci.c_name "
                 + "FROM Rental r "
                 + "JOIN Customer c ON r.customer_id = c.c_id "
                 + "JOIN ClothingItem ci ON r.clothing_item_id = ci.clothing_ID "
                 + "WHERE r.rental_id = ?";

    try (Connection con = CONFIG.connectDB();
         PreparedStatement pst = con.prepareStatement(query)) {

        pst.setInt(1, rentalId);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            // Retrieve and format all necessary data
            String firstName = rs.getString("c_fname");
            String lastName = rs.getString("c_lname");
            String customerId = rs.getString("customer_id");
            String contact = rs.getString("c_contact");
            String email = rs.getString("c_email");
            String clothingName = rs.getString("c_name");
            String rentalStartDate = rs.getString("rental_start_date");
            String rentalEndDate = rs.getString("rental_end_date");
            double rentalFee = rs.getDouble("r_price");
            double totalAmount = rs.getDouble("total_price");
            double payment = rs.getDouble("payment");
            int rentalDays = rs.getInt("num_days");
            String rentalStatus = rs.getString("r_status");
            double damageCharge = rs.getDouble("damage_charge");
            double lateReturnCharge = rs.getDouble("late_return_charge");
            double change = rs.getDouble("change");

            // Calculate the total payments made
            double totalPayments = payment - change + damageCharge + lateReturnCharge;

            // Calculate total rental days including late return days
            // Assuming that the late return charge corresponds to the late return days, for example:
            int lateReturnDays = (lateReturnCharge > 0) ? (int) (lateReturnCharge / rentalFee) : 0; // Calculate late days based on charge

            int totalRentalDays = rentalDays + lateReturnDays; // Add late days to rental days

            // Print the rental report
            System.out.println("\n***************************************************************************");
            System.out.println("                          RENTAL REPORT STATEMENT                          ");
            System.out.println("***************************************************************************");
            System.out.printf("%-30s: %-30s%n", "Rental ID", rentalId);
            System.out.printf("%-30s: %-30s%n", "Customer Name", firstName + " " + lastName);
            System.out.printf("%-30s: %-30s%n", "Customer ID", customerId);
            System.out.printf("%-30s: %-30s%n", "Contact", contact);
            System.out.printf("%-30s: %-30s%n", "Email", email);
            System.out.printf("%-30s: %-30s%n", "Clothing Item", clothingName);
            System.out.printf("%-30s: %-30s%n", "Rental Start Date", rentalStartDate);
            System.out.printf("%-30s: %-30s%n", "Rental End Date", rentalEndDate);
            System.out.println("--------------------------------------------------------------------------");
            System.out.printf("%-30s: PHP %-30.2f%n", "Rental Fee per Day", rentalFee);
            System.out.printf("%-30s: %-1ddays%n", "Number of Rental Days", totalRentalDays); // Updated rental days
            System.out.printf("%-30s: PHP %-30.2f%n", "Total Amount Due", totalAmount);
            System.out.printf("%-30s: PHP %-30.2f%n", "Total Charge for Damages", damageCharge);
            System.out.printf("%-30s: PHP %-30.2f%n", "Charge for Late Return", lateReturnCharge);
            System.out.printf("%-30s: PHP %-30.2f%n", "Total Payments Made", totalPayments);
            System.out.println("***************************************************************************");

            // Fetch and display customer transaction history
            displayCustomerTransactionHistory(customerId, totalPayments);
        } else {
            System.out.println("No rental report found for the given Rental ID.");
        }

    } catch (SQLException e) {
        System.out.println("Error retrieving rental report: " + e.getMessage());
    }
}
private void displayCustomerTransactionHistory(String customerId, double totalPayments) {
    CONFIG conf = new CONFIG();
    String query = "SELECT r.rental_id, ci.c_name, r.rental_start_date, r.rental_end_date, r.num_days, r.r_price, r.total_price, r.r_status, r.payment, r.change, r.damage_charge, r.late_return_charge "
                 + "FROM Rental r "
                 + "JOIN ClothingItem ci ON r.clothing_item_id = ci.clothing_ID "
                 + "WHERE r.customer_id = ? "
                 + "ORDER BY r.rental_start_date DESC"; // Order by most recent rental

    try (Connection con = conf.connectDB();
         PreparedStatement pst = con.prepareStatement(query)) {

        pst.setString(1, customerId);
        ResultSet rs = pst.executeQuery();

        int transactionCount = 0;
        System.out.println("\n* ************************************************************************************************************************************");
        System.out.println("|                                          CUSTOMER TRANSACTION HISTORY                                                              |");
        System.out.println("**************************************************************************************************************************************");

        // Adjusted column widths for a more consistent and neat display
        System.out.printf("| %-10s | %-30s | %-20s | %-20s | %-15s | %-20s |\n", "Rental ID", "Clothing Item", "Start Date", "End Date", "Rental Days", "Total Payments");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");

        while (rs.next()) {
            int rentalId = rs.getInt("rental_id");
            String clothingItem = rs.getString("c_name");
            String startDate = rs.getString("rental_start_date");
            String endDate = rs.getString("rental_end_date");
            int numDays = rs.getInt("num_days");
            double totalPrice = rs.getDouble("total_price");
            String status = rs.getString("r_status");
            double payment = rs.getDouble("payment");
            double change = rs.getDouble("change");
            double damageCharge = rs.getDouble("damage_charge");
            double lateReturnCharge = rs.getDouble("late_return_charge");

            // Calculate late return days based on late return charge
            int lateReturnDays = (lateReturnCharge > 0) ? (int) (lateReturnCharge / totalPrice) : 0; // Assuming totalPrice is rental fee for this calculation
            int totalRentalDays = numDays + lateReturnDays; // Add late days to rental days

            // Calculate the total payments for the current rental transaction
            double totalPaymentsForTransaction = payment - change + damageCharge + lateReturnCharge;

            // Print each transaction with more balanced column widths
            System.out.printf("| %-10d | %-30s | %-20s | %-20s | %-15d | PHP %-17.2f|\n", rentalId, clothingItem, startDate, endDate, totalRentalDays, totalPaymentsForTransaction);
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
            transactionCount++;
        }

        // Display transaction count instead of total payments made
        System.out.printf("\nTotal Transactions: %d%n", transactionCount);

    } catch (SQLException e) {
        System.out.println("Error retrieving customer transaction history: " + e.getMessage());
    }
}



 private void updateRental(Scanner sc) {
    CONFIG conf = new CONFIG();

    // SQL query to fetch only active ("rented") rentals with detailed information
    String query = "SELECT r.rental_id, c.c_lname, c.c_fname, ci.c_name, r.r_status, " +
                   "r.rental_start_date, r.rental_end_date " +
                   "FROM Rental r " +
                   "JOIN Customer c ON r.customer_id = c.c_ID " +
                   "JOIN ClothingItem ci ON r.clothing_item_id = ci.Clothing_ID " +
                   "WHERE r.r_status = 'rented'";  // Only rented rentals are shown

    // Define headers for the table display
    String[] headers = {"Rental ID", "Customer Name", "Clothing Name", "Status", "Start Date", "End Date"};

    // Fetch records and display the active rentals
    try (Connection con = CONFIG.connectDB();
         PreparedStatement pst = con.prepareStatement(query);
         ResultSet rs = pst.executeQuery()) {

        // Check if result set is empty
        if (!rs.isBeforeFirst()) {
            System.out.println("No active rental records found.");
            return;
        }

        // Print table headers
        System.out.println("|----------------------------------------------------------------------------------------------------------|");
        System.out.println("|                                         ACTIVE RENTALS                                                   |");
        System.out.println("|----------------------------------------------------------------------------------------------------------|");
        System.out.printf("| %-10s | %-25s | %-20s | %-10s | %-12s | %-12s |%n",
                (Object[]) headers);
        System.out.println("|----------------------------------------------------------------------------------------------------------|");

        // Iterate through and display records
        while (rs.next()) {
            int rentalId = rs.getInt("rental_id");
            String customerName = rs.getString("c_lname") + ", " + rs.getString("c_fname");
            String clothingName = rs.getString("c_name");
            String status = rs.getString("r_status");  // Directly fetch status from the database
            String startDate = rs.getString("rental_start_date");
            String endDate = rs.getString("rental_end_date");

            System.out.printf("| %-10d | %-25s | %-20s | %-10s | %-12s | %-12s |%n",
                    rentalId, customerName, clothingName, status, startDate, endDate);
        }

        System.out.println("|----------------------------------------------------------------------------------------------------------|");

    } catch (SQLException e) {
        System.out.println("Error retrieving rental records: " + e.getMessage());
        return;
    }

    // Prompt user to select a rental for updating
    boolean validId = false;
    int rentalId = 0;

    do {
        System.out.print("Enter Rental ID to update: ");
        rentalId = sc.nextInt();

        // Validate if the rental ID exists and is active (rented)
        String queryExistence = "SELECT COUNT(1) FROM Rental WHERE rental_id=? AND r_status='rented'";
        int exists = conf.checkExistence(queryExistence, rentalId);

        if (exists == 0) {
            System.out.println("\tERROR: Rental ID doesn't exist or is not currently rented.");
            System.out.print("Would you like to try again? (Y/N): ");
            String retry = sc.next();
            if (!retry.equalsIgnoreCase("Y")) {
                System.out.println("Exiting update process.");
                return;
            }
        } else {
            validId = true;
        }
    } while (!validId);

    // Update Specific Fields
    boolean updateComplete = false;
    while (!updateComplete) {
        System.out.println("\nWhich field would you like to update?");
        System.out.println("1. Customer ID");
        System.out.println("2. Clothing Item ID");
        System.out.println("3. Rental Start Date");
        System.out.println("4. Rental End Date");
        System.out.println("5. Rental Fee per Day");
        System.out.println("6. Exit Update Process");
        System.out.print("Select an option (1-6): ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                displayCustomerDetails();
                System.out.print("Enter New Customer ID: ");
                int newCustomerId = sc.nextInt();
                conf.updateRecord("UPDATE Rental SET customer_id=? WHERE rental_id=?", newCustomerId, rentalId);
                System.out.println("Customer ID updated successfully.");
                break;
            case 2:
                displayAvailableClothingItems();
                System.out.print("Enter New Clothing Item ID: ");
                int newClothingItemId = sc.nextInt();
                conf.updateRecord("UPDATE Rental SET clothing_item_id=? WHERE rental_id=?", newClothingItemId, rentalId);
                System.out.println("Clothing Item ID updated successfully.");
                break;
            case 3:
                sc.nextLine(); // Consume newline
                System.out.print("Enter New Rental Start Date (YYYY-MM-DD): ");
                String newStartDateStr = sc.next();
                conf.updateRecord("UPDATE Rental SET rental_start_date=? WHERE rental_id=?", newStartDateStr, rentalId);
                System.out.println("Rental Start Date updated successfully.");
                break;
            case 4:
                sc.nextLine(); // Consume newline
                System.out.print("Enter New Rental End Date (YYYY-MM-DD): ");
                String newEndDateStr = sc.next();
                conf.updateRecord("UPDATE Rental SET rental_end_date=? WHERE rental_id=?", newEndDateStr, rentalId);
                System.out.println("Rental End Date updated successfully.");
                break;
            case 5:
                System.out.print("Enter New Rental Fee per Day: ");
                double newDailyPrice = sc.nextDouble();
                // Recalculate total price based on the number of days
                String dateQuery = "SELECT rental_start_date, rental_end_date FROM Rental WHERE rental_id=?";
                String[] rentalDates = conf.getSingleRecord(dateQuery, rentalId);
                LocalDate startDate = LocalDate.parse(rentalDates[0]);
                LocalDate endDate = LocalDate.parse(rentalDates[1]);
                long newNumDays = ChronoUnit.DAYS.between(startDate, endDate);
                double newTotalPrice = newNumDays * newDailyPrice;

                conf.updateRecord("UPDATE Rental SET r_price=?, total_price=? WHERE rental_id=?", 
                                  newDailyPrice, newTotalPrice, rentalId);
                System.out.printf("Rental Fee updated successfully. New Total Price: %.2f%n", newTotalPrice);
                break;
            case 6:
                System.out.println("Exiting update process.");
                updateComplete = true;
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
                break;
        }
    }
}

private void deleteRental(Scanner sc) {
    CONFIG conf = new CONFIG();

    // SQL query to fetch only active ("rented") rentals with detailed information
    String query = "SELECT r.rental_id, c.c_lname, c.c_fname, ci.c_name, r.r_status, " +
                   "r.rental_start_date, r.rental_end_date " +
                   "FROM Rental r " +
                   "JOIN Customer c ON r.customer_id = c.c_ID " +
                   "JOIN ClothingItem ci ON r.clothing_item_id = ci.Clothing_ID " +
                   "WHERE r.r_status = 'rented'";  // Only rented rentals are shown

    // Define headers for the table display
    String[] headers = {"Rental ID", "Customer Name", "Clothing Name", "Status", "Start Date", "End Date"};

    // Fetch and display active rentals
    try (Connection con = CONFIG.connectDB();
         PreparedStatement pst = con.prepareStatement(query);
         ResultSet rs = pst.executeQuery()) {

        // Check if result set is empty
        if (!rs.isBeforeFirst()) {
            System.out.println("No active rental records found.");
            return;
        }

        // Print table headers
        System.out.println("|----------------------------------------------------------------------------------------------------------|");
        System.out.println("|                                         ACTIVE RENTALS                                                   |");
        System.out.println("|----------------------------------------------------------------------------------------------------------|");
        System.out.printf("| %-10s | %-25s | %-20s | %-10s | %-12s | %-12s |%n", (Object[]) headers);
        System.out.println("|----------------------------------------------------------------------------------------------------------|");

        // Iterate through and display records
        while (rs.next()) {
            int rentalId = rs.getInt("rental_id");
            String customerName = rs.getString("c_lname") + ", " + rs.getString("c_fname");
            String clothingName = rs.getString("c_name");
            String status = rs.getString("r_status");  // Directly fetch status from the database
            String startDate = rs.getString("rental_start_date");
            String endDate = rs.getString("rental_end_date");

            System.out.printf("| %-10d | %-25s | %-20s | %-10s | %-12s | %-12s |%n",
                    rentalId, customerName, clothingName, status, startDate, endDate);
        }

        System.out.println("|----------------------------------------------------------------------------------------------------------|");

    } catch (SQLException e) {
        System.out.println("" + e.getMessage());
        return;
    }

    
    boolean validId = false;
    int rentalId = 0;

    do {
        System.out.print("Enter Rental ID to delete: ");
        rentalId = sc.nextInt();

        // Check if the rental ID exists and is active (rented)
        String queryExistence = "SELECT COUNT(1) FROM Rental WHERE rental_id=? AND r_status='rented'";
        int exists = conf.checkExistence(queryExistence, rentalId);

        if (exists == 0) {
            System.out.println("\tERROR: Rental ID doesn't exist or is not currently rented.");
            System.out.print("Would you like to try again? (Y/N): ");
            String retry = sc.next();
            if (!retry.equalsIgnoreCase("Y")) {
                System.out.println("Exiting deletion process.");
                return;
            }
        } else {
            validId = true;
        }
    } while (!validId);

    // Check if the rental is active (still rented out)
    if (isRentalActive(rentalId)) {
        System.out.println("Sorry, you cannot delete any rental that is still active.");
        return;  // Cancel deletion if the rental is active
    }

    System.out.print("Are you sure you want to delete this rental? (yes/no): ");
    String confirmation = sc.next().toLowerCase();

    if (!confirmation.equals("yes")) {
        System.out.println("Deletion cancelled.");
        return;
    }

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

// Method to check if the rental is currently active
private boolean isRentalActive(int rentalId) {
    String query = "SELECT rental_end_date FROM Rental WHERE rental_id = ?";
    try (Connection conn = CONFIG.connectDB();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setInt(1, rentalId);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            // Ensure rental_end_date is fetched correctly as a Date
            java.sql.Date rentalEndDate = rs.getDate("rental_end_date");
            
            // Check if rentalEndDate is null or if it is in the future
            if (rentalEndDate == null) {
                return true; // If there's no end date, assume the rental is active
            }
            
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
            
            // If the rental end date is in the future, or if it's null, it's considered active
            return rentalEndDate.after(currentDate);
        }
    } catch (SQLException e) {
        System.out.println("Error checking rental status: " + e.getMessage());
    }
    return false;  // Return false if rental doesn't exist or is not active
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
