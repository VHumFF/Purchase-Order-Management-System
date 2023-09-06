/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author macbook
 */
public class PurchaseRequisitionManagement {
    public void OpenPage(){
        Scanner Sc = new Scanner(System.in);
        
        Outer:
        while(true){
            System.out.println("======== Purchase Requisition Management Submenu ========");
            System.out.println(String.format("%-2s %-1s", "1.", "Create Purchase Requisition"));
            System.out.println(String.format("%-2s %-1s", "2.", "Edit Pending Purchase Requisition"));
            System.out.println(String.format("%-2s %-1s", "3.", "Delete Pending Purchase Requisition"));
            System.out.println(String.format("%-2s %-1s", "4.", "Display Purchase Requisition"));
            System.out.println(String.format("%-2s %-1s", "0.", "Exit"));
            System.out.print("Enter your choice:");
            
            String option = Sc.nextLine();
            switch(option){
                case "1":
                    createPR();
                    System.out.println(System.lineSeparator().repeat(50));
                    break;
                case "2":
                    editPR();
                    System.out.println(System.lineSeparator().repeat(50));
                    break;
                case "3":
                    deletePR();
                    System.out.println(System.lineSeparator().repeat(50));
                    break;
                case "4":
                    String status = selectPRStatus();
                    displayPRList(status);
                    System.out.println("Press [Enter] to continue");
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
    
    private void createPR()
    {
        Outer:
        while (true){
            Scanner Sc = new Scanner(System.in);
            ItemManagement im = new ItemManagement();
            Item it = new Item(null, null, 0, 0, null, null);
            String category = im.selectItemCategory();
            ArrayList<String[]> itemList = im.displayItemListByCategory(category);
            System.out.print("Select The ItemID To Create Purchase Requisition:");
            String selectedItemID = Sc.nextLine().toUpperCase();

            boolean IDFound = false;
            boolean suppidValid = true;
            for(String[] item:itemList){
                if(item[0].equals(selectedItemID)){
                    InventoryDatabase invDB = new InventoryDatabase();
                    IDFound = true;
                    it.setItemID(item[0]);
                    it.setItemName(item[1]);
                    it.setItemUnitPrice(Float.parseFloat(item[2]));
                    it.setItemQuantity(Integer.parseInt(item[3]));
                    it.setItemCategory(item[4]);
                    //it.setItemSupplier(item[5]);
                    String[] itemSupp = item[5].strip().split("\\|");
                    if(itemSupp[0].equals("-")){
                        System.out.println("Item does not have a supplier.");
                        break Outer;
                    }
                    ArrayList<String[]> supplierList = invDB.getAllData(InventoryDatabase.files.SUPPLIER.getFile());
                    System.out.println("=============================================Item List=======");
                    System.out.printf("%-15s%-23s%s%n", "Supplier ID", "Supplier Name","Supplier Contact");
                    System.out.println("=============================================================");
                    
                    for(String isup:itemSupp){
                        for(String[] sup : supplierList){
                            if(sup[0].equals(isup)){
                                System.out.printf("%-15s%-23s%s%n", sup[0], sup[1], sup[2]);
                            }
                        }
                    }
                    System.out.println("=============================================================");
                    boolean idFound2 = false;
                    System.out.println("Please select a supplier:");
                    String supid = Sc.nextLine();
                    for(String isupp : itemSupp){
                        if(isupp.equals(supid)){
                            idFound2 = true;
                        }
                    }
                    if(!idFound2){
                        System.out.println("Please enter a valid supplier id.");
                        suppidValid = false;
                        break;
                    }
                    else{
                        item[5] = supid;
                    }
                    break;
                }
            }
            if(!suppidValid){
                continue;
            }
            if(!IDFound){
                System.out.println(System.lineSeparator().repeat(50));
                System.out.println("ItemID not Found");
                System.out.println("Please Enter a valid ItemID");
                System.out.println("Press [Enter] to continue...");
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                continue;
            }
            try
            {
                System.out.print("Please enter purchase quantity:");
                int prQuantity = Sc.nextInt();
                Sc.nextLine();
                if(prQuantity <= 0){
                    System.out.println(System.lineSeparator().repeat(50));
                    System.out.println("Please enter a positive number");
                    System.out.println("Press [Enter] to continue...");
                    Sc.nextLine();
                    System.out.println(System.lineSeparator().repeat(50));
                    continue;
                }
                
                PurchaseRequisition pr = new PurchaseRequisition(it, prQuantity);
                addPRToFile(pr);
                System.out.println(System.lineSeparator().repeat(50));
                System.out.println("PR created successfully");
                System.out.println("Press [Enter] to continue...");
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                break;
                
            }
            catch(InputMismatchException ie){
                // Clear invalid input left-over
                Sc.nextLine();
                System.out.println("Invalid input for quantity. Please enter a valid number.");
                System.out.println("Press [Enter] to continue...");

                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
            }            
        }
               
    }
    
    private void addPRToFile(PurchaseRequisition pr){
        InventoryDatabase iDB = new InventoryDatabase();
        UserDatabase uDB = new UserDatabase();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDate.now().format(formatter);
        
        String prID = "PR"+iDB.generateIDIndex(InventoryDatabase.files.PURCHASE_REQUISITION.getFile(), InventoryDatabase.files.USED_PURCHASE_REQUISITION_ID_INDEX.getFile());
        String prCreator = uDB.getCurrentUser();
        
        String[] prInfo = {prID, pr.getItem().getItemID(), pr.getItem().getItemName(),
            Float.toString(pr.getItem().getItemUnitPrice()), pr.getItem().getItemCategory(), 
            pr.getItem().getItemSupplierID(), Integer.toString(pr.getPRQuantity()), prCreator, date, "Pending"};
        
        
        iDB.appendToTextFile(prInfo, InventoryDatabase.files.PURCHASE_REQUISITION.getFile());
        
        
    }
    
    
    private void editPR(){
        while(true){
            Scanner Sc = new Scanner(System.in);
            ArrayList<String[]> prList = displayPRList("Pending");
            System.out.print("Please Enter the prID You Want to Edit or enter [BACK] to go back:");
            String selectedPRID = Sc.nextLine().toUpperCase();
            if(selectedPRID.equals("BACK")){
                break;
            }
            String[] selectedPR = {};
            boolean IDFound = false;
            for(String[] pr:prList){
                if(pr[0].equals(selectedPRID)){
                    IDFound = true;
                    selectedPR = pr;
                    break;
                    }
            }
            if(!IDFound){
                System.out.println(System.lineSeparator().repeat(50));
                System.out.println("prID not Found");
                System.out.println("Please Enter a valid prID");
                System.out.println("Press [Enter] to continue...");
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                continue;
            }


            try
            {
                System.out.print("Enter New Pruchase Quantity:");
                int newQuantity = Sc.nextInt();
                if(newQuantity <= 0){
                    Sc.nextLine();
                    System.out.println(System.lineSeparator().repeat(50));
                    System.out.println("Please enter a positive number");
                    System.out.println("Press [Enter] to continue...");
                    Sc.nextLine();
                    System.out.println(System.lineSeparator().repeat(50));
                    continue;
                }
                selectedPR[6] = Integer.toString(newQuantity);
                addEditedPRToFile(selectedPR);

            }
            catch(InputMismatchException ie){
                // Clear invalid input left-over
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                System.out.println("Invalid input for quantity. Please enter a valid number.");
                System.out.println("Press [Enter] to continue...");

                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
            } 
        }
        
    }
    
    
    private void addEditedPRToFile(String[] editedPR){
        InventoryDatabase invDB = new InventoryDatabase();
        
        ArrayList<String []> prList = invDB.getAllData(InventoryDatabase.files.PURCHASE_REQUISITION.getFile());
        
        //update pr info
        for(String[] pr : prList){
            if(pr[0].equals(editedPR[0])){
                pr[6] = editedPR[6];
                break;
            }
        }
        
        //update item new info to item textfile
        invDB.writeToTextFile(prList.get(0), InventoryDatabase.files.PURCHASE_REQUISITION.getFile());
        for(int i = 1; i < prList.size(); i++){
            invDB.appendToTextFile(prList.get(i), InventoryDatabase.files.PURCHASE_REQUISITION.getFile());
        }
    }

       
    public void deletePR(){
        Outer:
        while(true){
            Scanner Sc = new Scanner(System.in);
            ArrayList<String[]> prList = displayPRList("Pending");
            System.out.print("Please Enter the prID You Want to Delete or enter [BACK] to go back:");
            String selectedPRID = Sc.nextLine().toUpperCase();
            if(selectedPRID.equals("BACK")){
                break;
            }

            boolean IDFound = false;
            for(String[] pr:prList){
                if(pr[0].equals(selectedPRID)){
                    IDFound = true;
                    break;
                }
            }
            if(!IDFound){
                System.out.println(System.lineSeparator().repeat(50));
                System.out.println("prID not Found");
                System.out.println("Please Enter a valid prID");
                System.out.println("Press [Enter] to continue...");
                Sc.nextLine();
                System.out.println(System.lineSeparator().repeat(50));
                continue;
            }
            
            while(true){
                System.out.println(System.lineSeparator().repeat(50));
                System.out.println("===============================================================Item List===============================================================");
                System.out.printf("%-8s%-10s%-30s%-13s%-20s%-15s%-15s%-15s%s%n", "PR ID", "Item ID","Item Name", "Unit Price", 
                        "Purchase Quantity","Supplied By", "Created By", "Date", "Status");
                System.out.println("=======================================================================================================================================");
                for(String[] pr : prList){
                    if(pr[0].equals(selectedPRID)){
                        System.out.printf("%-8s%-10s%-30s%-13s%-20s%-15s%-15s%-15s%s%n", pr[0], pr[1], pr[2], "RM"+ pr[3], pr[6], pr[5], pr[7], pr[8], pr[9]);
                    }
                }
                System.out.println("=======================================================================================================================================\n");
                System.out.println("Are you sure want delete this PR?\n1.Confirm\n2.NO");
                System.out.print("Enter Your Choice: ");
                String choice = Sc.nextLine();
                if(choice.equals("1")){
                    deletePRfromFile(selectedPRID);
                    
                    System.out.println(System.lineSeparator().repeat(50));
                    System.out.println("PR deleted successfully");
                    System.out.println("Press [Enter] to continue...");
                    Sc.nextLine();
                    System.out.println(System.lineSeparator().repeat(50));
                    break Outer;
                }
                else if(choice.equals("2")){
                    break Outer;
                }
                else{
                    System.out.println(System.lineSeparator().repeat(50));
                    System.out.println("Please Enter a valid choice");
                    System.out.println("Press [Enter] to continue...");
                    Sc.nextLine();
                    System.out.println(System.lineSeparator().repeat(50));
                    continue; 
                }
            }
        }
    }
    
    public void deletePRfromFile(String prToDelete){
        InventoryDatabase iDB = new InventoryDatabase();
        ArrayList<String[]> prList = iDB.getAllData(InventoryDatabase.files.PURCHASE_REQUISITION.getFile());
        ArrayList<String[]> newPRList = new ArrayList();
        for(String[] pr:prList){
            if(pr[0].equals(prToDelete)){
                continue;
            }
            newPRList.add(pr);
        }
        
        if(newPRList.isEmpty()){
            iDB.clearFile( InventoryDatabase.files.PURCHASE_REQUISITION.getFile());
        }
        else{
            iDB.writeToTextFile(newPRList.get(0), InventoryDatabase.files.PURCHASE_REQUISITION.getFile());
            for(int i = 1; i < newPRList.size(); i++){
                iDB.appendToTextFile(newPRList.get(i), InventoryDatabase.files.PURCHASE_REQUISITION.getFile());
            }
        }
    } 
    
    public String selectPRStatus(){
        while(true){
            Scanner Sc = new Scanner(System.in);
            System.out.println("Display PR by Status. Please select a PR status");
            System.out.println(String.format("1. Pending\n2. Approved\n3.Rejected\n4.Completed\n5.All"));
            System.out.print("Enter your choice:");
            String choice = Sc.nextLine();
            if(choice.equals("1")){
                return "Pending";
            }
            else if(choice.equals("2")){
                return "Approved";
            }
            else if(choice.equals("3")){
                return "Rejected";
            }
            else if(choice.equals("4")){
                return "Completed";
            }
            else if(choice.equals("5")){
                return "All";
            }
            else{
                System.out.println("Please enter a valid choice.");
                continue;
            }
        }
        
    }
    
    
    
    public ArrayList<String[]> displayPRList(String status){
        InventoryDatabase iDB = new InventoryDatabase();
        ArrayList<String[]> prList = iDB.getAllData(InventoryDatabase.files.PURCHASE_REQUISITION.getFile());
        if(prList == null){
            System.out.println("No PR found.");
            return null;
        }
        ArrayList<String[]> statusPRList = new ArrayList();
        System.out.println(System.lineSeparator().repeat(50));
        System.out.println("===============================================================Item List===============================================================");
        System.out.printf("%-8s%-10s%-30s%-13s%-20s%-15s%-15s%-15s%s%n", "PR ID", "Item ID","Item Name", "Unit Price", 
                "Purchase Quantity","Supplied By", "Created By", "Date", "Status");
        System.out.println("=======================================================================================================================================");
        for(String[] pr : prList){
            if(pr[9].equals(status)){
                System.out.printf("%-8s%-10s%-30s%-13s%-20s%-15s%-15s%-15s%s%n", pr[0], pr[1], pr[2], "RM"+ pr[3], pr[6], pr[5], pr[7], pr[8], pr[9]);
                statusPRList.add(pr);
            }
            else if(status.equals("All")){
                System.out.printf("%-8s%-10s%-30s%-13s%-20s%-15s%-15s%-15s%s%n", pr[0], pr[1], pr[2], "RM"+ pr[3], pr[6], pr[5], pr[7], pr[8], pr[9]);
                
            }
                
        }
        
        System.out.println("=======================================================================================================================================\n");

        if(status.equals("All")){
            return prList;
        }
        else{
            return statusPRList;
        }
        
    }
    
}
