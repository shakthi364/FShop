/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 *
 * @author Shakthi
 */
@WebServlet(name = "ProductLoad", urlPatterns = {"/ProductLoad"})
public class ProductLoad extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Gson gson = new Gson();
        
        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Criteria criteria1 = session.createCriteria(Product.class);
        Criteria criteria2 = session.createCriteria(Product.class);
        
        criteria1.setFirstResult(0);
        criteria1.setMaxResults(8);
        criteria1.addOrder(Order.desc("id"));
        List<Product> productList1 = criteria1.list();
        
        for(Product product : productList1){
            product.setUser(null);
        }
        
        criteria2.setFirstResult(0);
        criteria2.setMaxResults(16);
        criteria2.addOrder(Order.desc("id"));
        List<Product> productList2 = criteria2.list();
        
        for(Product product : productList2){
            product.setUser(null);
        }
        
        responseJsonObject.addProperty("success", true);
        responseJsonObject.add("productList1", gson.toJsonTree(productList1));
        responseJsonObject.add("productList2", gson.toJsonTree(productList2));
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJsonObject));
    }
}
