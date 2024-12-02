package ClothingItem;

import it2c.lariosa.cr.CONFIG;
import java.text.DecimalFormat;
import java.util.Scanner;

public class ClothingItem {
        
    public void clothingTransaction(Scanner sc) {
        String ch=null;
        Scanner in = new Scanner (System.in);
        String another =null;
        do {
            System.out.println("\n|------------------|");
            System.out.println("|  CLOTHING ITEM   |");
            System.out.println("|------------------|");
            System.out.println("| 1. ADD ITEM      |");
            System.out.println("| 2. VIEW ITEMS    |");
            System.out.println("| 3. UPDATE ITEM   |");
            System.out.println("| 4. DELETE ITEM   |");
            System.out.println("| 5. EXIT          |");
            System.out.println("|------------------|");

          int action = -1;  // Initialize with an invalid action
            boolean validInput = false;

            // Loop until a valid integer between 1 and 5 is entered
            while (!validInput) {
                System.out.print("Choose from 1-5: ");
                if (sc.hasNextInt()) {
                    action = sc.nextInt();
                    if (action >= 1 && action <= 5) {
                        validInput = true;
                    } else {
                        System.out.println("\nInvalid action. Please enter a number between 1 and 5.");
                    }
                } else {
                    System.out.println("\nInvalid input. Please enter a number.");
                    sc.next(); // Clear the invalid input
                }
            }

            switch (action) {
                case 1:
                    addClothingItem(sc);
                    break;
                case 2:
                    viewClothingItem();
                    break;
                case 3:
                    updateClothingItem(sc);
                    break;
                case 4:
                    deleteClothingItem(sc);
                    break;
                case 5:
                    System.out.println("Thanks! UWU -_-");
                    return;
            }

            System.out.print("\nDo you still want to use Clothing Item menu? (Y/N): ");
            another = in.next().trim();
            
            while(!another.equals("Yes")&&!another.equals("yes")&&!another.equals("YES")&&!another.equals("y")&&!another.equals("Y")&&!another.equals("n")&&!another.equals("N")&&!another.equals("No")&&!another.equals("NO")&&!another.equals("no")){
                System.out.println("Enter again:");
                another=in.next().trim();
        }
            }while (another.equals("YES")||another.equals("yes")||another.equals("Yes")||another.equals("y")||another.equals("Y"));
                System.out.println("Thamkyou for using Clothing item Application");
    }

    public void addClothingItem(Scanner sc) {
        CONFIG conf = new CONFIG();
        DecimalFormat df = new DecimalFormat("#.00");

        System.out.print("\nEnter Clothing Item Name: ");
        String name = sc.nextLine();
        sc.nextLine(); // Consume newline left-over
        System.out.print("Enter Brand: ");
        String brand = sc.nextLine();

        System.out.print("Enter Category: ");
        String category = sc.nextLine();

        System.out.print("Enter Size (small, medium, large, extra small, extra large): ");
        String size = sc.nextLine().toLowerCase();

        System.out.print("Enter Color: ");
        String color = sc.nextLine();

        double price;
        while (true) {
            System.out.print("Enter Price: ");
            if (sc.hasNextDouble()) {
                price = sc.nextDouble();
                break;
            } else {
                System.out.println("Invalid price. Please enter a number.");
                sc.next(); 
            }
        }

        String formattedPrice = df.format(price);

        System.out.print("Enter Material: ");
        sc.nextLine(); 
        String material = sc.nextLine();

        String availability = "available"; // Default availability when adding a new item

        String qry = "INSERT INTO ClothingItem (c_name, c_brand, c_category, c_size, c_color, c_price, c_material, c_availability) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        conf.addRecord(qry, name, brand, category, size, color, formattedPrice, material, availability);

        System.out.println("Clothing item added successfully.");
    }

    // Method to update the availability of a clothing item
    public void updateAvailability(int id, String newStatus) {
        CONFIG conf = new CONFIG();
        
        String qry = "UPDATE ClothingItem SET c_availability = ? WHERE clothing_ID = ?";
        conf.updateRecord(qry, newStatus, id);
        
        System.out.println("Clothing item availability updated successfully.");
    }

    // View all clothing items, showing their availability
    public void viewClothingItem() {
        CONFIG conf = new CONFIG();

        String query = "SELECT * FROM ClothingItem";  // No filter, shows all items regardless of availability
        String[] headers = {"ID", "Name", "Brand", "Category", "Size", "Color", "Price", "Material", "Availability"};
        String[] columns = {"clothing_ID", "c_name", "c_brand", "c_category", "c_size", "c_color", "c_price", "c_material", "c_availability"};

        conf.viewRecords(query, headers, columns);
    }

