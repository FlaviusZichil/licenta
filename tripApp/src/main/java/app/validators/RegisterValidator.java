package app.validators;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RegisterValidator {
	
	public boolean isNameValid(String name) {
		if(name.length() < 2 || !this.containsJustLetters(name)) {
			return false;
		}
		return true;
	}
	
	private boolean containsJustLetters(String text) {
		for(int i = 0; i < text.length(); i++) {
			if(!Character.isLetter(text.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	private String formatNameProperly(String name) {
		return "";
	}
	
	private boolean containsCapitalLetter(String text) {
		for(int i = 0; i < text.length(); i++) {
			if(!text.equals(text.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isPasswordValid(String password) {
		if(password.length() <= 6 || !this.containsCapitalLetter(password)) {
			return false;
		}
		return true;
	}
	
	public boolean isEmailValid(String email) {
		if(email.length() < 10 || !this.containsAt(email)) {
			return false;
		}
		return true;
	}
	
	private boolean containsAt(String text) {
		for(int i = 0; i < text.length(); i++) {
			// @ cannot be the first letter of email
			if(text.charAt(i) == '@' && i != 0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isDateValid(String date) {
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
