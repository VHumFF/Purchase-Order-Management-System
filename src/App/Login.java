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
            System.out.println("======== Login Page ========");
            System.out.print("Enter Username:");
            String username = Sc.nextLine();
            System.out.print("Enter Password:");
            String password = Sc.nextLine();
            User LCredential = new User(username, password);
            UserDatabase uDB = new UserDatabase();
            String role = uDB.validateUserCredential(LCredential);
            if(role != null){
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
                System.out.println(System.lineSeparator().repeat(50));
                System.out.println("Login failed. Please check your username and password and try again.");
            }
        }
        
    }
}
