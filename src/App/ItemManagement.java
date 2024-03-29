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
                    System.out.println(System.lineSeparator().repeat(50));
                    addItem(category);
                    break;
                case "2":
                    category = selectItemCategory();
                    System.out.println(System.lineSeparator().repeat(50));
                    deleteItem(category);
                    break;
                case "3":
                    category = selectItemCategory();
                    System.out.println(System.lineSeparator().repeat(50));
                    editItem(category);
                    break;
                case "4":
                    displayItemList();
                    System.out.println("Press [Enter] to go back");
                    Sc.nextLine();
                    System.out.println(System.lineSeparator().repeat(50));
                    break;
                case "5":
                    category = selectItemCategory();
                    System.out.println(System.lineSeparator().repeat(50));
                    displayItemListByCategory(category);
                    System.out.println("Press [Enter] to go back");
                    Sc.nextLine();
                    System.out.println(System.lineSeparator().repeat(50));
                    break;
                default:
                    System.out.println(System.lineSeparator().repeat(50));
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println("Press [Enter] to continue");
                    Sc.nextLine();
                    break;
                case "0":
                    System.out.println(System.lineSeparator().repeat(50));
                    break Outer;
            }
        }
    }
    
    public String selectItemCategory(){
        System.out.println(System.lineSeparator().repeat(50));
        Scanner Sc = new Scanner(System.in);
        while(true){
            System.out.println("======== Category ========");
            System.out.println(String.format("%-2s %-1s", "1.", "Everyday Groceries"));
            System.out.println(String.format("%-2s %-1s", "2.", "Fresh Produce"));
            System.out.println(String.format("%-2s %-1s", "3.", "Fresh Food"));
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
                    App.displayMessage("Invalid choice. Please try again.");   
                    break;
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
            System.out.print("Enter item Name:");
            itemName = Sc.nextLine();
            boolean itemNameValid = validateItemName(itemName);
            if(!itemNameValid){

                continue;
            }
            //Ask user to enter item price
            try{
                System.out.print("Enter item unit price:");
                itemUnitPrice = Sc.nextFloat();
                // Consume newline left-over
                Sc.nextLine();
                
                if(itemUnitPrice <= 0){
                    App.displayMessage("Price cannot be less than or equal to 0");
                    continue;
                }
            }
            catch(InputMismatchException ie){
                // Clear invalid input left-over
                Sc.nextLine();
                App.displayMessage("Invalid input for price. Please enter a valid numerical value for the price (e.g., 10.99).");
                continue;
            }
            System.out.println(System.lineSeparator().repeat(50));
            //ask user to enter item supplier
            SupplierManagement supManage = new SupplierManagement();
            ArrayList<String[]>supplierList = supManage.displaySupplier();
            
            System.out.println("Please enter the supplier for the item (enter '-' if there is currently no supplier.)");
            System.out.print("Enter Supplier ID:");
            itemSupplierID = Sc.nextLine().toUpperCase();

            
            // if user enter "-" means the item have no supplier
            if(!itemSupplierID.equals("-")){
                boolean suppIDFound = false;
                for(String [] i : supplierList){
                    if(i[0].equals(itemSupplierID)){
                        suppIDFound = true;
                    }
                }
                if(!suppIDFound){
                    App.displayMessage("Invalid supplier ID. Please enter a valid supplier ID from the list or '-' to indicate no supplier.");
                    continue;
                }
            }
            
            
            Item newItem = new Item(itemName, itemUnitPrice, itemStockQuantity, itemCategory, itemSupplierID);
            registerItemToTextFile(newItem);
            break;
        }
        
    }
    
    private void registerItemToTextFile(Item item){
        Scanner Sc = new Scanner(System.in);
        InventoryDatabase invDB = new InventoryDatabase();
        
        boolean isDuplicate = invDB.isDuplicateName(item.getItemName(), InventoryDatabase.files.ITEM.getFile());
        //check whether item name exist.
        if(isDuplicate){
            
            App.displayMessage("Error: Item name already exists.");
            return;
        }
        
        String itemID = "IT" + invDB.generateIDIndex(InventoryDatabase.files.ITEM.getFile(), InventoryDatabase.files.USED_ITEM_ID_INDEX.getFile());
        
        String [] itemInfo = {itemID, item.getItemName(), String.format("%.2f", item.getItemUnitPrice()),
            Integer.toString(item.getItemStockQuantity()), item.getItemCategory(), item.getItemSupplierID()};
        
        invDB.appendToTextFile(itemInfo, InventoryDatabase.files.ITEM.getFile());
        App.displayMessage("Item registration successful.");
        
    }
    
    
    
    
    private boolean validateItemName(String itemName){
        if(!itemName.matches("^(?=.*[a-zA-Z])[a-zA-Z0-9 ]{2,50}$")){
            Scanner sc = new Scanner(System.in);
            if(itemName.length() < 3 || itemName.length() > 50){
                App.displayMessage("Invalid Item name length. Please ensure Item name has between 2 and 50 characters.");
            }
            else{
                App.displayMessage("Invalid item name format. Please use only letters and digits, and ensure it contains at least one letter.");
            }
            return false;
        }
        return true;
    }
    
    private void deleteItem(String category){
        Scanner Sc = new Scanner(System.in);
        Outer:
        while(true){
            ArrayList<String[]> itemList = displayItemListByCategory(category);
            if(itemList == null){
                System.out.println("Press [Enter] to continue.");
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                break Outer;
            }
            //ask user to enter item id from the list
            System.out.print("\nEnter the item ID to delete or [BACK] to return:");
            String itemToDelete = Sc.nextLine().toUpperCase();
            
            if(itemToDelete.equals("BACK")){
                System.out.println(System.lineSeparator().repeat(50));
                break Outer;
            }
            
            boolean idFound = false;
            //check whether user enter item id from list
            for(String[] item : itemList){
                String itemID = item[0];
                if(itemID.equals(itemToDelete)){
                    idFound = true;
                    break;
                }
            }
            
            if(!idFound){
                App.displayMessage("Please enter a valid Item ID");
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
                    System.out.println("Press [Enter] to continue.");
                    Sc.nextLine();
                    System.out.println(System.lineSeparator().repeat(50));
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
    }
        
    private void deleteItemFromTF(Item itemToDelete){
        String itemID = itemToDelete.getItemID();
        InventoryDatabase invDB = new InventoryDatabase();
        ArrayList<String []> prList = invDB.getAllData(InventoryDatabase.files.PURCHASE_REQUISITION.getFile());
        ArrayList<String []> poList = invDB.getAllData(InventoryDatabase.files.PURCHASE_ORDER.getFile());
        for(String[] pr: prList){
            if(pr[1].equals(itemID)){
                if(pr[9].equals("Pending") || pr[9].equals("Approved")){
                    System.out.println(System.lineSeparator().repeat(50));
                    System.out.println("Item still have ongoing PR unable to delete Item.");
                    return;
                }
            }
        }
        for(String[] po: poList){
            if(po[1].equals(itemID)){
                if(po[10].equals("Pending") || po[10].equals("Issued")){
                    System.out.println(System.lineSeparator().repeat(50));
                    System.out.println("Item still have ongoing PO unable to delete Item.");
                    return;
                }
            }
        }
        
        ArrayList<String[]> itemList = invDB.getAllData(InventoryDatabase.files.ITEM.getFile());

        //remove item from arraylist
        ArrayList<String[]> updatedItemList = new ArrayList<>();
        for(String[] item: itemList){
            if(item[0].equals(itemID)){
                continue;
            }
            updatedItemList.add(item);
        }
        //write updated item list to item text file
        if(updatedItemList.isEmpty()){
            invDB.clearFile( InventoryDatabase.files.ITEM.getFile());
        }
        else{
            invDB.writeToTextFile(updatedItemList.get(0), InventoryDatabase.files.ITEM.getFile());
            for(int i = 1; i < updatedItemList.size(); i++){
                invDB.appendToTextFile(updatedItemList.get(i), InventoryDatabase.files.ITEM.getFile());
            }
        }
        
        System.out.println(System.lineSeparator().repeat(50));
        System.out.println("Item delete successfully.");
        

    }     
    
    private void editItem(String category){
        Scanner Sc = new Scanner(System.in);
        Item item = new Item(null, null, 0, 0, null, null);
        
        Outer:
        while(true){
            ArrayList<String[]> itemList = displayItemListByCategory(category);
            if(itemList == null){
                System.out.println("Press [Enter] to continue.");
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                break Outer;
            }
            //ask user to enter item id from the list
            System.out.print("\nEnter the item ID to edit or [BACK] to return:");
            String itemToEdit = Sc.nextLine().toUpperCase();
            if(itemToEdit.equals("BACK")){
                System.out.println(System.lineSeparator().repeat(50));
                break Outer;
            }
            
            boolean idFound = false;
            //check whether user enter item id from list
            for(String[] i : itemList){
                String itemID = i[0];
                if(itemID.equals(itemToEdit)){
                    item = new Item(i[0], i[1], Float.parseFloat(i[2]), Integer.parseInt(i[3]), i[4], i[5]);
                    idFound = true;
                    break;
                }
            }
            
            if(!idFound){
                App.displayMessage("Please enter a valid item ID");
                continue;
            }
            
            selectItemAttributeAndEdit(item);
        }
    }
    
    private void selectItemAttributeAndEdit(Item item){
        Scanner Sc = new Scanner(System.in);
        String[] item_supp = item.getItemSupplierID().strip().split("\\|");
        Outer:
        while(true){
            System.out.println(System.lineSeparator().repeat(50));
            System.out.println("===============================================Item================================================");
            System.out.printf("%-10s%-23s%-15s%-20s%-20s%s%n", "Item ID", "Item Name","Unit Price", "Stock Quantity", "Category", "Supplied By");
            System.out.println("===================================================================================================");
            System.out.printf("%-10s%-23s%-15s%-20s%-20s%s%n", item.getItemID(), item.getItemName(),
                    "RM"+item.getItemUnitPrice(), item.getItemStockQuantity(), item.getItemCategory(), item_supp[0]);
            if(item_supp.length > 1){
                int i = 1;
                while(i < item_supp.length){
                    System.out.printf("%-10s%-23s%-15s%-20s%-20s%s%n", "", "","", "", "", item_supp[i]);
                    i++;
                } 
            }
            
            System.out.println("===================================================================================================\n");
            //ask user to select attribute to be edit.
            System.out.println("Choose an attribute to edit:");
            System.out.print("1. Item Name\n2. Item Unit Price\n3. Item Category\n4. Item Supplier\n0. Back\nPlease enter your choice:");
            String choice = Sc.nextLine();
            if(choice.equals("1")){
                while(true){
                    System.out.print("Enter item new name:");
                    String itemName = Sc.nextLine();
                    boolean nameValid = validateItemName(itemName);
                    if(!nameValid){
                        continue;
                    }
                    item.setItemName(itemName);
                    updateEditItem(item);
                    App.displayMessage("Item detail updated successfully");
                    break;
                }
            }
            else if(choice.equals("2")){
                while(true){
                    try{
                        System.out.print("Enter item new unit price:");
                        float itemUnitPrice = Sc.nextFloat();
                        
                        // Consume newline left-over
                        Sc.nextLine();
                        
                        
                        if(itemUnitPrice <= 0){
                            App.displayMessage("Item price cannot be equal or less than 0.");
                            break Outer;
                        }
                        item.setItemUnitPrice(itemUnitPrice);
                        updateEditItem(item);
                        App.displayMessage("Item detail updated successfully");
                    }
                    catch(InputMismatchException ie){
                        // Clear invalid input left-over
                        Sc.nextLine();
                        App.displayMessage("Invalid input for price. Please enter a valid numerical value for the price (e.g., 10.99).");
                        continue;
                    }
                    break;
                }
            }
            else if(choice.equals("3")){
                while(true){
                    String itemCategory = selectItemCategory();
                    item.setItemCategory(itemCategory);
                    updateEditItem(item);
                    App.displayMessage("Item detail updated successfully");
                    break;
                }
            }
            else if(choice.equals("4")){
                editSupplier(item, item_supp);
                break;
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
    
    private void editSupplier(Item item, String[] item_supp){
        Scanner Sc = new Scanner(System.in);
        SupplierManagement suppManage = new SupplierManagement();
        ArrayList<String []> supplierList = new ArrayList();
        Outer:
        while(true){
            System.out.print("Would you like to\n1.Add Supplier\n2.Remove Supplier\nEnter your choice:");
            String choice = Sc.nextLine();
            if(choice.equals("1")){
                System.out.println(System.lineSeparator().repeat(50));
                supplierList = suppManage.displaySupplier();
                System.out.print("Enter supplier ID to add:");
                String itemSupplierID = Sc.nextLine();
                
                for(String sup: item_supp){
                    if(itemSupplierID.equals(sup)){
                        App.displayMessage("Enter Supplier already supplying this item.");
                        break Outer;
                    }
                }

                boolean idFound = false;
                for(String[] supplier : supplierList){
                    if(supplier[0].equals(itemSupplierID)){
                        idFound = true;
                    }
                }
                if(!idFound){
                    continue;
                }
                String itemNewSupplier = "";
                for(String sup:item_supp){
                    if(sup.equals("-")){
                        break;
                    }
                    itemNewSupplier += sup + "|";
                }
                itemNewSupplier += itemSupplierID;
                
                item.setItemSupplier(itemNewSupplier);
                updateEditItem(item);
                App.displayMessage("Item detail updated successfully");
                break;
            }
            else if(choice.equals("2")){
                if(item_supp[0].equals("-")){
                    System.out.println("no supplier to delete");
                    break;
                }
                System.out.println("=============================================Item List=============================================");
                System.out.printf("%-10s%-23s%-15s%-20s%-20s%s%n", "Item ID", "Item Name","Unit Price", "Stock Quantity", "Category", "Supplied By");
                System.out.println("===================================================================================================");
                System.out.printf("%-10s%-23s%-15s%-20s%-20s%s%n", item.getItemID(), item.getItemName(),
                        "RM"+item.getItemUnitPrice(), item.getItemStockQuantity(), item.getItemCategory(), item_supp[0]);
                if(item_supp.length > 1){
                    int i = 1;
                    while(i < item_supp.length){
                        System.out.printf("%-10s%-23s%-15s%-20s%-20s%s%n", "", "","", "", "", item_supp[i]);
                        i++;
                    } 
                }
                System.out.println("===================================================================================================\n");
                System.out.println("Enter supplier ID to remove:");
                String supplierID = Sc.nextLine();
                boolean idFound = false;
                for(String sup : item_supp){
                    if(sup.equals(supplierID)){
                        idFound = true;
                    }
                }
                if(!idFound){
                    continue;
                }
                String itemNewSupplier = "";
                for(String sup:item_supp){
                    if(sup.equals(supplierID)){
                        continue;
                    }
                    itemNewSupplier += sup;
                    if (!sup.equals(item_supp[item_supp.length - 1])) {
                        itemNewSupplier += "|";
                    }
                }
                if(itemNewSupplier.isEmpty()){
                    itemNewSupplier = "-";
                }
                
                item.setItemSupplier(itemNewSupplier);
                updateEditItem(item);
                App.displayMessage("Item detail updated successfully");
                break;
            }
        }
    }
    
    
    private void updateEditItem(Item item){
        InventoryDatabase invDB = new InventoryDatabase();
        ArrayList<String []> itemList = invDB.getAllData(InventoryDatabase.files.ITEM.getFile());
        
        //update item info
        for(String[] i : itemList){
            if(i[0].equals(item.getItemID())){
                i[1] = item.getItemName();
                i[2] = String.format("%.2f", item.getItemUnitPrice());
                i[4] = item.getItemCategory();
                i[5] = item.getItemSupplierID();
                break;
            }
        }
        //update item new info to item textfile
        invDB.writeToTextFile(itemList.get(0), InventoryDatabase.files.ITEM.getFile());
        for(int i = 1; i < itemList.size(); i++){
            invDB.appendToTextFile(itemList.get(i), InventoryDatabase.files.ITEM.getFile());
        }
    }
    
   
   
    
    public void displayItemList(){
        ArrayList<String[]> itemList = getItemList();
        if(itemList == null){
            System.out.println("No item found.");
            return;
        }
        System.out.println(System.lineSeparator().repeat(50));
        System.out.println("=============================================Item List=============================================");
        System.out.printf("%-10s%-23s%-15s%-20s%-20s%s%n", "Item ID", "Item Name","Unit Price", "Stock Quantity", "Category", "Supplied By");
        System.out.println("===================================================================================================");
        
        for (String[] item : itemList) {
            String[] itemSupp = item[5].strip().split("\\|");
            System.out.printf("%-10s%-23s%-15s%-20s%-20s%s%n", item[0], item[1], "RM"+item[2], item[3], item[4], itemSupp[0]);
            if(itemSupp.length > 1){
                int i = 1;
                while(i < itemSupp.length){
                    System.out.printf("%-10s%-23s%-15s%-20s%-20s%s%n", "", "","", "", "", itemSupp[i]);
                    i++;
                } 
            } 
        }
        System.out.println("===================================================================================================\n");
       
    }
    
    
    public ArrayList<String []> displayItemListByCategory(String category){
        ArrayList<String[]> itemList = getItemList();
        if(itemList == null){
            System.out.println("No item found.");
            return null;
        }
        
        System.out.println(System.lineSeparator().repeat(50));
        System.out.println("=============================================Item List=============================================");
        System.out.printf("%-10s%-23s%-15s%-20s%-20s%s%n", "Item ID", "Item Name","Unit Price", "Stock Quantity", "Category", "Supplied By");
        System.out.println("===================================================================================================");
        
        ArrayList<String[]> itemListByCategory = new ArrayList();
        for (String[] item : itemList) {
            if(item[4].equals(category)){
                String[] itemSupp = item[5].strip().split("\\|");
                System.out.printf("%-10s%-23s%-15s%-20s%-20s%s%n", item[0], item[1], "RM"+item[2], item[3], item[4], itemSupp[0]);
                if(itemSupp.length > 1){
                int i = 1;
                while(i < itemSupp.length){
                    System.out.printf("%-10s%-23s%-15s%-20s%-20s%s%n", "", "","", "", "", itemSupp[i]);
                    i++;
                } 
            } 
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
