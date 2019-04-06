
function createNewPoint(){		
		var allRoutePoints = document.getElementById("puncteIntermediare");

		var select = createSelectTag();
		createDefaultOption(select);
     	createOptionsForSelect(select);
     	
     	var removeButton = createRemoveButton();
			
		var rowDiv = document.createElement("div");
		rowDiv.setAttribute("class", "row");
			
		var col1Div = document.createElement("div");
		col1Div.setAttribute("class", "col-md-10 col-sm-10 col-xs-2");
		
		var col2Div = document.createElement("div");
		col2Div.setAttribute("class", "col-md-2 col-sm-2 col-xs-2");
		
		allRoutePoints.appendChild(rowDiv);
		rowDiv.appendChild(col1Div);
		rowDiv.appendChild(col2Div);
		col1Div.appendChild(select);
		col2Div.appendChild(removeButton);
	}
	
	function createRemoveButton(){
		var removeButton = document.createElement("p");
		removeButton.innerHTML = "Elimina";
		removeButton.setAttribute("class", "btn btn-danger");
		removeButton.setAttribute("onclick", "removeAction(this)");
		return removeButton;
	}
	
	function createOptionsForSelect(select){		
        var points = [[${pointsDTO}]]; 
        for (var i = 0; i < points.length; i++) { 
        	var option = document.createElement("option");
    		var optionText = document.createElement("p");
    		optionText.innerHTML = points[i].pointName;
    		option.appendChild(optionText);
    		select.appendChild(option);
        }      
	}
	
	function createSelectTag(){
		var select = document.createElement("select");
		select.setAttribute("name", "punctIntermediar");
		select.setAttribute("class", "browser-default custom-select custom-select-md mb-3");
		return select;
	}
	
	function createDefaultOption(selectTag){
		var defaultOption = document.createElement("option");
		defaultOption.setAttribute("value", "");
		defaultOption.setAttribute("disabled", "disabled");
		defaultOption.setAttribute("selected", "selected");
				
		var text = document.createElement("p");
		text.innerHTML = "Punct intermediar";
		
		defaultOption.appendChild(text);
		selectTag.appendChild(defaultOption);
	}
	
	function removeAction(e){
		e.parentNode.parentNode.parentNode.removeChild(e.parentNode.parentNode);
	}