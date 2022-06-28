/* Set rates + misc */
var taxRate = 0.05;
var shippingRate = 15.00;
var fadeTime = 300;
var listPatients = []

$(document).ready(function () {
    console.log("ready");
    fetchProducts();
    getDonation();
});


function fetchProducts() {
    $.ajax({
        type: "GET",
        url: "/api/v1/donor/getOrders",
        success: function (result) {


            resultStr = "{\"result\":" + JSON.stringify(result) + "}";
            resultJSON = JSON.parse(resultStr);

            for (var i = 0; i < resultJSON.result.length; i++) {
                if (resultJSON.result[i].orderStatus.indexOf("Order Placed") > -1) {

                    $('#product0').clone().insertBefore('.totals').attr('id', 'product' + (i + 1)).css('display', 'block');
                    $('#product' + (i + 1)).find('.product-details').children(".product-title").html("Patient Name :" + resultJSON.result[i].firstName).attr('id', 'product-title' + (i + 1));
                    $('#product' + (i + 1)).find('.product-details').children(".product-description").html("Products List: " + resultJSON.result[i].productName.join()).attr('id', 'product-description' + (i + 1));
                    $('#product' + (i + 1)).find('.product-price').html(resultJSON.result[i].totalAmount).attr('id', 'product-price' + (i + 1));
                    $('#product' + (i + 1)).find('.product-quantity').children().attr('id', 'product-quantity' + (i + 1));
                    $('#product' + (i + 1)).find('.product-line-price').attr('id', 'product-line-price' + (i + 1));
                    $('#product' + (i + 1)).find('.product-details').children(".order-id").html("Order id : " + resultJSON.result[i].orderId).attr('id', 'order-id' + (i + 1));
                }
            }
        },
        error: function (e) {
            console.log("ERROR: ", e);
        },
    });
}


function callf(ele) {
    let orderNumber = $(ele).attr('id').split('product-quantity')[1];
    if ($('#product-quantity' + orderNumber).is(':checked')) {
        $('#product-line-price' + orderNumber).html($('#product-price' + orderNumber).html());
        listPatients.push($('#product-title' + orderNumber).html());
    } else {
        $('#product-line-price' + orderNumber).html('0');
         const index = listPatients.indexOf($('#product-title' + orderNumber).html());
         if (index > -1) {
            listPatients.splice(index, 1); // 2nd parameter means remove one item only
         }
    }
    updateTotal();
}

function updateTotal() {
    var sum = 0;
    $('div.product-line-price').each(function () {
        sum += parseFloat($(this).text());
        $('#cart-subtotal').text(sum);// Or this.innerHTML, this.innerText
        recalculateCart();
    });
}

function finalOrder() {
    var totalAmountList = new Array();
    var selectedOrders = new Array();
    var nameList = "";
    var orderList = new Array();
    for (var i = 0; i < resultJSON.result.length; i++) {
        orderList.push(resultJSON.result[i].orderId);

    }

    for (var j = 0; j <= orderList.length; j++) {
        var num = j + 1;
        if ($('#product-quantity' + num).is(':checked')) {
            selectedOrders.push(orderList[j]);
            totalAmountList.push(resultJSON.result[j].totalAmount);
            nameList += '"' + resultJSON.result[j].firstName + '",';

        }
    }
    nameList = nameList.slice(0, -1);
    var str = "{\"orderId\":[" + selectedOrders.toString() + "]}";
    strJson = JSON.parse(str);
    $.ajax({
        type: "POST",
        url: "/finalOrderDonor",
        contentType: "application/json",
        dataType: "json",
        async: false,
        data: JSON.stringify(strJson),
        success: function (result) {
            location.reload();
        },
        error: function (e) {
            console.log("ERROR: ", e);
        },
    });

    var str1 = {donationAmount: totalAmountList};


    $.ajax({
        type: "POST",
        url: "/donationInfo",
        contentType: "application/json",
        dataType: "json",
        async: false,
        data: JSON.stringify(str1),
        success: function (result) {
            location.reload();
        },
        error: function (e) {
            console.log("ERROR: ", e);
        },
    });
    console.log("Patients : ",listPatients);
    for (const x of listPatients) {
        $.post(
            "/api/v1/analytics/updatePatientHelp",
            {patient: x},
            function (data) {
            }, 'json');
    }
    $.ajax({
           type: "POST",
            url: "/paymentAmount",
            contentType: "application/json",
            dataType: "json",
            async: false,
            data: JSON.stringify({
                "amount": $('#cart-total').text()
            }),
            success: function(response) {
                window.location = "/paymentGateway";
            },
            error: function(xhr) {
               console.log(xhr);
            }
       });
}

