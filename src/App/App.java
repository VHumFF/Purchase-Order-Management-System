/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package App;


public class App {

    public static void main(String[] args) {
        App appInstance = new App();
        appInstance.App();
    }
    
    public void App(){
        changeMenu(AppMenu.ADMIN_PANEL.getMenuClass());
        System.out.println("welcome back");
       
}
    
    enum AppMenu{
        ADMIN_PANEL(AdminPanel.class),
        SALES_MANAGEMENT_MENU(SalesManagementMenu.class),
        PURCHASE_MANAGEMENT_MENU(PurchaseManagementMenu.class);
        
        private final Class<?> menuClass;
        
        AppMenu(Class<?> menuClass){
            this.menuClass = menuClass;
        }
        
        public Class<?> getMenuClass(){
            return this.menuClass;
        }
    }
    

    public void changeMenu(Class<?> menuClass){
        System.out.println(System.lineSeparator().repeat(50));//simulate clear screen
        try {
        Object menuObject = menuClass.getDeclaredConstructor().newInstance();
        menuClass.getDeclaredMethod("OpenMenu").invoke(menuObject);
        } 
        catch (Exception e) {
        System.out.println("Error: Failed to change menu.");
        e.printStackTrace();
        }
    }

}
