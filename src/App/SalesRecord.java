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
    private String recordKeeper;
    private String[] itemSold;
    
    public SalesRecord(String date, String... itemSold){
        this.date = date;
        this.itemSold = itemSold;
    }
    
    public String getDate(){
        return date;
    }
    
    public String[] getItemSold(){
        return itemSold;
    }
    
}
