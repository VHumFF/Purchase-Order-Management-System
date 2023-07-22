
package App;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author verno
 */
public class InventoryDatabase {
    
    
    enum files{
        ITEM("Database/Item.txt"),
        SUPPLIER("Database/Supplier.txt");
        
        private File textFile;
        
        
        files(String filePath){
            File dataTF = new File(filePath);
            this.textFile = dataTF;
        }
        
        public File getFile(){
            return this.textFile;
        }
        
    }
    
    public InventoryDatabase(){
    }
    
    
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
    

    public String generateIDIndex(File file)
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
}


