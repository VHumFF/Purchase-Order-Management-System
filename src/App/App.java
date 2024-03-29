/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package App;

import java.util.Scanner;


public class App {

    public static void main(String[] args) {
        App appInstance = new App();
        appInstance.App();
    }
    
    public void App(){
        UserDatabase.initializeUserFile();
        InventoryDatabase.initializeInventoryFile();
        changePage(AppPage.LOGIN_PAGE.getPageClass());
}
    
    enum AppPage{
        LOGIN_PAGE(Login.class),
        ADMIN_PANEL(AdminPanel.class),
        SALES_MANAGEMENT_MENU(SalesManagementMenu.class),
        PURCHASE_MANAGEMENT_MENU(PurchaseManagementMenu.class),
        USER_REGISTRATION_PAGE(UserRegistration.class),
        SUPPLIER_MANAGEMENT(SupplierManagement.class),
        ITEM_MANAGEMENT(ItemManagement.class),
        SALES_RECORD_MANAGEMENT(SalesRecordManagement.class),
        PURCHASE_REQUISITION_MANAGEMENT(PurchaseRequisitionManagement.class),
        PURCHASE_ORDER_MANAGEMENT(PurchaseOrderManagement.class);
        
        
        private final Class<?> pageClass;
        
        AppPage(Class<?> pageClass){
            this.pageClass = pageClass;
        }
        
        public Class<?> getPageClass(){
            return this.pageClass;
        }
    }
    

    public void changePage(Class<?> pageClass){
        System.out.println(System.lineSeparator().repeat(50));//simulate clear screen
        try {
            
            Object pageObject = pageClass.getDeclaredConstructor().newInstance();
            pageClass.getDeclaredMethod("OpenPage").invoke(pageObject);
        } 
        catch (Exception e) {
            System.out.println("Error: Failed to change page.");
            e.printStackTrace();
        }
    }
    
    public static void displayMessage(String message){
        Scanner Sc = new Scanner(System.in);
        System.out.println(System.lineSeparator().repeat(50));
        System.out.println(message);
        System.out.println("Press [Enter] to continue...");
        Sc.nextLine();
        System.out.println(System.lineSeparator().repeat(50));
    }

}
