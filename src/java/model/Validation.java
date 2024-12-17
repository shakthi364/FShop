/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Shakthi
 */
public class Validation {
    
    public static boolean isEmailValid(String email){
        return email.matches("[a-zA-Z0-9.*%±]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}");
    }
    
    public static boolean isMobileValid(String mobile){
        return mobile.matches("^[0]{1}[7]{1}[01245678]{1}[0-9]{7}$");
    }
    
    public static boolean isPasswordValid(String password){
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }
    
    public static boolean isDoubel(String text){
        return text.matches("^\\d+(\\.\\d{2})?$");
    }
    
    public static boolean isInteger(String text){
        return text.matches("^\\d+$");
    }
    
}
