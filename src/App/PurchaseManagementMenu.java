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
    public void OpenPage(){
        App appInstance = new App();
        Scanner Sc = new Scanner(System.in);
        Outer:
        while(true){
            System.out.println("======== Purchase Management Menu ========");
            System.out.println(String.format("%-2s %-1s", "1.", "Display List of Items"));
            System.out.println(String.format("%-2s %-1s", "2.", "Display List of Supplier"));
            System.out.println(String.format("%-2s %-1s", "3.", "Display Requisition"));
            System.out.println(String.format("%-2s %-1s", "4.", "Generate Purchase Order"));
            System.out.println(String.format("%-2s %-1s", "0.", "Exit"));
            System.out.print("Enter your choice:");
            
            String option = Sc.nextLine();
            switch(option){
                case "1":
                    ItemManagement im = new ItemManagement();
                    im.displayItemList();
                    System.out.println("Press [Enter] to go back");
                    Sc.nextLine();
                    System.out.println(System.lineSeparator().repeat(50));
                    break;
                case "2":
                    SupplierManagement sm = new SupplierManagement();
                    sm.displaySupplier();
                    System.out.println("Press [Enter] to go back.");
                    Sc.nextLine();
                    System.out.println(System.lineSeparator().repeat(50));
                    break;
                case "3":
                    System.out.println(System.lineSeparator().repeat(50));
                    PurchaseRequisitionManagement prm = new PurchaseRequisitionManagement();
                    String status = prm.selectPRStatus();
                    prm.displayPRList(status);
                    System.out.println("Press [Enter] to go back.");
                    Sc.nextLine();
                    System.out.println(System.lineSeparator().repeat(50));
                    break;
                case "4":
                    appInstance.changePage(App.AppPage.PURCHASE_ORDER_MANAGEMENT.getPageClass());
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println(System.lineSeparator().repeat(50));
                    break;
                case "0":
                    System.out.println("Exiting Purchase Management Menu. Goodbye!");
                    System.out.println(System.lineSeparator().repeat(50));
                    break Outer;
            }
            
        }
    }
}
