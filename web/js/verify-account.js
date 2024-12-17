/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
async function verifyAccount() {

//    console.log("ko");

    const dto = {
        verification: document.getElementById("verification").value,
    };

    const responce = await fetch(
            "Verification",
            {
                method: "POST",
                body: JSON.stringify(dto),
                headers: {
                    "Content-Type": "application/json"
                }
            });

    if (responce.ok) {

        const json = await responce.json();

        if (json.success) {
            window.location = "index.html";
        } else {

            document.getElementById("message").innerHTML = json.content;

        }
    } else {
        document.getElementById("message").innerHTML = "Plese try again later!";
    }


}

