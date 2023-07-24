/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

import java.util.ArrayList;

/**
 *
 * @author verno
 */
public class SalesRecord {
    private String salesRecordID;
    private String date;
    private String recordKeeperID;
    private String[] itemSold;
    
    public SalesRecord(String salesRecordID){
        this.salesRecordID = salesRecordID;
    }
    
    public SalesRecord(String date, String... itemSold){
        this.date = date;
        this.itemSold = itemSold;
    }
    
    public SalesRecord(String salesRecordID, String date, String recordKeeperID, String... itemSold){
        this.salesRecordID = salesRecordID;
        this.date = date;
        this.recordKeeperID = recordKeeperID;
        this.itemSold = itemSold;
    }
    
    public String getDate(){
        return date;
    }
    
    public String[] getItemSold(){
        return itemSold;
    }
    
    public String getItemSoldID(){
        return itemSold[0];
    }
    
    public String getItemSoldQuantity(){
        return itemSold[3];
    }
    
    public String getSalesRecordID(){
        return salesRecordID;
    }
    
    public String getSalesRecordDate(){
        return date;
    }
    
    public String getRecordKeeperID(){
        return recordKeeperID;
    }
    
    public String getItemSoldName(){
        return itemSold[1];
    }
    
    public String getItemSoldPrice(){
        return itemSold[2];
    }
    
    public String getSalesTotal(){
        
        String total = String.format("%.2f", Float.parseFloat(itemSold[2]) * Integer.parseInt(itemSold[3]));
        return total;
    }
    
    public void setItemSoldQuantity(String newQuantitySold){
        this.itemSold[3] = newQuantitySold;
    }
    
    
}
