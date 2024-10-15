
package Clothingitems;

import it2c.lariosa.cr.CONFIG;
import java.util.Scanner;


public class Clothing {
    
    public void Clothing (){
    
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
                    
                    break;
                case 2:
                    
                    break;
                case 3:
                    
                    break;
                case 4:
                    
                    break;
                case 5:
                    
                    break;
            }

            System.out.print("\nDo you want to use another system? (Y/N): ");
            ch = sc.next();
        } while (ch.equalsIgnoreCase("Y"));

        System.out.println("\nThank you for using This application");
    }

    
    
    public void addClothings(){
        Scanner sc = new Scanner(System.in);
        CONFIG conf = new CONFIG();
        
        System.out.print("------------------------------------");
        System.out.print("Name of item: ");
        String name = sc.next();
        
        System.out.print("Clothing Category (DRESS, SUIT): ");
        String cat = sc.next();
        
        System.out.print("Clothing Size (SMALL, MEDIUM, LARGE): ");
        String size = sc.next();
        
        System.out.print("Clothing Price :");
        double price = sc.nextDouble();
        
        String sql = "INSERT INTO Customer (Clothingname, clothingcategory, clothingsize, clothingprice) VALUES (?, ?, ?, ?)";
 
        conf.addRecord(sql,name,cat, size, price);
        
    }
    

    private void viewCustomer() {
        CONFIG con = new CONFIG();
    
        String lariosaQuery = "SELECT * FROM Clothing";
        String[] lariosaHeaders = {"ID", "FirstName", "Lastname", "Email", "Contact"};
        String[] lariosaColumns = {"c_id", "c_fname", "c_lname", "c_email", "c_contact"};
        
        con.viewRecords(lariosaQuery,lariosaHeaders,lariosaColumns);
    }

    private void updateCustomer(){
    Scanner sc = new Scanner (System.in);
    
        System.out.print("Name of item: ");
        String name = sc.next();
        
        System.out.print("Clothing Category (DRESS, SUIT): ");
        String cat = sc.next();
        
        System.out.print("Clothing Size (SMALL, MEDIUM, LARGE): ");
        String size = sc.next();
        
        System.out.print("Clothing Price :");
        double price = sc.nextDouble();
    
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
        
}
