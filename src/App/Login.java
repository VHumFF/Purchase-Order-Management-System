/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

import java.util.Scanner;

/**
 *
 * @author verno
 */
public class Login {
    public void OpenPage(){
        App appInstance = new App();
        Scanner Sc = new Scanner(System.in);
        
        
        while(true){
            System.out.println("Welcome to SIGMA SDN BHD Purchase Order Management System ");
            System.out.println("========================= Login Page =========================");
            System.out.println("Enter you credential to login or enter [EXIT] to exit program");
            System.out.print("Enter Username:");
            String username = Sc.nextLine();
            
            if(username.toUpperCase().equals("EXIT")){
                System.out.println(System.lineSeparator().repeat(50));
                System.out.println("Thank You for Using.\nLoging out System");
                System.exit(0);
            }
            
            System.out.print("Enter Password:");
            String password = Sc.nextLine();
            
            User LCredential = new User(username, password);
            UserDatabase uDB = new UserDatabase(LCredential);
            String role = uDB.validateUserCredential();
            if(role != null){
                uDB.storeCurrentUserName();
                switch(role){
                    case "Admin":
                        appInstance.changePage(App.AppPage.ADMIN_PANEL.getPageClass());
                        break;
                    case "Sales":
                        appInstance.changePage(App.AppPage.SALES_MANAGEMENT_MENU.getPageClass());
                        break;
                    case "Purchase":
                        appInstance.changePage(App.AppPage.PURCHASE_MANAGEMENT_MENU.getPageClass());
                        break;
                }
            }
            else{
                App.displayMessage("Login failed. Please check your username and password and try again.");
            }
        }
        
    }
}
