/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Cart_DTO;
import dto.ResponseDTO;
import dto.UserDTO;
import entity.Cart;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ResponseDTO responseDTO = new ResponseDTO();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        UserDTO userDTO = gson.fromJson(req.getReader(), UserDTO.class);

        if (userDTO.getEmail().isEmpty()) {
            responseDTO.setContent("Plese Enter Your Email");
        } else if (userDTO.getPassword().isEmpty()) {
            responseDTO.setContent("Plese Enter Your password");
        } else {

            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", userDTO.getEmail()));
            criteria1.add(Restrictions.eq("password", userDTO.getPassword()));

            if (!criteria1.list().isEmpty()) {

                User user = (User) criteria1.list().get(0);

                if (!user.getVarification().equals("Verified")) {
                    //not verified
                    req.getSession().setAttribute("email", userDTO.getEmail());
                    responseDTO.setContent("Unverified");
                } else {
                    //verified
                    userDTO.setFirst_name(user.getFirst_name());
                    userDTO.setLast_name(user.getLast_name());
                    userDTO.setPassword(null);
                    req.getSession().setAttribute("user", userDTO);

                    //transfer session cart to DB cart
                    if (req.getSession().getAttribute("sessionCart") != null) {

                        ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) req.getSession().getAttribute("sessionCart");

                        Criteria criteria2 = session.createCriteria(Cart.class);
                        criteria2.add(Restrictions.eq("user", user));
                        List<Cart> dbCart = criteria2.list();

                        if (dbCart.isEmpty()) {
                            //db cart empty
                            for (Cart_DTO cart_DTO : sessionCart) {
                                Cart cart = new Cart();
                                cart.setProduct(cart_DTO.getProduct());
                                cart.setQty(cart_DTO.getQty());
                                cart.setUser(user);
                                session.save(cart);
                            }
                        } else {
                            //found item in DB cart
                            for (Cart_DTO cart_DTO : sessionCart) {

                                boolean isFondDbCart = false;

                                for (Cart cart : dbCart) {
                                    if (cart_DTO.getProduct().getId() == cart.getProduct().getId()) {
                                        //same item found in session cart & DB cart
                                        isFondDbCart = true;
                                        
                                        if((cart_DTO.getQty() + cart.getQty()) <= cart.getProduct().getQty()){
                                            //quentity availabel
                                            cart.setQty(cart_DTO.getQty()+cart.getQty());
                                            session.update(cart);
                                        }else{
                                            //quentity not availabel
                                            //set max availabel qty
                                            cart.setQty(cart.getProduct().getQty());
                                            session.update(cart);
                                        }
                                    }
                                }

                                if (!isFondDbCart) {
                                    //not found in DB cart
                                    Cart cart = new Cart();
                                    cart.setProduct(cart_DTO.getProduct());
                                    cart.setQty(cart_DTO.getQty());
                                    cart.setUser(user);
                                    session.save(cart);
                                }

                            }

                        }
                        req.getSession().removeAttribute("sessionCart");
                        session.beginTransaction().commit();
                    }

                    responseDTO.setSuccess(true);
                    responseDTO.setContent("Signin success");
                }

            } else {
                responseDTO.setContent("Invalid details! plese try agein");
            }
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseDTO));
        System.out.println(gson.toJson(responseDTO));

    }
}
