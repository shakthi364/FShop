/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.ResponseDTO;
import entity.Model;
import entity.Product;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Shakthi
 */
@WebServlet(name = "LoadSingleProduct", urlPatterns = {"/LoadSingleProduct"})
public class LoadSingleProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
            Gson gson = new Gson();
            Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            
            String productId = req.getParameter("id");
            
            
            if(Validation.isInteger(productId)){
                
                Product product = (Product) session.get(Product.class, Integer.parseInt(productId));
                product.getUser().setPassword(null);
                product.getUser().setVarification(null);
                product.getUser().setEmail(null);
                
                Criteria criteria1 = session.createCriteria(Model.class);
                criteria1.add(Restrictions.eq("category", product.getModel().getCategory()));
                List<Model> modelList = criteria1.list();
                
                Criteria criteria2 = session.createCriteria(Product.class);
                criteria2.add(Restrictions.in("model", modelList));
                criteria2.add(Restrictions.ne("id", product.getId()));
                criteria2.setMaxResults(4);
                
                List<Product> productList = criteria2.list();
                
                for(Product product1 : productList){
                    product1.getUser().setPassword(null);
                    product1.getUser().setVarification(null);
                    product1.getUser().setEmail(null);
                }
                
                JsonObject jsonObject = new JsonObject();
                jsonObject.add("product", gson.toJsonTree(product));
                jsonObject.add("productList", gson.toJsonTree(productList));
                
                
                resp.setContentType("application/json");
                resp.getWriter().write(gson.toJson(jsonObject));
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    
}
