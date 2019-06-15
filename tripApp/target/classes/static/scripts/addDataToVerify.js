function addSelectToVerify(select, resultParagraph){
		var result = select.options[select.selectedIndex].text;
		
		switch(resultParagraph){
//			case verifyMountain:
//				resultParagraph.innerHTML = result;
//				break;
//			case verifyCity:
//				resultParagraph.innerHTML = result;
//				break;
			case verifyPeak:
				resultParagraph.innerHTML = result;
				break;
//			case verifyAltitude:
//				resultParagraph.innerHTML = result;
//				break;
			case verifyInitialPoint:
				resultParagraph.innerHTML = result;
				break;
			case verifyIntermediarPoint:
				if(resultParagraph.innerHTML == ""){
					resultParagraph.innerHTML = resultParagraph.innerHTML + result;
				}
				else{
					resultParagraph.innerHTML = resultParagraph.innerHTML + " - " + result;
				}
				break;
				
			case verifyFinalPoint:
				resultParagraph.innerHTML = result;
				break;
			case verifyDifficulty:
				resultParagraph.innerHTML = result;
				break;
		}	
	}
	
	function addInputToVerify(input, resultParagraph){
		var result = input.value;
		
		switch(resultParagraph){
			case verifyStartDate:
				resultParagraph.innerHTML = result;
				break;
			case verifyEndDate:
				resultParagraph.innerHTML = result;
				break;
			case verifyCapacity:
				resultParagraph.innerHTML = result;
				break;
			case verifyPoints:
				resultParagraph.innerHTML = result;
				break;
		}
	}	