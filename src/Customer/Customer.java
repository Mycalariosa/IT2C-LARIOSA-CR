package Customer;

import it2c.lariosa.cr.CONFIG;
import java.util.Scanner;

public class Customer {

    public void customerTransaction(Scanner sc) {
        String ch=null;
        Scanner in = new Scanner (System.in);
        String another =null;

        do {
                System.out.println("\n|--------------------|");
                System.out.println("|   CUSTOMER MENU    |");
                System.out.println("|--------------------|");
                System.out.println("| 1. ADD CUSTOMER    |");
                System.out.println("| 2. VIEW CUSTOMER   |");
                System.out.println("| 3. UPDATE CUSTOMER |");
                System.out.println("| 4. DELETE CUSTOMER |");
                System.out.println("| 5. EXIT            |");
                System.out.println("|--------------------|");

                int action = -1;  // Initialize with an invalid action
                boolean validInput = false;

    
        while (!validInput) {
          System.out.print("Choose from 1-5: ");
        if (sc.hasNextInt()) {
            action = sc.nextInt();
            if (action >= 1 && action <= 5) {
                validInput = true;
            } else {
                System.out.println("\tInvalid action. Please enter a number between 1 and 4.");
            }
        } else {
            System.out.println("\tInvalid input. Please enter a number.");
            sc.next(); // Clear the invalid input
        }
        switch (action) {
                case 1:
                    addCustomer(sc);
                    break;
                case 2:
                    viewCustomer();
                    break;
                case 3:
                    updateCustomer(sc);
                    break;
                case 4:
                    deleteCustomer(sc);
                    break;
                case 5:
                    System.out.println("Thanks!! UWU -_-");
                    return;
            }  
    }
            System.out.print("\nDo you still want to use Customer menu? (Y/N): ");
            another = in.next().trim();
            
            while(!another.equals("Yes")&&!another.equals("yes")&&!another.equals("YES")&&!another.equals("y")&&!another.equals("Y")&&!another.equals("n")&&!another.equals("N")&&!another.equals("No")&&!another.equals("NO")&&!another.equals("no")){
                System.out.print("Number is not Allowed! Please Try again (Y/N):");
                another=in.next().trim();
        }
            }while (another.equals("YES")||another.equals("yes")||another.equals("Yes")||another.equals("y")||another.equals("Y"));
                System.out.println("Thamkyou for using Customer Application");
    }

    public void addCustomer(Scanner sc) {
        CONFIG conf = new CONFIG();

        System.out.print("------------------------------------");
        System.out.print("\nCustomer's First Name: ");
        String fname = sc.next();

        System.out.print("Customer's Last Name: ");
        String lname = sc.next();

        System.out.print("Email: ");
        String email = sc.next();

        String contact = "";
        while (true) {
            System.out.print("Contact Number: ");
            contact = sc.next();

            if (contact.matches("\\d{11}")) {
                break;
            } else {
                System.out.println("Invalid contact number. Please enter exactly 11 digits.");
            }
        }

        System.out.print("Address: ");
        String address = sc.next();
        
        System.out.print("------------------------------------\n");

        String sql = "INSERT INTO Customer (c_fname, c_lname, c_email, c_contact, c_address) VALUES (?, ?, ?, ?, ?)";
        conf.addRecord(sql, fname, lname, email, contact, address);
    }

    private void viewCustomer() {
        CONFIG con = new CONFIG();

        String lariosaQuery = "SELECT * FROM Customer";
        String[] lariosaHeaders = {"ID", "First Name", "Last Name", "Email", "Contact", "Address"};
        String[] lariosaColumns = {"c_id", "c_fname", "c_lname", "c_email", "c_contact", "c_address"};

        con.viewRecords(lariosaQuery, lariosaHeaders, lariosaColumns);
    }

   private void updateCustomer(Scanner sc) {
    // First, display all customers (to make sure user knows the customer IDs)
    viewCustomer();

    System.out.print("Enter Customer ID you want to update: ");
    int id = sc.nextInt();

    boolean validId = false;
    // Validate if customer ID exists
    CONFIG conf = new CONFIG();
    String queryExistence = "SELECT COUNT(1) FROM Customer WHERE c_id=?";
    int exists = conf.checkExistence(queryExistence, id);

    if (exists == 0) {
        System.out.println("\tERROR: Customer ID doesn't exist.");
        return; // Exit if customer doesn't exist
    }

    System.out.println("Customer ID " + id + " exists. What would you like to update?");
    boolean updateComplete = false;
    while (!updateComplete) {
        // Display a menu for the fields that can be updated
        System.out.println("\nWhich field would you like to update?");
        System.out.println("1. First Name");
        System.out.println("2. Last Name");
        System.out.println("3. Email");
        System.out.println("4. Contact Number");
        System.out.println("5. Address");
        System.out.println("6. Exit Update Process");
        System.out.print("Select an option (1-6): ");
        
        int choice = sc.nextInt();
        
        switch (choice) {
            case 1:
                System.out.print("Enter New First Name: ");
                String fname = sc.next();
                String query1 = "UPDATE Customer SET c_fname=? WHERE c_id=?";
                conf.updateRecord(query1, fname, id);
                System.out.println("First Name updated successfully.");
                break;
            case 2:
                System.out.print("Enter New Last Name: ");
                String lname = sc.next();
                String query2 = "UPDATE Customer SET c_lname=? WHERE c_id=?";
                conf.updateRecord(query2, lname, id);
                System.out.println("Last Name updated successfully.");
                break;
            case 3:
                System.out.print("Enter New Email: ");
                String email = sc.next();
                String query3 = "UPDATE Customer SET c_email=? WHERE c_id=?";
                conf.updateRecord(query3, email, id);
                System.out.println("Email updated successfully.");
                break;
            case 4:
                String contact = "";
                while (true) {
                    System.out.print("Enter New Contact Number: ");
                    contact = sc.next();

                    if (contact.matches("\\d{11}")) {
                        break;
                    } else {
                        System.out.println("Invalid contact number. Please enter exactly 11 digits.");
                    }
                }
                String query4 = "UPDATE Customer SET c_contact=? WHERE c_id=?";
                conf.updateRecord(query4, contact, id);
                System.out.println("Contact Number updated successfully.");
                break;
            case 5:
                System.out.print("Enter New Address: ");
                String address = sc.next();
                String query5 = "UPDATE Customer SET c_address=? WHERE c_id=?";
                conf.updateRecord(query5, address, id);
                System.out.println("Address updated successfully.");
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

    private void deleteCustomer(Scanner sc) {
        viewCustomer();

        System.out.print("Enter Customer ID: ");
        int id = sc.nextInt();

        String sqlDelete = "DELETE FROM Customer WHERE c_ID = ?";
        CONFIG con = new CONFIG();
        con.deleteRecord(sqlDelete, id);
    }
}
