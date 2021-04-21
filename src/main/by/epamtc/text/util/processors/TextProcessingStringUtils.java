package by.epamtc.text.util.processors;

import java.util.Optional;
import java.util.function.Predicate;

public final class TextProcessingStringUtils {

    private TextProcessingStringUtils() {
    }

    public static String replaceLetterByPositionInWord(String text, int positionInWord, char replacement)
            throws TextProcessingException {
        checkTextOnNull(text);
        if (positionInWord < 0) {
            throw new TextProcessingException("Replacement position cannot be negative.");
        }
        StringBuilder builder = new StringBuilder(text);
        int offset = 0;
        var optionalWordPositions = findFirstWordPositions(text, offset);
        while (optionalWordPositions.isPresent()) {
            var wordPositions = optionalWordPositions.get();
            int wordStartPosition = wordPositions.getFirst();
            int wordEndPosition = wordPositions.getSecond();
            int wordLength = wordEndPosition - wordStartPosition;
            if (wordLength > positionInWord) {
                int replacementPosition = wordStartPosition + positionInWord;
                builder.setCharAt(replacementPosition, replacement);
            }
            offset = wordEndPosition;
            optionalWordPositions = findFirstWordPositions(text, offset);
        }
        return builder.toString();
    }

    private static Optional<Pair<Integer, Integer>> findFirstWordPositions(String text, int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset cannot be negative.");
        }
        int wordStartPosition = offset;
        while (wordStartPosition < text.length()) {
            char textChar = text.charAt(wordStartPosition);
            if (isAlphabeticOrDigit(textChar)) {
                break;
            }
            wordStartPosition++;
        }
        if (wordStartPosition >= text.length()) {
            return Optional.empty();
        }
        int wordEndPosition = wordStartPosition;
        while (wordEndPosition < text.length()) {
            char textChar = text.charAt(wordEndPosition);
            if (!isAlphabeticOrDigit(textChar)) {
                break;
            }
            wordEndPosition++;
        }
        Pair<Integer, Integer> wordPositions = new Pair<>(wordStartPosition, wordEndPosition);
        return Optional.of(wordPositions);
    }

    private static boolean isAlphabeticOrDigit(char textChar) {
        return Character.isLetter(textChar) || Character.isDigit(textChar);
    }

    private static Optional<Pair<Integer, Integer>> findFirstNonAlphabeticSequencePositions(String text, int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset cannot be negative.");
        }
        int sequenceStartPosition = offset;
        while (sequenceStartPosition < text.length()) {
            char textChar = text.charAt(sequenceStartPosition);
            if (!isAlphabeticOrSpace(textChar)) {
                break;
            }
            sequenceStartPosition++;
        }
        if (sequenceStartPosition >= text.length()) {
            return Optional.empty();
        }
        int sequenceEndPosition = sequenceStartPosition;
        while (sequenceEndPosition < text.length()) {
            char textChar = text.charAt(sequenceEndPosition);
            if (isAlphabeticOrSpace(textChar)) {
                break;
            }
            sequenceEndPosition++;
        }
        Pair<Integer, Integer> sequencePositions = new Pair<>(sequenceStartPosition, sequenceEndPosition);
        return Optional.of(sequencePositions);
    }

    private static boolean isAlphabeticOrSpace(char textChar) {
        return Character.isLetter(textChar) || Character.isWhitespace(textChar);
    }

    public static String replaceEachLetterAfter(String text, char charBehind, char charToReplace, char replacement)
            throws TextProcessingException {
        checkTextOnNull(text);
        StringBuilder builder = new StringBuilder(text);
        for (int i = 0; i < builder.length(); i++) {
            char textChar = builder.charAt(i);
            int nextTextCharPosition = i + 1;
            if (textChar == charBehind && nextTextCharPosition < builder.length()) {
                char nextTextChar = builder.charAt(nextTextCharPosition);
                if (nextTextChar == charToReplace) {
                    builder.setCharAt(nextTextCharPosition, replacement);
                    i++;
                }
            }
        }
        return builder.toString();
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
        int offset = 0;
        var optionalWordPositions = findFirstWordPositions(text, offset);
        while (optionalWordPositions.isPresent()) {
            var wordPositions = optionalWordPositions.get();
            int wordStartPosition = wordPositions.getFirst();
            int wordEndPosition = wordPositions.getSecond();
            String word = text.substring(wordStartPosition, wordEndPosition);
            if (predicate.test(word)) {
                builder.append(text, offset, wordStartPosition);
                builder.append(replacement);
            } else {
                builder.append(text, offset, wordEndPosition);
            }
            offset = wordEndPosition;
            optionalWordPositions = findFirstWordPositions(text, offset);
        }
        builder.append(text, offset, text.length());
        return builder.toString();
    }

    public static String removeAllNotAlphabeticExceptSpaces(String text) throws TextProcessingException {
        checkTextOnNull(text);
        StringBuilder builder = new StringBuilder();
        int offset = 0;
        var optionalNotAlphabeticSequencePositions = findFirstNonAlphabeticSequencePositions(text, offset);
        while (optionalNotAlphabeticSequencePositions.isPresent()) {
            var notAlphabeticSequencePositions = optionalNotAlphabeticSequencePositions.get();
            int positionBeforeSequence = notAlphabeticSequencePositions.getFirst() - 1;
            int positionAfterSequence = notAlphabeticSequencePositions.getSecond();
            boolean betweenTwoLetterSequences = isValidIndex(positionBeforeSequence, text.length()) &&
                                                isValidIndex(positionAfterSequence, text.length()) &&
                                                Character.isLetter(text.charAt(positionBeforeSequence)) &&
                                                Character.isLetter(text.charAt(positionAfterSequence));
            builder.append(text, offset, positionBeforeSequence + 1);
            if (betweenTwoLetterSequences) {
                builder.append(' ');
            }
            offset = positionAfterSequence;
            optionalNotAlphabeticSequencePositions = findFirstNonAlphabeticSequencePositions(text, offset);
        }
        builder.append(text, offset, text.length());
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
