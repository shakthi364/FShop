/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
async function signIn(){
    
//    console.log("ko");
    
    const user_dto = {
        email:document.getElementById("email").value,
        password:document.getElementById("password").value
    };
    
    const responce = await fetch(
            "SignIn",
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
            window.location = "index.html";
        }else{
            
            if(json.content === "Unverified"){
                
                window.location = "verifyAccount.html";
            }else{
                
            document.getElementById("message").innerHTML = json.content;
            }
            
        }
    }else {
        document.getElementById("message").innerHTML = "Plese try again later!";
    }

    
}

