/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.ResponseDTO;
import dto.UserDTO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Shakthi
 */
@WebServlet(name = "CheckSignIn", urlPatterns = {"/CheckSignIn"})
public class CheckSignIn extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        ResponseDTO response_DTO = new ResponseDTO();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        
        if(req.getSession().getAttribute("user") != null){
            //already signIn
            
            UserDTO user_DTO = (UserDTO) req.getSession().getAttribute("user");
            response_DTO.setSuccess(true);
            response_DTO.setContent(user_DTO);
            
        }else{
            //not sigIn
            response_DTO.setContent("Not SigIn");
        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(response_DTO));
        
    }
    
}
