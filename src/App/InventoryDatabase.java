
package App;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author verno
 */
public class InventoryDatabase {
    private File file;
    private Supplier supp;
    
    public InventoryDatabase(){
        
    }
    
    public InventoryDatabase(Supplier supp){
        this.supp = supp;
        file = new File("Database/Supplier.txt");
        
    }
    
    private ArrayList<String[]> getAllData(File filepath){
        ArrayList<String[]> dataList = new ArrayList<String[]>();
        try
        {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            while((line = reader.readLine()) != null){
                String[] dataRecords = line.strip().split(";");
                dataList.add(dataRecords);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if (dataList.isEmpty())
        {
            return null;
        }
        else
        {
            return dataList;
        }
    }
    
    private void writeToTextFile(String[] data){
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            String dataText = "";
            
            for (int i = 0; i < data.length; i++) {
                dataText += data[i];
                if (i < data.length - 1) {
                    dataText += ";";
                }
            }

            writer.write(dataText+="\n");
            writer.close();
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void registerSupplier(){
        Scanner Sc = new Scanner(System.in);
        String supplierName = supp.getSuppName();
        String supplierContact = supp.getSuppContact();

        boolean isDuplicate = isDuplicateName(supplierName);
        
        if(isDuplicate){
            System.out.println(System.lineSeparator().repeat(50));
            System.out.println("Error: Supplier name already exists.");
            System.out.println("Press [Enter] to continue...");
            Sc.nextLine();
            return;
        }
        String supplierID = "SP" + generateID();
        String [] suppInfo = {supplierID, supplierName, supplierContact};
        
        writeToTextFile(suppInfo);
        
        System.out.println(System.lineSeparator().repeat(50));
        System.out.println("Supplier registration successful.");
        System.out.println("Press [Enter] to continue...");
        Sc.nextLine();
    }
    
    
    private boolean isDuplicateName(String Name){

        try
        {
            String[] suppInfo;
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            
            while((line = reader.readLine()) != null){
                suppInfo = line.strip().split(";");
                if(suppInfo[1].equals(Name)){
                    
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    

    private String generateID()
    {
        ArrayList<String[]> dataList = getAllData(file);

        String lastID = "";
        String newIndex = "001";

        if (dataList != null) {
            for (String[] i : dataList) {
                lastID = i[0];
            }
        }
        if (!lastID.isEmpty()) {
            // Extract the numeric part of the lastID and increment it
            int lastIndex = Integer.parseInt(lastID.substring(2));
            int Index = lastIndex + 1;

            newIndex = String.format("%03d", Index);
        }
        

        return newIndex;
    }
    
    public ArrayList<String[]> getSupplierList(){
        File suppFile = new File("Database/Supplier.txt");
        ArrayList<String[]> suppList = getAllData(suppFile);
        if(suppList == null){
            return suppList;
        }
        
        File itemFile = new File("Database/Item.txt");
        ArrayList<String[]> itemList = getAllData(itemFile);
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
            
            
        }
        
        return supplier_item_List;
    }
    
    public void deleteSupplier(){
        String supplierID = supp.getSuppID();
        ArrayList<String[]> SupplierList = getAllData(file);
        
        File itemTF = new File("Database/Item.txt");
        ArrayList<String[]> DataList = getAllData(itemTF);
        
        
        
        
        
        
    }
    
    
    
    

}


