/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.UserDTO;
import entity.Address;
import entity.Cart;
import entity.City;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Shakthi
 */
@WebServlet(name = "LoadCheckout", urlPatterns = {"/LoadCheckout"})
public class LoadCheckout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);
        
        HttpSession httpSession = req.getSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        
         try {
            if (httpSession.getAttribute("user") != null) {
                
                UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");
                
                // Get user from DB
                Criteria userCriteria = session.createCriteria(User.class);
                userCriteria.add(Restrictions.eq("email", userDTO.getEmail()));
                User user = (User) userCriteria.uniqueResult();
                
                jsonObject.add("email", gson.toJsonTree(userDTO.getEmail()));
                
                if (user == null) {
                    jsonObject.addProperty("message", "User not found in database");
                    resp.setContentType("application/json");
                    resp.getWriter().write(gson.toJson(jsonObject));
                    return;
                }
                
                // Get user last address
                Criteria addressCriteria = session.createCriteria(Address.class);
                addressCriteria.add(Restrictions.eq("user", user));
                addressCriteria.addOrder(Order.desc("id"));
                addressCriteria.setMaxResults(1);
                List<Address> addressList = addressCriteria.list();
                
                Address address = null;
                if (!addressList.isEmpty()) {
                    address = addressList.get(0);
                    address.setUser(null);
                    jsonObject.add("address", gson.toJsonTree(address));
                } else {
                    jsonObject.addProperty("address", "No address found");
                }
                
                // Get cities from DB
                Criteria cityCriteria = session.createCriteria(City.class);
                cityCriteria.addOrder(Order.asc("name"));
                List<City> cityList = cityCriteria.list();
                jsonObject.add("cityList", gson.toJsonTree(cityList));
                
                // Get cart items
                Criteria cartCriteria = session.createCriteria(Cart.class);
                cartCriteria.add(Restrictions.eq("user", user));
                List<Cart> cartList = cartCriteria.list();
                
                for (Cart cart : cartList) {
                    cart.setUser(null); 
                    if (cart.getProduct() != null) {
                        cart.getProduct().setUser(null);
                    }
                }
                jsonObject.add("cartList", gson.toJsonTree(cartList));
                
                jsonObject.addProperty("success", true);
            } else {
                jsonObject.addProperty("message", "Not signed in");
            }
        } catch (Exception e) {
            jsonObject.addProperty("message", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(jsonObject));
    }
    
}
