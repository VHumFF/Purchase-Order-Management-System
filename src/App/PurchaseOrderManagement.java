
package App;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Kai Wen
 */
public class PurchaseOrderManagement {
    public void OpenPage(){
        Scanner Sc = new Scanner(System.in);
        
        Outer:
        while(true){
            System.out.println("======== Purchase Requisition Management Submenu ========");
            System.out.println(String.format("%-2s %-1s", "1.", "Purchase Requisition Approval"));
            System.out.println(String.format("%-2s %-1s", "2.", "Create Purchase Order"));
            System.out.println(String.format("%-2s %-1s", "3.", "Delete Pending Purchase Order"));
            System.out.println(String.format("%-2s %-1s", "4.", "Issue Pending Purchase Order"));
            System.out.println(String.format("%-2s %-1s", "5.", "Update Issued Purchase Order Status"));
            System.out.println(String.format("%-2s %-1s", "6.", "Display Purchase Order"));
            System.out.println(String.format("%-2s %-1s", "0.", "Exit"));
            System.out.print("Enter your choice:");
            
            String option = Sc.nextLine();
            switch(option){
                case "1":
                    PRApproval();
                    System.out.println(System.lineSeparator().repeat(50));
                    break;
                case "2":
                    createPO();
                    System.out.println(System.lineSeparator().repeat(50));
                    break;
                case "3":
                    deletePO();
                    System.out.println(System.lineSeparator().repeat(50));
                    break;
                case "4":
                    
                    System.out.println(System.lineSeparator().repeat(50));
                    break;
                case "5":
                    
                    System.out.println(System.lineSeparator().repeat(50));
                    break;
                case "6":
                    
                    displayPOList("Pending");
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
    
    
    private void PRApproval()
    {
        PurchaseRequisitionManagement prM = new PurchaseRequisitionManagement();
        
        Scanner sc = new Scanner(System.in);
        while (true){
            ArrayList<String []> prlist = new ArrayList();
            prlist = prM.displayPRList("Pending");
            System.out.print("Please select the Purchase Requisite ID:");
            String inputPRID = sc.nextLine().toUpperCase();
            boolean IDFound = false;
            for (String[] pr:prlist) 
            {
                if(pr[0].equals(inputPRID))
                {
                    IDFound = true;
                    break;
                }
            }
            if (!IDFound){
                System.out.println("Please enter a valid PR ID.");
                continue;
            }
            String newstatus = "";
            System.out.print("Do you want to approve the Purchase Requisite? (Y/N): ");
            String ApprovalChoice = sc.nextLine().toUpperCase();
            if(ApprovalChoice.equals("Y"))
            {
                newstatus = "Approved";
                changePRStatus(inputPRID,newstatus);
            }
            else if(ApprovalChoice.equals("N"))
            {
                newstatus = "Rejected";
                changePRStatus(inputPRID,newstatus);
            }
            else
            {
                System.out.println("Please enter 'Y' or 'N'");
                continue;
            }
        }
   
        
    }
    private void changePRStatus(String InputPRID, String newstatus)
    {
        InventoryDatabase idb = new InventoryDatabase();
        ArrayList<String[]> prlist = new ArrayList();
        prlist = idb.getAllData(InventoryDatabase.files.PURCHASE_REQUISITION.getFile());
        for(String[]pr:prlist)
        {
            if(pr[0].equals(InputPRID))
            {
                pr[9] = newstatus;
            }
        }
        
        idb.writeToTextFile(prlist.get(0), InventoryDatabase.files.PURCHASE_REQUISITION.getFile());
        for(int i = 1; i < prlist.size(); i++){
            idb.appendToTextFile(prlist.get(i), InventoryDatabase.files.PURCHASE_REQUISITION.getFile());
        }
    }
    private void createPO()
    {
        PurchaseRequisitionManagement prM = new PurchaseRequisitionManagement();
        
        Scanner sc = new Scanner(System.in);
        
        while (true){
            ArrayList<String []> prlist = new ArrayList();
            prlist = prM.displayPRList("Approved");
            System.out.print("Please select the Purchase Requisite ID:");
            String inputPRID = sc.nextLine().toUpperCase();
            boolean IDFound = false;
            String[] selectedPR = {};
            for (String[] pr:prlist) 
            {
                if(pr[0].equals(inputPRID))
                {
                    IDFound = true;
                    selectedPR = pr;
                    break;
                }
            }
            if (!IDFound)
            {
                System.out.println("Please enter a valid Purchase Requisite ID.");
                continue;
            }
            
            System.out.println("===============================================================Item List===============================================================");
            System.out.printf("%-8s%-10s%-30s%-13s%-20s%-15s%-15s%-15s%s%n", "PR ID", "Item ID","Item Name", "Unit Price", 
                    "Purchase Quantity","Supplied By", "Created By", "Date", "Status");
            System.out.println("=======================================================================================================================================");
            System.out.printf("%-8s%-10s%-30s%-13s%-20s%-15s%-15s%-15s%s%n", selectedPR[0], selectedPR[1], selectedPR[2], "RM"+ selectedPR[3], selectedPR[6], selectedPR[5], selectedPR[7], selectedPR[8], selectedPR[9]);   
            System.out.println("=======================================================================================================================================\n");
            System.out.println("Do you want to create a Purchase Order according to this PR? (Y/N): ");
            String choiceCreatePO = sc.nextLine().toUpperCase();
            if(choiceCreatePO.equals("Y"))
            {
                addPOtoFile(selectedPR);
            }
            else if(choiceCreatePO.equals("N")){
                break;
            }
            else{
                System.out.println("Please enter a valid choice");
            }
                
        }    
            
    }
    
    private void addPOtoFile(String[] selectedPR){
        InventoryDatabase idb = new InventoryDatabase();
        UserDatabase udb = new UserDatabase();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDate.now().format(formatter);
        
        String poID = "PO" + idb.generateIDIndex(InventoryDatabase.files.PURCHASE_ORDER.getFile(), InventoryDatabase.files.PURCHASE_ORDER.USED_PURCHASE_ORDER_ID_INDEX.getFile());
        changePRStatus(selectedPR[0], "Completed");
        String[] order = {poID, selectedPR[0], selectedPR[1],selectedPR[2],selectedPR[3],selectedPR[4],selectedPR[5],selectedPR[6],date, udb.getCurrentUser(), "Pending"};
        idb.appendToTextFile(order, InventoryDatabase.files.PURCHASE_ORDER.getFile());
        
    }
    
    private void deletePO(){
        while(true){
            ArrayList<String[]> poList = new ArrayList();
            Scanner Sc = new Scanner(System.in);
            poList = displayPOList("Pending");
            System.out.println("Please enter Order ID to delete:");
            String inputPOID = Sc.nextLine().toUpperCase();
            boolean IDFound = false;
            String[] selectedPO = {};
            for(String[] po:poList){
                if(po[0].equals(inputPOID)){
                    IDFound = true;
                    selectedPO = po;
                }
            }

            if(!IDFound){
                System.out.println("Please enter a valid Purchase Order ID.");
                    continue;
            }
            System.out.println("===============================================================Item List===============================================================");
            System.out.printf("%-8s%-10s%-30s%-13s%-20s%-15s%-15s%-15s%s%n", "Order ID" ,"PRID","Item ID","Item Name", "Unit Price", 
                    "Purchase Quantity","Supplied By", "Created By", "Date", "Status");
            System.out.println("=======================================================================================================================================");
            System.out.printf("%-8s%-10s%-30s%-13s%-20s%-15s%-15s%-15s%s%n", selectedPO[0], selectedPO[1], 
                    selectedPO[2], selectedPO[3],"RM"+ selectedPO[4], selectedPO[7], selectedPO[6], selectedPO[9], selectedPO[8], selectedPO[10]);
            System.out.println("=======================================================================================================================================");
            System.out.println("Do you want to delete this Purchase Order? (Y/N): ");
            String choiceDeletePO = Sc.nextLine().toUpperCase();
            
            if(choiceDeletePO.equals("Y"))
            {
                deletePOFromFile(selectedPO);
                System.out.println("Purchase Order successfully deleted from list.");
            }
            else if(choiceDeletePO.equals("N")){
                break;
            }
            else{
                System.out.println("Please enter a valid choice");
            }
        }
        
    }
    
    private void deletePOFromFile(String[] selectedPO)
    {
        InventoryDatabase idb = new InventoryDatabase();
        ArrayList<String[]> poList = idb.getAllData(InventoryDatabase.files.PURCHASE_ORDER.getFile());
        ArrayList<String[]> updatedPOList = new ArrayList();
        
        for(String [] po:poList)
        {
            if(po[0].equals(selectedPO[0]))
            {
                continue;
            }
            updatedPOList.add(po);
        }
        if(updatedPOList.isEmpty())
        {
            idb.clearFile(InventoryDatabase.files.PURCHASE_ORDER.getFile());
        }
        else{
            idb.writeToTextFile(updatedPOList.get(0), InventoryDatabase.files.PURCHASE_ORDER.getFile());
            for(int i = 1; i < updatedPOList.size(); i++){
                idb.appendToTextFile(updatedPOList.get(i), InventoryDatabase.files.PURCHASE_ORDER.getFile());
            }
        }
    }
    
    private void issuePO()
    {
        while(true){
            ArrayList<String[]> poList = new ArrayList();
            Scanner Sc = new Scanner(System.in);
            poList = displayPOList("Pending");
            System.out.println("Please enter Order ID to issue:");
            String inputPOID = Sc.nextLine().toUpperCase();
            boolean IDFound = false;
            String[] selectedPO = {};
            for(String[] po:poList){
                if(po[0].equals(inputPOID)){
                    IDFound = true;
                    selectedPO = po;
                }
            }

            if(!IDFound){
                System.out.println("Please enter a valid Purchase Order ID.");
                    continue;
            }
            System.out.println("===============================================================Item List===============================================================");
            System.out.printf("%-8s%-10s%-30s%-13s%-20s%-15s%-15s%-15s%s%n", "Order ID" ,"PRID","Item ID","Item Name", "Unit Price", 
                    "Purchase Quantity","Supplied By", "Created By", "Date", "Status");
            System.out.println("=======================================================================================================================================");
            System.out.printf("%-8s%-10s%-30s%-13s%-20s%-15s%-15s%-15s%s%n", selectedPO[0], selectedPO[1], 
                    selectedPO[2], selectedPO[3],"RM"+ selectedPO[4], selectedPO[7], selectedPO[6], selectedPO[9], selectedPO[8], selectedPO[10]);
            System.out.println("=======================================================================================================================================");
            System.out.println("Do you want to issue this Purchase Order? (Y/N): ");
            String choiceIssuePO = Sc.nextLine();
            
            if(choiceIssuePO.equals("Y"))
            {
                saveIssuedPOtoFile(selectedPO);
                System.out.println("Purchase Order successfully issued to supplier.");
            }
            else if(choiceIssuePO.equals("N")){
                break;
            }
            else{
                System.out.println("Please enter a valid choice");
            }
        }
    }
    
    
    private void saveIssuedPOtoFile(String[] selectedPO)
    {
        InventoryDatabase idb = new InventoryDatabase();
        ArrayList<String[]> poList = idb.getAllData(InventoryDatabase.files.PURCHASE_ORDER.getFile());
        
        for(String [] po:poList)
        {
            if(po[0].equals(selectedPO[0]))
            {
                po[10] = "Issued";
            }
        }
        
        idb.writeToTextFile(poList.get(0), InventoryDatabase.files.PURCHASE_ORDER.getFile());
        for(int i = 1; i < poList.size(); i++)
        {
            idb.appendToTextFile(poList.get(i), InventoryDatabase.files.PURCHASE_ORDER.getFile());
        }
        
    }
    
    private void updateIssuedPOStatus()
    {
        while(true){
            ArrayList<String[]> poList = new ArrayList();
            Scanner Sc = new Scanner(System.in);
            poList = displayPOList("Issued");
            System.out.println("Please enter Order ID to update status:");
            String inputPOID = Sc.nextLine().toUpperCase();
            boolean IDFound = false;
            String[] selectedPO = {};
            for(String[] po:poList){
                if(po[0].equals(inputPOID)){
                    IDFound = true;
                    selectedPO = po;
                }
            }

            if(!IDFound){
                System.out.println("Please enter a valid Purchase Order ID.");
                    continue;
            }
            System.out.println("===============================================================Item List===============================================================");
            System.out.printf("%-8s%-10s%-30s%-13s%-20s%-15s%-15s%-15s%s%n", "Order ID" ,"PRID","Item ID","Item Name", "Unit Price", 
                    "Purchase Quantity","Supplied By", "Created By", "Date", "Status");
            System.out.println("=======================================================================================================================================");
            System.out.printf("%-8s%-10s%-30s%-13s%-20s%-15s%-15s%-15s%s%n", selectedPO[0], selectedPO[1], 
                    selectedPO[2], selectedPO[3],"RM"+ selectedPO[4], selectedPO[7], selectedPO[6], selectedPO[9], selectedPO[8], selectedPO[10]);
            System.out.println("=======================================================================================================================================");
            System.out.println("Do you want to update this issued Purchase Order status? (Y/N): ");
            String choiceUpdatePO = Sc.nextLine();
            
            if(choiceUpdatePO.equals("Y"))
            {
                addPurchasedItemtoDB(selectedPO);
                System.out.println("Purchase Order status successfully updated.");
            }
            else if(choiceUpdatePO.equals("N")){
                break;
            }
            else{
                System.out.println("Please enter a valid choice");
            }
        }
    }
        
    private void addPurchasedItemtoDB(String[] selectedPO)
    {
        InventoryDatabase invDB = new InventoryDatabase();
        ArrayList<String[]> itemList = invDB.getAllData(InventoryDatabase.files.ITEM.getFile());
        ArrayList<String[]> poList = invDB.getAllData(InventoryDatabase.files.PURCHASE_ORDER.getFile());
        for(String[] item : itemList){
            if(selectedPO[2].equals(item[0])){
                item[3] = Integer.toString(Integer.parseInt(item[3]) + Integer.parseInt(selectedPO[7]));
            }
        }
        for(String[] po:poList){
            if(po[0].equals(selectedPO[0])){
                po[10] = "Delivered";
            }
        }
        
        invDB.writeToTextFile(itemList.get(0), InventoryDatabase.files.ITEM.getFile());
        for(int i = 1; i < itemList.size(); i++)
        {
            invDB.appendToTextFile(itemList.get(i), InventoryDatabase.files.ITEM.getFile());
        }
        
        invDB.writeToTextFile(poList.get(0), InventoryDatabase.files.PURCHASE_ORDER.getFile());
        for(int i = 1; i < poList.size(); i++)
        {
            invDB.appendToTextFile(poList.get(i), InventoryDatabase.files.PURCHASE_ORDER.getFile());
        }
        
        
    }
        
    public ArrayList<String[]> displayPOList(String status){
        InventoryDatabase iDB = new InventoryDatabase();
        ArrayList<String[]> poList = iDB.getAllData(InventoryDatabase.files.PURCHASE_ORDER.getFile());
        if(poList == null){
            System.out.println("No PO found.");
            return null;
        }
        ArrayList<String[]> statusPOList = new ArrayList();
        System.out.println(System.lineSeparator().repeat(50));
        System.out.println("===============================================================Item List===============================================================");
        System.out.printf("%-8s%-10s%-30s%-13s%-20s%-15s%-15s%-15s%s%n", "PR ID", "Item ID","Item Name", "Unit Price", 
                "Purchase Quantity","Supplied By", "Created By", "Date", "Status");
        System.out.println("=======================================================================================================================================");
        for(String[] po : poList){
            if(po[9].equals(status)){
                System.out.printf("%-8s%-10s%-30s%-13s%-20s%-15s%-15s%-15s%s%n", po[0], po[1], po[2], "RM"+ po[3], po[6], po[5], po[7], po[8], po[9]);
                statusPOList.add(po);
            }
            else if(status.equals("All")){
                System.out.printf("%-8s%-10s%-30s%-13s%-20s%-15s%-15s%-15s%s%n", po[0], po[1], po[2], "RM"+ po[3], po[6], po[5], po[7], po[8], po[9]);                
            }
                
        }
        
        System.out.println("=======================================================================================================================================\n");

        if(status.equals("All")){
            return poList;
        }
        else{
            return statusPOList;
        }
    }
    
}
