function atLeastOneRadio() {
    return ($('input[type=radio]:checked').size() > 0);
}

if(atLeastOneRadio()){
	var div = document.getElementById("resetFilers");
	div.innerHTML = "servus";
	console.log("servus");
}