/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

const notyf = new Notyf({
    duration: 5000,
    position: { x: 'right', y: 'top' },
});

var modelList;

async function loadFeatures() {

    const response = await fetch(
            "LoadFeatures"
            );

    if (response.ok) {

        const json = await response.json();

        const categoryList = json.categoryList;
        modelList = json.modelList;
        const colorList = json.colorList;
        const sizeList = json.sizeList;
        const conditionList = json.conditionList;

        loadSelect("categorySelect", categoryList, ["id", "name"]);
//        loadSelect("modelSelect",modelList,["id","name"]);
        loadSelect("colorSelect", colorList, ["id", "name"]);
        loadSelect("sizeSelect", sizeList, ["id", "value"]);
        loadSelect("conditionSelect", conditionList, ["id", "name"]);

    } else {
        document.getElementById("messge").innerHTML = "Plese Try again later!"
    }

}

function loadSelect(selectTagId, list, propertyArray) {
    const selectTag = document.getElementById(selectTagId);
    list.forEach(item => {
        let optionTag = document.createElement("option");
        optionTag.value = item[propertyArray[0]];
        optionTag.innerHTML = item[propertyArray[1]];
        selectTag.appendChild(optionTag);
    });
}

function updateModel() {
    let modeltag = document.getElementById("modelSelect");
    modeltag.length = 1;

    let categoryId = document.getElementById("categorySelect").value;

    modelList.forEach(model => {
        if (model.category.id == categoryId) {
            let optionTag = document.createElement("option");
            optionTag.value = model.id;
            optionTag.innerHTML = model.name;
            modeltag.appendChild(optionTag);
        }
    });

}

async function productListing() {
    
    const categotySelectTag = document.getElementById("categorySelect");
    const modelSelectTag = document.getElementById("modelSelect");
    const titleTag = document.getElementById("title");
    const descriptionTag = document.getElementById("description");
    const sizeSelectTag = document.getElementById("sizeSelect");
    const colorSelectTag = document.getElementById("colorSelect");
    const conditionSelectTag = document.getElementById("conditionSelect");
    const priceTag = document.getElementById("price");
    const qtyTag = document.getElementById("qty");
    const image1Tag = document.getElementById("image1");
    const image2Tag = document.getElementById("image2");
    const image3Tag = document.getElementById("image3");
    const image4Tag = document.getElementById("image4");
    
//    console.log(categotySelectTag.value);
//    console.log(modelSelectTag.value);
//    console.log(titleTag.value);
//    console.log(descriptionTag.value);
//    console.log(sizeSelectTag.value);
//    console.log(colorSelectTag.value);
//    console.log(conditionSelectTag.value);
//    console.log(priceTag.value);
//    console.log(qtyTag.value);
//    console.log(image1Tag.files[0]);
//    console.log(image2Tag.files[0]);

    const data = new FormData();
    data.append("categorySelectId", categotySelectTag.value);
    data.append("modelSelectId", modelSelectTag.value);
    data.append("title", titleTag.value);
    data.append("description", descriptionTag.value);
    data.append("sizeSelectId", sizeSelectTag.value);
    data.append("colorSelectId", colorSelectTag.value);
    data.append("conditionSelectId", conditionSelectTag.value);
    data.append("price", priceTag.value);
    data.append("qty", qtyTag.value);
    data.append("image1", image1Tag.files[0]);
    data.append("image2", image2Tag.files[0]);
    data.append("image3", image3Tag.files[0]);
    data.append("image4", image4Tag.files[0]);

    const response = await fetch(
            "productListing",
            {
                method:"POST",
                body:data
            }
    );
    
    if(response.ok){
        
        const json = await response.json();
        
        if(json.success){
            
            categotySelectTag.value = 0;
            modelSelectTag.length = 1;
            titleTag.value = "";
            descriptionTag.value = "";
            sizeSelectTag.value = 0;
            colorSelectTag.value = 0;
            conditionSelectTag.value = 0;
            priceTag.value = "";
            qtyTag.value = 0;
            image1Tag.files = null;
            image2Tag.files = null;
            image3Tag.files = null;
            image4Tag.files = null;
            
            notyf.success(json.content);
            
        }else{
            notyf.error(json.content);
        }
        
    }else{
        notyf.error("Please try again later!");
    }

}