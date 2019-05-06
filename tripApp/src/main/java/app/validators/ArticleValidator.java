package app.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArticleValidator {

	public boolean isTitleValid(String title) {
		if(title.length() < 2) {
			return false;
		}
		return true;
	}
	
	public boolean isDescriptionValid(String description) {
		if(description.length() < 200) {
			return false;
		}
		return true;
	}

	public boolean hasEmptyValues(String listAsString, Integer minLength) {
		List<String> elements = new ArrayList<>(Arrays.asList(listAsString.split(",")));
		for(String element : elements) {
			if(element.length() < minLength) {
				return true;
			}
		}
		return false;
	}
}
