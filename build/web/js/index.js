/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

async function checkSignIn() {

    loadProduct();

    const responce = await fetch("CheckSignIn");

    if (responce.ok) {

        const json = await responce.json();

        if (json.success) {
            const user = json.content;
            console.log(user);

            let st_navbav_nav = document.getElementById("st-navbav-nav");
            let st_login_link = document.getElementById("st-login-link");
            let st_register_link = document.getElementById("st-register-link");

            st_login_link.remove();
            st_register_link.remove();

            let new_a_tag1 = document.createElement("a");
            new_a_tag1.innerHTML = user.first_name + " " + user.last_name;
            st_navbav_nav.appendChild(new_a_tag1);

            let logi_in_button = document.getElementById("logi-in-button");
            logi_in_button.href = "SignOut";
            logi_in_button.innerHTML = "Sign Out";

            let div_i = document.getElementById("div-1");
            div_i.remove();

        } else {
            console.log("Not SignIn");
        }

    }
}

async function viewCart() {

    const response = await fetch("cart.html");

    if (response.ok) {
        const cartHtmlText = await response.text();
//        console.log(cartHtmlText);

        const parser = new DOMParser();
        const cartHtml = parser.parseFromString(cartHtmlText, "text/html");

        const cart_main = cartHtml.querySelector("#cari_main");

        document.querySelector("#main_wrapper").innerHTML = cart_main.innerHTML;

        loadCartItem();

    }
}

async function loadProduct() {

    const response = await fetch("ProductLoad");

    if (response.ok) {

        const json = await response.json();

        if (json.success) {
            console.log(json);
            
            let product_load_main_div = document.getElementById("product_load_main_div");
            let product_load_sub_div = document.getElementById("product_load_sub_div");
            
            product_load_main_div.innerHTML="";
            
            let product_load_main_div2 = document.getElementById("product_load_main_div2");
            let product_load_sub_div2 = document.getElementById("product_load_sub_div2");
            
            product_load_main_div2.innerHTML="";
            
            json.productList1.forEach(product =>{
                let product_load_sub_div_clone = product_load_sub_div.cloneNode(true);
                
                product_load_sub_div_clone.querySelector("#product_load_img").src = "product-image/" + product.id + "/image1.png";
                product_load_sub_div_clone.querySelector("#product_load_title").innerHTML = product.title;
                product_load_sub_div_clone.querySelector("#product_load_view").href = "detail.html?id="+product.id;
                product_load_sub_div_clone.querySelector("#product_load_view_i").href = "detail.html?id="+product.id;
                product_load_sub_div_clone.querySelector("#product_load_cart").addEventListener("click",e=>{
                    addToCart(product.id,1);
                    e.preventDefault();
                });
                product_load_sub_div_clone.querySelector("#product_load_price").innerHTML = new Intl.NumberFormat(
                "en-US", {
                    minimumFractionDigits: 2
                }).format(product.price);
                product_load_sub_div_clone.querySelector("#product_load_price").innerHTML = new Intl.NumberFormat(
                "en-US", {
                    minimumFractionDigits: 2
                }).format((product.price)* 10/100 +(product.price));
                
                product_load_main_div.appendChild(product_load_sub_div_clone);
                
            });
            
            json.productList2.forEach(product =>{
                let product_load_sub_div_clone2 = product_load_sub_div2.cloneNode(true);
                
                product_load_sub_div_clone2.querySelector("#product_load_img2").src = "product-image/" + product.id + "/image1.png";
                product_load_sub_div_clone2.querySelector("#product_load_title2").innerHTML = product.title;
                product_load_sub_div_clone2.querySelector("#product_load_view2").href = "detail.html?id="+product.id;
                product_load_sub_div_clone2.querySelector("#product_load_view2_i").href = "detail.html?id="+product.id;
                product_load_sub_div_clone2.querySelector("#product_load_cart2").addEventListener("click",e=>{
                    addToCart(product.id,1);
                    e.preventDefault();
                });
                product_load_sub_div_clone2.querySelector("#product_load_price2").innerHTML = new Intl.NumberFormat(
                "en-US", {
                    minimumFractionDigits: 2
                }).format(product.price);
                product_load_sub_div_clone2.querySelector("#product_load_price2").innerHTML = new Intl.NumberFormat(
                "en-US", {
                    minimumFractionDigits: 2
                }).format((product.price)* 10/100 +(product.price));
                
                product_load_main_div2.appendChild(product_load_sub_div_clone2);
                
            });
            
        }else{
            console.log("error1");
        }

    } else {
        console.log("error");
    }
}

async  function addToCart(id, qty) {
//    console.log("add to cart: " + id);
//    console.log("add to qty: " + qty);

    const responce = await fetch(
            "AddToCart?id=" + id + "&qty=" + qty
            );

    if (responce.ok) {

        const json = await responce.json();

        if (json.success) {
            notyf.success(json.content);
        } else {
            notyf.error(json.content);
        }

    } else {
        notyf.error("Unable to process your request.");
    }
}