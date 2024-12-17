/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

const notyf = new Notyf({
    duration: 5000,
    position: {x: 'right', y: 'top'},
});

async function loadCartItem() {
    const response = await fetch("LoadCartItem");

    if (response.ok) {
        const json = await response.json();

        console.log(json);

        const cartItemContainer = document.getElementById("cart-item-container");
        const cartItemRowTemplate = document.querySelector(".cart-item-row");

        cartItemContainer.innerHTML = "";

        let totalQty = 0;
        let totalPrice = 0;

        json.forEach((item) => {
            const itemSubTotal = item.product.price * item.qty;

            totalQty += item.qty;
            totalPrice += itemSubTotal;

            const cartItemRow = cartItemRowTemplate.cloneNode(true);

            cartItemRow.querySelector(".cart-item-image").src = `product-image/${item.product.id}/image1.png`;
            cartItemRow.querySelector(".cart-item-title").textContent = item.product.title;
            cartItemRow.querySelector(".cart-item-price").textContent = item.product.price.toFixed(2);
            cartItemRow.querySelector(".cart-item-qty").value = item.qty;
            cartItemRow.querySelector(".cart-item-total").textContent = itemSubTotal.toFixed(2);

            cartItemRow.querySelector(".btn-minus").addEventListener("click", () => updateQuantity(item.product.id, -1));
            cartItemRow.querySelector(".btn-plus").addEventListener("click", () => updateQuantity(item.product.id, 1));
            cartItemRow.querySelector(".cart-item-remove").addEventListener("click", async () => {
                await removeFromCart(item.product.id);
                loadCartItem();
            });

            cartItemContainer.appendChild(cartItemRow);
        });

        document.getElementById("subtotal").textContent = totalPrice.toFixed(2);
        document.getElementById("total").textContent = (totalPrice + 10).toFixed(2);
    } else {
        notyf.error("Unable to process your request.");
    }
}

async function updateQuantity(productId, delta) {
    const response = await fetch(`UpdateCartItem?productId=${productId}&delta=${delta}`, {method: "POST"});
    if (response.ok) {
        loadCartItem();
    } else {
        notyf.error("Failed to update quantity.");
    }
}

async function removeFromCart(productId) {
    const response = await fetch(`RemoveCartItem?productId=${productId}`, {method: "POST"});
    if (response.ok) {
        notyf.success("Item removed from cart.");
    } else {
        notyf.error("Failed to remove item.");
    }
}

