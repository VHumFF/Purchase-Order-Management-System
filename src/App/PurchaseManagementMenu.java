/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

import java.util.Scanner;

/**
 *
 * @author verno
 */
public class PurchaseManagementMenu {
    public void OpenMenu(){
        Scanner Sc = new Scanner(System.in);
        Outer:
        while(true){
            System.out.println("======== Purchase Management Menu ========");
            System.out.println(String.format("%-2s %-1s", "1.", "Display List of Items"));
            System.out.println(String.format("%-2s %-1s", "2.", "Display List of Supplier"));
            System.out.println(String.format("%-2s %-1s", "3.", "Display Requisition"));
            System.out.println(String.format("%-2s %-1s", "4.", "Generate Purchase Order"));
            System.out.println(String.format("%-2s %-1s", "5.", "Display List of Purchaser Orders"));
            System.out.println(String.format("%-2s %-1s", "0.", "Exit"));
            System.out.print("Enter your choice:");
            
            String option = Sc.nextLine();
            switch(option){
                case "1":
                    System.out.println("You selected List of Items");
                    break;
                case "2":
                    System.out.println("You selected Display List of Supplier");
                    break;
                case "3":
                    System.out.println("You selected Display Requisition");
                    break;
                case "4":
                    System.out.println("You selected Generate Purchase Order");
                    break;
                case "5":
                    System.out.println("You selected Display List of Purchaser Orders");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
                case "0":
                    System.out.println("Exiting Purchase Management Menu. Goodbye!");
                    break Outer;
            }
            
        }
        Sc.close();
    }
}
