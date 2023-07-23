/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

/**
 *
 * @author verno
 */
public class Item {
    private String itemID;
    private String itemName;
    private float itemUnitPrice;
    private int itemStockQuantity;
    private String itemCategory;
    private String itemSupplierID;
    
    
    public Item(String itemID){
        this.itemID = itemID;
    }
    
    public Item(String itemName, float itemUnitPrice,int itemStockQuantity, String itemCategory, String itemSupplierID){
        this.itemName = itemName;
        this.itemUnitPrice = itemUnitPrice;
        this.itemStockQuantity = itemStockQuantity;
        this.itemCategory = itemCategory;
        this.itemSupplierID = itemSupplierID;
        
    }
    
    public Item(String itemID, String itemName, float itemUnitPrice, int itemStockQuantity, String itemCategory, String itemSupplierID){
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemUnitPrice = itemUnitPrice;
        this.itemStockQuantity = itemStockQuantity;
        this.itemCategory = itemCategory;
        this.itemSupplierID = itemSupplierID;
        
    }
    
    public String getItemID(){
        return itemID;
    }
    
    public String getItemName(){
        return itemName;
    }
    
    public float getItemUnitPrice(){
        return itemUnitPrice;
    }
    
    public int getItemStockQuantity(){
        return itemStockQuantity;
    }
    
    public String getItemCategory(){
        return itemCategory;
    }
    
    public String getItemSupplierID(){
        return itemSupplierID;
    }
    
    public void setItemName(String itemName){
        this.itemName = itemName;
    }
    
    public void setItemUnitPrice(float itemUnitPrice){
        this.itemUnitPrice = itemUnitPrice;
    }
    
    public void setItemCategory(String itemCategory){
        this.itemCategory = itemCategory;
    }
    
    public void setItemSupplier(String itemSupplierID){
        this.itemSupplierID = itemSupplierID;
    }
    
}
