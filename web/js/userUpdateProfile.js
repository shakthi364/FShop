/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

// Dropdown Wrapper Elements
const dropdownWrapper = document.querySelector('.dropdown-wrapper');
const dropdownHeader = dropdownWrapper.querySelector('.dropdown-header');
const dropdownContent = dropdownWrapper.querySelector('.dropdown-content');
const searchInput = dropdownWrapper.querySelector('input');
const optionsList = dropdownWrapper.querySelector('.dropdown-options');

window.addEventListener("load", function () {
    loadUserData();
});

async function loadUserData() {
    const response = await fetch("LoadUser");

    if (response.ok) {
        const json = await response.json();
        console.log(json);

        if (json.success) {
            address = json.address;
            const cityList = json.cityList;
            const userList = json.userList;

//            let citySelect = document.getElementById("city");

            initializeDropdown(cityList);

                let first_name = document.getElementById("first_name");
                let last_name = document.getElementById("last_name");
                let email = document.getElementById("email");
                let mobile = document.getElementById("mobile");
                let address1 = document.getElementById("address1");
                let address2 = document.getElementById("address2");
                let postal_code = document.getElementById("postal_code");
                let city = document.getElementById("city");

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
            window.location = "log-in.html";
        }
    }
}

// Populate the dropdown dynamically
function populateOptions(cityList = [], filter = "") {
    optionsList.innerHTML = ""; // Clear previous options

    const filteredCities = cityList.filter(city =>
        city.name.toLowerCase().includes(filter.toLowerCase()) // Handle filter case-insensitivity
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

// Handle city selection
function selectCity(city) {
    dropdownHeader.querySelector('span').textContent = city.name;
    dropdownWrapper.classList.remove('active');
}

// Toggle dropdown visibility
dropdownHeader.addEventListener('click', () => {
    dropdownWrapper.classList.toggle('active');
});

// Filter options on search input
searchInput.addEventListener('input', (e) => {
    const filter = e.target.value;
    const cityList = JSON.parse(sessionStorage.getItem('cityList')) || []; // Use saved city list from sessionStorage
    populateOptions(cityList, filter); // Populate based on filter
});

// Initialize dropdown
function initializeDropdown(cityList) {
    // Save city data to session storage for filtering
    sessionStorage.setItem('cityList', JSON.stringify(cityList));
    populateOptions(cityList); // Populate dropdown with initial city list
}

