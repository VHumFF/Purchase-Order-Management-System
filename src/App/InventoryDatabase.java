
package App;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


/**
Inventory database class that handles text file related to item, 
supplier, purchase requisition and purchase order and file handing operation
 */

public class InventoryDatabase {
    
    //enum that stores types of database file and file location of the text file
    enum files{
        ITEM("Database/Item.txt"),
        SUPPLIER("Database/Supplier.txt"),
        SALES_RECORD("Database/SalesRecord.txt"),
        USED_ITEM_ID_INDEX("Database/Used_Item_ID_Index.txt"),
        USED_SUPPLIER_ID_INDEX("Database/Used_Supplier_ID_Index.txt"),
        USED_SALES_RECORD_ID_INDEX("Database/Used_SALES_RECORD_ID_Index.txt");
        
        private File textFile;
        
        
        files(String filePath){
            File dataTF = new File(filePath);
            this.textFile = dataTF;
        }
        
        public File getFile(){
            return this.textFile;
        }
        
    }
    
    //empty constructor
    public InventoryDatabase(){
    }
    
    //return data from textfile in Arraylist.
    public ArrayList<String[]> getAllData(File filepath){
        ArrayList<String[]> dataList = new ArrayList<String[]>();
        try
        {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            while((line = reader.readLine()) != null){
                String[] dataRecords = line.strip().split(";");
                dataList.add(dataRecords);
            }
            reader.close();
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
    
    public void clearFile(File filepath){
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, false));
            String dataText = "";
            writer.write(dataText);
            writer.close();
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //overite file
    public void writeToTextFile(String[] data, File filepath){
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, false));
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
    
    //append data to file
    public void appendToTextFile(String[] data, File filepath){
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true));
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
    
    //check whether the item name or supplier name is already exist in the text file
    public boolean isDuplicateName(String Name, File file){

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
    
    //generate id index for record.
    public String generateIDIndex(File file, File usedIndexFile)
    {
        ArrayList<String[]> dataList = getAllData(file);
        ArrayList<String[]> usedIndex = getAllData(usedIndexFile);
        String lastIndex = "";
        String newIndex = "001";
        
        //get the last id of the record
        if (usedIndex != null) {
            for (String[] i : usedIndex) {
                lastIndex = i[0];
            }
        }
        if (!lastIndex.isEmpty()) {
            // Extract the numeric part of the lastID and increment it
            int Index = Integer.parseInt(lastIndex) + 1;

            newIndex = String.format("%03d", Index);
        }
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(usedIndexFile, true));
            writer.write(newIndex+"\n");
            writer.close();
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newIndex;
    }
    
    
}


