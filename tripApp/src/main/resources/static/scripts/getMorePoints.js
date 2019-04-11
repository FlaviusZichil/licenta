	var getMorePointsButton = document.getElementById("getMorePointsButton");
	var howToObtainDiv = document.getElementById("howToObtain");
	var card = document.getElementById("card");
	
	getMorePointsButton.onclick = function(){		
		howToObtainDiv.setAttribute("class", "collapse show");
 		card.scrollIntoView();;
	}