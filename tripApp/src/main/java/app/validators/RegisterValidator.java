package app.validators;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import antlr.StringUtils;

public class RegisterValidator {
		
	public static boolean isNameValid(String name) {
		if(name.length() < 2 || !isWellFormated(name) || calculateNumberOfConsecutiveSpaces(name) > 1) {
			return false;
		}
		return true;
	}
	
	private static Integer calculateNumberOfConsecutiveSpaces(String name) {
		Integer numberOfSpaces = 0;
		for(int i = 0; i < name.length() - 1; i++) {
			if(name.charAt(i) == ' ' && name.charAt(i + 1) == ' ') {
				numberOfSpaces++;
			}
		}
		return numberOfSpaces + 1;
	}
	
	private static boolean isWellFormated(String text) {
		for(int i = 0; i < text.length(); i++) {
			if(!Character.isLetter(text.charAt(i)) && text.charAt(i) != '-' && text.charAt(i) != ' ') {
				return false;
			}
		}
		if(text.charAt(0) == '-' || text.charAt(text.length() - 1) == '-' ) {
			return false;
		}
		return true;
	}
	
	public static String formatNameProperly(String name) {		
		String formatedName = formatName(name);
		String extraFormatedName = verify(formatedName, "-");
		String finalName = verify(extraFormatedName, " ");
		
		return finalName;
	}
	
	private static String formatName(String name) {
		String formatedName = name.toLowerCase().trim();
		formatedName = formatedName.substring(0, 1).toUpperCase() + formatedName.substring(1).toLowerCase();
		return formatedName;
	}
	
	private static String verify(String formatedName, String element) {
		if(formatedName.contains(element)) {
			List<String> names = new ArrayList<>(Arrays.asList(formatedName.split(element)));
			String newName = "";
			
			for(String nameObject : names) {
				newName += formatName(nameObject);
				newName += " ";
			}
			formatedName = newName.trim();
		}
		return formatedName;
	}
	
	private static boolean containsCapitalLetter(String text) {
		for(int i = 0; i < text.length(); i++) {
			if(!text.equals(text.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isPasswordValid(String password) {
		if(password.length() < 6 || !containsCapitalLetter(password)) {
			return false;
		}
		return true;
	}
	
	public static boolean isEmailValid(String email) {
		if(email.length() < 10 || !containsAt(email)) {
			return false;
		}
		return true;
	}
	
	private static boolean containsAt(String text) {
		for(int i = 0; i < text.length(); i++) {
			// @ cannot be the first letter of email
			if(text.charAt(i) == '@' && i != 0) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isDateValid(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate now = LocalDate.now();
		LocalDate givenDate = LocalDate.parse(date, formatter);
		LocalDate yearsAgoDate = now.minusYears(16);
		
		if(givenDate.isAfter(yearsAgoDate)) {
			return false;
		}
		return true;
	}
}
