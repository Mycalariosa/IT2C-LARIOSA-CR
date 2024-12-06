package ClothingItem;

import it2c.lariosa.cr.CONFIG;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Scanner;

public class ClothingItem {

    public void clothingTransaction(Scanner sc) {
        Scanner in = new Scanner(System.in);
        String another;

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

            int action = -1; // Initialize with an invalid action
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
                    System.out.println("Thankyou for using Clothing item menu! :)");
                    return;
            }

            System.out.print("\nDo you still want to use Clothing Item menu? (Y/N): ");
            another = in.next().trim();

            while (!another.equalsIgnoreCase("yes") && !another.equalsIgnoreCase("y")
                    && !another.equalsIgnoreCase("n") && !another.equalsIgnoreCase("no")) {
                System.out.println("Enter again:");
                another = in.next().trim();
            }
        } while (another.equalsIgnoreCase("yes") || another.equalsIgnoreCase("y"));
        System.out.println("Thank you for using the Clothing Item Application.");
    }

 public void addClothingItem(Scanner sc) {
    CONFIG conf = new CONFIG();
    DecimalFormat df = new DecimalFormat("#.00");

    // Input Clothing Name
    String name;
    boolean firstAttemptName = true;
    do {
        if (!firstAttemptName) {
            System.out.println("Invalid name. Please enter a name without numbers.");
        }
        System.out.print("\nEnter Clothing Item Name: ");
        name = sc.next().trim();
        firstAttemptName = false;
    } while (!isValidName(name));

    // Input Brand
    String brand;
    boolean firstAttemptBrand = true;
    do {
        if (!firstAttemptBrand) {
            System.out.println("Invalid brand. Please enter letters or periods only.");
        }
        System.out.print("Enter Brand: ");
        brand = sc.next().trim();
        firstAttemptBrand = false;
    } while (!isValidBrand(brand));

    // Input Category
    String category;
    boolean firstAttemptCategory = true;
    do {
        if (!firstAttemptCategory) {
            System.out.println("Invalid category. Please enter a category without numbers.");
        }
        System.out.print("Enter Category: ");
        category = sc.next().trim();
        firstAttemptCategory = false;
    } while (!isValidNameOrCategory(category));

    // Input Size
    String size;
    boolean firstAttemptSize = true;
    do {
        if (!firstAttemptSize) {
            System.out.println("Invalid size. Please choose from small, medium, large, extra small, or extra large.");
        }
        System.out.print("Enter Size (small, medium, large, extra small, extra large): ");
        size = sc.next().trim().toLowerCase();
        firstAttemptSize = false;
    } while (!isValidSize(size));

    // Input Color
    String color;
    boolean firstAttemptColor = true;
    do {
        if (!firstAttemptColor) {
            System.out.println("Invalid color. Please enter a valid color name (letters only, no numbers).");
        }
        System.out.print("Enter Color: ");
        color = sc.next().trim();
        firstAttemptColor = false;
    } while (!isValidColor(color));

    // Input Price
    double price = -1;
    while (price < 0) {
        System.out.print("Enter Price: ");
        if (sc.hasNextDouble()) {
            price = sc.nextDouble();
            sc.nextLine(); // Consume leftover newline
        } else {
            System.out.println("Invalid price. Please enter a valid number.");
            sc.next(); // Clear invalid input
        }
    }

    // Format price for display purposes
    String formattedPrice = df.format(price);

    // Input Material
    System.out.print("Enter Material: ");
    String material = sc.nextLine().trim();

    // Default Availability
    String availability = "available";

    // Insert record into database
    String qry = "INSERT INTO ClothingItem (c_name, c_brand, c_category, c_size, c_color, c_price, c_material, c_availability) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    conf.addRecord(qry, name, brand, category, size, color, formattedPrice, material, availability);

    System.out.println("\nClothing item added successfully.");
}

    public void viewClothingItem() {
    CONFIG conf = new CONFIG();
    String query = "SELECT * FROM ClothingItem";
    String[] headers = {"ID", "Name", "Brand", "Category", "Size", "Color", "Price", "Material", "Availability"};
    ResultSet rs = null;

    try {
        rs = conf.getRecords(query, null);

        // Check if result set is empty
        if (rs == null || !rs.isBeforeFirst()) {
            System.out.println("No clothing items found.");
            return;
        }

        // Print the table header
        String headerFormat = "| %-10s | %-25s | %-20s | %-15s | %-10s | %-12s | %-10s | %-12s | %-12s |%n";
        String rowFormat = "| %-10d | %-25s | %-20s | %-15s | %-10s | %-12s | %-10.2f | %-12s | %-12s |%n";

        System.out.println("|--------------------------------------------------------------------------------------------------------------------------------------------------------|");
        System.out.println("|                                                       CLOTHING ITEM LIST                                                                               |");
        System.out.println("|--------------------------------------------------------------------------------------------------------------------------------------------------------|");
        System.out.printf(headerFormat, headers[0], headers[1], headers[2], headers[3], headers[4], headers[5], headers[6], headers[7], headers[8]);
        System.out.println("|--------------------------------------------------------------------------------------------------------------------------------------------------------|");

        // Process and display the records
        while (rs.next()) {
            int clothingId = rs.getInt("clothing_ID");
            String name = rs.getString("c_name");
            String brand = rs.getString("c_brand");
            String category = rs.getString("c_category");
            String size = rs.getString("c_size");
            String color = rs.getString("c_color");
            double price = rs.getDouble("c_price");
            String material = rs.getString("c_material");
            String availability = rs.getString("c_availability");

            // If availability is null, set default value
            if (availability == null) {
                availability = "Unavailable";
            }

            // Print out each record with proper formatting
            System.out.printf(rowFormat, clothingId, name, brand, category, size, color, price, material, availability);
        }

        System.out.println("|--------------------------------------------------------------------------------------------------------------------------------------------------------|");

    } catch (SQLException e) {
        System.out.println("Error fetching clothing items: " + e.getMessage());
    }
}

   private void updateClothingItem(Scanner sc) {
    CONFIG conf = new CONFIG();
    DecimalFormat df = new DecimalFormat("#.00");

    viewClothingItem();

    // Ask for Clothing Item ID
    System.out.print("Enter Clothing Item ID to update: ");
    int id = sc.nextInt();
    sc.nextLine(); // Consume newline character

    // Check if the item exists in the database
    String checkExistenceQuery = "SELECT COUNT(1) FROM ClothingItem WHERE clothing_ID = ?";
    int exists = conf.checkExistence(checkExistenceQuery, id);

    if (exists == 0) {
        System.out.println("\tERROR: Clothing Item ID doesn't exist.");
        return;
    }

    boolean updateComplete = false;
    while (!updateComplete) {
        // Ask the user which field they want to update
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
        sc.nextLine(); // Consume newline character

        switch (choice) {
            case 1: // Update Name
                String newName;
                do {
                    System.out.print("Enter New Name: ");
                    newName = sc.nextLine().trim();
                } while (!isValidName(newName)); // Validate name
                String query1 = "UPDATE ClothingItem SET c_name=? WHERE clothing_ID=?";
                conf.updateRecord(query1, newName, id);
                System.out.println("Name updated successfully.");
                break;

            case 2: // Update Brand
                String newBrand;
                do {
                    System.out.print("Enter New Brand: ");
                    newBrand = sc.nextLine().trim();
                } while (!isValidBrand(newBrand)); // Validate brand
                String query2 = "UPDATE ClothingItem SET c_brand=? WHERE clothing_ID=?";
                conf.updateRecord(query2, newBrand, id);
                System.out.println("Brand updated successfully.");
                break;

            case 3: // Update Category
                String newCategory;
                do {
                    System.out.print("Enter New Category: ");
                    newCategory = sc.nextLine().trim();
                } while (!isValidNameOrCategory(newCategory)); // Validate category
                String query3 = "UPDATE ClothingItem SET c_category=? WHERE clothing_ID=?";
                conf.updateRecord(query3, newCategory, id);
                System.out.println("Category updated successfully.");
                break;

            case 4: // Update Size
                String newSize;
                do {
                    System.out.print("Enter New Size (small, medium, large, extra small, extra large): ");
                    newSize = sc.nextLine().trim().toLowerCase();
                } while (!isValidSize(newSize)); // Validate size
                String query4 = "UPDATE ClothingItem SET c_size=? WHERE clothing_ID=?";
                conf.updateRecord(query4, newSize, id);
                System.out.println("Size updated successfully.");
                break;

            case 5: // Update Color
                String newColor;
                do {
                    System.out.print("Enter New Color: ");
                    newColor = sc.nextLine().trim();
                } while (!isValidColor(newColor)); // Validate color
                String query5 = "UPDATE ClothingItem SET c_color=? WHERE clothing_ID=?";
                conf.updateRecord(query5, newColor, id);
                System.out.println("Color updated successfully.");
                break;

            case 6: // Update Price
                double newPrice;
                do {
                    System.out.print("Enter New Price: ");
                    newPrice = sc.nextDouble();
                    sc.nextLine(); // Consume newline character
                } while (newPrice < 0); // Validate price
                String formattedPrice = df.format(newPrice);
                String query6 = "UPDATE ClothingItem SET c_price=? WHERE clothing_ID=?";
                conf.updateRecord(query6, formattedPrice, id);
                System.out.println("Price updated successfully.");
                break;

            case 7: // Update Material
                System.out.print("Enter New Material: ");
                String newMaterial = sc.nextLine().trim();
                String query7 = "UPDATE ClothingItem SET c_material=? WHERE clothing_ID=?";
                conf.updateRecord(query7, newMaterial, id);
                System.out.println("Material updated successfully.");
                break;

            case 8: // Update Availability
                System.out.print("Enter New Availability (available/unavailable): ");
                String newAvailability = sc.nextLine().trim().toLowerCase();
                if (!newAvailability.equals("available") && !newAvailability.equals("unavailable")) {
                    System.out.println("Invalid availability status.");
                    break;
                }
                String query8 = "UPDATE ClothingItem SET c_availability=? WHERE clothing_ID=?";
                conf.updateRecord(query8, newAvailability, id);
                System.out.println("Availability updated successfully.");
                break;

            case 9: // Exit Update Process
                System.out.println("Exiting update process.");
                updateComplete = true;
                break;

            default:
                System.out.println("Invalid choice. Please select a valid option.");
        }
    }
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
        return size.equalsIgnoreCase("small") ||
               size.equalsIgnoreCase("medium") ||
               size.equalsIgnoreCase("large") ||
               size.equalsIgnoreCase("extra small") ||
               size.equalsIgnoreCase("extra large");
    }
private boolean isValidBrand(String brand) {
    // Allows letters, numbers, spaces, and decimals
    return brand.matches("^[a-zA-Z. ]+$");
}
    private boolean isValidColor(String color) {
        return color.matches("^[a-zA-Z]+$");
    }
    private boolean isValidName(String name) {
    // Check if the name contains only letters and spaces (no numbers or special characters)
    return name.matches("^[a-zA-Z ]+$");
}

    private boolean isValidNameOrCategory(String input) {
    // Check if the input contains only letters and spaces (no numbers or special characters)
    return input.matches("^[a-zA-Z ]+$");
}

}

    
