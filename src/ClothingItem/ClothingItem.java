package ClothingItem;

import it2c.lariosa.cr.CONFIG;
import java.text.DecimalFormat;
import java.util.Scanner;

public class ClothingItem {

    public void clothingTransaction(Scanner sc) {
        String ch;

        do {
            System.out.println("\n|------------------|");
            System.out.println("|  CLOTHING ITEM   |");
            System.out.println("|------------------|");
            System.out.println("| 1. ADD ITEM      |");
            System.out.println("| 2. VIEW ITEMS    |");
            System.out.println("| 3. UPDATE ITEM   |");
            System.out.println("| 4. DELETE ITEM   |");
            System.out.println("|------------------|");

            System.out.print("Choose from 1-5: ");
            int action = sc.nextInt();

            while (action < 1 || action > 5) {
                System.out.print("\tInvalid action. Please enter a number between 1 and 5: ");
                action = sc.nextInt();
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
            }

            System.out.print("\nDo you want to use Item menu? (Y/N): ");
            ch = sc.next();
        } while (ch.equalsIgnoreCase("Y"));

        System.out.println("\nThank you for using this application");
    }

    public void addClothingItem(Scanner sc) {
        CONFIG conf = new CONFIG();
        DecimalFormat df = new DecimalFormat("#.00"); // Format for two decimal places

        System.out.print("------------------------------------");
        System.out.print("\nName: ");
        sc.nextLine();
        String name = sc.nextLine();

        System.out.print("Brand: ");
        String brand = sc.nextLine();

        System.out.print("Category: ");
        String category = sc.nextLine();

        String size;
        do {
            System.out.print("Size (small, medium, large, extra small, extra large): ");
            size = sc.nextLine().toLowerCase();
        } while (!isValidSize(size));

        String color;
        do {
            System.out.print("Color (numbers not allowed): ");
            color = sc.nextLine();
        } while (!isValidColor(color));

        System.out.print("Material: ");
        String material = sc.nextLine();

        double price;
        while (true) {
            System.out.print("Price: ");
            if (sc.hasNextDouble()) {
                price = sc.nextDouble();
                break;
            } else {
                System.out.println("Invalid price. Please enter a number.");
                sc.next();
            }
        }

        
        String formattedPrice = df.format(price);

        String availability;
        sc.nextLine(); // Clear buffer
        while (true) {
            System.out.print("Availability (available/unavailable): ");
            availability = sc.nextLine().trim().toLowerCase();
            if (availability.equals("available") || availability.equals("unavailable")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'available' or 'unavailable'.");
            }
        }

        String sql = "INSERT INTO ClothingItem (c_name, c_brand, c_category, c_size, c_color, c_price, c_material, c_availability) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        conf.addRecord(sql, name, brand, category, size, color, formattedPrice, material, availability);
    }

    private boolean isValidSize(String size) {
        return size.equals("small") || size.equals("medium") || size.equals("large") || size.equals("extra small") || size.equals("extra large");
    }

    private boolean isValidColor(String color) {
        return color.matches("^[A-Za-z]+$");
    }

    public void viewClothingItem() {
        CONFIG con = new CONFIG();

        String query = "SELECT * FROM ClothingItem";
        String[] headers = {"ID", "Name", "Brand", "Category", "Size", "Color", "Price", "Material", "Availability"};
        String[] columns = {"clothing_ID", "c_name", "c_brand", "c_category", "c_size", "c_color", "c_price", "c_material", "c_availability"};

        con.viewRecords(query, headers, columns);
    }

    private void updateClothingItem(Scanner sc) {
        CONFIG conf = new CONFIG();
        DecimalFormat df = new DecimalFormat("#.00"); 

        viewClothingItem();

        System.out.print("Enter Clothing Item ID to update: ");
        int id = sc.nextInt();
        sc.nextLine(); 

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Brand: ");
        String brand = sc.nextLine();

        System.out.print("Enter Category: ");
        String category = sc.nextLine();

        String size;
        do {
            System.out.print("Size (small, medium, large, extra small, extra large): ");
            size = sc.nextLine().toLowerCase();
        } while (!isValidSize(size));

        String color;
        do {
            System.out.print("Color (numbers not allowed): ");
            color = sc.nextLine();
        } while (!isValidColor(color));

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

        String availability;
        do {
            System.out.print("Update Availability (available/unavailable): ");
            availability = sc.nextLine().trim().toLowerCase();
            if (!availability.equals("available") && !availability.equals("unavailable")) {
                System.out.println("Invalid input. Please enter 'available' or 'unavailable'.");
            }
        } while (!availability.equals("available") && !availability.equals("unavailable"));

        
        String qry = "UPDATE ClothingItem SET c_name=?, c_brand=?, c_category=?, c_size=?, c_color=?, c_price=?, c_material=?, c_availability=? WHERE clothing_ID=?";
        conf.updateRecord(qry, name, brand, category, size, color, formattedPrice, material, availability, id);
    }

    private void deleteClothingItem(Scanner sc) {
        CONFIG conf = new CONFIG();

        viewClothingItem();

        System.out.print("Enter Clothing Item ID to delete: ");
        int id = sc.nextInt();

        String sqlDelete = "DELETE FROM ClothingItem WHERE clothing_ID=?";
        conf.deleteRecord(sqlDelete, id);
    }
}
