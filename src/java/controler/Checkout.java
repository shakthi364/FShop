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
import entity.Order_Item;
import entity.Order_Status;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.PayHere;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Shakthi
 */
@WebServlet(name = "Checkout", urlPatterns = {"/Checkout"})
public class Checkout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();

        JsonObject requestJsonObject = gson.fromJson(req.getReader(), JsonObject.class);

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);

        HttpSession httpSession = req.getSession();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        boolean isCurrentAddress = requestJsonObject.get("isCurrentAddress").getAsBoolean();
        String first_name = requestJsonObject.get("first_name").getAsString();
        String last_name = requestJsonObject.get("last_name").getAsString();
        String email = requestJsonObject.get("email").getAsString();
        String mobile = requestJsonObject.get("mobile").getAsString();
        String address1 = requestJsonObject.get("address1").getAsString();
        String address2 = requestJsonObject.get("address2").getAsString();
        String city_id = requestJsonObject.get("city").getAsString();
        String postal_code = requestJsonObject.get("postal_code").getAsString();

        if (httpSession.getAttribute("user") != null) {

            UserDTO user_dto = (UserDTO) httpSession.getAttribute("user");
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_dto.getEmail()));
            User user = (User) criteria1.uniqueResult();

            if (isCurrentAddress) {

                Criteria criteria2 = session.createCriteria(Address.class);
                criteria2.add(Restrictions.eq("user", user));
                criteria2.addOrder(Order.desc("id"));
                criteria2.setMaxResults(1);

                if (criteria2.list().isEmpty()) {
                    //current address ont found
                    responseJsonObject.addProperty("message", "Current address not found. plese create a new address.");
                } else {
                    //get Address
                    Address address = (Address) criteria2.list().get(0);

                    saveOrders(session, transaction, user, address, responseJsonObject);
                }

            } else {
                //new address

                if (first_name.isEmpty()) {
                    responseJsonObject.addProperty("message", "Plese fill first name");
                } else if (last_name.isEmpty()) {
                    responseJsonObject.addProperty("message", "Plese fill last name");
                } else if (email.isEmpty()) {
                    responseJsonObject.addProperty("message", "Plese fill Email");
                } else if (mobile.isEmpty()) {
                    responseJsonObject.addProperty("message", "Plese fill Mobile");
                } else if (!Validation.isMobileValid(mobile)) {
                    responseJsonObject.addProperty("message", "Plese Enter Valid Mobile");
                } else if (address1.isEmpty()) {
                    responseJsonObject.addProperty("message", "Plese fill Address 1");
                } else if (address2.isEmpty()) {
                    responseJsonObject.addProperty("message", "Plese fill Address 2");
                } else if (!Validation.isInteger(city_id)) {
                    responseJsonObject.addProperty("message", "Invalid City");
                } else {
                    Criteria criteria3 = session.createCriteria(City.class);
                    criteria3.add(Restrictions.eq("id", Integer.parseInt(city_id)));

                    if (criteria3.list().isEmpty()) {
                        responseJsonObject.addProperty("message", "Invalid City Selected");
                    } else {
                        //city foun
                        City city = (City) criteria3.list().get(0);

                        if (postal_code.isEmpty()) {
                            responseJsonObject.addProperty("message", "Plese fill postel code");
                        } else if (!Validation.isInteger(postal_code)) {
                            responseJsonObject.addProperty("message", "Invalid postel Code");
                        } else {

                            //Create new address
                            Address address = new Address();
                            address.setCity(city);
                            address.setFname(first_name);
                            address.setLname(last_name);
                            address.setLine_1(address1);
                            address.setLine_2(address2);
                            address.setMobile(mobile);
                            address.setPostal_code(postal_code);
                            address.setUser(user);

                            session.save(address);

                            saveOrders(session, transaction, user, address, responseJsonObject);

                        }
                    }
                }

            }

        } else {
            responseJsonObject.addProperty("message", "User not signin");
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJsonObject));

    }

    private void saveOrders(Session session, Transaction transaction, User user, Address address, JsonObject responseJsonObject) {

        try {
            entity.Order order = new entity.Order();
            order.setAddress(address);
            order.setDate_Time(new Date());
            order.setUser(user);
            int order_id = (int) session.save(order);

            session.save(order);

            //get cart item
            Criteria criteria4 = session.createCriteria(Cart.class);
            criteria4.add(Restrictions.eq("user", user));
            List<Cart> cartList = criteria4.list();

            //get order status 5 payment panding
            Order_Status order_Status = (Order_Status) session.get(Order_Status.class, 5);

            //create order item in bd
            double amount = 0;
            String item = "";
            for (Cart cartItem : cartList) {

                //calculete amount
                amount += cartItem.getQty() * cartItem.getProduct().getPrice();

                //get item details
                item += cartItem.getProduct().getTitle() + " x" + cartItem.getQty() + " ";

                //get product
                Product product = cartItem.getProduct();

                Order_Item order_Item = new Order_Item();
                order_Item.setOrder(order);
                order_Item.setOrder_status(order_Status);
                order_Item.setProduct(product);
                order_Item.setQty(cartItem.getQty());
                session.save(order_Item);

                //update product qty in bd
                product.setQty(product.getQty() - cartItem.getQty());
                session.update(product);

                //delet cart item from bd
                session.delete(cartItem);
            }

            transaction.commit();

            //start: set payment date
            String merchant_id = "1221041";
            String amountFormated = new DecimalFormat("0.00").format(amount);
            String currency = "LKR";
            String merchantSecret = "MzA1NzI5MTg3NzIwOTE5OTkwNjEyMDk4Mjg2MDU3MTAzNzg0NTA0Mg==";
            String merchantSecretMd5Hash = PayHere.generateMd5(merchantSecret);

            JsonObject payhere = new JsonObject();
            payhere.addProperty("sandbox", true);
            payhere.addProperty("merchant_id", "1221041");

            payhere.addProperty("return_url", "");
            payhere.addProperty("cancel_url", "");
            payhere.addProperty("notify_url", "https://a0b1-2402-d000-a400-37f-b04e-d814-2ee5-8a46.ngrok-free.app/Fshop/VerifyPayment");

            
            System.out.println("ok");
            payhere.addProperty("first_name", user.getFirst_name());
            payhere.addProperty("last_name", user.getLast_name());
            payhere.addProperty("email", user.getEmail());
            payhere.addProperty("phone", "");
            payhere.addProperty("address", "");
            payhere.addProperty("city", "");
            payhere.addProperty("country", "");
            payhere.addProperty("order_id", String.valueOf(order_id));
            payhere.addProperty("items", "helooooo");
            payhere.addProperty("currency", "LKR");
            payhere.addProperty("amount", new DecimalFormat("0.00").format(amount));

            //genaret MD% Hash
            String md5Hash = PayHere.generateMd5(merchant_id + order_id + amountFormated + currency + merchantSecretMd5Hash);
            payhere.addProperty("hash", md5Hash);

            responseJsonObject.addProperty("success", true);
            responseJsonObject.addProperty("message", "Checkout Completed");

            responseJsonObject.add("payhereJson", new Gson().toJsonTree(payhere));

        } catch (Exception e) {
            transaction.rollback();
        }

    }

}
