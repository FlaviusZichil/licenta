				var modal = document.getElementById('previewModal');	
				var modalTrigger = document.getElementById("modalTrigger");	
				var span = document.getElementsByClassName("close")[0];
				
				function verifyIfDivContainsElement(div, element, counterInput){
					if(div.contains(element)){
						div.setAttribute("class", "collapse show");
						element.focus();
					}					
				}
				
				function focusInputs(inputs){
					var counterInput = 0;
					for(var i = 0; i < inputs.length; i++){
						if(inputs[i].value == ""){		
							verifyIfDivContainsElement(collapseFour, inputs[i], counterInput++);
							verifyIfDivContainsElement(collapseTwo, inputs[i], counterInput++);
						}
					}
					return counterInput;
				}
				
				function focusSelects(selects){
					var counterSelect = 0;
					for(var i = 0; i < selects.length; i++){
						var option = selects[i].options[selects[i].selectedIndex].text;
						if(option == "Numele varfului" || option == "Dificultate" || 
						   option == "Punctul initial" || option == "Punctul final" || option == "Punct intermediar"){
							verifyIfDivContainsElement(collapseOne, selects[i], counterSelect++);
							verifyIfDivContainsElement(collapseTwo, selects[i], counterSelect++);
							verifyIfDivContainsElement(collapseThree, selects[i], counterSelect++);
							verifyIfDivContainsElement(collapseFour, selects[i], counterSelect++);
						}
					}
					return counterSelect;
				}
			
				modalTrigger.onclick = function() {
					
					var collapseOne = document.getElementById("collapseOne");
					var collapseTwo = document.getElementById("collapseTwo");
					var collapseThree = document.getElementById("collapseThree");
					var collapseFour = document.getElementById("collapseFour");
					
					var inputs = document.forms["addTripForm"].getElementsByTagName("input");
					var selects = document.forms["addTripForm"].getElementsByTagName("select");
									
					counterInput = focusInputs(inputs);
					counterSelect = focusSelects(selects);
									
					if(counterSelect == 0 && counterInput == 0){
						modal.style.display = "block";
					}				  
				}
				
				span.onclick = function() {
				  modal.style.display = "none";
				}
				
				window.onclick = function(event) {
				  if (event.target == modal) {
				    modal.style.display = "none";
				  }
				}