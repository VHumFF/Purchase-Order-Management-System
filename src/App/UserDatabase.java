
package App;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


/**
User database class that handles text file related to user account and file handing operation
 */
public class UserDatabase {
    private User userData;
    
    public UserDatabase(){
        
    }
    
    public UserDatabase(User user){
        this.userData = user;
    }

    public void registerUser(){
        String username = userData.getUname();
        String password = userData.getUpass();
        String role = userData.getRole();
        Scanner Sc = new Scanner(System.in);
          
        Boolean userExist = checkUserExist(username);
        if(userExist){
            System.out.println(System.lineSeparator().repeat(50));
            System.out.println("User registration failed. Username Exist.");
            System.out.println("Press [Enter] to continue...");
            
            Sc.nextLine();
            return;
        }
        
        File file = new File("Database/UserAccount.txt");
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            String userAcc = username + ";" + password + ";" + role + "\n";


            writer.write(userAcc);
            writer.close();
            
            System.out.println(System.lineSeparator().repeat(50));
            System.out.println("User registration successful.");
            System.out.println("Press [Enter] to continue...");
            
            Sc.nextLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private boolean checkUserExist(String username){
        
        File file = new File("Database/UserAccount.txt");
        try
        {
            String[] uInfo;
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            //check whether username exist
            while((line = reader.readLine()) != null){
                uInfo = line.strip().split(";");
                if(uInfo[0].equals(username)){
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    
    public String validateUserCredential(){
        String username = userData.getUname();
        String password = userData.getUpass();

        File file = new File("Database/UserAccount.txt");
        try
        {
            
            String[] uInfo;
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            //match username and password in database
            while((line = reader.readLine()) != null){
                uInfo = line.strip().split(";");
                if(uInfo[0].equals(username)){
                    if(uInfo[1].equals(password)){
                        
                        //return user role
                        return uInfo[2];
                    }
                    else{
                        return null;
                    }
                }
                else{
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public void storeCurrentUserName(){
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Database/UserSession.txt", false));
            writer.write(userData.getUname());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getCurrentUser(){
        File file = new File("Database/UserSession.txt");
        String username = "";
        try
        {
            String line;
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            //get current user's username
            username = reader.readLine().strip();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return username;
    }
}
