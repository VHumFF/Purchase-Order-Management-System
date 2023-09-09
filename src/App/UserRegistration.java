
package App;

import java.util.Scanner;


public class UserRegistration{
    
    public void OpenPage(){
        String username = null;
        String password = null;
        
        Scanner Sc = new Scanner(System.in);
        
        while(true){
            System.out.println("======== User Registration ========");
            System.out.print(String.format("Enter Username:"));
            username = Sc.nextLine();
            boolean usernValid = usernValidation(username);//validate username format
            if(!usernValid){
                continue;
            }
            
            System.out.print(String.format("Enter Password:"));
            password = Sc.nextLine();
            
            boolean passwValid = passwValidation(password);//validate password format
            if(!passwValid){
                continue;
            }
            break;
            
        }

        boolean selected = false;
        String role = null;
        
        Outer:
        while(!selected){
            //select a role for the new user
            System.out.println(System.lineSeparator().repeat(50));
            System.out.println("======== Roles ========");
            System.out.println(String.format("%-2s %-1s", "1.", "Admin"));
            System.out.println(String.format("%-2s %-1s", "2.", "Sales Manager"));
            System.out.println(String.format("%-2s %-1s", "3.", "Purchase Manager"));
            System.out.println(String.format("%-2s %-1s", "0.", "Quit Registration"));
            System.out.print("Enter ROLE for the User:");
            role = Sc.nextLine();
                switch(role){
                    case "1":
                        System.out.println("You selected Admin");
                        role = "Admin";
                        selected = true;
                        break;
                    case "2":
                        System.out.println("You selected Sales Manger");
                        role = "Sales";
                        selected = true;
                        break;
                    case "3":
                        System.out.println("You selected Purchase Manager");
                        role = "Purchase";
                        selected = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                    case "0":
                        break Outer;
                }
            }
        
        if(selected){
            User newUser = new User(username, password, role);
            UserDatabase uDB = new UserDatabase(newUser);
            uDB.registerUser();
        }
        System.out.println(System.lineSeparator().repeat(50));
        
    }
    
    private boolean usernValidation(String username){
        if(!username.matches("^(?=.*[a-zA-Z])[a-zA-Z0-9]{5,15}$")){
            Scanner sc = new Scanner(System.in);
            if(username.length() < 5 || username.length() > 15){
                System.out.println("Invalid username length. Please ensure your username has between 5 and 15 characters.");
            }
            else if(username.matches(".*\\s.*")){
                System.out.println("Please do not include any spaces in the username.");
            }
            else{
                System.out.println("Invalid username format. Please use only letters and digits, and ensure it contains at least one letter.");
            }
            System.out.println("Press [Enter] to continue...");
            sc.nextLine();
            System.out.println(System.lineSeparator().repeat(50));
            return false;
        }
        
        return true;
    }
    
    private boolean passwValidation(String password){
        
        if(!password.matches("^(?=.*[a-zA-Z])(?=.*[!@#$%^&*])(?=.*[0-9])[a-zA-Z0-9!@#$%^&*]{9,20}$")){
            Scanner sc = new Scanner(System.in);
            if(password.length() < 9 || password.length() > 20){
                System.out.println("Invalid password length. Please ensure your username has between 9 and 20 characters.");

            }
            else if(password.matches(".*\\s.*")){
                System.out.println("Please do not include any spaces in the password.");

            }
            else{
                System.out.println("Invalid password format. Please use only letters, digits, and the special characters !@#$%^&*, and ensure it contains at least one letter, digit and special chracters.");

            }
            System.out.println("Press [Enter] to continue...");
            sc.nextLine();
            System.out.println(System.lineSeparator().repeat(50));
            return false;
        }
        return true;
    }
    
}
