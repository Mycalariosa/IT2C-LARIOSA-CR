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
        viewCustomer();

        System.out.print("Enter Customer ID: ");
        int id = sc.nextInt();

        System.out.print("Enter First Name: ");
        String fname = sc.next();

        System.out.print("Enter Last Name: ");
        String lname = sc.next();

        System.out.print("Enter Email: ");
        String email = sc.next();

        String contact = "";
        while (true) {
            System.out.print("Enter Contact Number: ");
            contact = sc.next();

            if (contact.matches("\\d{11}")) {   
                break;
            } else {
                System.out.println("Invalid contact number. Please enter exactly 11 digits.");
            }
        }

        System.out.print("Enter Address: ");
        String address = sc.next();

        String qry = "UPDATE Customer SET c_fname=?, c_lname=?, c_email=?, c_contact=?, c_address=? WHERE c_id = ?";
        CONFIG con = new CONFIG();
        con.updateRecord(qry, fname, lname, email, contact, address, id);
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
