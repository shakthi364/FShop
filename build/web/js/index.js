/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

async function checkSignIn(){
    
    const responce = await fetch("CheckSignIn");

    if (responce.ok) {

        const json = await responce.json();
        
        if(json.success){
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
            
        }else{
            console.log("Not SignIn");
        }
        
    }
}

async function viewCart(){
    
    const response = await fetch("cart.html");
    
    if(response.ok){
        const cartHtmlText = await response.text();
//        console.log(cartHtmlText);
        
        const parser = new DOMParser();
        const cartHtml = parser.parseFromString(cartHtmlText,"text/html");
        
        const cart_main = cartHtml.querySelector("#cari_main");
        
        document.querySelector("#main_wrapper").innerHTML = cart_main.innerHTML;
        
        loadCartItem();
        
    }
}
