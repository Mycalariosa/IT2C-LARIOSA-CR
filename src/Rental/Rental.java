package Rental;

import ClothingItem.ClothingItem;
import it2c.lariosa.cr.CONFIG;
import java.util.Scanner;

public class Rental {

    public void rentalTransaction() {
        Scanner sc = new Scanner(System.in);
        String ch;

        do {
            System.out.println("\n|-------------------|");
            System.out.println("|     RENTAL MENU   |");
            System.out.println("|-------------------|");
            System.out.println("| 1. ADD RENTAL     |");
            System.out.println("| 2. VIEW RENTALS    |");
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
        System.out.print("\nCustomer ID: ");
        int customerId = sc.nextInt();

        System.out.print("Clothing Item ID: ");
        int clothingItemId = sc.nextInt();

        System.out.print("Rental Start Date (YYYY-MM-DD): ");
        String startDate = sc.next();

        System.out.print("Rental End Date (YYYY-MM-DD): ");
        String endDate = sc.next();

        String sql = "INSERT INTO Rental (customer_id, clothing_item_id, rental_start_date, rental_end_date) VALUES (?, ?, ?, ?)";
        conf.addRecord(sql, customerId, clothingItemId, startDate, endDate);
        System.out.println("Rental added successfully.");
    }

    private void viewRentals() {
        CONFIG con = new CONFIG();

        String query = "SELECT * FROM Rental";
        String[] headers = {"Rental ID", "Customer ID", "Clothing Item ID", "Start Date", "End Date"};
        String[] columns = {"rental_id", "customer_id", "clothing_item_id", "rental_start_date", "rental_end_date"};

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

        System.out.print("New Rental Start Date (YYYY-MM-DD): ");
        String newStartDate = sc.next();

        System.out.print("New Rental End Date (YYYY-MM-DD): ");
        String newEndDate = sc.next();

        String qry = "UPDATE Rental SET customer_id=?, clothing_item_id=?, rental_start_date=?, rental_end_date=? WHERE rental_id=?";
        CONFIG con = new CONFIG();
        con.updateRecord(qry, newCustomerId, newClothingItemId, newStartDate, newEndDate, rentalId);
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