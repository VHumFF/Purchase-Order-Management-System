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
public class SalesManagementMenu {
    public void OpenPage(){
        Scanner Sc = new Scanner(System.in);
        
        Outer:
        while(true){
            System.out.println("======== Sales Management Menu ========");
            System.out.println(String.format("%-2s %-1s", "1.", "Item Entry"));
            System.out.println(String.format("%-2s %-1s", "2.", "Supplier Entry"));
            System.out.println(String.format("%-2s %-1s", "3.", "Daily Item-wise Sales Entry"));
            System.out.println(String.format("%-2s %-1s", "4.", "Create Purchse Requisition"));
            System.out.println(String.format("%-2s %-1s", "5.", "Display Requisition"));
            System.out.println(String.format("%-2s %-1s", "6.", "Display Purchaser Orders"));
            System.out.println(String.format("%-2s %-1s", "0.", "Exit"));
            System.out.print("Enter your choice:");

            String option = Sc.nextLine();
            switch(option){
                case "1":
                    System.out.println("You selected Item Entry");
                    break;
                case "2":
                    System.out.println("You selected Supplier Entry");
                    break;
                case "3":
                    System.out.println("You selected Daily Item-wise Sales Entry");
                    break;
                case "4":
                    System.out.println("You selected Create Purchse Requisition");
                    break;
                case "5":
                    System.out.println("You selected Display Requisition");
                    break;
                case "6":
                    System.out.println("You selected Display Purchaser Orders");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
                case "0":
                    System.out.println("Exiting Sales Management Menu. Goodbye!");
                    break Outer;
            }
        }

    }
}