
package App;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


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
                    System.out.println(System.lineSeparator().repeat(50));
                    addSupplier();
                    break;
                case "2":
                    System.out.println(System.lineSeparator().repeat(50));
                    deleteSupplier();
                    break;
                case "3":
                    System.out.println(System.lineSeparator().repeat(50));
                    editSupplier();
                    break;
                case "4":
                    displaySupplier();
                    System.out.println("Press [Enter] to go back.");
                    Sc.nextLine();
                    System.out.println(System.lineSeparator().repeat(50));
                    break;
                default:
                    System.out.println(System.lineSeparator().repeat(50));
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println("Press [Enter] to continue.");
                    break;
                case "0":
                    System.out.println(System.lineSeparator().repeat(50));
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
            //ask user to enter supplier name
            System.out.println("======== Add New Supplier ========");
            System.out.print("Enter Supplier Name:");
            supplierName = Sc.nextLine();
            boolean spNameValid = validateSupplierName(supplierName);
            if(!spNameValid){
                continue;
            }
            
            //ask user to enter supplier contact
            System.out.print("Enter Supplier Contact No.:");
            supplierContact = Sc.nextLine();
            boolean spContactValid = validateSupplierContact(supplierContact);
            if(!spContactValid){
                continue;
            }
            
            while(true){
                System.out.println(System.lineSeparator().repeat(50));
                System.out.println("===Comfirm supplier detail.===\nSupplier Name : "+supplierName+"\nSupplier Contact : "+ supplierContact);
                System.out.println("1. Comfirm");
                System.out.println("0. Cancel");
                System.out.print("Enter your choice:");
                String choice = Sc.nextLine();
                switch(choice){
                case "1":
                    Supplier newSupp = new Supplier(supplierName, supplierContact);
                    registerSupplierToTextFile(newSupp);
                    System.out.println(System.lineSeparator().repeat(50));
                    break Outer;
                case "0":
                    System.out.println(System.lineSeparator().repeat(50));
                    break Outer;
                    
                default:
                    App.displayMessage("Invalid choice. Please try again.");
                    break;
                }
            }
        }
    }
    
    private void registerSupplierToTextFile(Supplier supp){
        Scanner Sc = new Scanner(System.in);
        String supplierName = supp.getSuppName();
        String supplierContact = supp.getSuppContact();
        InventoryDatabase invDB = new InventoryDatabase();
        
        //check whether supplier name exist
        boolean isDuplicate = invDB.isDuplicateName(supplierName, InventoryDatabase.files.SUPPLIER.getFile());
        
        if(isDuplicate){
            App.displayMessage("Error: Supplier name already exists.");

            return;
        }
        String supplierID = "SP" + invDB.generateIDIndex(InventoryDatabase.files.SUPPLIER.getFile(), InventoryDatabase.files.USED_SUPPLIER_ID_INDEX.getFile());
        String [] suppInfo = {supplierID, supplierName, supplierContact};
        
        invDB.appendToTextFile(suppInfo, InventoryDatabase.files.SUPPLIER.getFile());
        
        App.displayMessage("Supplier registration successful.");
    }
    
    
    
    private boolean validateSupplierName(String supplierName){
        if(!supplierName.matches("^(?=.*[a-zA-Z])[a-zA-Z0-9 ]{3,50}$")){
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
            if(supplierList == null){
                System.out.println("Press [Enter] to continue.");
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                break Outer;
            }
            
            //ask user to enter supplier id to delete
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
                //check whether user enter a valid supplier ID
                if(supplierID.equals(supplierToDelete)){
                    idFound = true;
                    //get the item supplied by the supplier
                    for(int i = 3; i < supplier.length; i++){
                        itemSupplied.add(supplier[i]);
                    }
                    break;
                }
            }
            
            if(!idFound){
                App.displayMessage("Please enter a valid supplierID");
                continue;
            }
            
            
            if(!itemSupplied.isEmpty()){
                //if supplier is associate with item, display warning.
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
                        System.out.println(System.lineSeparator().repeat(50));
                        break Outer;
                    }
                    else{
                        App.displayMessage("Invalid choice. Try again.");
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
                        System.out.println(System.lineSeparator().repeat(50));
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
    
    private void deleteSupplierFromTF(Supplier supp){
        String supplierID = supp.getSuppID();
        
        InventoryDatabase invDB = new InventoryDatabase();
        ArrayList<String[]> supplierList = invDB.getAllData(InventoryDatabase.files.SUPPLIER.getFile());
        
        ArrayList<String[]> updatedSupplierList = new ArrayList<>();
        //delete supplier from supplier list
        for(String[] supplier: supplierList){
            if(!(supplier[0].equals(supplierID))){
                updatedSupplierList.add(supplier);
            }
        }
        //update new supplier list to textfile
        if(updatedSupplierList.isEmpty()){
            invDB.clearFile(InventoryDatabase.files.SUPPLIER.getFile());
        }
        else{
            invDB.writeToTextFile(updatedSupplierList.get(0), InventoryDatabase.files.SUPPLIER.getFile());
            for(int i = 1; i < updatedSupplierList.size(); i++){
                invDB.appendToTextFile(updatedSupplierList.get(i), InventoryDatabase.files.SUPPLIER.getFile());
            }
        }
        
        ArrayList<String[]> itemList = invDB.getAllData(InventoryDatabase.files.ITEM.getFile());
        //update item supplier to "-" after item supplier is deleted
        if(itemList != null){
            for(String[] item: itemList){
                String itemNewSupplier = "";
                String[] itemSupList = item[5].strip().split("\\|");
                for(String sup:itemSupList){
                    if(sup.equals(supplierID) || sup.equals("-")){
                        continue;
                    }
                    itemNewSupplier += sup;
                    if (!sup.equals(itemSupList[itemSupList.length - 1])) {
                        itemNewSupplier += "|";
                    }
                }
                if(itemNewSupplier.isEmpty()){
                    item[5]="-";
                    continue;
                }
                item[5] = itemNewSupplier;
            }
            //update item list
            invDB.writeToTextFile(itemList.get(0), InventoryDatabase.files.ITEM.getFile());
            for(int i = 1; i < itemList.size(); i++){
                invDB.appendToTextFile(itemList.get(i), InventoryDatabase.files.ITEM.getFile());
            }
        }
        
        
    }
    

    private void editSupplier(){
        Scanner Sc = new Scanner(System.in);
        Supplier supp = new Supplier(null, null, null);
        Outer:
        while(true){
            ArrayList<String[]> supplierList = displaySupplier();
            if(supplierList == null){
                System.out.println("Press [Enter] to continue.");
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                break Outer;
            }
            //ask user to enter supplier id to edit
            System.out.print("\nEnter the supplier ID to edit or [BACK] to return:");
            String supplierToEdit = Sc.nextLine().toUpperCase();
            if(supplierToEdit.equals("BACK")){
                System.out.println(System.lineSeparator().repeat(50));
                break Outer;
            }
            
            boolean idFound = false;
            //check whether user enter a valid supplier id
            for(String[] supplier : supplierList){
                String supplierID = supplier[0];
                if(supplierID.equals(supplierToEdit)){
                    supp = new Supplier(supplier[0], supplier[1], supplier[2]);
                    idFound = true;
                    break;
                }
            }
            
            if(!idFound){
                App.displayMessage("Please enter a valid supplierID");
                continue;
            }
            
            selectSuppAttributeAndEdit(supp);
            
        }
    }
    
    private void selectSuppAttributeAndEdit(Supplier supp){
        Scanner Sc = new Scanner(System.in);
        while(true){
            System.out.println(System.lineSeparator().repeat(50));
            //ask user to select an attribute to edit
            System.out.println("==============================");
            System.out.println("Supplier ID     : " + supp.getSuppID());
            System.out.println("Supplier Name   : " + supp.getSuppName());
            System.out.println("Supplier Contact: " + supp.getSuppContact());
            System.out.println("==============================\n");
            System.out.println("Choose an attribute to edit:");
            System.out.print("1. Supplier Name\n2. Supplier Contact Information\n0. Back\nPlease enter your choice:");
            String choice = Sc.nextLine();
            if(choice.equals("1")){
                while(true){
                    System.out.print("Enter supplier new name:");
                    String suppName = Sc.nextLine();
                    boolean nameValid = validateSupplierName(suppName);
                    if(!nameValid){
                        continue;
                    }
                    supp.setSuppName(suppName);
                    updateEditSupplier(supp);
                    App.displayMessage("Supplier detail updated successfully");
                    break;
                }
            }
            else if(choice.equals("2")){
                while(true){
                    System.out.print("Enter supplier new contact:");
                    String suppContact = Sc.nextLine();
                    boolean contactValid = validateSupplierContact(suppContact);
                    if(!contactValid){
                        continue;
                    }
                    supp.setSuppContact(suppContact);
                    updateEditSupplier(supp);
                    App.displayMessage("Supplier detail updated successfully");
                    break;
                }
  
            }
            else if(choice.equals("0")){
                System.out.println(System.lineSeparator().repeat(50));
                break;
            }
            else{
                App.displayMessage("Please enter a valid choice.");
                continue;
            }
        }
    }
    
    
    private void updateEditSupplier(Supplier supp){
        InventoryDatabase invDB = new InventoryDatabase();
        ArrayList<String []> supplierList = invDB.getAllData(InventoryDatabase.files.SUPPLIER.getFile());
        //update supplier info
        for(String[] supplier : supplierList){
            if(supplier[0].equals(supp.getSuppID())){
                supplier[1] = supp.getSuppName();
                supplier[2] = supp.getSuppContact();
                break;
            }
        }
        //update supplier info in text file
        invDB.writeToTextFile(supplierList.get(0), InventoryDatabase.files.SUPPLIER.getFile());
        for(int i = 1; i < supplierList.size(); i++){
            invDB.appendToTextFile(supplierList.get(i), InventoryDatabase.files.SUPPLIER.getFile());
        }
        
    }
    
    public ArrayList<String[]> displaySupplier(){
        ArrayList<String[]> supplierList = getSupplierItemList();
        if(supplierList == null){
            System.out.println("No supplier found.");
            return null;
        }
        System.out.println(System.lineSeparator().repeat(50));
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
    
    
    private ArrayList<String[]> getSupplierItemList(){
        InventoryDatabase invDB = new InventoryDatabase();
        
        ArrayList<String[]> suppList = invDB.getAllData(InventoryDatabase.files.SUPPLIER.getFile());
        if(suppList == null){
            return suppList;
        }
        
        ArrayList<String[]> itemList = invDB.getAllData(InventoryDatabase.files.ITEM.getFile());
        if(itemList == null){
            return suppList;
        }
        
        ArrayList<String[]> supplier_item_List = new ArrayList<>();
        
        for (String[] supplier : suppList) {
            List<String> itemID = new ArrayList<>();
            String supplierID = supplier[0];
            
            //get item supplied by supplier
            for (String[] item : itemList) {
                String[] itemSupp = item[5].strip().split("\\|");
                for(String supp : itemSupp){
                    if (supp.equals(supplierID)) {
                        itemID.add(item[0]);
                    }
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
