package Customer;

import it2c.lariosa.cr.CONFIG;
import java.util.Scanner;

public class Customer {

    public void customerTransaction(Scanner sc) {
        String ch = null;
        Scanner in = new Scanner(System.in);
        String another = null;

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
                        System.out.println("\tInvalid action. Please enter a number between 1 and 5.");
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
                        System.out.println("Thank you for using customer information -_-");
                        return;
                }
            }
            System.out.print("\nDo you still want to use Customer menu? (Y/N): ");
            another = in.next().trim();

            while (!another.equalsIgnoreCase("Y") && !another.equalsIgnoreCase("N")) {
                System.out.print("Invalid input! Please try again (Y/N): ");
                another = in.next().trim();
            }
        } while (another.equalsIgnoreCase("Y"));
        System.out.println("Thank you for using Customer Application");
    }

    public void addCustomer(Scanner sc) {
        CONFIG conf = new CONFIG();

        System.out.print("------------------------------------");
        System.out.print("\nCustomer's First Name: ");
        String fname = sc.next();

        // Validate that first name doesn't contain numbers
        while (!isValidName(fname)) {
            System.out.println("Invalid first name. Please enter a name without numbers.");
            System.out.print("Customer's First Name: ");
            fname = sc.next();
        }

        System.out.print("Customer's Last Name: ");
        String lname = sc.next();

        // Validate that last name doesn't contain numbers
        while (!isValidName(lname)) {
            System.out.println("Invalid last name. Please enter a name without numbers.");
            System.out.print("Customer's Last Name: ");
            lname = sc.next();
        }

        System.out.print("Email: ");
        String email = sc.next();

        // Validate email for valid domains
        while (!isValidEmail(email)) {
            System.out.println("Invalid email. Please enter a valid email with a domain like gmail.com, yahoo.com, etc.");
            System.out.print("Email: ");
            email = sc.next();
        }

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

    private boolean isValidName(String name) {
        // Check if name contains only letters and spaces
        return name.matches("^[a-zA-Z ]+$");
    }

    private boolean isValidEmail(String email) {
        // Check if email ends with gmail.com, yahoo.com, or other valid domain
        return email.matches("^[a-zA-Z0-9._%+-]+@(gmail\\.com|yahoo\\.com|outlook\\.com)$");
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
                    // Validate email
                    while (!isValidEmail(email)) {
                        System.out.println("Invalid email. Please enter a valid email with a domain like gmail.com, yahoo.com, etc.");
                        System.out.print("Email: ");
                        email = sc.next();
                    }
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
        // Display all customers for reference
        viewCustomer();

        // Prompt the user for the customer ID to delete
        System.out.print("Enter the Customer ID to delete: ");
        int id = -1;

        while (true) {
            if (sc.hasNextInt()) {
                id = sc.nextInt();
                break;
            } else {
                System.out.print("Invalid input. Please enter a valid numeric Customer ID: ");
                sc.next(); // Clear the invalid input
            }
        }

        CONFIG con = new CONFIG();
        String queryExistence = "SELECT COUNT(1) FROM Customer WHERE c_id=?";
        int exists = con.checkExistence(queryExistence, id);

        if (exists == 0) {
            // If the customer ID does not exist, display an error message
            System.out.println("ERROR: Customer ID " + id + " does not exist.");
            return;
        }

        // Confirm deletion with the user
        System.out.print("Are you sure you want to delete Customer ID " + id + "? (Y/N): ");
        String confirmation = sc.next().trim();

        // Validate confirmation input
        while (!confirmation.equalsIgnoreCase("Y") && !confirmation.equalsIgnoreCase("N")) {
            System.out.print("Invalid input. Please enter 'Y' to confirm or 'N' to cancel: ");
            confirmation = sc.next().trim();
        }

        if (confirmation.equalsIgnoreCase("Y")) {
            // Proceed with deletion
            String sqlDelete = "DELETE FROM Customer WHERE c_id=?";
            try {
                con.deleteRecord(sqlDelete, id);
                System.out.println("Customer ID " + id + " has been successfully deleted.");
            } catch (Exception e) {
                System.out.println("Error while deleting the record: " + e.getMessage());
            }
        } else {
            // Deletion canceled by the user
            System.out.println("Deletion process canceled.");
        }
    }
}
