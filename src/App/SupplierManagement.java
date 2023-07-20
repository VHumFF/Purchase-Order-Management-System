
package App;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author verno
 */
public class SupplierManagement {
    private File suppTF = new File("Database/Supplier.txt");
    private File itemTF = new File("Database/Item.txt");
    
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
                    editSupplier();
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
    
    private void addSupplier(){
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
                    registerSupplierToTextFile(newSupp);
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
    
    public void registerSupplierToTextFile(Supplier supp){
        Scanner Sc = new Scanner(System.in);
        String supplierName = supp.getSuppName();
        String supplierContact = supp.getSuppContact();
        InventoryDatabase invDB = new InventoryDatabase();

        boolean isDuplicate = invDB.isDuplicateName(supplierName, suppTF);
        
        if(isDuplicate){
            System.out.println(System.lineSeparator().repeat(50));
            System.out.println("Error: Supplier name already exists.");
            System.out.println("Press [Enter] to continue...");
            Sc.nextLine();
            return;
        }
        String supplierID = "SP" + invDB.generateID(suppTF);
        String [] suppInfo = {supplierID, supplierName, supplierContact};
        
        invDB.appendToTextFile(suppInfo, suppTF);
        
        System.out.println(System.lineSeparator().repeat(50));
        System.out.println("Supplier registration successful.");
        System.out.println("Press [Enter] to continue...");
        Sc.nextLine();
    }
    
    
    
    private boolean validateSupplierName(String supplierName){
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
    
    
    private boolean validateSupplierContact(String supplierContact){
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
    
    
    private void deleteSupplier(){
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
                        deleteSupplierFromTF(supp);
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
                        deleteSupplierFromTF(supp);
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
    
    public void deleteSupplierFromTF(Supplier supp){
        String supplierID = supp.getSuppID();
        
        InventoryDatabase invDB = new InventoryDatabase();
        ArrayList<String[]> supplierList = invDB.getAllData(suppTF);
        
        ArrayList<String[]> updatedSupplierList = new ArrayList<>();
        for(String[] supplier: supplierList){
            if(!(supplier[0].equals(supplierID))){
                updatedSupplierList.add(supplier);
            }
        }
        invDB.writeToTextFile(updatedSupplierList.get(0), suppTF);
        for(int i = 1; i < updatedSupplierList.size(); i++){
            invDB.appendToTextFile(updatedSupplierList.get(i), suppTF);
        }
        
 
        ArrayList<String[]> itemList = invDB.getAllData(itemTF);
        
        for(String[] item: itemList){
            if(item[5].equals(supplierID)){
                item[5] = "-";
            }
        }
        invDB.writeToTextFile(itemList.get(0), itemTF);
        for(int i = 1; i < itemList.size(); i++){
            invDB.appendToTextFile(itemList.get(i), itemTF);
        }
        
    }
    

    private void editSupplier(){
        Scanner Sc = new Scanner(System.in);
        Supplier supp = new Supplier(null, null, null);
        Outer:
        while(true){
            ArrayList<String[]> supplierList = displaySupplier();

            System.out.print("\nEnter the supplier ID to edit or [BACK] to return:");
            String supplierToEdit = Sc.nextLine().toUpperCase();
            if(supplierToEdit.equals("BACK")){
                System.out.println(System.lineSeparator().repeat(50));
                break Outer;
            }
            
            boolean idFound = false;
            
            for(String[] supplier : supplierList){
                String supplierID = supplier[0];
                if(supplierID.equals(supplierToEdit)){
                    supp = new Supplier(supplier[0], supplier[1], supplier[2]);
                    idFound = true;
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
            
            selectSuppAttributeAndEdit(supp);
            
        }
    }
    
    private boolean selectSuppAttributeAndEdit(Supplier supp){
        Scanner Sc = new Scanner(System.in);
        while(true){
            System.out.println("==============================");
            System.out.println("Supplier ID     : " + supp.getSuppID());
            System.out.println("Supplier Name   : " + supp.getSuppName());
            System.out.println("Supplier Contact: " + supp.getSuppContact());
            System.out.println("==============================\n");
            System.out.println("Choose an attribute to edit:");
            System.out.print("1. Supplier Name\n2. Supplier Contact Information\n0. Back\nPlease enter your choice:");
            String choice = Sc.nextLine();
            if(choice.equals("1")){
                System.out.print("Enter supplier new name:");
                String suppName = Sc.nextLine();
                boolean nameValid = validateSupplierName(suppName);
                supp.setSuppName(suppName);

                return true;
            }
            else if(choice.equals("2")){
                System.out.print("Enter supplier new contact:");
                String suppContact = Sc.nextLine();
                supp.setSuppContact(suppContact);
                return true;
            }
            else if(choice.equals("0")){

                return false;
            }
            else{
                return false;
            }
        }
    }
    
    public ArrayList<String[]> displaySupplier(){
        InventoryDatabase invDB = new InventoryDatabase();
        ArrayList<String[]> supplierList = getSupplierItemList();
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
        System.out.println("=============================================================================\n");
        
        return supplierList;
    }
    
    
    public ArrayList<String[]> getSupplierItemList(){
        InventoryDatabase invDB = new InventoryDatabase();
        
        ArrayList<String[]> suppList = invDB.getAllData(suppTF);
        if(suppList == null){
            return suppList;
        }
        
        ArrayList<String[]> itemList = invDB.getAllData(itemTF);
        if(itemList == null){
            return suppList;
        }
        
        ArrayList<String[]> supplier_item_List = new ArrayList<>();
        
        for (String[] supplier : suppList) {
            List<String> itemID = new ArrayList<>();
            String supplierID = supplier[0];

            for (String[] item : itemList) {
                if (item[5].equals(supplierID)) {
                    itemID.add(item[0]);
                }
            }
            if (!itemID.isEmpty()) {
                String[] updatedSupplier = new String[supplier.length + itemID.size()];
                System.arraycopy(supplier, 0, updatedSupplier, 0, supplier.length);
                for (int i = 0; i < itemID.size(); i++) {
                    updatedSupplier[supplier.length + i] = itemID.get(i);
                }
                supplier_item_List.add(updatedSupplier);
            }
            else{
                String[] updatedSupplier = new String[supplier.length];
                System.arraycopy(supplier, 0, updatedSupplier, 0, supplier.length);
                supplier_item_List.add(updatedSupplier);
            }
        }
        return supplier_item_List;
    }
}
