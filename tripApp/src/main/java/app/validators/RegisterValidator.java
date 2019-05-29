package app.validators;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
		String allLowerCase = name.toLowerCase();
		// transform first letter to upper case 
		allLowerCase = allLowerCase.replace(allLowerCase.charAt(0), Character.toUpperCase(allLowerCase.charAt(0)));
		for(int i = 0; i < allLowerCase.length() - 1; i++) {
			if(allLowerCase.charAt(i) == ' ' || allLowerCase.charAt(i) == '-') {
				allLowerCase = allLowerCase.replace(allLowerCase.charAt(i + 1), Character.toUpperCase(allLowerCase.charAt(i + 1)));
			}
		}
		return allLowerCase.trim();
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
