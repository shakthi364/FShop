/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.GsonBuildConfig;
import dto.ResponseDTO;
import dto.UserDTO;
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
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Shakthi
 */
@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ResponseDTO responseDTO = new ResponseDTO();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        UserDTO userDTO = gson.fromJson(req.getReader(), UserDTO.class);

        if (userDTO.getEmail().isEmpty()) {
            responseDTO.setContent("Plese Enter Your Email");
        } else if (!Validation.isEmailValid(userDTO.getEmail())) {
            responseDTO.setContent("Plese Enter Valid Email");
        } else if (userDTO.getFirst_name().isEmpty()) {
            responseDTO.setContent("Plese Enter Your First Name");
        } else if (userDTO.getLast_name().isEmpty()) {
            responseDTO.setContent("Plese Enter Your Last Name");
        } else if (userDTO.getPassword().isEmpty()) {
            responseDTO.setContent("Plese Enter Your Password");
        } else if (!Validation.isPasswordValid(userDTO.getPassword())) {
            responseDTO.setContent("Password must include at least one uppercase"
                    + " letter, number, special character and at least eight character long");
        } else {

            Session sesstin = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria1 = sesstin.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", userDTO.getEmail()));

            if (!criteria1.list().isEmpty()) {
                responseDTO.setContent("User with this Email already exists");
            } else {

                //genarate verification code
                int code = (int) (Math.random() * 1000000);

                final User user = new User();
                user.setEmail(userDTO.getEmail());
                user.setFirst_name(userDTO.getFirst_name());
                user.setLast_name(userDTO.getLast_name());
                user.setPassword(userDTO.getPassword());
                user.setVarification(String.valueOf(code));

//                Thread sendMailThread = new Thread() {
//                    @Override
//                    public void run() {
//
//                        Mail.sendMail(user.getEmail(), "Fshop Verification",
//                                "<h1 style=\"color:#6482AD;\">Your Verification Code:" + user.getVarification() + "</h1>");
//                    }
//
//                };

//                sendMailThread.start();
                
                sesstin.save(user);
                sesstin.beginTransaction().commit();
                
                req.getSession().setAttribute("email", userDTO.getEmail());
                responseDTO.setSuccess(true);
                responseDTO.setContent("Registration Complete. Plese check your inbox for verification code!");

            }

            sesstin.clear();

        }
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseDTO));
        System.out.println(gson.toJson(responseDTO));
    }

}