/* Recalculate cart */
function recalculateCart() {
    var subtotal = 0;
    /* Sum up row totals */
    $('.product').each(function () {
        subtotal += parseFloat($(this).children('.product-line-price').text());
    });

    /* Calculate totals */
    var tax = subtotal * taxRate;
    var shipping = (subtotal > 0 ? shippingRate : 0);
    var total = subtotal + tax + shipping;

    /* Update totals display */
    $('.totals-value').fadeOut(fadeTime, function () {
        $('#cart-subtotal').html(subtotal.toFixed(2));
        $('#cart-tax').html(tax.toFixed(2));
        $('#cart-shipping').html(shipping.toFixed(2));
        $('#cart-total').html(total.toFixed(2));
        if (total == 0) {
            $('.checkout').fadeOut(fadeTime);
        } else {
            $('.checkout').fadeIn(fadeTime);
        }
        $('.totals-value').fadeIn(fadeTime);
    });
}

/* Update quantity */
function updateQuantity(quantityInput) {
    /* Calculate line price */
    var productRow = $(quantityInput).parent().parent();
    var price = parseFloat(productRow.children('.product-price').text());
    var quantity = $(quantityInput).val();
    var linePrice = price * quantity;

    /* Update line price display and recalc cart totals */
    productRow.children('.product-line-price').each(function () {
        $(this).fadeOut(fadeTime, function () {
            $(this).text(linePrice.toFixed(2));
            recalculateCart();
            $(this).fadeIn(fadeTime);
        });
    });
}

/* Remove item from cart */
function removeItem(removeButton) {
    /* Remove row from DOM and recalc cart total */
    var productRow = $(removeButton).parent().parent();
    productRow.slideUp(fadeTime, function () {
        productRow.remove();
        recalculateCart();
    });
}

function getDonation() {
    $.ajax({
        type: "GET",
        url: "/getDonationById",
        contentType: "application/json",
        dataType: "json",
        async: false,
        success: function (result) {
            resultStr = "{\"result\":" + JSON.stringify(result) + "}";
            resultJSON = JSON.parse(resultStr);
            console.log(resultJSON["result"]["data"]["amount"]);
            $('#totalDonation').append(resultJSON["result"]["data"]["amount"]).append("$");
        },
        error: function (e) {
            console.log("ERROR: ", e);
        },
    });
}

<!--custom javascript for handling subscription-->
$(function () {
    var API_KEY = $('#api-key').val();
    // Create a Stripe client.
    var stripe = Stripe(API_KEY);

    // Create an instance of Elements.
    var elements = stripe.elements();

    // Create an instance of the card Element.
    var card = elements.create('card');

    // Add an instance of the card Element into the `card-element` <div>.
    card.mount('#card-element');

    // Handle real-time validation errors from the card Element.
    card.addEventListener('change', function (event) {
        var displayError = document.getElementById('card-errors');
        if (event.error) {
            displayError.textContent = event.error.message;
        } else {
            displayError.textContent = '';
        }
    });

    // Handle form submission.
    var form = document.getElementById('payment-form');
    form.addEventListener('submit', function (event) {
        event.preventDefault();
        // handle payment
        handlePayments();
    });

    //handle card submission
    function handlePayments() {
        var amountvalue = $('#cart-total').text();
        console.log("Amount : ", amountvalue);
        stripe.createToken(card).then(function (result) {
            if (result.error) {
                // Inform the user if there was an error.
                var errorElement = document.getElementById('card-errors');
                errorElement.textContent = result.error.message;
            } else {
                // Send the token to your server.
                var token = result.token.id;
                var email = $('#email').val();
                $.post(
                    "/api/v1/create-charge",
                    {email: email, token: token, amount: amountvalue},
                    function (data) {
                        finalOrder();
                        window.location = '/paymentSuccess';
                    }, 'json');
            }
        });
    }

    function finalOrder() {
        var totalAmountList = new Array();
        var selectedOrders = new Array();
        var nameList = "";
        var orderList = new Array();
        for (var i = 0; i < resultJSON.result.length; i++) {
            orderList.push(resultJSON.result[i].orderId);

        }

        for (var j = 0; j <= orderList.length; j++) {
            var num = j + 1;
            if ($('#product-quantity' + num).is(':checked')) {
                selectedOrders.push(orderList[j]);
                totalAmountList.push(resultJSON.result[j].totalAmount);
                nameList += '"' + resultJSON.result[j].firstName + '",';

            }
        }
        nameList = nameList.slice(0, -1);
        var str = "{\"orderId\":[" + selectedOrders.toString() + "]}";
        strJson = JSON.parse(str);
        $.ajax({
            type: "POST",
            url: "/finalOrder",
            contentType: "application/json",
            dataType: "json",
            async: false,
            data: JSON.stringify(strJson),
            success: function (result) {
                location.reload();
            },
            error: function (e) {
                console.log("ERROR: ", e);
            },
        });

        var str1 = {donationAmount: totalAmountList};
        console.log(str1);


        $.ajax({
            type: "POST",
            url: "/donationInfo",
            contentType: "application/json",
            dataType: "json",
            async: false,
            data: JSON.stringify(str1),
            success: function (result) {
                location.reload();
            },
            error: function (e) {
                console.log("ERROR: ", e);
            },
        });
    }

});

function checkout() {
    var x = document.getElementById("paymentModule");
    if (x.style.display === "none") {
        x.style.display = "block";
        $("#checkoutButton").html('Cancel');
    } else {
        x.style.display = "none";
        $("#checkoutButton").html('Checkout');
    }
}