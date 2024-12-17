/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Date;
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
@Table (name = "product")
public class Product implements Serializable{
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column (name = "title", nullable = false)
    private String title;
    
    @Column (name = "description", nullable = false)
    private String description;
    
    @Column(name = "price", nullable = false)
    private double price;
    
    @Column(name = "qty", nullable = false)
    private int qty;
    
    @Column (name = "datte_time", nullable = false)
    private Date dateTime;
    
    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;
    
    @ManyToOne
    @JoinColumn(name = "size_id")
    private Size size;
    
    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "condition_id")
    private ProductCondition productcondition;
    
    @ManyToOne
    @JoinColumn(name = "status_id")
    private ProductStatus productstatus;

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProductCondition getProductcondition() {
        return productcondition;
    }

    public void setProductcondition(ProductCondition productcondition) {
        this.productcondition = productcondition;
    }

    public ProductStatus getProductstatus() {
        return productstatus;
    }

    public void setProductstatus(ProductStatus productstatus) {
        this.productstatus = productstatus;
    }
    
    
    
}
