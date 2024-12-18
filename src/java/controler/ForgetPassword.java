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
import model.Mail;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Shakthi
 */
@WebServlet(name = "ForgetPassword", urlPatterns = {"/ForgetPassword"})
public class ForgetPassword extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Gson gson = new Gson();
        
        JsonObject requstJsonObject = gson.fromJson(req.getReader(), JsonObject.class);
        String email = requstJsonObject.get("email").getAsString();
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);
        
        try {
          
        if(email == null ||email.isEmpty()){
            responseJsonObject.addProperty("content","Plese Enter Your Email.");
        }else if(!Validation.isEmailValid(email)){
            responseJsonObject.addProperty("content", "Plese Enter Valid Email.");
        }else{
            
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", email));
            User user = (User) criteria1.uniqueResult();
            
            if(!criteria1.list().isEmpty()){
                
                int code = (int) (Math.random() * 1000000);
                
                user.setVarification(String.valueOf(code));
                
                session.update(user);
                session.beginTransaction().commit();
                
                
                Thread sendThread = new Thread(){
                    @Override
                    public void run(){
                        
                        Mail.sendMail(user.getEmail(), "Fshop Verification",
                                "<h1 style=\"color:#6482AD;\">Your Verification Code:" + user.getVarification() + "</h1>");
                    }
                };
                
//                sendThread.start();
                
                responseJsonObject.add("email", gson.toJsonTree(user.getEmail()));
                responseJsonObject.addProperty("success", true);
                responseJsonObject.addProperty("content", "Verification code sent successfully.");
            }else{
                responseJsonObject.addProperty("content", "No user found with this email.");
            }
            
        }
        
          
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            responseJsonObject.addProperty("content", "An error occurred while processing your request.");
        } finally{
            session.close();
        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(responseJsonObject.toString());
        
    }

   
}
