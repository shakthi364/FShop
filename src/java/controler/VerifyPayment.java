/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.PayHere;

/**
 *
 * @author Shakthi
 */
@WebServlet(name = "VerifyPayment", urlPatterns = {"/VerifyPayment"})
public class VerifyPayment extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String merchant_id = req.getParameter("merchant_id");
        String order_id = req.getParameter("order_id");
        String payhere_amount = req.getParameter("payhere_amount");
        String payhere_currency = req.getParameter("payhere_currency");
        String status_code = req.getParameter("status_code");
        String md5sig = req.getParameter("md5sig");

        String merchant_secret = "MzA1NzI5MTg3NzIwOTE5OTkwNjEyMDk4Mjg2MDU3MTAzNzg0NTA0Mg==";
        String merchant_secret_md5hash = PayHere.generateMd5(merchant_secret);

        String generatedMd5hash = PayHere.generateMd5(merchant_id + order_id + payhere_amount + payhere_currency + status_code + merchant_secret_md5hash);
        
        if(generatedMd5hash.equals(md5sig) && status_code.equals("2")){
            System.out.println("Payment Complete of"+order_id);
            //update order status paid
        }

    }

}
