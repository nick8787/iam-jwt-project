package spring.security.jwt.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import spring.security.jwt.model.constants.ApiConstants;

import java.util.ArrayList;
import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PasswordUtils {
    private static final Random RND = new Random();

    public static String generatePassword() {
        int charactersNumber = ApiConstants.REQUIRED_MIN_CHARACTERS_NUMBER_IN_PASSWORD;
        int digitsNumber = ApiConstants.REQUIRED_MIN_DIGITS_NUMBER_IN_PASSWORD;
        int lettersUCaseNumber = ApiConstants.REQUIRED_MIN_LETTERS_NUMBER_EVERY_CASE_IN_PASSWORD;
        int lettersLCaseNumber = ApiConstants.REQUIRED_MIN_PASSWORD_LENGTH
                - charactersNumber - digitsNumber - lettersUCaseNumber;
        String characters = RandomStringUtils.random(charactersNumber, ApiConstants.PASSWORD_CHARACTERS);
        String digits = RandomStringUtils.random(digitsNumber, ApiConstants.PASSWORD_DIGITS);
        String lettersUCase = RandomStringUtils.random(lettersUCaseNumber, ApiConstants.PASSWORD_LETTERS_UPPER_CASE);
        String lettersLCase = RandomStringUtils.random(lettersLCaseNumber, ApiConstants.PASSWORD_LETTERS_LOWER_CASE);

        ArrayList<Character> randomPasswordCharacters = new ArrayList<>();
        for (char character : (characters + digits + lettersUCase + lettersLCase).toCharArray()) {
            randomPasswordCharacters.add(character);
        }

        StringBuilder password = new StringBuilder();
        int length = randomPasswordCharacters.size();
        for (int i = 0; i < length; i++) {
            int randomPosition = RND.nextInt((randomPasswordCharacters.size()));
            password.append(randomPasswordCharacters.get(randomPosition));
            randomPasswordCharacters.remove(randomPosition);
        }

        return password.toString();
    }

    public static boolean isNotValidPassword(String password) {
        if (password == null || password.isEmpty() || password.trim().isEmpty()) {
            return true;
        }
        String trim = password.trim();
        if (trim.length() < ApiConstants.REQUIRED_MIN_PASSWORD_LENGTH) {
            return true;
        }
        int charactersNumber = ApiConstants.REQUIRED_MIN_CHARACTERS_NUMBER_IN_PASSWORD;
        int lettersUCaseNumber = ApiConstants.REQUIRED_MIN_LETTERS_NUMBER_EVERY_CASE_IN_PASSWORD;
        int lettersLCaseNumber = ApiConstants.REQUIRED_MIN_LETTERS_NUMBER_EVERY_CASE_IN_PASSWORD;
        int digitsNumber = ApiConstants.REQUIRED_MIN_DIGITS_NUMBER_IN_PASSWORD;
        for (int i = 0; i < trim.length(); i++) {
            String currentLetter = String.valueOf(trim.charAt(i));
            if (!ApiConstants.PASSWORD_ALL_CHARACTERS.contains(currentLetter)) {
                return true;
            }
            charactersNumber -= ApiConstants.PASSWORD_CHARACTERS.contains(currentLetter) ? 1 : 0;
            lettersUCaseNumber -= ApiConstants.PASSWORD_LETTERS_UPPER_CASE.contains(currentLetter) ? 1 : 0;
            lettersLCaseNumber -= ApiConstants.PASSWORD_LETTERS_LOWER_CASE.contains(currentLetter) ? 1 : 0;
            digitsNumber -= ApiConstants.PASSWORD_DIGITS.contains(currentLetter) ? 1 : 0;
        }
        return ((charactersNumber > 0) || (lettersUCaseNumber > 0) || (lettersLCaseNumber > 0) || (digitsNumber > 0));
    }

}
