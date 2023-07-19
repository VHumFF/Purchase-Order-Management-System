/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

/**
 *
 * @author verno
 */
public class Supplier {
    private String supplierID;
    private String supplierName;
    private String supplierContact;
    
    public Supplier(String supplierID){
        this.supplierID = supplierID;
    }
    
    public Supplier(String supplierName, String supplierContact){
        this.supplierName = supplierName;
        this.supplierContact = supplierContact;
    }
    
    public Supplier(String supplierID, String supplierName, String supplierContact){
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.supplierContact = supplierContact;
    }
    
    public String getSuppName(){
        return supplierName;
    }
    
    public String getSuppContact(){
        return supplierContact;
    }
    
    public String getSuppID(){
        return supplierID;
    }
    
    public void setSuppName(String suppName){
        this.supplierName = suppName;
    }
    
    public void setSuppContact(String suppContact){
        this.supplierContact = suppContact;
    }
}

