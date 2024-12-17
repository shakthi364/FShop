/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Shakthi
 */
@Entity
@Table (name = "user")
public class User implements Serializable{
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "first_name",length = 45,nullable = false)
    private String first_name;
    
    @Column(name = "last_name",length = 45,nullable = false)
    private String last_name;
    
    @Column(name = "email",length = 45,nullable = false)
    private String email;
    
    @Column(name = "password",length = 45,nullable = false)
    private String password;
    
    @Column(name = "varification",length = 45,nullable = false)
    private String varification;

    public User() { }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVarification() {
        return varification;
    }

    public void setVarification(String varification) {
        this.varification = varification;
    }
    
}