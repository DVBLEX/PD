var validateForm = function() {

    var emailRegexp = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[\d]{1,3}\.[\d]{1,3}\.[\d]{1,3}\.[\d]{1,3}])|(([\w\-]+\.)+[a-zA-Z]{2,}))$/;
    var msisdnRegexp = /^(\+?[0-9]\d{10,12})$/;
    var passwordRegexp = /(?=(:?.*[^A-Za-z0-9].*))(?=(:?.*[A-Z].*){1,})(?=(:?.*\d.*){1,})(:?^[\w\&\?\!\$\#\*\+\=\%\^\@\-\.\,\_]{8,32}$)/;

    var displayLoginFormServerErrorResponse = document.getElementById("displayLoginFormServerErrorResponse");
    var displayLoginFormErrorResponse = document.getElementById("displayLoginFormErrorResponse");

    var accountTypeValue = null;
    var accountTypeRadios = document.transporterLoginForm.elements["accountType"];

    for(var i=0; i<accountTypeRadios.length; i++) {
        if(accountTypeRadios[i].checked == true) {
            accountTypeValue = accountTypeRadios[i].value;
        }
    }
    
    var input1Value = document.transporterLoginForm.input1.value;
    var input2Value = document.transporterLoginForm.input2.value;

    if (displayLoginFormServerErrorResponse != null && displayLoginFormServerErrorResponse != undefined) {
        displayLoginFormServerErrorResponse.style.display = "none";
    }
    
    if (input1Value == null || input1Value == undefined || input1Value == "") {
        displayLoginFormErrorResponse.style.display = "block";
        return false;
        
    } else if (input2Value == null || input2Value == undefined || input2Value == "") {
        displayLoginFormErrorResponse.style.display = "block";
        return false;

    } else if (accountTypeValue == null || accountTypeValue == undefined || accountTypeValue == "") {
        displayLoginFormErrorResponse.style.display = "block";
        return false;
    } 

    if (accountTypeValue == 1 && !emailRegexp.test(input1Value)) {
        displayLoginFormErrorResponse.style.display = "block";
        return false;

    } else if (accountTypeValue == 2 && !msisdnRegexp.test(input1Value)) {
        displayLoginFormErrorResponse.style.display = "block";
        return false;
    }

    if (!passwordRegexp.test(input2Value)) {
        displayLoginFormErrorResponse.style.display = "block";
        return false;
    }

    return true;
};
