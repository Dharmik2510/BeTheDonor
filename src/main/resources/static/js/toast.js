
document.addEventListener("DOMContentLoaded", function(){

    var element = document.getElementById("myToast");
    var btn= document.getElementsByClassName("hideBtn")

    // Create toast instance
    var myToast = new bootstrap.Toast(element);


   hideBtn.addEventListener("click", function(){
                     myToast.hide();
                 });

});

