/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

/**
 *
 * @author verno
 */
public class User {
    private String username;
    private String password;
    private String role;
    
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
    
    public User(String username,String password, String role){
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    public String getUname(){
        return username;
    }
    
    public String getUpass(){
        return password;
    }
    
    public String getRole(){
        return role;
    }
}
