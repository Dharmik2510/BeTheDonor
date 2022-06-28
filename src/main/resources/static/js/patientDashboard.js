$(document).ready(function() {
    var productCount = 0;
    var resultJSON = null;

    function fetchProducts() {
        $.ajax({
            type: "GET",
            url: "/api/v1/products",
            async: false,
            success: function(result) {
                resultStr = "{\"result\":" + JSON.stringify(result) + "}";
                resultJSON = JSON.parse(resultStr);
                localStorage.setItem("resultStr", resultStr);
                productCount = resultJSON["result"].length;
                localStorage.setItem("productCount", productCount);
                var str="";
                for(var i=0;i<productCount;i++) {
                    str+="<div class='Cart-Items filterDiv "+resultJSON['result'][i]['category']+"'> <div class='about'> <h1 class='title'>"+resultJSON['result'][i]['productName']+"</h1> <h3 class='subtitle'>"+resultJSON['result'][i]['comment']+"</h3> </div> <div class='error-div'><p class='showError' style='display:none;'>Only <span>"+resultJSON['result'][i]['quantity']+"</span> are left.</p></div><div class='div counter'>  <input type='button' id='down' class='btn' value='-'> <input class='item count' id='item_"+resultJSON['result'][i]['productId']+"' style='width:50px;' type='text' value='0' max='"+resultJSON['result'][i]['quantity']+"'> <input type='button' class='btn' id='up' value='+'> <br> </div> <div class='prices'> <div class='amount'>$"+resultJSON['result'][i]['price']+"</div>  </div> </div>";
                }
                $(".products-container").append(str);
            },
            error: function(e) {
                console.log("ERROR: ", e);
            },
        });
    }

    function fetchCategories() {
        $.ajax({
            type: "GET",
            url: "/api/v1/products/getCategories",
            async: false,
            success: function(result) {
                resultStr = "{\"result\":" + JSON.stringify(result) + "}";
                resultJSON = JSON.parse(resultStr);
                console.log(resultJSON["result"].length);
                var str="<button class='category-btn active' id='category' value='showAll' onclick='filterSelection(\"all\")'> Show all</button>";
                for(var i=0;i<resultJSON["result"].length;i++) {
                    str += "<button class='category-btn' id='category' value='"+resultJSON["result"][i]+"'>"+resultJSON["result"][i]+"</button>";
                    console.log(str);
                }
                $("#category-container").append(str);
            },
            error: function(e) {
                console.log("ERROR: ", e);
            },
        });
    }

    fetchCategories();
    fetchProducts();

});

function getUserInput() {
    var productCount = localStorage.getItem("productCount");
    var resultStr = localStorage.getItem("resultStr");
    var resultJSON = JSON.parse(resultStr);
    var str = "[";
    var total = parseFloat(0);
    var itemCount = 0;
    for (var i = 0; i < productCount; i++) {
        if ($('#item_' + resultJSON["result"][i]["productId"]).attr('value') != 0) {
            str += "{ \"productId\":\"" + resultJSON["result"][i]["productId"] + "\",\"quantity\":\"" + $('#item_' + resultJSON["result"][i]["productId"]).attr('value') + "\"},";

            total += parseFloat($('#item_' + resultJSON["result"][i]["productId"]).attr('value')) * parseFloat(resultJSON["result"][i]["price"]);
            itemCount += parseInt($('#item_' + resultJSON["result"][i]["productId"]).attr('value'));

        }
    }
    str = str.slice(0, -1);
    str += "]"
    localStorage.setItem("orderJSONStr", str);
    localStorage.setItem("orderTotal", total);
    $('.total-amount').text("$"+total);
    $('.items-count').text(itemCount+" items");
}

$(document).on("click", '[id^="up"]', function() {
    var itemId = $(this).closest('div').find('.item').attr('id');
    value = $("#" + itemId).attr('value');
    value = isNaN(value) ? parseInt("0") : parseInt(value);
    max = $("#" + itemId).attr('max');
    if (value <= max - 1) {
        value = parseInt(value) + parseInt("1");
    } else {
        $(this).closest('div').parent('div').find('.showError').attr("style", "display:block");
    }
    $("#" + itemId).attr("value", value);
    $("#" + itemId).val(value);
    getUserInput();

});

$(document).on("click", '[id^="down"]', function() {
    var itemId = $(this).closest('div').find('.item').attr('id');
    value = $("#" + itemId).attr('value');
    value = isNaN(value) ? parseInt("0") : parseInt(value);
    value = parseInt(value) - parseInt("1");
    if (value < 0) {
        value = parseInt("0");
    }
    if (value <= max) {
        $(this).closest('div').parent('div').find('.showError').attr("style", "display:none");
    }
    $("#" + itemId).attr("value", value);
    $("#" + itemId).val(value);
    getUserInput();
});

$(document).on('input', '[id^="item_"]', function() {
    var value = $(this).val();
    var max = $(this).attr('max');
    if (parseInt(value) > parseInt(max)) {
        value = parseInt(max);
        $(this).closest('div').parent('div').find('.showError').attr("style", "display:block");
    } else {
        $(this).closest('div').parent('div').find('.showError').attr("style", "display:none");
    }
    $(this).attr("value", value);
    $(this).val(value);
    getUserInput();
});

$(document).on('change', '[id^="item_"]', function() {

    var value = $(this).val();
    var max = $(this).attr('max');
    if (parseInt(value) > parseInt(max)) {
        $(this).closest('div').parent('div').find('.showError').attr("style", "display:block");
    } else {
        $(this).closest('div').parent('div').find('.showError').attr("style", "display:none");
    }
    getUserInput();
});



$(document).on("click", "#placeOrder", function() {
    var str = localStorage.getItem("orderJSONStr");
    var total = localStorage.getItem("orderTotal");
    var firstName = $('#firstname').attr('value');
    var lastName = $('#lastname').attr('value');
    var address = $('#address').attr('value');
    var country = $('#country').attr('value');
    var postalCode = $('#postalcode').attr('value');
    var city = $('#city').attr('value');
    var province = $('#province').attr('value');
    var address = "[{\"address\": \""+address+"\", \"city\": \""+ city+"\", \"province\": \""+province+"\", \"country\": \""+country+"\", \"postalCode\": \""+postalCode+"\"}]";
    var strJSON = JSON.parse(str);
    var addressJSON = JSON.parse(address);
    var payload = {
        "order": strJSON,
        "total": parseFloat(total).toFixed(2),
        "address": addressJSON
    };

    $.ajax({
        type: "POST",
        url: "/api/v1/patient/order",
        contentType: "application/json",
        dataType: "json",
        async: false,
        data: JSON.stringify(payload),
        success: function(result) {
            location.href = "/patient/order-placed";
        },
        error: function(e) {
            console.log("ERROR: ", e);
        },
    });
});

$(document).on('change', '.field__input', function() {
    var value = $(this).val();
    $(this).attr("value", value);
});

$(document).on('click', '.category-btn', function() {

    var divID = $(this).closest('div').attr('id');
    $("#"+divID+">button.active").removeClass("active");
    if($(this).hasClass('active')) {
        $(this).removeClass('active')
    }
    else {
        $(this).addClass('active')
    }
    var categorySelected = $(this).val();
    if(categorySelected == 'showAll') {
        $('.filterDiv ').removeAttr("style");
    } else {
        $('.filterDiv ').attr("style", "display:none");
        $('.' + categorySelected).removeAttr("style");
    }
});

$(document).on('click', '.removeAll', function() {
    $('[id^="item_"]').attr("value", 0);
    $('[id^="item_"]').val(0);
    $('.total-amount').text('$0');
});
