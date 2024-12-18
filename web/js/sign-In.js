/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
async function signIn() {

//    console.log("ko");

    const user_dto = {
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    };

    const responce = await fetch(
            "SignIn",
            {
                method: "POST",
                body: JSON.stringify(user_dto),
                headers: {
                    "Content-Type": "application/json"
                }
            });

    if (responce.ok) {

        const json = await responce.json();

        if (json.success) {
            window.location = "index.html";
        } else {

            if (json.content === "Unverified") {

                window.location = "verifyAccount.html";
            } else {

                document.getElementById("message1").innerHTML = json.content;
            }

        }
    } else {
        document.getElementById("message1").innerHTML = "Plese try again later!";
    }
}

document.getElementById("forget_password").addEventListener("click", (e) => {

    forgetPassword();
    e.preventDefault();

});

var email = "";

async function forgetPassword() {
    const user_dto = {
        email: document.getElementById("email").value,
    };

    const responce = await fetch(
            "ForgetPassword",
            {
                method: "POST",
                body: JSON.stringify(user_dto),
                headers: {
                    "Content-Type": "application/json"
                }
            });

    if (responce.ok) {

        const json = await responce.json();

        if (json.success) {
            console.log(json);
            
            email = json.email;
            
            document.getElementById("login_visibal").classList.add("hidden");
            document.getElementById("forget_visibel").classList.remove("hidden");
            
            document.getElementById("sendEmail").innerHTML = json.email;
        } else {

            document.getElementById("message1").innerHTML = json.content;

        }
    } else {
        document.getElementById("message1").innerHTML = "Plese try again later!";
    }
}

async function verificationCodeContinue(){
//    console.log("ok");
    const user_dto = {
        email: email,
        verificationcode: document.getElementById("verificationcode").value,
    };

    const responce = await fetch(
            "verificationCodeContinue",
            {
                method: "POST",
                body: JSON.stringify(user_dto),
                headers: {
                    "Content-Type": "application/json"
                }
            });

    if (responce.ok) {

        const json = await responce.json();

        if (json.success) {
            console.log(json);
            
            document.getElementById("login_visibal").classList.add("hidden");
            document.getElementById("forget_visibel").classList.add("hidden");
            document.getElementById("reset_visibel").classList.remove("hidden");
            
        } else {

            document.getElementById("message2").innerHTML = json.content;

        }
    } else {
        document.getElementById("message2").innerHTML = "Plese try again later!";
    }
}

async function resetPassword(){
//    console.log("ok");
    const user_dto = {
        email: email,
        password1: document.getElementById("password1").value,
        password2: document.getElementById("password2").value,
    };

    const responce = await fetch(
            "ResetPassword",
            {
                method: "POST",
                body: JSON.stringify(user_dto),
                headers: {
                    "Content-Type": "application/json"
                }
            });

    if (responce.ok) {

        const json = await responce.json();

        if (json.success) {
            console.log(json);
            
            document.getElementById("login_visibal").classList.remove("hidden");
            document.getElementById("forget_visibel").classList.add("hidden");
            document.getElementById("reset_visibel").classList.add("hidden");
            
        } else {

            document.getElementById("message3").innerHTML = json.content;

        }
    } else {
        document.getElementById("message3").innerHTML = "Plese try again later!";
    }
}