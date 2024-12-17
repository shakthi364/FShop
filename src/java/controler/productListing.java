/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controler;

import com.google.gson.Gson;
import dto.ResponseDTO;
import dto.UserDTO;
import entity.Category;
import entity.Color;
import entity.Model;
import entity.Product;
import entity.ProductCondition;
import entity.ProductStatus;
import entity.Size;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Shakthi
 */
@WebServlet(name = "productListing", urlPatterns = {"/productListing"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class productListing extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ResponseDTO response_dto = new ResponseDTO();

        Gson gson = new Gson();

        String categorySelectId = req.getParameter("categorySelectId");
        String modelSelectId = req.getParameter("modelSelectId");
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String sizeSelectId = req.getParameter("sizeSelectId");
        String colorSelectId = req.getParameter("colorSelectId");
        String conditionSelectId = req.getParameter("conditionSelectId");
        String price = req.getParameter("price");
        String qty = req.getParameter("qty");

        Part image1 = req.getPart("image1");
        Part image2 = req.getPart("image2");
        Part image3 = req.getPart("image3");
        Part image4 = req.getPart("image4");

        Session sessing = HibernateUtil.getSessionFactory().openSession();

        if (!Validation.isInteger(categorySelectId)) {
            response_dto.setContent("Invalid Category");
        } else if (!Validation.isInteger(modelSelectId)) {
            response_dto.setContent("Invalid Model");
        } else if (title.isEmpty()) {
            response_dto.setContent("Plese fill Product Title");
        } else if (description.isEmpty()) {
            response_dto.setContent("Plese fill Product description");
        } else if (!Validation.isInteger(sizeSelectId)) {
            response_dto.setContent("Invalid Size");
        } else if (!Validation.isInteger(colorSelectId)) {
            response_dto.setContent("Invalid Color");
        } else if (price.isEmpty()) {
            response_dto.setContent("Plese fill Product Price");
        } else if (!Validation.isDoubel(price)) {
            response_dto.setContent("Invalid Price");
        } else if (Double.parseDouble(price) <= 0) {
            response_dto.setContent("Price must be greater than 0");
        } else if (qty.isEmpty()) {
            response_dto.setContent("Plese fill Product Quantity");
        } else if (!Validation.isInteger(qty)) {
            response_dto.setContent("Invalid Quantity");
        } else if (Integer.parseInt(qty) <= 0) {
            response_dto.setContent("Price must be greater than 0");
        } else if (image1.getSubmittedFileName() == null) {
            response_dto.setContent("Plese upload image 1");
        } else if (image2.getSubmittedFileName() == null) {
            response_dto.setContent("Plese upload image 2");
        } else if (image3.getSubmittedFileName() == null) {
            response_dto.setContent("Plese upload image 3");
        } else if (image4.getSubmittedFileName() == null) {
            response_dto.setContent("Plese upload image 4");
        } else {

            Category category = (Category) sessing.get(Category.class, Integer.parseInt(categorySelectId));

            if (category == null) {
                response_dto.setContent("Plese Select a valid Category");
            } else {

                Model model = (Model) sessing.get(Model.class, Integer.parseInt(modelSelectId));

                if (model == null) {
                    response_dto.setContent("Plese Select a valid Model");
                } else {

                    if (model.getCategory().getId() != category.getId()) {
                        response_dto.setContent("Plese Select a valid Model");
                    } else {

                        Size size = (Size) sessing.get(Size.class, Integer.parseInt(sizeSelectId));

                        if (size == null) {
                            response_dto.setContent("Plese Select a valid Size");
                        } else {

                            Color color = (Color) sessing.get(Color.class, Integer.parseInt(colorSelectId));

                            if (color == null) {
                                response_dto.setContent("Plese Select a valid Color");
                            } else {

                                ProductCondition condition = (ProductCondition) sessing.get(ProductCondition.class, Integer.parseInt(conditionSelectId));

                                if (condition == null) {
                                    response_dto.setContent("Plese Select a valid Condition");
                                } else {

                                    Product product = new Product();
                                    product.setColor(color);
                                    product.setDateTime(new Date());
                                    product.setDescription(description);
                                    product.setModel(model);
                                    product.setPrice(Double.parseDouble(price));

                                    ProductStatus productstatus = (ProductStatus) sessing.get(ProductStatus.class, 1);
                                    product.setProductstatus(productstatus);

                                    product.setProductcondition(condition);
                                    product.setQty(Integer.parseInt(qty));
                                    product.setSize(size);
                                    product.setTitle(title);

                                    UserDTO userDto = (UserDTO) req.getSession().getAttribute("user");
                                    Criteria criteria1 = sessing.createCriteria(User.class);
                                    criteria1.add(Restrictions.eq("email", userDto.getEmail()));
                                    User user = (User) criteria1.uniqueResult();
                                    product.setUser(user);

                                    int pid = (int) sessing.save(product);
                                    sessing.beginTransaction().commit();

                                    String applicationPath = req.getServletContext().getRealPath("");
                                    String newApplicationPath = applicationPath.replace("build"+File.separator+"web", "web");

                                    File folder = new File(newApplicationPath + "//product-image//" + pid);
                                    folder.mkdir();

                                    File file1 = new File(folder, "image1.png");
                                    InputStream inputStream1 = image1.getInputStream();
                                    Files.copy(inputStream1, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                    File file2 = new File(folder, "image2.png");
                                    InputStream inputStream2 = image2.getInputStream();
                                    Files.copy(inputStream2, file2.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                    File file3 = new File(folder, "image3.png");
                                    InputStream inputStream3 = image3.getInputStream();
                                    Files.copy(inputStream3, file3.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                    File file4 = new File(folder, "image4.png");
                                    InputStream inputStream4 = image4.getInputStream();
                                    Files.copy(inputStream4, file4.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                    response_dto.setSuccess(true);
                                    response_dto.setContent("New product added");

                                }

                            }

                        }

                    }

                }

            }
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(response_dto));
        System.out.println(gson.toJson(response_dto));
        sessing.close();
    }
}
