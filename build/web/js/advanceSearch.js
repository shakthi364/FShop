/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

async function loadAdvanseSearchData() {
    const response = await fetch("LoadData");

    if (response.ok) {
        const json = await response.json();
        console.log(json);

        // Load category
        let categoryOption = document.getElementById("category_option");
        let categoryTemplate = document.getElementById("category_div");
        categoryOption.innerHTML = ""; // Clear existing categories

        let categoryList = json.categoryList;
        categoryList.forEach((category, index) => {
            let categoryClone = categoryTemplate.cloneNode(true);
            let categoryId = `category_${index + 2}`;

            categoryClone.querySelector(".custom-control-input").id = categoryId;
            categoryClone.querySelector(".custom-control-input").value = category.name;
            categoryClone.querySelector(".custom-control-label").setAttribute("for", categoryId);
            categoryClone.querySelector(".custom-control-label").innerHTML = category.name;

            categoryClone.id = ""; // Remove ID to prevent duplicates
            categoryOption.appendChild(categoryClone);
        });

        //load color
        let colorOption = document.getElementById("color_option");
        let colorTemplate = document.getElementById("color_div");
        colorOption.innerHTML = "";

        let colorlist = json.colorList;
        colorlist.forEach((color, index) => {
            let colorClone = colorTemplate.cloneNode(true);
            let coloryId = `color_${index + 2}`;

            colorClone.querySelector(".custom-control-input").id = coloryId;
            colorClone.querySelector(".custom-control-input").value = color.name;
            colorClone.querySelector(".custom-control-label").setAttribute("for", coloryId);
            colorClone.querySelector(".custom-control-label").innerHTML = color.name;

            colorClone.id = ""; // Remove ID to prevent duplicates
            colorOption.appendChild(colorClone);
        });

        //load size
        let sizeOption = document.getElementById("size_option");
        let sizeTemplate = document.getElementById("size_div");
        sizeOption.innerHTML = "";

        let sizeList = json.sizeList;
        sizeList.forEach((size, index) => {
            let sizeClone = sizeTemplate.cloneNode(true);
            let sizeId = `size_${index + 2}`;

            sizeClone.querySelector(".custom-control-input").id = sizeId;
            sizeClone.querySelector(".custom-control-input").value = size.value;
            sizeClone.querySelector(".custom-control-label").setAttribute("for", sizeId);
            sizeClone.querySelector(".custom-control-label").innerHTML = size.value;

            sizeClone.id = ""; // Remove ID to prevent duplicates
            sizeOption.appendChild(sizeClone);
        });

        updateProductViwe(json);

    } else {
        console.error("Failed to fetch data. Try again later.");
    }
}

async function searchProducts(firstRusult) {
    
    const selectedCategory = document.querySelector('input[name="option"]:checked').value;
//    console.log("Selected Category:", selectedCategory.value);

    const selectedColor = document.querySelector('input[name="option1"]:checked').value;
//    console.log("Selected color:", selectedColor.value);

    const selectedSize = document.querySelector('input[name="option2"]:checked').value;
//    console.log("Selected Size:", selectedSize.value);

    const price_range_min = document.getElementById("price_range_min").value;
//    console.log("Selected min:", price_range_min);

    const price_range_max = document.getElementById("price_range_max").value;
//    console.log("Selected max:", price_range_max);

    const sort_text = document.getElementById("sort_select").value;
//    console.log("Selected sort_text:", sort_text);

    const data = {
        firstRusult: firstRusult,
        selectedCategory: selectedCategory,
        selectedColor: selectedColor,
        selectedSize: selectedSize,
        price_range_min: price_range_min,
        price_range_max: price_range_max,
        sort_text: sort_text,
    };

    const response = await fetch(
            "SearchProduct",
            {
                method: "POST",
                body: JSON.stringify(data),
                headers: {
                    "Content-Type": "application/json"
                }
            });

    if (response.ok) {
        const json = await response.json();
        console.log(json);

        if (json.success) {
            console.log("search Complete");
            updateProductViwe(json);
        } else {
            console.log("Try agen later1!");
        }
    } else {
        console.log("Try agen later!");
    }
}


var product_div = document.getElementById("product_div");
var pagination_button = document.getElementById("pagination_button");
var currantPage = 0;

function updateProductViwe(json) {

    //load product list
    let product_conteiner_div = document.getElementById("product_conteiner_div");
    product_conteiner_div.innerHTML = "";

    json.productList.forEach(product => {
        let product_div_clone = product_div.cloneNode(true);

        product_div_clone.querySelector("#product_link").href = "detail.html?id=" + product.id;
        product_div_clone.querySelector("#product_i_link").href = "detail.html?id=" + product.id;
        product_div_clone.querySelector("#product_title").innerHTML = product.title;
        product_div_clone.querySelector("#product_image").src = "product-image/" + product.id + "/image1.png";
        product_div_clone.querySelector("#product_price").innerHTML = new Intl.NumberFormat(
                "en-US", {
                    minimumFractionDigits: 2
                }).format(product.price);
        product_div_clone.querySelector("#product_price_d").innerHTML = new Intl.NumberFormat(
                "en-US", {
                    minimumFractionDigits: 2
                }).format((product.price * 5 / 100) + product.price);

        product_conteiner_div.appendChild(product_div_clone);
    });

    // pagination
    let pagination_container = document.getElementById("pagination_container");
    pagination_container.innerHTML = "";

    let product_count = json.allproductCount;
    const product_per_page = 6;

    let pages = Math.ceil(product_count / product_per_page);

    if (currantPage != 0) {
        //add priviw button
        let pagination_button_clone_prev = pagination_button.cloneNode(true);
        pagination_button_clone_prev.querySelector("#pagination_button_a").innerHTML = "Prev";
        pagination_button_clone_prev.addEventListener("click", e => {
            currantPage--;
            searchProducts(currantPage * 6);
        });
        pagination_container.appendChild(pagination_button_clone_prev);
    }

    for (let i = 0; i < pages; i++) {
        let pagination_button_clone = pagination_button.cloneNode(true);
        pagination_button_clone.querySelector("#pagination_button_a").innerHTML = i + 1;

        pagination_button_clone.addEventListener("click", e => {
            currantPage = i;
            searchProducts(i * 6);
//            e.preventDefault();
        });

        if (i == currantPage) {
            pagination_button_clone.className = "page-item active ml-2";
        } else {
            pagination_button_clone.className = "page-item ml-2";
        }

        pagination_container.appendChild(pagination_button_clone);
    }

    if (currantPage != (pages - 1)) {
        //add next button
        let pagination_button_clone_next = pagination_button.cloneNode(true);
        pagination_button_clone_next.querySelector("#pagination_button_a").innerHTML = "Next";
        pagination_button_clone_next.addEventListener("click", e => {
            currantPage++;
            searchProducts(currantPage * 6);
        });
        pagination_container.appendChild(pagination_button_clone_next);

    }


}