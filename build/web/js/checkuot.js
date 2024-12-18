/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

// Payment completed. It can be a successful failure.
//payhere.onCompleted = function onCompleted(orderId) {
//    console.log("Payment completed. OrderID:" + orderId);
//    // Note: validate the payment and show success or failure page to the customer
//    console.log("Order Plase Complete");
//    window.location = "index.html"
//};
//
//// Payment window closed
//payhere.onDismissed = function onDismissed() {
//    // Note: Prompt user to pay again or show an error page
//    console.log("Payment dismissed");
//};
//
//// Error occurred
//payhere.onError = function onError(error) {
//    // Note: show an error page
//    console.log("Error:" + error);
//};

// Dropdown Wrapper Elements
const dropdownWrapper = document.querySelector('.dropdown-wrapper');
const dropdownHeader = dropdownWrapper.querySelector('.dropdown-header');
const dropdownContent = dropdownWrapper.querySelector('.dropdown-content');
const searchInput = dropdownWrapper.querySelector('input');
const optionsList = dropdownWrapper.querySelector('.dropdown-options');


let pd_sub_shipping = document.getElementById("pd_sub_shipping");
let pd_sub_total = document.getElementById("pd_sub_total");


let pd_total_price_div = document.getElementById("pd_total_price_div");
let pd_total_price = document.getElementById("pd_total_price");

var address;

let shipping_amount = 0;
let sub_total = 0;
let item_count;
let shipping;

async function loadData() {
    console.log("check")
    const response = await fetch("LoadCheckout");

    if (response.ok) {
        const json = await response.json();
        console.log(json);

        if (json.success) {
            address = json.address;
            const cityList = json.cityList;
            const cartList = json.cartList;

//            let citySelect = document.getElementById("city");

            initializeDropdown(cityList);

            let currentAddressCheckBox = document.getElementById("currentaddress");
            currentAddressCheckBox.addEventListener("change", e => {

                let first_name = document.getElementById("first_name");
                let last_name = document.getElementById("last_name");
                let email = document.getElementById("email");

                if (currentAddressCheckBox.checked) {
                    first_name.value = address.fname;
                    last_name.value = address.lname;
                    email.value = json.email;
                    mobile.value = address.mobile;
                    address1.value = address.line_1;
                    address2.value = address.line_2;
                    postal_code.value = address.postal_code;
                    city.textContent = address.city.name;

                    populateOptions(JSON.parse(sessionStorage.getItem('cityList')), address.city.name);
                    selectCity(address.city);

                } else {
                    first_name.value = "";
                    last_name.value = "";
                    mobile.value = "";
                    address1.value = "";
                    address2.value = "";
                    postal_code.value = "";
                    city.textContent = "Select Country";
                    email.value = "";

                    populateOptions(JSON.parse(sessionStorage.getItem('cityList')), "");
                }

            });

            let pd_body = document.getElementById("pd_body");
            let pd_item = document.getElementById("pd_item");


            pd_body.innerHTML = "";


            cartList.forEach(item => {

                let pd_item_clon = pd_item.cloneNode(true);
                pd_item_clon.querySelector("#pd_title").innerHTML = item.product.title;
                pd_item_clon.querySelector("#pd_qty").innerHTML = item.qty;

                let item_sub_total = item.product.price * item.qty
                sub_total += item_sub_total;

                pd_item_clon.querySelector("#pd_item_subtotal").innerHTML = new Intl.NumberFormat(
                        "en-US", {
                            minimumFractionDigits: 2
                        }).format(item_sub_total);

                pd_body.appendChild(pd_item_clon);
            });

            let hr = document.createElement("hr");
            hr.className = "mt-0";
            pd_body.appendChild(hr);

            pd_sub_total.querySelector("#pd_subtotal_price").innerHTML = new Intl.NumberFormat(
                    "en-US", {
                        minimumFractionDigits: 2
                    }).format(sub_total);
            pd_body.appendChild(pd_sub_total);

            //shipping price
            item_count = cartList.length;

        } else {
            window.location = "log-in.html";
        }
    }
}

function populateOptions(cityList = [], filter = "") {
    optionsList.innerHTML = "";

    const filteredCities = cityList.filter(city =>
        city.name.toLowerCase().includes(filter.toLowerCase())
    );

    if (filteredCities.length === 0) {
        optionsList.innerHTML = "<li>No results found</li>";
        return;
    }

    filteredCities.forEach(city => {
        const li = document.createElement('li');
        li.textContent = city.name;
        li.addEventListener('click', () => selectCity(city));
        optionsList.appendChild(li);
    });
}

let city_id = 0;

function selectCity(city) {
    dropdownHeader.querySelector('span').textContent = city.name;
//    console.log(city.shipping);
    dropdownWrapper.classList.remove('active');
    shipping_amount = city.shipping * item_count;
    city_id = city.id;

    pd_total_price_div.innerHTML = "";

    console.log(shipping_amount);
    pd_sub_shipping.querySelector("#pd_shipping_charg").innerHTML = new Intl.NumberFormat(
            "en-US", {
                minimumFractionDigits: 2
            }).format(shipping_amount);

    pd_body.appendChild(pd_sub_shipping);

    let total = shipping_amount + sub_total;
    let pd_total_price_clone = pd_total_price.cloneNode(true);
    pd_total_price_clone.querySelector("#pd_total_price").innerHTML = new Intl.NumberFormat(
            "en-US", {
                minimumFractionDigits: 2
            }).format(total);

    pd_total_price_div.append(pd_total_price_clone);

}

dropdownHeader.addEventListener('click', () => {
    dropdownWrapper.classList.toggle('active');
});

searchInput.addEventListener('input', (e) => {
    const filter = e.target.value;
    const cityList = JSON.parse(sessionStorage.getItem('cityList')) || [];
    populateOptions(cityList, filter);
});

function initializeDropdown(cityList) {
    sessionStorage.setItem('cityList', JSON.stringify(cityList));
    populateOptions(cityList);
}

async function checkout() {
    let isCurrentAddress = document.getElementById("currentaddress").checked;

    let first_name = document.getElementById("first_name");
    let last_name = document.getElementById("last_name");
    let email = document.getElementById("email");
    let mobile = document.getElementById("mobile");
    let address1 = document.getElementById("address1");
    let address2 = document.getElementById("address2");
//    let city = document.getElementById("city");
    let postal_code = document.getElementById("postal_code");

    const data = {
        isCurrentAddress: isCurrentAddress,
        first_name: first_name.value,
        last_name: last_name.value,
        email: email.value,
        mobile: mobile.value,
        address1: address1.value,
        address2: address2.value,
        city: city_id,
        postal_code: postal_code.value,
    };

    const response = await fetch(
            "Checkout",
            {
                method: "POST",
                body: JSON.stringify(data),
                headers: {
                    "Content-Type": "application/json"
                }
            });

    if (response.ok) {
        const json = await response.json();
//        console.log(json);

        if (json.success) {

            console.log(json.payhereJson);
            payhere.startPayment(json.payhereJson);

        } else {
            console.log(json.message)
        }
    } else {
        console.log("Try again leter!");
    }
}
