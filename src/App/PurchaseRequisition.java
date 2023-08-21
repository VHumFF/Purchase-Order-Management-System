/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

/**
 *
 * @author macbook
 */
public class PurchaseRequisition {
    private String prID;
    private int prQuantity;
    private Item item;
    
    
    public PurchaseRequisition(Item item, int prQuantity){
        this.item = item;
        this.prQuantity = prQuantity;
    }
    
    
    public Item getItem(){
        return item;
    }
    
    public int getPRQuantity(){
        return prQuantity;
    }
}
