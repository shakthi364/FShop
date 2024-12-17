/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
async function signUp(){
    
    const user_dto = {
        first_name:document.getElementById("firstName").value,
        last_name:document.getElementById("lastName").value,
        email:document.getElementById("email").value,
        password:document.getElementById("password").value
    };
    
    const responce = await fetch(
            "SignUp",
    {
        method:"POST",
        body: JSON.stringify(user_dto),
        headers:{
            "Content-Type":"application/json"
        }
    });
    
    if(responce.ok){
        
        const json = await responce.json();
        
        if(json.success){
            window.location = "verifyAccount.html";
        }else{
            document.getElementById("message").innerHTML = json.content;
        }
    }else {
        document.getElementById("message").innerHTML = "Plese try again later!";
    }

    
}

