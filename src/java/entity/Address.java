/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Shakthi
 */
@Entity
@Table (name = "address")
public class Address {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "fname",length = 50, nullable = false)
    private String fname;
    
    @Column(name = "lname",length = 50, nullable = false)
    private String lname;
    
    @Column(name = "line_1", nullable = false)
    private String line_1;
    
    @Column(name = "line_2", nullable = false)
    private String line_2;
    
    @Column(name = "postal_code",length = 10, nullable = false)
    private String postal_code;
    
    @Column(name = "mobile",length = 15, nullable = false)
    private String mobile;
    
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getLine_1() {
        return line_1;
    }

    public void setLine_1(String line_1) {
        this.line_1 = line_1;
    }

    public String getLine_2() {
        return line_2;
    }

    public void setLine_2(String line_2) {
        this.line_2 = line_2;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
}