   private void updateClothingItem(Scanner sc) {
    CONFIG conf = new CONFIG();
    DecimalFormat df = new DecimalFormat("#.00");

    viewClothingItem();  // Display items before updating

    System.out.print("Enter Clothing Item ID to update: ");
    int id = sc.nextInt();
    sc.nextLine();  // Consume newline character after reading integer

    // Check if the clothing item exists
    String checkExistenceQuery = "SELECT COUNT(1) FROM ClothingItem WHERE clothing_ID = ?";
    int exists = conf.checkExistence(checkExistenceQuery, id);
    
    if (exists == 0) {
        System.out.println("\tERROR: Clothing Item ID doesn't exist.");
        return; // Exit if clothing item doesn't exist
    }

    boolean updateComplete = false;
    while (!updateComplete) {
        // Display a menu for the fields that can be updated
        System.out.println("\nWhich field would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. Brand");
        System.out.println("3. Category");
        System.out.println("4. Size");
        System.out.println("5. Color");
        System.out.println("6. Price");
        System.out.println("7. Material");
        System.out.println("8. Availability");
        System.out.println("9. Exit Update Process");
        System.out.print("Select an option (1-9): ");
        
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline character after reading integer

        switch (choice) {
            case 1:
                System.out.print("Enter New Name: ");
                String name = sc.nextLine();
                String query1 = "UPDATE ClothingItem SET c_name=? WHERE clothing_ID=?";
                conf.updateRecord(query1, name, id);
                System.out.println("Name updated successfully.");
                break;
            case 2:
                System.out.print("Enter New Brand: ");
                String brand = sc.nextLine();
                String query2 = "UPDATE ClothingItem SET c_brand=? WHERE clothing_ID=?";
                conf.updateRecord(query2, brand, id);
                System.out.println("Brand updated successfully.");
                break;
            case 3:
                System.out.print("Enter New Category: ");
                String category = sc.nextLine();
                String query3 = "UPDATE ClothingItem SET c_category=? WHERE clothing_ID=?";
                conf.updateRecord(query3, category, id);
                System.out.println("Category updated successfully.");
                break;
            case 4:
                String size;
                do {
                    System.out.print("Enter New Size (small, medium, large, extra small, extra large): ");
                    size = sc.nextLine().toLowerCase();
                } while (!isValidSize(size));
                String query4 = "UPDATE ClothingItem SET c_size=? WHERE clothing_ID=?";
                conf.updateRecord(query4, size, id);
                System.out.println("Size updated successfully.");
                break;
            case 5:
                String color;
                do {
                    System.out.print("Enter New Color (no numbers allowed): ");
                    color = sc.nextLine();
                } while (!isValidColor(color));
                String query5 = "UPDATE ClothingItem SET c_color=? WHERE clothing_ID=?";
                conf.updateRecord(query5, color, id);
                System.out.println("Color updated successfully.");
                break;
            case 6:
                double price;
                while (true) {
                    System.out.print("Enter New Price: ");
                    if (sc.hasNextDouble()) {
                        price = sc.nextDouble();
                        sc.nextLine(); // Consume newline
                        break;
                    } else {
                        System.out.println("Invalid price. Please enter a valid number.");
                        sc.next(); // Clear invalid input
                    }
                }
                String formattedPrice = df.format(price);
                String query6 = "UPDATE ClothingItem SET c_price=? WHERE clothing_ID=?";
                conf.updateRecord(query6, formattedPrice, id);
                System.out.println("Price updated successfully.");
                break;
            case 7:
                System.out.print("Enter New Material: ");
                String material = sc.nextLine();
                String query7 = "UPDATE ClothingItem SET c_material=? WHERE clothing_ID=?";
                conf.updateRecord(query7, material, id);
                System.out.println("Material updated successfully.");
                break;
            case 8:
                String availability;
                do {
                    System.out.print("Enter New Availability (available/unavailable): ");
                    availability = sc.nextLine().trim().toLowerCase();
                    if (!availability.equals("available") && !availability.equals("unavailable")) {
                        System.out.println("Invalid input. Please enter 'available' or 'unavailable'.");
                    }
                } while (!availability.equals("available") && !availability.equals("unavailable"));

                // Check if the item is rented before marking as available
                String checkRentalQuery = "SELECT COUNT(1) FROM Rental WHERE clothing_item_id = ? AND rental_end_date >= CURDATE()";
                int rentedCount = conf.checkExistence(checkRentalQuery, id);
                
                if (rentedCount > 0 && availability.equals("available")) {
                    System.out.println("This item is currently rented and cannot be marked as available.");
                    break;
                }

                String query8 = "UPDATE ClothingItem SET c_availability=? WHERE clothing_ID=?";
                conf.updateRecord(query8, availability, id);
                System.out.println("Availability updated successfully.");
                break;
            case 9:
                System.out.println("Exiting update process.");
                updateComplete = true;
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
                break;
        }
    }

    // Show the updated clothing items after the update
    viewClothingItem();
}


    private void deleteClothingItem(Scanner sc) {
        viewClothingItem();

        System.out.print("Enter Clothing Item ID to delete: ");
        int id = sc.nextInt();

        String sqlDelete = "DELETE FROM ClothingItem WHERE clothing_ID=?";
        CONFIG con = new CONFIG();
        con.deleteRecord(sqlDelete, id);
        System.out.println("Clothing item deleted successfully.");
    }

    private boolean isValidSize(String size) {
        return size.equals("small") || size.equals("medium") || size.equals("large") ||
               size.equals("extra small") || size.equals("extra large");
    }

    private boolean isValidColor(String color) {
        return !color.matches(".*\\d.*");
    }

    // This method will be called after renting an item, marking it as unavailable
    public void markAsUnavailable(int clothingItemId) {
        updateAvailability(clothingItemId, "unavailable");
    }

    // This method will be called after returning an item, marking it as available
    public void markAsAvailable(int clothingItemId) {
        updateAvailability(clothingItemId, "available");
    }
}
