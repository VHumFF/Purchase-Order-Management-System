
package App;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author verno
 */
public class SupplierManagement {
    

    //sales management submenu(supplier management)
    public void OpenPage(){
        Scanner Sc = new Scanner(System.in);
        
        Outer:
        while(true){
            System.out.println("======== Supplier Management Submenu ========");
            System.out.println(String.format("%-2s %-1s", "1.", "Add Supplier"));
            System.out.println(String.format("%-2s %-1s", "2.", "Delete Supplier"));
            System.out.println(String.format("%-2s %-1s", "3.", "Edit Supplier"));
            System.out.println(String.format("%-2s %-1s", "4.", "View Supplier List"));
            System.out.println(String.format("%-2s %-1s", "0.", "Exit"));
            System.out.print("Enter your choice:");

            String option = Sc.nextLine();
            switch(option){
                case "1":
                    addSupplier();
                    break;
                case "2":
                    deleteSupplier();
                    break;
                case "3":
                    System.out.println("You selected Daily Item-wise Sales Entry");
                    break;
                case "4":
                    displaySupplier();
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
    
    public void addSupplier(){
        String supplierName = null;
        String supplierContact = null;
        Scanner Sc = new Scanner(System.in);
        
        Outer:
        while(true){
            System.out.println("======== Add New Supplier ========");
            System.out.print(String.format("Enter Supplier Name:"));
            supplierName = Sc.nextLine();
            boolean spNameValid = validateSupplierName(supplierName);
            if(!spNameValid){
                continue;
            }
            
            
            System.out.print(String.format("Enter Supplier Contact No.:"));
            supplierContact = Sc.nextLine();
            boolean spContactValid = validateSupplierContact(supplierContact);
            if(!spContactValid){
                continue;
            }
            
            
            while(true){
                System.out.println("Comfirm supplier detail.");
                System.out.println("1. Comfirm");
                System.out.println("0. Cancel");
                System.out.print("Enter your choice:");
                String choice = Sc.nextLine();
                switch(choice){
                case "1":
                    Supplier newSupp = new Supplier(supplierName, supplierContact);
                    InventoryDatabase invDB = new InventoryDatabase(newSupp);
                    invDB.registerSupplier();
                    break Outer;
                case "0":
                    System.out.println(System.lineSeparator().repeat(50));
                    break Outer;
                    
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
                }
            }
        }
    }
    
    
    
    public boolean validateSupplierName(String supplierName){
        if(!supplierName.matches("^(?=.*[a-zA-Z])[a-zA-Z0-9 ]{3,30}$")){
            Scanner sc = new Scanner(System.in);
            if(supplierName.length() < 3 || supplierName.length() > 50){
                System.out.println("Invalid supplier name length. Please ensure supplier name has between 3 and 30 characters.");
            }
            else{
                System.out.println("Invalid supplier name format. Please use only letters and digits, and ensure it contains at least one letter.");
            }
            System.out.println("Press [Enter] to continue...");
            sc.nextLine();
            System.out.println(System.lineSeparator().repeat(50));
            return false;
        }
        return true;
    }
    
    
    public boolean validateSupplierContact(String supplierContact){
        if(!supplierContact.matches("[0-9]{9,11}$")){
            Scanner sc = new Scanner(System.in);
            if(supplierContact.length() < 9 || supplierContact.length() > 11){
                System.out.println("Invalid supplier name length. Please ensure supplier name has between 9 and 11 characters.");
            }
            else{
                System.out.println("Invalid supplier contact format. Please use only digits.");
            }
            System.out.println("Press [Enter] to continue...");
            sc.nextLine();
            System.out.println(System.lineSeparator().repeat(50));
            return false;
        }
        return true;
    }
    
    
    public void deleteSupplier(){
        
        Scanner Sc = new Scanner(System.in);
        Outer:
        while(true){
            ArrayList<String[]> supplierList = displaySupplier();

            System.out.print("\nEnter the supplier ID to delete or [BACK] to return:");
            String supplierToDelete = Sc.nextLine().toUpperCase();
            if(supplierToDelete.equals("BACK")){
                System.out.println(System.lineSeparator().repeat(50));
                break Outer;
            }
            
            boolean idFound = false;
            ArrayList<String> itemSupplied = new ArrayList<>();
            for(String[] supplier : supplierList){
                String supplierID = supplier[0];
                if(supplierID.equals(supplierToDelete)){
                    idFound = true;
                    for(int i = 3; i < supplier.length; i++){
                        itemSupplied.add(supplier[i]);
                    }
                    break;
                }
            }
            if(!idFound){
                System.out.println(System.lineSeparator().repeat(50));
                System.out.println("Please enter a valid supplierID");
                System.out.println("Press [Enter] to continue...");
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                continue;
            }
            
            if(!itemSupplied.isEmpty()){
                while(true){
                    System.out.println("Warning: Deleting this supplier will also remove their association with the following items:");
                    for(String i : itemSupplied){
                        System.out.println(i);
                    }
                    System.out.println("This action cannot be undone. Are you sure you want to proceed?");
                    System.out.print("1. Delete\n2. Cancel\nPlease enter your choice:");
                    String choice = Sc.nextLine();
                    if(choice.equals("1")){
                        Supplier supp = new Supplier(supplierToDelete);
                        InventoryDatabase invDB = new InventoryDatabase(supp);
                        invDB.deleteSupplier();
                        break;
                    }
                    else if(choice.equals("2")){
                        break Outer;
                    }
                    else{
                        System.out.println("Invalid choice. Try again.");
                        continue;
                    }
                }
            }
            else{
                while(true){
                    System.out.println("Warning: Are you sure you want to delete the supplier?");
                    System.out.println("This action cannot be undone. Are you sure you want to proceed?");
                    System.out.print("1. Delete\n2. Cancel\nPlease enter your choice:");
                    String choice = Sc.nextLine();
                    if(choice.equals("1")){
                        Supplier supp = new Supplier(supplierToDelete);
                        InventoryDatabase invDB = new InventoryDatabase(supp);
                        invDB.deleteSupplier();
                        break;
                    }
                    else if(choice.equals("2")){
                        break Outer;
                    }
                    else{
                        System.out.println("Invalid choice. Try again.");
                        continue;
                    }
                }
            }
            
            
            
            
            
        }
        

        
    }
    
    
    public ArrayList<String[]> displaySupplier(){
        InventoryDatabase invDB = new InventoryDatabase();
        ArrayList<String[]> supplierList = invDB.getSupplierList();
        if(supplierList == null){
            System.out.println("No supplier found.");
            return null;
        }
        
        System.out.println("================================Supplier List================================");
        System.out.printf("%-15s%-25s%-24s%s%n", "Supplier ID", "Supplier Name","Supplier Contact", "Item Supplied");
        System.out.println("=============================================================================");
        
        for (String[] supplier : supplierList) {
            System.out.printf("%-15s%-25s%-24s", supplier[0], supplier[1], supplier[2]);
            for (int i = 3; i < supplier.length; i++) {
                if(i == 3){
                    System.out.println(supplier[i]);
                }
                else{
                    System.out.printf("%-15s%-25s%-24s%s%n", "", "", "", supplier[i]);
                }
            }
            System.out.println();
        }
        System.out.println("=============================================================================");
        
        return supplierList;
    }
    
}
