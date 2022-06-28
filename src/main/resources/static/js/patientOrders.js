$(document).ready(function() {
    function fetchOrders() {
        $.ajax({
            type: "GET",
            url: "/api/v1/patient/getOrders",
            success: function(result) {
                resultStr = "{\"result\":" + JSON.stringify(result) + "}";
                resultJSON = JSON.parse(resultStr);
                console.log(resultJSON);
                orderCount = resultJSON["result"].length;
                var str="";
                var newstr="";
                for(var i=0;i<orderCount;i++)
                {
                    newstr+="<div class='card w-75 shadow-lg rounded-2 text-black m-4 font-weight-bold col-15'> <div class='card-body'> <ul class='list-group'> <li class='list-group-item list-group-item-info' ><i class='fa fa-dollar' style='margin-right: .2rem'>Order ID:</i>"+resultJSON["result"][i]["orderId"]+"</li> <li class='list-group-item list-group-item-info' ><i class='fa fa-sort-amount-asc'  style='margin-right: .2rem' >Order Total:</i>"+resultJSON["result"][i]["totalAmount"]+"</li> <li class='list-group-item list-group-item-info'><i class='fa fa-question-circle' style='margin-right: .2rem'>Order Status:</i>"+resultJSON["result"][i]["orderStatus"]+"</li> </ul> </div> <div class='card-footer"+i+"'> <button type='button' id='show_details'  class='btn btn-outline-info m-1 font-weight-bold' aria-expanded='false' aria-controls='collapseId"+i+"' data-bs-toggle='collapse' href='#collapseId"+i+"'>Show Items</button> </div> </div>";
                }
                $(".patientOrdersList").append(newstr);

                for(var i=0;i<orderCount;i++)
                {   str="";
                    str+="<div class='collapse' id='collapseId"+i+"'> <div class='card shadow-lg rounded-2 w-75 text-black font-weight-bold col-15 border' style='margin: .50rem .25rem .25rem .25rem' > <div class='card-body-list'> <div class='container'><table class='table table-striped'><thead> <tr> <th>Product</th> <th>Price</th> <th>Quantity</th> </tr> </thead>";

                    for(var j=0;j<resultJSON["result"][i]["productName"].length;j++)
                    {
                        str+="<tbody> <tr><td>"+resultJSON["result"][i]["productName"][j]+"</td><td>"+resultJSON["result"][i]["price"][j]+"</td><td>"+resultJSON["result"][i]["quantity"][j]+"</td></tr></tbody> ";
                    }
                    str+="</table> </div></div></div></div>";
                    $(".card-footer"+i).append(str);
                }

            },
            error: function(e) {
                console.log("ERROR: ", e);
            },
        });
    }
    fetchOrders();
});