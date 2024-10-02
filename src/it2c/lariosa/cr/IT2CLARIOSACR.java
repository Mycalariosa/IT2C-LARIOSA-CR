
package it2c.lariosa.cr;

import java.util.Scanner;

public class IT2CLARIOSACR {



    public void addCustomer(){
        Scanner sc = new Scanner(System.in);
        CONFIG conf = new CONFIG();
        System.out.print("Customers First Name: ");
        String fname = sc.next();
        System.out.print("Customers Last Name: ");
        String lname = sc.next();
        System.out.print("Email: ");
        String email = sc.next();
        System.out.print(" Status: ");
        String status = sc.next();
        
        String sql = "INSERT INTO CUSTOMER (c_fname, c_lname, c_email, c_status) VALUES (?, ?, ?, ?)";

        
        conf.addRecord(sql, fname, lname, email, status);
        
    }
    
    public static void main(String[] args) {
       
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
        
          switch (action) {
                case 1:
                    IT2CLARIOSACR cr = new IT2CLARIOSACR();
                    cr.addCustomer();
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
}
        
    
    

