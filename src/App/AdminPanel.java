package App;

import java.util.Scanner;


public class AdminPanel {

    public void OpenPage(){
        App appInstance = new App();
        Scanner Sc = new Scanner(System.in);
        Outer:
        while(true){
            
            System.out.println("======== Admin Panel ========");
            System.out.println(String.format("%-2s %-1s", "1.", "Sales Management"));
            System.out.println(String.format("%-2s %-1s", "2.", "Purchase Management"));
            System.out.println(String.format("%-2s %-1s", "3.", "User Registration"));
            System.out.println(String.format("%-2s %-1s", "0.", "Exit"));
            System.out.print("Enter your choice:");
            String option = Sc.nextLine();
            switch(option){
                case "1":
                    System.out.println("You selected Sales Management"); 
                    appInstance.changePage(App.AppPage.SALES_MANAGEMENT_MENU.getPageClass());
                    break;
                case "2":
                    System.out.println("You selected Purchase Management");
                    appInstance.changePage(App.AppPage.PURCHASE_MANAGEMENT_MENU.getPageClass());
                    break;
                case "3":
                    System.out.println("You selected User Registration");
                    appInstance.changePage(App.AppPage.USER_REGISTRATION_PAGE.getPageClass());
                    break;
                default:
                    System.out.println(System.lineSeparator().repeat(50));
                    System.out.println("Invalid choice. Please try again.");
                    break;
                case "0":
                    System.out.println(System.lineSeparator().repeat(50));
                    System.out.println("Exiting Admin Panel. GoodBye!");
                    break Outer;
            }
        }

    }
}
