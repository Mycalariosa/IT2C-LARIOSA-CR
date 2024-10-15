
package Customer;

import it2c.lariosa.cr.CONFIG;
import java.util.Scanner;


public class Customer {
    
    public void customerTransaction (){
    
      Scanner sc = new Scanner(System.in);
        String ch;

        do {
            System.out.println("\n|------------------|");
            System.out.println("|        MENU      |");
            System.out.println("|------------------|");
            System.out.println("| 1. ADD           |");
            System.out.println("| 2. VIEW          |");
            System.out.println("| 3. UPDATE        |");
            System.out.println("| 4. DELETE        |");
            System.out.println("| 5. EXIT          |");
            System.out.println("|------------------|");
            
            System.out.print("Choose from 1-5: ");
            int action = sc.nextInt();

            while (action < 1 || action > 5) {
                System.out.print("\tInvalid action. Please enter a number between 1 and 5: ");
                action = sc.nextInt();
            }
            Customer cr = new Customer();
            
          switch (action) {
                case 1:
                    cr.addCustomer();
                    break;
                case 2:
                    cr.viewCustomer();
                    break;
                case 3:
                    cr.updateCustomer();
                    break;
                case 4:
                    cr.deleteCustomer();
                    break;
                case 5:
                    
                    break;
            }

            System.out.print("\nDo you want to use another system? (Y/N): ");
            ch = sc.next();
        } while (ch.equalsIgnoreCase("Y"));

        System.out.println("\nThank you for using This application");
    }

    
    
    public void addCustomer(){
        Scanner sc = new Scanner(System.in);
        CONFIG conf = new CONFIG();
        
        System.out.print("------------------------------------");
        System.out.print("\nCustomers First Name: ");
        String fname = sc.next();
        
        System.out.print("Customers Last Name: ");
        String lname = sc.next();
        
        System.out.print("Email: ");
        String email = sc.next();
        
        System.out.print("Contact Number:");
        String contact = sc.next();
        
        String sql = "INSERT INTO Customer (c_fname, c_lname, c_email, c_contact) VALUES (?, ?, ?, ?)";
 
        conf.addRecord(sql, fname, lname, email, contact);
        
    }
    

    private void viewCustomer() {
        CONFIG con = new CONFIG();
    
        String lariosaQuery = "SELECT * FROM Customer";
        String[] lariosaHeaders = {"ID", "FirstName", "Lastname", "Email", "Contact"};
        String[] lariosaColumns = {"c_id", "c_fname", "c_lname", "c_email", "c_contact"};
        
        con.viewRecords(lariosaQuery,lariosaHeaders,lariosaColumns);
    }

    private void updateCustomer(){
    Scanner sc = new Scanner (System.in);
    
        System.out.print("Enter Customer ID:");
        int id =sc.nextInt();
    
        System.out.print("Enter First Name:");
        String fname = sc.next();
    
        System.out.print("Enter Last Name:");
        String lname =sc.next();
    
        System.out.print("Enter Email:");
        String email =sc.next();
    
        System.out.print("Enter Contact Number:");
        String number =sc.next();
    
        String qry = "UPDATE Customer SET c_fname=?, c_lname=?, c_email=?, c_contact=? WHERE c_id = ?";
    
    CONFIG con = new CONFIG();
    con.updateRecord (qry, fname, lname, email, number, id);
    
}
    private void deleteCustomer(){
       Scanner sc = new Scanner (System.in); 
       System.out.print("Enter Customer ID:");
       int id =sc.nextInt();
       
       String sqlDelete ="DELETE FROM Customer WHERE c_id =?";
       CONFIG con = new CONFIG();
       con.deleteRecord (sqlDelete,id);
               
    }
    
}
        


    
      



            
            
            
    

