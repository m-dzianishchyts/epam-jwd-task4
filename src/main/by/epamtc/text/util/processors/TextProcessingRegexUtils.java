package by.epamtc.text.util.processors;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextProcessingRegexUtils {

    private static final Pattern NON_LETTER_SPACE_SEQUENCE_PATTERN = Pattern
            .compile("[^a-zа-яё ]+", Pattern.CASE_INSENSITIVE);
    private static final Pattern WORD_PATTERN = Pattern.compile("[a-zа-яё0-9]+", Pattern.CASE_INSENSITIVE);

    private TextProcessingRegexUtils() {
    }

    public static String replaceLetterByPositionInWord(String text, int positionInWord, char replacement)
            throws TextProcessingException {
        checkTextOnNull(text);
        if (positionInWord < 0) {
            throw new TextProcessingException("Replacement position cannot be negative.");
        }
        StringBuilder builder = new StringBuilder(text);
        Matcher wordMatcher = WORD_PATTERN.matcher(text);
        while (wordMatcher.find()) {
            int wordLength = wordMatcher.end() - wordMatcher.start();
            if (wordLength > positionInWord) {
                int replacementPosition = wordMatcher.start() + positionInWord;
                builder.setCharAt(replacementPosition, replacement);
            }
        }
        return builder.toString();
    }

    public static String replaceEachLetterAfter(String text, char charBehind, char charToReplace, char replacement)
            throws TextProcessingException {
        checkTextOnNull(text);
        String wrongSequence = String.format("%c%c", charBehind, charToReplace);
        String rightSequence = String.format("%c%c", charBehind, replacement);
        String result = text.replaceAll(wrongSequence, rightSequence);
        return result;
    }

    public static String replaceEachWordByPredicate(String text, Predicate<String> predicate, String replacement)
            throws TextProcessingException {
        checkTextOnNull(text);
        if (predicate == null) {
            throw new TextProcessingException("Predicate cannot be null.");
        }
        if (replacement == null) {
            throw new TextProcessingException("Replacement cannot be null.");
        }
        StringBuilder builder = new StringBuilder();
        Matcher wordMatcher = WORD_PATTERN.matcher(text);
        while (wordMatcher.find()) {
            String word = wordMatcher.group();
            if (predicate.test(word)) {
                wordMatcher.appendReplacement(builder, replacement);
            }
        }
        wordMatcher.appendTail(builder);
        return builder.toString();
    }

    public static String removeAllNotAlphabeticExceptSpaces(String text) throws TextProcessingException {
        checkTextOnNull(text);
        StringBuilder builder = new StringBuilder();
        Matcher nonLetterSpaceSequenceMatcher = NON_LETTER_SPACE_SEQUENCE_PATTERN.matcher(text);
        while (nonLetterSpaceSequenceMatcher.find()) {
            int positionBeforeGroup = nonLetterSpaceSequenceMatcher.start() - 1;
            int positionAfterGroup = nonLetterSpaceSequenceMatcher.end();
            boolean betweenTwoLetterSequences = isValidIndex(positionBeforeGroup, text.length()) &&
                                                isValidIndex(positionAfterGroup, text.length()) &&
                                                Character.isLetter(text.charAt(positionBeforeGroup)) &&
                                                Character.isLetter(text.charAt(positionAfterGroup));
            nonLetterSpaceSequenceMatcher.appendReplacement(builder, betweenTwoLetterSequences ? " " : "");
        }
        nonLetterSpaceSequenceMatcher.appendTail(builder);
        return builder.toString();
    }

    private static boolean isValidIndex(int index, int dataLength) {
        return index >= 0 && index < dataLength;
    }

    private static void checkTextOnNull(String text) throws TextProcessingException {
        if (text == null) {
            throw new TextProcessingException("Text cannot be null.");
        }
    }
}
