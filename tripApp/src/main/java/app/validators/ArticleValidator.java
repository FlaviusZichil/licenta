package app.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

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
	
	public static boolean isArticleValid(String title, String description, String sectionsTitle, String sectionsContent, HttpSession session) {
		ArticleValidator validator = new ArticleValidator();
		
		boolean isTitleValid = true;
		boolean isDescriptionValid = true;
		boolean areSubtitlesValid = true;		
		boolean areSectionsValid = true;
		
		if(!validator.isTitleValid(title)) {
			session.setAttribute("wrongTitle", true);
			isTitleValid = false;
		}
		
		if(!validator.isDescriptionValid(description)) {
			session.setAttribute("wrongDescription", true);
			isDescriptionValid = false;
		}
			
		if(validator.hasEmptyValues(sectionsTitle, 1)) {
			session.setAttribute("wrongSectionTitle", true);
			areSubtitlesValid = false;
		}
		
		if(validator.hasEmptyValues(sectionsContent, 50)) {
			session.setAttribute("wrongSectionContent", true);
			areSectionsValid = false;
		}
		
		if(isTitleValid && isDescriptionValid && areSubtitlesValid && areSectionsValid) {
			return true;
		}
		return false;
	}
}
