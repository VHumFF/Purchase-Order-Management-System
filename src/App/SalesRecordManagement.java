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

                    break;
                case "3":
                    System.out.println(System.lineSeparator().repeat(50));

                    break;
                case "4":

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
            itemID = Sc.nextLine();
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
            
            while(true){
                System.out.println(System.lineSeparator().repeat(50));
                System.out.println("===Sales Record Detail.===\nItem ID : "+itemInfo.get(0)+"\nItem Name : "+ itemInfo.get(1)+"\nUnit Price : "+itemInfo.get(2)+"\nQuantity Sold : "+quantitySold);
                System.out.println("Do you want to save this sales record?");
                System.out.println("1. Save record");
                System.out.println("2. No");
                System.out.print("Enter your choice:");
                String choice = Sc.nextLine();
                if(choice.equals("1")){
                    //add item to record
                    String[] itemSold = {itemInfo.get(0), itemInfo.get(1), itemInfo.get(2),Integer.toString(quantitySold)};
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
        
        String salesRecordID = "SR" + invDB.generateIDIndex(InventoryDatabase.files.SALES_RECORD.getFile());
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
    

}
