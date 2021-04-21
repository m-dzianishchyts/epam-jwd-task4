package by.epamtc.text.runner;

import by.epamtc.text.util.processors.TextProcessingCharArrayUtils;
import by.epamtc.text.util.processors.TextProcessingException;
import by.epamtc.text.util.processors.TextProcessingRegexUtils;
import by.epamtc.text.util.processors.TextProcessingStringUtils;
import by.epamtc.text.util.processors.WordPredicate;
import by.epamtc.text.util.providers.FileException;
import by.epamtc.text.util.providers.TextProvidingUtils;

import java.io.File;
import java.util.function.Predicate;

public class Runner {

    public static void main(String[] args) throws FileException, TextProcessingException {
        	    String text = TextProvidingUtils.readTextFromConsole();
//        String text = TextProvidingUtils.readTextFromFile(new File("input.txt"));
        text = text.trim();
        System.out.println("Your text: " + text);
        System.out.println();

        String editedTextViaRegex = TextProcessingRegexUtils.replaceLetterByPositionInWord(text, 2, '_');
        String editedTextViaString = TextProcessingStringUtils.replaceLetterByPositionInWord(text, 2, '_');
        char[] editedTextArray = text.toCharArray();
        TextProcessingCharArrayUtils.replaceLetterByPositionInWord(editedTextArray, 2, '_');
        String editedTextViaCharArray = new String(editedTextArray);
        System.out.println("Regex result 1:      " + editedTextViaRegex);
        System.out.println("String result 1:     " + editedTextViaString);
        System.out.println("Char array result 1: " + editedTextViaCharArray);
        System.out.println();

        editedTextViaRegex = TextProcessingRegexUtils.replaceEachLetterAfter(text, 'r', 'd', 'k');
        editedTextViaString = TextProcessingStringUtils.replaceEachLetterAfter(text, 'r', 'd', 'k');
        editedTextArray = text.toCharArray();
        TextProcessingCharArrayUtils.replaceEachLetterAfter(editedTextArray, 'r', 'd', 'k');
        editedTextViaCharArray = new String(editedTextArray);
        System.out.println("Regex result 2:      " + editedTextViaRegex);
        System.out.println("String result 2:     " + editedTextViaString);
        System.out.println("Char array result 2: " + editedTextViaCharArray);
        System.out.println();

        Predicate<String> stringPredicate = WordPredicate.specifyWordAsStringLength(3);
        Predicate<char[]> charArrayPredicate = WordPredicate.specifyWordAsCharArrayLength(3);
        editedTextViaRegex = TextProcessingRegexUtils.replaceEachWordByPredicate(text, stringPredicate, "replacement");
        editedTextViaString = TextProcessingStringUtils.replaceEachWordByPredicate(text, stringPredicate,
                                                                                   "replacement");
        char[] textArrayCopy = text.toCharArray();
        editedTextArray = TextProcessingCharArrayUtils.replaceEachWordByPredicate(textArrayCopy, charArrayPredicate,
                                                                                  "replacement".toCharArray());
        editedTextViaCharArray = new String(editedTextArray);
        System.out.println("Regex result 3:      " + editedTextViaRegex);
        System.out.println("String result 3:     " + editedTextViaString);
        System.out.println("Char array result 3: " + editedTextViaCharArray);
        System.out.println();

        editedTextViaRegex = TextProcessingRegexUtils.removeAllNotAlphabeticExceptSpaces(text);
        editedTextViaString = TextProcessingStringUtils.removeAllNotAlphabeticExceptSpaces(text);
        textArrayCopy = text.toCharArray();
        editedTextArray = TextProcessingCharArrayUtils.removeAllNotAlphabeticExceptSpaces(textArrayCopy);
        editedTextViaCharArray = new String(editedTextArray);
        System.out.println("Regex result 4:      " + editedTextViaRegex);
        System.out.println("String result 4:     " + editedTextViaString);
        System.out.println("Char array result 4: " + editedTextViaCharArray);
        System.out.println();

        stringPredicate = WordPredicate.specifyWordAsStringLength(4)
                                       .and(WordPredicate.CONSONANT_PREFIX_PREDICATE_STRING_IMPLEMENTATION);
        charArrayPredicate = WordPredicate.specifyWordAsCharArrayLength(4)
                                          .and(WordPredicate.CONSONANT_PREFIX_PREDICATE_CHAR_ARRAY_IMPLEMENTATION);
        editedTextViaRegex = TextProcessingRegexUtils.replaceEachWordByPredicate(text, stringPredicate, "");
        editedTextViaString = TextProcessingStringUtils.replaceEachWordByPredicate(text, stringPredicate, "");
        textArrayCopy = text.toCharArray();
        editedTextArray = TextProcessingCharArrayUtils.replaceEachWordByPredicate(textArrayCopy, charArrayPredicate,
                                                                                  "".toCharArray());
        editedTextViaCharArray = new String(editedTextArray);
        System.out.println("Regex result 5:      " + editedTextViaRegex);
        System.out.println("String result 5:     " + editedTextViaString);
        System.out.println("Char array result 5: " + editedTextViaCharArray);
        System.out.println();
    }
}
