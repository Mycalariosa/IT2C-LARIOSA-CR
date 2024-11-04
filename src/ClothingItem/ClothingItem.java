package ClothingItem;

import it2c.lariosa.cr.CONFIG;
import java.util.Scanner;

public class ClothingItem {

    public void clothingTransaction() {
        try (Scanner sc = new Scanner(System.in)) {
            String ch;
            
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
                
                System.out.print("Choose from 1-5: ");
                int action = sc.nextInt();
                
                while (action < 1 || action > 5) {
                    System.out.print("\tInvalid action. Please enter a number between 1 and 5: ");
                    action = sc.nextInt();
                }
                
                switch (action) {
                    case 1:
                        addClothingItem();
                        break;
                    case 2:
                        viewClothingItem();
                        break;
                    case 3:
                        updateClothingItem();
                        break;
                    case 4:
                        deleteClothingItem();
                        break;
                    case 5:
                        break;
                }
                
                System.out.print("\nDo you want to use another system? (Y/N): ");
                ch = sc.next();
            } while (ch.equalsIgnoreCase("Y"));
            
            System.out.println("\nThank you for using this application");
        }
    }

    public void addClothingItem() {
        Scanner sc = new Scanner(System.in);
        CONFIG conf = new CONFIG();

        System.out.print("------------------------------------");
        System.out.print("\nName: ");
        String name = sc.nextLine();

        System.out.print("Brand: ");
        String brand = sc.nextLine();

        System.out.print("Category: ");
        String category = sc.nextLine();

        System.out.print("Size: ");
        String size = sc.nextLine();

        System.out.print("Color: ");
        String color = sc.nextLine();

        System.out.print("Material: ");
        String material = sc.nextLine();

        System.out.print("Price: ");
        double price = sc.nextDouble();

        System.out.print("Availability (true/false): ");
        boolean availability = sc.nextBoolean();

        String sql = "INSERT INTO ClothingItem (c_name, c_brand, c_category, c_size, c_color, c_price, c_material, c_availability) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        conf.addRecord(sql, name, brand, category, size, color, String.valueOf(price), material, availability);
    }

    public void viewClothingItem() {
        CONFIG con = new CONFIG();

        String query = "SELECT * FROM ClothingItem";
        String[] headers = {"ID", "Name", "Brand", "Category", "Size", "Color", "Price", "Material", "Availability"};
        String[] columns = {"clothing_ID", "c_name", "c_brand", "c_category", "c_size", "c_color", "c_price", "c_material", "c_availability"};

        con.viewRecords(query, headers, columns);
    }

    private void updateClothingItem() {
        Scanner sc = new Scanner(System.in);
        CONFIG conf = new CONFIG();

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

        System.out.print("Enter Size: ");
        String size = sc.nextLine();

        System.out.print("Enter Color: ");
        String color = sc.nextLine();

        System.out.print("Enter Price: ");
        double price = sc.nextDouble();

        System.out.print("Enter Material: ");
        sc.nextLine(); 
        String material = sc.nextLine();

        System.out.print("Update Availability (true/false): ");
        boolean availability = sc.nextBoolean();

        String qry = "UPDATE ClothingItem SET c_name=?, c_brand=?, c_category=?, c_size=?, c_color=?, c_price=?, c_material=?, c_availability=? WHERE clothing_ID=?";
        conf.updateRecord(qry, name, brand, category, size, color, price, material, availability, id);
    }

    private void deleteClothingItem() {
        Scanner sc = new Scanner(System.in);
        CONFIG conf = new CONFIG();

        viewClothingItem();

        System.out.print("Enter Clothing Item ID to delete: ");
        int id = sc.nextInt();

        String sqlDelete = "DELETE FROM ClothingItem WHERE clothing_ID=?";
        conf.deleteRecord(sqlDelete, id);
    }
}
