/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

const notyf = new Notyf({
    duration: 5000,
    position: {x: 'right', y: 'top'},
});


async function loadProduct() {

    const parameters = new URLSearchParams(window.location.search);

    if (parameters.has("id")) {
        const productId = parameters.get("id");
        
        const response = await fetch("LoadSingleProduct?id=" + productId);

        if (response.ok) {

            const json = await response.json();
            console.log(json.product);
            console.log(json.productList);

            const id = json.product.id;
            document.getElementById("image1").src = "product-image/" + id + "/image1.png";
            document.getElementById("image2").src = "product-image/" + id + "/image2.png";
            document.getElementById("image3").src = "product-image/" + id + "/image3.png";
            document.getElementById("image4").src = "product-image/" + id + "/image4.png";


            document.getElementById("product-title").innerHTML = json.product.title;
            document.getElementById("product-price").innerHTML = new Intl.NumberFormat(
                    "en-US", {
                        minimumFractionDigits: 2
                    }).format(json.product.price);
            document.getElementById("product-price-des").innerHTML = new Intl.NumberFormat(
                    "en-US", {
                        minimumFractionDigits: 2
                    }).format((json.product.price) * 10 / 100 + (json.product.price));
            document.getElementById("product-description").innerHTML = json.product.description;

            document.getElementById("product-size").innerHTML = json.product.size.value;
            document.getElementById("product-color").innerHTML = json.product.color.name;

            document.getElementById("add-to-cart-main").addEventListener(
                    "click",
                    (e) => {
                addToCart(json.product.id, document.getElementById("add-to-cart-qty").value);
            });

            let similarProductHtml = document.getElementById("similar-product");
            document.getElementById("similar-product-main").innerHTML = "";

            json.productList.forEach(item => {

                let productClouneHtml = similarProductHtml.cloneNode(true);

                productClouneHtml.querySelector("#similar-product-image").src = "product-image/" + item.id + "/image1.png";
                productClouneHtml.querySelector("#similar-product-title").innerHTML = item.title;
                productClouneHtml.querySelector("#similar-product-a1").href = "detail.html?id=" + item.id;
                productClouneHtml.querySelector("#similar-product-a2").href = "detail.html?id=" + item.id;
                productClouneHtml.querySelector("#similar-product-a2").href = "detail.html?id=" + item.id;
                productClouneHtml.querySelector("#similar-product-price").innerHTML = new Intl.NumberFormat(
                        "en-US", {
                            minimumFractionDigits: 2
                        }).format(item.price);
                productClouneHtml.querySelector("#similar-product-price-des").innerHTML = new Intl.NumberFormat(
                        "en-US", {
                            minimumFractionDigits: 2
                        }).format((item.price) * 10 / 100 + (item.price));

                productClouneHtml.querySelector("#similar-product-add-to-cart").addEventListener(
                        "click",
                        (e) => {
                    addToCart(item.id, 1);
                    e.preventDefault();
                });
                document.getElementById("similar-product-main").appendChild(productClouneHtml);

            });

        } else {
            window.location = "index.html";
        }

    } else {
        window.location = "index.html";
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


