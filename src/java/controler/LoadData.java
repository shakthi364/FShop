/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Category;
import entity.Color;
import entity.Product;
import entity.Size;
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
@WebServlet(name = "LoadData", urlPatterns = {"/LoadData"})
public class LoadData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);
        
        Gson gson = new Gson();
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        //get category list
        Criteria criteria1 = session.createCriteria(Category.class);
        List<Category> categoryList = criteria1.list();
        jsonObject.add("categoryList", gson.toJsonTree(categoryList));
        
        //get color list
        Criteria criteria2 = session.createCriteria(Color.class);
        List<Color> colorList = criteria2.list();
        jsonObject.add("colorList", gson.toJsonTree(colorList));
        
        //get size list
        Criteria criteria3 = session.createCriteria(Size.class);
        List<Size> sizeList = criteria3.list();
        jsonObject.add("sizeList", gson.toJsonTree(sizeList));
        
        //get product list
        Criteria criteria4 = session.createCriteria(Product.class);
        
        //get last product
        criteria4.addOrder(Order.desc("id"));
        jsonObject.addProperty("allproductCount", criteria4.list().size());
        
        //set product range
        criteria4.setFirstResult(0);
        criteria4.setMaxResults(6);
        
        List<Product> productList = criteria4.list();
        
        //remove user from product
        for(Product product : productList){
            product.setUser(null);
        }
        
        jsonObject.add("productList", gson.toJsonTree(productList));
        
        jsonObject.addProperty("success", true);
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(jsonObject));
    }
}
