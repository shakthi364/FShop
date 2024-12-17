/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Category;
import entity.Color;
import entity.Model;
import entity.Product;
import entity.Size;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Shakthi
 */
@WebServlet(name = "SearchProduct", urlPatterns = {"/SearchProduct"})
public class SearchProduct extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);

        //get requst data
        JsonObject requestJsonObject = gson.fromJson(req.getReader(), JsonObject.class);

        Session session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria1 = session.createCriteria(Product.class);

        String selectedCategory = requestJsonObject.get("selectedCategory").getAsString();
        String selectedColor = requestJsonObject.get("selectedColor").getAsString();
        String selectedSize = requestJsonObject.get("selectedSize").getAsString();
        double price_range_min = requestJsonObject.get("price_range_min").getAsDouble();
        double price_range_max = requestJsonObject.get("price_range_max").getAsDouble();
        String sort_text = requestJsonObject.get("sort_text").getAsString();

        //filter category selected
        Criteria criteria2 = session.createCriteria(Category.class);
        if (selectedCategory.equals("all_category")) {
            List<Category> allcategoryList = criteria2.list();

            Criteria criteria3 = session.createCriteria(Model.class);
            criteria3.add(Restrictions.in("category", allcategoryList));
            List<Model> modelList = criteria3.list();

            criteria1.add(Restrictions.in("model", modelList));

        } else {
            criteria2.add(Restrictions.eq("name", selectedCategory));
            Category category = (Category) criteria2.uniqueResult();

            Criteria criteria3 = session.createCriteria(Model.class);
            criteria3.add(Restrictions.eq("category", category));
            List<Model> modelList = criteria3.list();

            criteria1.add(Restrictions.in("model", modelList));
        }

        //filter color selected
        if (selectedColor.equals("all_color")) {
            Criteria criteria4 = session.createCriteria(Color.class);
            List<Color> colorList = criteria4.list();

            criteria1.add(Restrictions.in("color", colorList));
        } else {
            Criteria criteria4 = session.createCriteria(Color.class);
            criteria4.add(Restrictions.eq("name", selectedColor));
            Color color = (Color) criteria4.uniqueResult();

            criteria1.add(Restrictions.eq("color", color));
        }

        //filter size selected
        if (selectedSize.equals("all_size")) {
            Criteria criteria5 = session.createCriteria(Size.class);
            List<Size> sizeList = criteria5.list();

            criteria1.add(Restrictions.in("size", sizeList));
        } else {
            Criteria criteria5 = session.createCriteria(Size.class);
            criteria5.add(Restrictions.eq("value", selectedSize));
            Size size = (Size) criteria5.uniqueResult();

            criteria1.add(Restrictions.eq("size", size));
        }

        //filter price selected
        criteria1.add(Restrictions.ge("price", price_range_min));
        criteria1.add(Restrictions.le("price", price_range_max));

        //filter product sort
        if (sort_text.equals("Sort by Latest")) {
            criteria1.addOrder(Order.desc("id"));
        } else if (sort_text.equals("Sort by Oldest")) {
            criteria1.addOrder(Order.asc("id"));
        } else if (sort_text.equals("Sort by Name")) {
            criteria1.addOrder(Order.asc("title"));
        } else if (sort_text.equals("Sort by Price")) {
            criteria1.addOrder(Order.asc("price"));
        }

        responseJsonObject.addProperty("allproductCount", criteria1.list().size());

        int firstRusult = requestJsonObject.get("firstRusult").getAsInt();
        criteria1.setFirstResult(firstRusult);
        criteria1.setMaxResults(6);

        criteria1.addOrder(Order.desc("id"));
        List<Product> productList = criteria1.list();

        for (Product product : productList) {
            product.setUser(null);
        }

        responseJsonObject.addProperty("success", true);
        responseJsonObject.add("productList", gson.toJsonTree(productList));

        //send response
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJsonObject));
    }
}
