$(document).on('click', '#donate-now', function() {
    var amount = $('#donation-amt').val();
    $.ajax({
        type: "GET",
        url: "/api/v1/autoPayByCredit/updateCreditAmount/"+amount,
        success: function (result) {
            location.href = "/thank-you";
        }
    });
});