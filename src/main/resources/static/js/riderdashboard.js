$(function () {
    $('#city').autocomplete({
        source: function (request, response) {
            $.ajax({
                url: "/riderDashboard/keyword",
                dataType: "json",
                data: {
                    q: request.term
                },
                success: function (data) {
                    //alert(data);
                    console.log(data);
                    response(data);
                }
            });
        },
        minLength: 1,
        cache: false
    });

});

function addOrder() {

    $.ajax({
            type: "GET",
            url: "/api/v1/donor/getOrders",
        success: function (result) {

            resultStr = "{\"result\":" + JSON.stringify(result) + "}";
            resultJSON = JSON.parse(resultStr);
            var myList = new Array();
            var orderList = new Array();
            for (var i = 0; i < resultJSON.result.length; i++) {
                var n = (resultJSON.result[i].orderId);
                if ($('#c' + n).is(':checked')) {
                    myList.push(n);
                }

            }
            console.log(myList.toString());
            pushOrderStatus(myList);

        },
        error: function (e) {
            console.log("ERROR: ", e);
        },
    });

}


function pushOrderStatus(myList)
{

    var str = "{\"orderId\":[" + myList + "]}";
    var payload = JSON.parse(str);
    console.log(payload);
    $.ajax({
        type: 'POST',
        url: "/riderDashboard/showSelectedOrders",
        contentType: "application/json",
        dataType: "json",
        async: false,
        data: JSON.stringify(payload),
        success: function(e) {
            window.location = "/riderDashboard/showSelectedOrders";
        },
        error: function(e) {
            console.log("ERROR: ", e);
        },
        complete: function(xhr) {

                window.location = "/riderDashboard/showSelectedOrders";

        },
    });
}


$(document).on("click", ".close", function (evt) {
    evt.preventDefault();
    console.log("hELLO");
    $(this).parent().parent().parent().prop('hidden', true);
});


function load(orderId) {

    var order_id = orderId;
    console.log(orderId);
    $('.orderItems').each(function () {
        var value = $(this).data("id");
        console.log(value);
        if (value == order_id) {
            $(this).removeAttr('hidden');
        } else {
            $(this).prop('hidden', true);
        }
    });
}







