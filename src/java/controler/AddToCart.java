/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controler;

import com.google.gson.Gson;
import dto.Cart_DTO;
import dto.ResponseDTO;
import dto.UserDTO;
import entity.Cart;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.smartcardio.Card;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Shakthi
 */
@WebServlet(name = "AddToCart", urlPatterns = {"/AddToCart"})
public class AddToCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ResponseDTO response_DTO = new ResponseDTO();
        Gson gson = new Gson();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            String id = req.getParameter("id");
            String qty = req.getParameter("qty");

            if (!Validation.isInteger(id)) {
                response_DTO.setContent("product not found");
            } else if (!Validation.isInteger(qty)) {
                response_DTO.setContent("Invalid quentuty");
            } else {
                int productId = Integer.parseInt(id);
                int productQty = Integer.parseInt(qty);

                Product product = (Product) session.get(Product.class, productId);

                if (productQty <= 0) {
                    //quentity must be greater then 0
                    response_DTO.setContent("quentity must be greater then 0");
                } else {
                    if (product != null) {

                        if (req.getSession().getAttribute("user") != null) {
                            //DB cart

                            UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");

                            Criteria criteria1 = session.createCriteria(User.class);
                            criteria1.add(Restrictions.eq("email", userDTO.getEmail()));
                            User user = (User) criteria1.uniqueResult();

                            Criteria criteria2 = session.createCriteria(Cart.class);
                            criteria2.add(Restrictions.eq("user", user));
                            criteria2.add(Restrictions.eq("product", product));

                            if (criteria2.list().isEmpty()) {

                                if (productQty <= product.getQty()) {

                                    Cart cart = new Cart();
                                    cart.setProduct(product);
                                    cart.setQty(productQty);
                                    cart.setUser(user);
                                    session.save(cart);
                                    transaction.commit();

                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("product add to the cart");
                                } else {
                                    response_DTO.setContent("quentity not available");
                                }

                            } else {
                                //already item found availabel
                                Cart cartItem = (Cart) criteria2.uniqueResult();

                                if ((cartItem.getQty() + productQty) <= product.getQty()) {
                                    cartItem.setQty(cartItem.getQty() + productQty);
                                    session.update(cartItem);
                                    transaction.commit();

                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("cart item update");
                                } else {
                                    response_DTO.setContent("can't update your cart. quantity not available");
                                }

                            }

                        } else {
                            //Session cart

                            HttpSession httpSession = req.getSession();

                            if (httpSession.getAttribute("sessionCart") != null) {

                                ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) httpSession.getAttribute("sessionCart");

                                Cart_DTO foundCart_DTO = null;

                                for (Cart_DTO cart_DTO : sessionCart) {

                                    if (cart_DTO.getProduct().getId() == product.getId()) {
                                        foundCart_DTO = cart_DTO;
                                        break;
                                    }
                                }

                                if (foundCart_DTO != null) {

                                    if ((foundCart_DTO.getQty() + productQty) <= product.getQty()) {
                                        foundCart_DTO.setQty(foundCart_DTO.getQty() + productQty);

                                        response_DTO.setSuccess(true);
                                        response_DTO.setContent("Session cart update");
                                    } else {
                                        response_DTO.setContent("Quantity not available");
                                    }

                                } else {
                                    if (productQty <= product.getQty()) {
                                        Cart_DTO cart_DTO = new Cart_DTO();
                                        cart_DTO.setProduct(product);
                                        cart_DTO.setQty(productQty);
                                        sessionCart.add(cart_DTO);

                                        response_DTO.setSuccess(true);
                                        response_DTO.setContent("Session cart add");
                                    } else {
                                        response_DTO.setContent("Quantity not available");
                                    }
                                }

                            } else {

                                if (productQty <= product.getQty()) {
                                    ArrayList<Cart_DTO> sessionCart = new ArrayList<>();

                                    Cart_DTO cart_DTO = new Cart_DTO();
                                    cart_DTO.setProduct(product);
                                    cart_DTO.setQty(productQty);
                                    sessionCart.add(cart_DTO);

                                    httpSession.setAttribute("sessionCart", sessionCart);

                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("Session cart add");
                                } else {
                                    response_DTO.setContent("Quantity not available");
                                }
                            }
                        }
                    } else {
                        //product not found
                        response_DTO.setContent("product not found");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response_DTO.setContent("Unable to process your request.");
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(response_DTO));

    }

}
