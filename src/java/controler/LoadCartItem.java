/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controler;

import com.google.gson.Gson;
import dto.Cart_DTO;
import dto.UserDTO;
import entity.Cart;
import entity.Product;
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
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Shakthi
 */
@WebServlet(name = "LoadCartItem", urlPatterns = {"/LoadCartItem"})
public class LoadCartItem extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();
        HttpSession httpSession = req.getSession();
        ArrayList<Cart_DTO> Cart_DTO_List = new ArrayList<>();

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {

            if (httpSession.getAttribute("user") != null) {

                UserDTO user_DTO = (UserDTO) httpSession.getAttribute("user");

                Criteria criteria1 = session.createCriteria(User.class);
                criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
                User user = (User) criteria1.uniqueResult();

                Criteria criteria2 = session.createCriteria(Cart.class);
                criteria2.add(Restrictions.eq("user", user));

                List<Cart> cartList = criteria2.list();

                for (Cart cart : cartList) {

                    Cart_DTO cart_DTO = new Cart_DTO();

                    Product product = cart.getProduct();
                    product.setUser(null);
                    cart_DTO.setProduct(product);

                    cart_DTO.setQty(cart.getQty());

                    Cart_DTO_List.add(cart_DTO);

                }

            } else {

                if (httpSession.getAttribute("sessionCart") != null) {

                    Cart_DTO_List = (ArrayList<Cart_DTO>) httpSession.getAttribute("sessionCart");

                    for (Cart_DTO cart_DTO : Cart_DTO_List) {
                        cart_DTO.getProduct().setUser(null);
                    }

                } else {

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        session.close();
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(Cart_DTO_List));

    }
}
