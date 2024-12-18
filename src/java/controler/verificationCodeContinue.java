/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Shakthi
 */
@WebServlet(name = "verificationCodeContinue", urlPatterns = {"/verificationCodeContinue"})
public class verificationCodeContinue extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        System.out.println("ok");
        
        Gson gson = new Gson();

        JsonObject requstJsonObject = gson.fromJson(req.getReader(), JsonObject.class);
        String email = requstJsonObject.get("email").getAsString();
        String verificationcode = requstJsonObject.get("verificationcode").getAsString();

//        System.out.println(email);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);

        try {

            if (verificationcode.isEmpty() || verificationcode == null) {
                responseJsonObject.addProperty("content", "Plese Enter Your Code.");
            } else if (email.isEmpty()) {
                responseJsonObject.addProperty("content", "No Email");
            } else {
                Criteria criteria1 = session.createCriteria(User.class);
                criteria1.add(Restrictions.eq("email", email));
                criteria1.add(Restrictions.eq("varification", verificationcode));

                if (!criteria1.list().isEmpty()) {
                    User user = (User) criteria1.list().get(0);
                    user.setVarification("Verified");

                    session.update(user);
                    session.beginTransaction().commit();

                    responseJsonObject.addProperty("success", true);
                    responseJsonObject.addProperty("content", "Verification Mach Succesfull");
                }else{
                    responseJsonObject.addProperty("content", "Invalid Code Plese Check Email");
                }
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            responseJsonObject.addProperty("content", "An error occurred while processing your request.");
        } finally {
            session.close();
        }

        resp.setContentType("application/json");
        resp.getWriter().write(responseJsonObject.toString());

    }

}