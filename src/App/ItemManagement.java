/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

import java.io.File;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author verno
 */
public class ItemManagement {
    
    
    //sales management submenu(item management)
    public void OpenPage(){
        Scanner Sc = new Scanner(System.in);
        
        Outer:
        while(true){
            System.out.println("======== Item Management Submenu ========");
            System.out.println(String.format("%-2s %-1s", "1.", "Add Item"));
            System.out.println(String.format("%-2s %-1s", "2.", "Delete Item"));
            System.out.println(String.format("%-2s %-1s", "3.", "Edit Item"));
            System.out.println(String.format("%-2s %-1s", "4.", "View Item List"));
            System.out.println(String.format("%-2s %-1s", "5.", "View Item List By Category"));
            System.out.println(String.format("%-2s %-1s", "0.", "Exit"));
            System.out.print("Enter your choice:");
            
            String option = Sc.nextLine();
            String category = null;
            switch(option){
                case "1":
                    category = selectItemCategory();
                    if(category != null){
                        addItem(category);
                    }
                    break;
                case "2":
                    category = selectItemCategory();
                    if(category != null){
                        deleteItem(category);
                    }
                    break;
                case "3":
                    category = selectItemCategory();
                    if(category != null){
                        //editItem(category);
                    }
                    break;
                case "4":
                    displayItemList();
                    break;
                case "5":
                    category = selectItemCategory();
                    if(category != null){
                        displayItemListByCategory(category);
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
                case "0":
                    System.out.println("Exiting Item Management Menu. Goodbye!");
                    break Outer;
            }
        }
    }
    
    private String selectItemCategory(){
        Scanner Sc = new Scanner(System.in);
        while(true){
            System.out.println("======== Category ========");
            System.out.println(String.format("%-2s %-1s", "1.", "Everyday Groceries"));
            System.out.println(String.format("%-2s %-1s", "2.", "Fresh Produce"));
            System.out.println(String.format("%-2s %-1s", "3.", "Fresh Food"));
            System.out.println(String.format("%-2s %-1s", "0.", "Back"));
            System.out.print("Enter your choice:");
            String category;
            String option = Sc.nextLine();
            switch(option){
                case "1":
                    category = "Everyday Groceries";
                    return category;
                case "2":
                    category ="Fresh Produce";
                    return category;
                case "3":
                    category = "Fresh Food";
                    return category;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
                case "0":
                    System.out.println("Exiting Item Management Menu. Goodbye!");
                    return null;
            }
        }
    }
    
    
    
    private void addItem(String category){
        String itemName;
        float itemUnitPrice;
        int itemStockQuantity = 0;
        String itemCategory = category;
        String itemSupplierID;
        
        Scanner Sc = new Scanner(System.in);
        
        Outer:
        while(true){
            System.out.println("======== Add New Item ========");
            //ask user to enter item name
            System.out.print(String.format("Enter item Name:"));
            itemName = Sc.nextLine();
            boolean itemNameValid = validateItemName(itemName);
            if(!itemNameValid){
                continue;
            }
            
            //Ask user to enter item price
            try{
                System.out.print(String.format("Enter item unit price:"));
                itemUnitPrice = Sc.nextFloat();
                
                // Consume newline left-over
                Sc.nextLine();
            }
            catch(InputMismatchException ie){
                // Clear invalid input left-over
                Sc.nextLine();
                System.out.println("Invalid input for price. Please enter a valid numerical value for the price (e.g., 10.99).");
                System.out.println("Press [Enter] to continue...");
                
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                continue;
            }
            
            //ask user to enter item supplier
            SupplierManagement supManage = new SupplierManagement();
            ArrayList<String[]>supplierList = supManage.displaySupplier();
            
            System.out.println("Please enter the supplier for the item (enter '-' if there is currently no supplier.)");
            System.out.print("Enter Supplier ID:");
            itemSupplierID = Sc.nextLine();

            
            
            if(itemSupplierID != "-"){
                boolean suppIDFound = false;
                for(String [] i : supplierList){
                    if(i[0].equals(itemSupplierID)){
                        suppIDFound = true;
                    }
                }
                if(!suppIDFound){
                    System.out.println("Invalid supplier ID. Please enter a valid supplier ID from the list or '-' to indicate no supplier.");
                    System.out.println("Press [Enter] to continue...");
                    Sc.nextLine();
                    continue;
                }
            }
            
            Item newItem = new Item(itemName, itemUnitPrice, itemStockQuantity, itemCategory, itemSupplierID);
            registerItemToTextFile(newItem);
        }
        
    }
    
    private void registerItemToTextFile(Item item){
        Scanner Sc = new Scanner(System.in);
        InventoryDatabase invDB = new InventoryDatabase();

        boolean isDuplicate = invDB.isDuplicateName(item.getItemName(), InventoryDatabase.files.ITEM.getFile());
        
        if(isDuplicate){
            System.out.println(System.lineSeparator().repeat(50));
            System.out.println("Error: Item name already exists.");
            System.out.println("Press [Enter] to continue...");
            Sc.nextLine();
            return;
        }
        
        String itemID = "IT" + invDB.generateIDIndex(InventoryDatabase.files.ITEM.getFile());
        
        String [] itemInfo = {itemID, item.getItemName(), String.format("%.2f", item.getItemUnitPrice()),
            Integer.toString(item.getItemStockQuantity()), item.getItemCategory(), item.getItemSupplierID()};
        
        invDB.appendToTextFile(itemInfo, InventoryDatabase.files.ITEM.getFile());
        
        System.out.println(System.lineSeparator().repeat(50));
        System.out.println("Item registration successful.");
        System.out.println("Press [Enter] to continue...");
        Sc.nextLine();
    }
    
    
    
    
    private boolean validateItemName(String itemName){
        if(!itemName.matches("^(?=.*[a-zA-Z])[a-zA-Z0-9 ]{2,50}$")){
            Scanner sc = new Scanner(System.in);
            if(itemName.length() < 3 || itemName.length() > 50){
                System.out.println("Invalid Item name length. Please ensure Item name has between 2 and 50 characters.");
            }
            else{
                System.out.println("Invalid item name format. Please use only letters and digits, and ensure it contains at least one letter.");
            }
            System.out.println("Press [Enter] to continue...");
            sc.nextLine();
            System.out.println(System.lineSeparator().repeat(50));
            return false;
        }
        return true;
    }
    
    private void deleteItem(String category){
        Scanner Sc = new Scanner(System.in);
        Outer:
        while(true){
            ArrayList<String[]> itemList = displayItemListByCategory(category);

            System.out.print("\nEnter the item ID to delete or [BACK] to return:");
            String itemToDelete = Sc.nextLine().toUpperCase();
            if(itemToDelete.equals("BACK")){
                System.out.println(System.lineSeparator().repeat(50));
                break Outer;
            }
            
            boolean idFound = false;
            for(String[] item : itemList){
                String itemID = item[0];
                if(itemID.equals(itemToDelete)){
                    idFound = true;
                    break;
                }
            }
            
            if(!idFound){
                System.out.println(System.lineSeparator().repeat(50));
                System.out.println("Please enter a valid Item ID");
                System.out.println("Press [Enter] to continue...");
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                continue;
            }
            
            while(true){
                System.out.println("Warning: Are you sure you want to delete the item?");
                System.out.println("This action cannot be undone. Are you sure you want to proceed?");
                System.out.print("1. Delete\n2. Cancel\nPlease enter your choice:");
                String choice = Sc.nextLine();
                if(choice.equals("1")){
                    Item item = new Item(itemToDelete);
                    deleteItemFromTF(item);
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
        
    private void deleteItemFromTF(Item itemToDelete){
       String itemID = itemToDelete.getItemID();
        
        InventoryDatabase invDB = new InventoryDatabase();
        ArrayList<String[]> itemList = invDB.getAllData(InventoryDatabase.files.ITEM.getFile());
        
        ArrayList<String[]> updatedItemList = new ArrayList<>();
        for(String[] item: itemList){
            if(item[0].equals(itemID)){
                continue;
            }
            updatedItemList.add(item);
        }
        invDB.writeToTextFile(updatedItemList.get(0), InventoryDatabase.files.ITEM.getFile());
        
        for(int i = 1; i < updatedItemList.size(); i++){
            invDB.appendToTextFile(updatedItemList.get(i), InventoryDatabase.files.ITEM.getFile());
        }

    }     
    
    private void editItem(){
        
    }
   
   
    
    public void displayItemList(){
        ArrayList<String[]> itemList = getItemList();
        if(itemList == null){
            System.out.println("No item found.");
            return;
        }
        
        System.out.println("=============================================Item List=============================================");
        System.out.printf("%-10s%-23s%-15s%-20s%-20s%s%n", "Item ID", "Item Name","Unit Price", "Stock Quantity", "Category", "Supplied By");
        System.out.println("===================================================================================================");
        
        for (String[] item : itemList) {
            System.out.printf("%-10s%-23s%-15s%-20s%-20s%s%n", item[0], item[1], "RM"+item[2], item[3], item[4], item[5]);
        }
        System.out.println("===================================================================================================\n");
       
    }
    
    
    public ArrayList<String []> displayItemListByCategory(String category){
        ArrayList<String[]> itemList = getItemList();
        if(itemList == null){
            System.out.println("No item found.");
            return null;
        }
        
        
        System.out.println("=============================================Item List=============================================");
        System.out.printf("%-10s%-23s%-15s%-20s%-20s%s%n", "Item ID", "Item Name","Unit Price", "Stock Quantity", "Category", "Supplied By");
        System.out.println("===================================================================================================");
        
        ArrayList<String[]> itemListByCategory = new ArrayList();
        for (String[] item : itemList) {
            if(item[4].equals(category)){
                System.out.printf("%-10s%-23s%-15s%-20s%-20s%s%n", item[0], item[1], "RM"+item[2], item[3], item[4], item[5]);
                itemListByCategory.add(item);
            }
        }
        System.out.println("===================================================================================================\n");
       
        return itemListByCategory;
    }
    
    
    private ArrayList<String []> getItemList(){
        InventoryDatabase invDB = new InventoryDatabase();
        ArrayList<String []> itemList = invDB.getAllData(InventoryDatabase.files.ITEM.getFile());
        
        return itemList;
        
    }
    
}