package App;

import java.util.Scanner;


public class AdminPanel {

    public void OpenMenu(){
        App appInstance = new App();
        System.out.println("======== Admin Panel ========");
        System.out.println(String.format("%-2s %-1s", "1.", "Sales Management"));
        System.out.println(String.format("%-2s %-1s", "2.", "Purchase Management"));
        System.out.println(String.format("%-2s %-1s", "3.", "User Registration"));
        System.out.println(String.format("%-2s %-1s", "0.", "Exit"));
        System.out.print("Enter your choice:");
        Scanner Sc = new Scanner(System.in);
        String option = Sc.nextLine();
        switch(option){
            case "1":
                System.out.println("You selected Sales Management");
                appInstance.changeMenu(App.AppMenu.SALES_MANAGEMENT_MENU.getMenuClass());
                break;
            case "2":
                System.out.println("You selected Purchase Management");
                appInstance.changeMenu(App.AppMenu.PURCHASE_MANAGEMENT_MENU.getMenuClass());
                break;
            case "3":
                System.out.println("You selected User Registration");
                break;
            case "0":
                System.out.println("Exiting Admin Panel. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
        Sc.close();
    }
    
    
}
