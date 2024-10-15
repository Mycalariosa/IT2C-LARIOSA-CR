
package it2c.lariosa.cr;
import Customer.Customer;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
       
        Scanner sc = new Scanner(System.in);
        String ch;

        do {
            System.out.println("\n|------------------------------------|");
            System.out.println("|    WELCOME TO CLOTHING RENTAL      |");
            System.out.println("|------------------------------------|");
            System.out.println("| 1. CUSTOMER                        |");
            System.out.println("| 2. CLOTHING                        |");
            System.out.println("| 3. RENTAL                          |");
            System.out.println("| 4. EXIT                            |");
            System.out.println("|------------------------------------|");
            
            System.out.print("Choose from 1-4: ");
            int action = sc.nextInt();

            while (action < 1 || action > 5) {
                System.out.print("\tInvalid action. Please enter a number between 1 and 5: ");
                action = sc.nextInt();
            }
        Customer cr = new Customer();
          switch (action) {
                case 1:
                     cr.customerTransaction();
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
        
    
    

