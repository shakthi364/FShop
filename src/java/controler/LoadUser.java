/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.UserDTO;
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
@WebServlet(name = "LoadUser", urlPatterns = {"/LoadUser"})
public class LoadUser extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);

        HttpSession httpSession = req.getSession();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            
            if(httpSession.getAttribute("user") != null){
                
                UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");
                
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
                
                
                jsonObject.addProperty("success", true);
                
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
