/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author verno
 */
public class SalesRecordManagement {
    
    public void OpenPage(){
        Scanner Sc = new Scanner(System.in);
        
        Outer:
        while(true){
            System.out.println("======== Sales Record Management Submenu ========");
            System.out.println(String.format("%-2s %-1s", "1.", "Add Sales Record"));
            System.out.println(String.format("%-2s %-1s", "2.", "Delete Sales Record"));
            System.out.println(String.format("%-2s %-1s", "3.", "Edit Sales Record"));
            System.out.println(String.format("%-2s %-1s", "4.", "View Sales Record"));
            System.out.println(String.format("%-2s %-1s", "0.", "Exit"));
            System.out.print("Enter your choice:");

            String option = Sc.nextLine();
            switch(option){
                case "1":
                    System.out.println(System.lineSeparator().repeat(50));
                    createSalesRecord();
                    break;
                case "2":
                    System.out.println(System.lineSeparator().repeat(50));
                    deleteSalesRecord();
                    break;
                case "3":
                    System.out.println(System.lineSeparator().repeat(50));
                    editSalesRecord();
                    break;
                case "4":
                    displaySalesRecord();
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
    
    private void createSalesRecord(){
        String itemID;
        int quantitySold;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDate.now().format(formatter);
        
        Scanner Sc = new Scanner(System.in);
        InventoryDatabase invDB = new InventoryDatabase();
       
        Outer:
        while(true){
            //ask user to enter item name or id
            System.out.println("======== Add Sales Record ========");
            System.out.print("Enter Item ID:");
            itemID = Sc.nextLine().toUpperCase();
            ArrayList<String[]> itemList = invDB.getAllData(InventoryDatabase.files.ITEM.getFile());
            ArrayList<String> itemInfo = new ArrayList();
            boolean idFound = false;
            for(String[] item:itemList){
                if(item[0].equals(itemID)){
                    idFound = true;
                    itemInfo.addAll(Arrays.asList(item[0], item[1], item[2], item[3]));
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
            
            System.out.println("===============Item Information ===============");
            System.out.println("Item ID             : " + itemInfo.get(0));
            System.out.println("Item Name           : " + itemInfo.get(1));
            System.out.println("Item Unit Price     : " + itemInfo.get(2));
            System.out.println("Available Quantity  : " + itemInfo.get(3));
            System.out.println("===============================================");
            
            try{
                System.out.print("Enter item sold quantity:");
                quantitySold = Sc.nextInt();
                // Consume newline left-over
                Sc.nextLine();
            }
            catch(InputMismatchException ie){
                // Clear invalid input left-over
                Sc.nextLine();
                System.out.println("Invalid input for quantity sold. Please enter a valid integer value for the quantity.");
                System.out.println("Press [Enter] to continue...");
                
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                continue;
            }
            
            if(quantitySold > Integer.parseInt(itemInfo.get(3))){
                System.out.println(System.lineSeparator().repeat(50));
                System.out.println("Insufficient Stock!");
                System.out.println("The quantity you entered is larger than the available stock for this item.");
                System.out.println("Press [Enter] to continue.");
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                continue;
            }
            else if(quantitySold <= 0){
                System.out.println(System.lineSeparator().repeat(50));
                System.out.println("Invalid input");
                System.out.println("The quantity you entered is equal or less zero.");
                System.out.println("Press [Enter] to continue.");
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                continue;
            }
            
            double markup_rate = 0;
            String sold_unit_price = "0";
            try{
                System.out.print("Enter item markup rate:");
                markup_rate = Sc.nextDouble();
                // Consume newline left-over
                Sc.nextLine();
                
                if(markup_rate <= 0){
                    System.out.println(System.lineSeparator().repeat(50));
                    System.out.println("Invalid input");
                    System.out.println("The markup you entered is equal or less zero.");
                    System.out.println("Press [Enter] to continue.");
                    Sc.nextLine();
                    System.out.println(System.lineSeparator().repeat(50));
                    continue;
                }
                
                sold_unit_price = String.format("%.2f",Double.parseDouble(itemInfo.get(2))*markup_rate);
            }
            catch(InputMismatchException ie){
                // Clear invalid input left-over
                Sc.nextLine();
                System.out.println("Invalid input for markup rate. Please enter a valid value for the markup rate.");
                System.out.println("Press [Enter] to continue...");
                
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                continue;
            }
            
            
            while(true){
                System.out.println(System.lineSeparator().repeat(50));
                System.out.println("===Sales Record Detail.===\nItem ID : "+itemInfo.get(0)+"\nItem Name : "+ itemInfo.get(1)+"\nSold unit Price : "+sold_unit_price+"\nQuantity Sold : "+quantitySold);
                System.out.println("Do you want to save this sales record?");
                System.out.println("1. Save record");
                System.out.println("2. No");
                System.out.print("Enter your choice:");
                String choice = Sc.nextLine();
                if(choice.equals("1")){
                    //add item to record
                    String[] itemSold = {itemInfo.get(0), itemInfo.get(1), sold_unit_price,Integer.toString(quantitySold)};
                    SalesRecord sr = new SalesRecord(date, itemSold);
                    saveSalesRecordToTF(sr);
                    break Outer;
                }
                else if(choice.equals("2")){
                    break Outer;
                }
                else{
                    System.out.println(System.lineSeparator().repeat(50));
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println("Press [Enter] to continue.");
                    Sc.nextLine();
                    System.out.println(System.lineSeparator().repeat(50));
                    continue;
                }

            }
        }
    }
    
    private void saveSalesRecordToTF(SalesRecord sr){
        Scanner Sc = new Scanner(System.in);
        InventoryDatabase invDB = new InventoryDatabase();
        UserDatabase uDB = new UserDatabase();
        String[] itemSold = sr.getItemSold();
        
        String salesRecordID = "SR" + invDB.generateIDIndex(InventoryDatabase.files.SALES_RECORD.getFile(), InventoryDatabase.files.USED_SALES_RECORD_ID_INDEX.getFile());
        String recordKeeper = uDB.getCurrentUser();
        String [] salesRecord = {salesRecordID, itemSold[0], itemSold[1], itemSold[2], itemSold[3], sr.getDate(), recordKeeper};
        invDB.appendToTextFile(salesRecord, InventoryDatabase.files.SALES_RECORD.getFile());
        
        ArrayList<String[]> itemList = invDB.getAllData(InventoryDatabase.files.ITEM.getFile());
        for(String[] item : itemList){
            if(item[0].equals(itemSold[0])){
                item[3] = Integer.toString(Integer.parseInt(item[3])-Integer.parseInt(itemSold[3]));
                break;
            }
        }
        
        invDB.writeToTextFile(itemList.get(0), InventoryDatabase.files.ITEM.getFile());
        
        for(int i = 1; i < itemList.size(); i++){
            invDB.appendToTextFile(itemList.get(i), InventoryDatabase.files.ITEM.getFile());
        }
        
        
        System.out.println(System.lineSeparator().repeat(50));
        System.out.println("Sales record saved successfully.");
        System.out.println("Press [Enter] to continue...");
        Sc.nextLine();
    }
    
    
    
    private void deleteSalesRecord(){
        
        Scanner Sc = new Scanner(System.in);
        InventoryDatabase invDB = new InventoryDatabase();
        Outer:
        while(true){
            ArrayList<String[]> salesRecordList = invDB.getAllData(InventoryDatabase.files.SALES_RECORD.getFile());
            if(salesRecordList == null){
                System.out.println("No sales record.");
                System.out.println("Press [Enter] to continue.");
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                break Outer;
            }
            //ask user to enter sale record id from the list
            System.out.print("\nEnter the record ID to delete or [BACK] to return:");
            String recordToDelete = Sc.nextLine().toUpperCase();
            
            if(recordToDelete.equals("BACK")){
                System.out.println(System.lineSeparator().repeat(50));
                break Outer;
            }
            
            boolean idFound = false;
            //check whether user enter item id from list
            for(String[] record : salesRecordList){
                String recordID = record[0];
                if(recordID.equals(recordToDelete)){
                    idFound = true;
                    break;
                }
            }
            
            if(!idFound){
                System.out.println(System.lineSeparator().repeat(50));
                System.out.println("Please enter a valid sales record ID");
                System.out.println("Press [Enter] to continue...");
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                continue;
            }
            
            
            while(true){
                System.out.println("Warning: Are you sure you want to delete the sales record?");
                System.out.println("This action cannot be undone. Are you sure you want to proceed?");
                System.out.print("1. Delete\n2. Cancel\nPlease enter your choice:");
                String choice = Sc.nextLine();
                if(choice.equals("1")){
                    SalesRecord sr= new SalesRecord(recordToDelete);
                    deleteSalesRecordFromTF(sr);
                    System.out.println(System.lineSeparator().repeat(50));
                    break;
                }
                else if(choice.equals("2")){
                    System.out.println(System.lineSeparator().repeat(50));
                    break Outer;
                }
                else{
                    System.out.println(System.lineSeparator().repeat(50));
                    System.out.println("Invalid choice. Try again.");
                    System.out.println("Press [Enter] to continue.");
                    Sc.nextLine();
                    System.out.println(System.lineSeparator().repeat(50));
                    continue;
                }
            }
        }
    }
    
    
    private void deleteSalesRecordFromTF(SalesRecord sr){
        String recordID = sr.getSalesRecordID();
        LocalDate currentDate = LocalDate.now();
        InventoryDatabase invDB = new InventoryDatabase();
        ArrayList<String[]> salesRecordList = invDB.getAllData(InventoryDatabase.files.SALES_RECORD.getFile());
        ArrayList<String[]> itemList = invDB.getAllData(InventoryDatabase.files.ITEM.getFile());
        
        //remove item from arraylist
        
        ArrayList<String[]> updatedRecordList = new ArrayList<>();
        for(String[] record: salesRecordList){
            if(record[0].equals(recordID)){
                long daysDifference = currentDate.toEpochDay() - LocalDate.parse(record[5], DateTimeFormatter.ISO_LOCAL_DATE).toEpochDay();
                if(daysDifference > 3){
                    System.out.println("The date is more than 3 days old.");
                    return;
                }
                else{
                    if(itemList != null){
                        for(String[] item: itemList){
                            if(record[1].equals(item[0])){
                                item[3] = Integer.toString(Integer.parseInt(record[4])+Integer.parseInt(item[3]));
                                break;
                            }
                        }
                    }
                }
                continue;
            }
            updatedRecordList.add(record);
        }
        
        //write updated item list to item text file
        if(updatedRecordList.isEmpty()){
            invDB.clearFile(InventoryDatabase.files.SALES_RECORD.getFile());
        }
        else{
            invDB.writeToTextFile(updatedRecordList.get(0), InventoryDatabase.files.SALES_RECORD.getFile());
            for(int i = 1; i < updatedRecordList.size(); i++){
                invDB.appendToTextFile(updatedRecordList.get(i), InventoryDatabase.files.SALES_RECORD.getFile());
            }
        }
        
        if(itemList != null){
            invDB.writeToTextFile(itemList.get(0), InventoryDatabase.files.ITEM.getFile());

            for(int i = 1; i < itemList.size(); i++){
                invDB.appendToTextFile(itemList.get(i), InventoryDatabase.files.ITEM.getFile());
            }
        }
    }
    
    
    private void editSalesRecord(){
        Scanner Sc = new Scanner(System.in);
        InventoryDatabase invDB = new InventoryDatabase();
        SalesRecord sr = new SalesRecord(null, null, null, null);
        
        Outer:
        while(true){
            ArrayList<String[]> salesRecordList = invDB.getAllData(InventoryDatabase.files.SALES_RECORD.getFile());
            if(salesRecordList == null){
                System.out.println("No record found.");
                System.out.println("Press [Enter] to continue.");
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                break Outer;
            }
            
            //ask user to enter item id from the list
            System.out.print("\nEnter the sales record ID to edit or [BACK] to return:");
            String recordToEdit = Sc.nextLine().toUpperCase();
            if(recordToEdit.equals("BACK")){
                System.out.println(System.lineSeparator().repeat(50));
                break Outer;
            }
            
            boolean idFound = false;
            //check whether user enter item id from list
            for(String[] record : salesRecordList){
                String recordID = record[0];
                if(recordID.equals(recordToEdit)){
                    String[] itemSold = {record[1], record[2], record[3], record[4]};
                    sr = new SalesRecord(record[0], record[5],record[6], itemSold);
                    idFound = true;
                    break;
                }
            }
            LocalDate currentDate = LocalDate.now();
            long daysDifference = currentDate.toEpochDay() - LocalDate.parse(sr.getDate(), DateTimeFormatter.ISO_LOCAL_DATE).toEpochDay();
            if(daysDifference > 3){
                System.out.println(System.lineSeparator().repeat(50));
                System.out.println("The date is more than 3 days old.");
                System.out.println("Press [Enter] to continue...");
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                continue;
            }
            
            if(!idFound){
                System.out.println(System.lineSeparator().repeat(50));
                System.out.println("Please enter a valid sales record ID");
                System.out.println("Press [Enter] to continue...");
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                continue;
            }
            editQuantitySold(sr);
            
        }
    }
    
    
    private void editQuantitySold(SalesRecord sr){
        Scanner Sc = new Scanner(System.in);
        InventoryDatabase invDB = new InventoryDatabase();
        ArrayList<String[]> itemList = invDB.getAllData(InventoryDatabase.files.ITEM.getFile());
        int itemQuantityInStock = 0;
        boolean itemFound = false;
        for(String [] item : itemList){
            if(item[0].equals(sr.getItemSoldID())){
                itemQuantityInStock = Integer.parseInt(item[3]);
                itemFound = true;
            }
        }
        if(!itemFound){
            //do not allow edit of sales record if the item already been deleted.
            System.out.println(System.lineSeparator().repeat(50));
            System.out.println("Error: Item Not Found");
            System.out.println("The item associated with this sales record has been deleted from the database");
            System.out.println("Editing the quantity sold is not possible.");
            System.out.println("Press [Enter] to continue.");
            Sc.nextLine();
            System.out.println(System.lineSeparator().repeat(50));
            return;
        }
        
        Outer:
        while(true){
            System.out.println("====================== SALES RECORD ======================");
            System.out.println("Sales Record Id : "+ sr.getSalesRecordID());
            System.out.println("Date : "+sr.getSalesRecordDate());
            System.out.println("Record Keeper Id : "+ sr.getRecordKeeperID());
            System.out.println("==========================================================");
            System.out.printf("%-10s%-23s%-15s%-20s%s%n", "Item ID", "Item Name","Unit Price", "Quantity Sold", "Total");
            System.out.println("===================================================================================================");
            System.out.printf("%-10s%-23s%-15s%-20s%s%n", sr.getItemSoldID(), sr.getItemSoldName(),
                    "RM"+sr.getItemSoldPrice(), sr.getItemSoldQuantity(), "RM"+sr.getSalesTotal());
            
            System.out.println("===================================================================================================\n");
            //ask user to select attribute to be edit.
            
            
            try{
                System.out.print("Enter new quantity sold:");
                int quantitySold = Sc.nextInt();
                // Consume newline left-over
                Sc.nextLine();
                if(quantitySold <= 0){
                    System.out.println(System.lineSeparator().repeat(50));
                    System.out.println("Invalid input");
                    System.out.println("The quantity you entered is equal or less zero.");
                    System.out.println("Press [Enter] to continue.");
                    Sc.nextLine();
                    System.out.println(System.lineSeparator().repeat(50));
                    continue;
                }
                
                
                int QuantityInRecord = Integer.parseInt(sr.getItemSoldQuantity());
                
                sr.setItemSoldQuantity(Integer.toString(quantitySold));
                boolean updateSuccessful = updateEditSalesRecord(sr, itemQuantityInStock, QuantityInRecord);
                if(updateSuccessful){
                    System.out.println(System.lineSeparator().repeat(50));
                    System.out.println("Sales Record updated successfully");
                    System.out.println("Press [Enter] to continue...");
                    Sc.nextLine();
                    System.out.println(System.lineSeparator().repeat(50));
                    break;
                }
                else{
                    System.out.println(System.lineSeparator().repeat(50));
                    System.out.println("Insufficient Stock!");
                    System.out.println("The quantity you entered is larger than the available stock for this item.");
                    System.out.println("Press [Enter] to continue.");
                    //Sc.nextLine();
                    System.out.println(System.lineSeparator().repeat(50));
                    break;
                }
                

            }
            catch(InputMismatchException ie){
                // Clear invalid input left-over
                Sc.nextLine();
                System.out.println("Invalid input for quantity. Please enter a valid numerical value.");
                System.out.println("Press [Enter] to continue...");
                
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                continue;
            }
            
        }
    }
    
    private boolean updateEditSalesRecord(SalesRecord sr, int itemQuantityInStock, int QuantityInRecord){
        int totalStock = itemQuantityInStock + QuantityInRecord;
        if(totalStock < Integer.parseInt(sr.getItemSoldQuantity())){
            return false;
        }
        InventoryDatabase invDB = new InventoryDatabase();
        ArrayList<String []> itemList = invDB.getAllData(InventoryDatabase.files.ITEM.getFile());
        ArrayList<String []> salesRecordList = invDB.getAllData(InventoryDatabase.files.SALES_RECORD.getFile());
        
        for(String[] i : itemList){
            if(i[0].equals(sr.getItemSoldID())){
                i[3] = Integer.toString(totalStock - Integer.parseInt(sr.getItemSoldQuantity()));
                break;
            }
        }
        //update item new info to item textfile
        invDB.writeToTextFile(itemList.get(0), InventoryDatabase.files.ITEM.getFile());
        for(int i = 1; i < itemList.size(); i++){
            invDB.appendToTextFile(itemList.get(i), InventoryDatabase.files.ITEM.getFile());
        }
        
        //update sales record list
        for(String[] record : salesRecordList){
            if(record[0].equals(sr.getSalesRecordID())){
                record[4] = sr.getItemSoldQuantity();
                break;
            }
        }
        invDB.writeToTextFile(salesRecordList.get(0), InventoryDatabase.files.SALES_RECORD.getFile());
        for(int i = 1; i < salesRecordList.size(); i++){
            invDB.appendToTextFile(salesRecordList.get(i), InventoryDatabase.files.SALES_RECORD.getFile());
        }
        return true;
    }
    
    
    private void displaySalesRecord(){
        InventoryDatabase invDB = new InventoryDatabase();
        ArrayList<String[]> salesRecordList = invDB.getAllData(InventoryDatabase.files.SALES_RECORD.getFile());
        if(salesRecordList == null){
            return;
        }
        Scanner Sc = new Scanner(System.in);
        int records_per_page = 5;
        int totalPages = (salesRecordList.size() + records_per_page - 1) / records_per_page;
        int currentPage = 1;
        Outer:
        while(true){
            System.out.println(System.lineSeparator().repeat(50));
            displayDataForPage(salesRecordList, currentPage, records_per_page);
            System.out.println("Page Controls:");
            System.out.println("1. First Page");
            System.out.println("2. Previous Page");
            System.out.println("3. Next Page");
            System.out.println("4. Last Page");
            System.out.println("0. Quit");
            System.out.print("Enter your choice:");
            String choice = Sc.nextLine();
            switch(choice){
                case "1":
                    currentPage = 1;
                    break;
                case "2":
                    if(currentPage != 1){
                        currentPage--;
                    }
                    break;
                case "3":
                    if(currentPage != totalPages){
                        currentPage++;
                    }
                    break;
                case "4":
                    currentPage = totalPages;
                    break;
                    
                default:
                    System.out.println(System.lineSeparator().repeat(50));
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println("Press [Enter] to continue.");
                    Sc.nextLine();
                    break;
                    
                case "0":
                    System.out.println(System.lineSeparator().repeat(50));
                    break Outer;
            }
            
        }
        
        
    }
    
    private void displayDataForPage(ArrayList<String[]> salesRecordList, int currentPage, int records_per_page) {
        int startIndex = (currentPage - 1) * records_per_page;
        int endIndex = Math.min(startIndex + records_per_page, salesRecordList.size());

        System.out.println("Page " + currentPage + " of " + ((salesRecordList.size() + records_per_page - 1) / records_per_page));
        int count = 1;
        for (int i = startIndex; i < endIndex; i++) {
            String[] salesRecord = salesRecordList.get(i);
            String totalPrice = String.format("%.2f", Float.parseFloat(salesRecord[3]) * Integer.parseInt(salesRecord[4]));
            System.out.println(count+")");
            System.out.println("================================ SALES RECORD ================================");
            System.out.println("Sales Record Id : "+ salesRecord[0]);
            System.out.println("Date : "+salesRecord[5]);
            System.out.println("Record Keeper Id : "+ salesRecord[6]);
            System.out.println("==============================================================================");
            System.out.printf("%-10s%-23s%-15s%-20s%s%n", "Item ID", "Item Name","Unit Price", "Quantity Sold", "Total");
            System.out.println("==============================================================================");
            System.out.printf("%-10s%-23s%-15s%-20s%s%n", salesRecord[1], salesRecord[2],
                    "RM"+salesRecord[3], salesRecord[4], "RM"+totalPrice);
            System.out.println("==============================================================================\n\n\n\n");
            count++;
        }
    }
    
    
    
    

}
