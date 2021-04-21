package by.epamtc.text.util.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class TextProcessingCharArrayUtils {

    private TextProcessingCharArrayUtils() {
    }

    public static void replaceLetterByPositionInWord(char[] text, int positionInWord, char replacement)
            throws TextProcessingException {
        checkTextOnNull(text);
        if (positionInWord < 0) {
            throw new TextProcessingException("Replacement position cannot be negative.");
        }
        int offset = 0;
        var optionalWordPositions = findFirstWordPositions(text, offset);
        while (optionalWordPositions.isPresent()) {
            var wordPositions = optionalWordPositions.get();
            int wordStartPosition = wordPositions.getFirst();
            int wordEndPosition = wordPositions.getSecond();
            int wordLength = wordEndPosition - wordStartPosition;
            if (wordLength > positionInWord) {
                int replacementPosition = wordStartPosition + positionInWord;
                text[replacementPosition] = replacement;
            }
            offset = wordEndPosition;
            optionalWordPositions = findFirstWordPositions(text, offset);
        }
    }

    private static Optional<Pair<Integer, Integer>> findFirstWordPositions(char[] text, int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset cannot be negative.");
        }
        int wordStartPosition = offset;
        while (wordStartPosition < text.length) {
            char textChar = text[wordStartPosition];
            if (isAlphabeticOrDigit(textChar)) {
                break;
            }
            wordStartPosition++;
        }
        if (wordStartPosition >= text.length) {
            return Optional.empty();
        }
        int wordEndPosition = wordStartPosition;
        while (wordEndPosition < text.length) {
            char textChar = text[wordEndPosition];
            if (!isAlphabeticOrDigit(textChar)) {
                break;
            }
            wordEndPosition++;
        }
        Pair<Integer, Integer> wordPositions = new Pair<>(wordStartPosition, wordEndPosition);
        return Optional.of(wordPositions);
    }

    private static List<Pair<Integer, Integer>> findWordsPositionsByPredicate(char[] text,
                                                                              Predicate<char[]> predicate) {
        List<Pair<Integer, Integer>> wordsPositionsList = new ArrayList<>();
        int offset = 0;
        var optionalWordPositions = findFirstWordPositions(text, offset);
        while (optionalWordPositions.isPresent()) {
            var wordPositions = optionalWordPositions.get();
            int wordStartPosition = wordPositions.getFirst();
            int wordEndPosition = wordPositions.getSecond();
            int wordLength = wordEndPosition - wordStartPosition;
            char[] word = new char[wordLength];
            System.arraycopy(text, wordStartPosition, word, 0, wordLength);
            if (predicate.test(word)) {
                wordsPositionsList.add(wordPositions);
            }
            offset = wordEndPosition;
            optionalWordPositions = findFirstWordPositions(text, offset);
        }
        return wordsPositionsList;
    }

    private static boolean isAlphabeticOrDigit(char textChar) {
        return Character.isLetter(textChar) || Character.isDigit(textChar);
    }

    private static Optional<Pair<Integer, Integer>> findFirstNonAlphabeticSequencePositions(char[] text, int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset cannot be negative.");
        }
        int sequenceStartPosition = offset;
        while (sequenceStartPosition < text.length) {
            char textChar = text[sequenceStartPosition];
            if (!isAlphabeticOrSpace(textChar)) {
                break;
            }
            sequenceStartPosition++;
        }
        if (sequenceStartPosition >= text.length) {
            return Optional.empty();
        }
        int sequenceEndPosition = sequenceStartPosition;
        while (sequenceEndPosition < text.length) {
            char textChar = text[sequenceEndPosition];
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

    public static void replaceEachLetterAfter(char[] text, char charBehind, char charToReplace, char replacement)
            throws TextProcessingException {
        checkTextOnNull(text);
        for (int i = 0; i < text.length; i++) {
            char textChar = text[i];
            int nextTextCharPosition = i + 1;
            if (textChar == charBehind && nextTextCharPosition < text.length) {
                char nextTextChar = text[nextTextCharPosition];
                if (nextTextChar == charToReplace) {
                    text[nextTextCharPosition] = replacement;
                    i++;
                }
            }
        }
    }

    public static char[] replaceEachWordByPredicate(char[] text, Predicate<char[]> predicate, char[] replacement)
            throws TextProcessingException {
        checkTextOnNull(text);
        if (predicate == null) {
            throw new TextProcessingException("Predicate cannot be null.");
        }
        if (replacement == null) {
            throw new TextProcessingException("Replacement cannot be null.");
        }
        List<Pair<Integer, Integer>> wordsToReplacePositionsList = findWordsPositionsByPredicate(text, predicate);
        int editedTextLength = computeEditedTextLength(text, replacement, wordsToReplacePositionsList);
        char[] editedText = new char[editedTextLength];
        int textOffset = 0;
        int editedTextOffset = 0;
        for (var wordPositions : wordsToReplacePositionsList) {
            int wordStartPosition = wordPositions.getFirst();
            int wordEndPosition = wordPositions.getSecond();
            int wordLength = wordEndPosition - wordStartPosition;
            int beforeWordAppendingLength = wordStartPosition - textOffset;
            System.arraycopy(text, textOffset, editedText, editedTextOffset, beforeWordAppendingLength);
            editedTextOffset += beforeWordAppendingLength;
            System.arraycopy(replacement, 0, editedText, editedTextOffset, replacement.length);
            editedTextOffset += replacement.length;
            textOffset += beforeWordAppendingLength + wordLength;
        }
        int tailLength = text.length - textOffset;
        System.arraycopy(text, textOffset, editedText, editedTextOffset, tailLength);
        return editedText;
    }

    private static int computeEditedTextLength(char[] text, char[] replacement,
                                               List<Pair<Integer, Integer>> wordsToReplacePositionsList) {
        int wordsToReplaceTotalLength = 0;
        for (var wordPositions : wordsToReplacePositionsList) {
            int wordLength = wordPositions.getSecond() - wordPositions.getFirst();
            wordsToReplaceTotalLength += wordLength;
        }
        int replacementTotalLength = replacement.length * wordsToReplacePositionsList.size();
        int textLengthDelta = replacementTotalLength - wordsToReplaceTotalLength;
        int editedTextLength = text.length + textLengthDelta;
        return editedTextLength;
    }

    public static char[] removeAllNotAlphabeticExceptSpaces(char[] text) throws TextProcessingException {
        checkTextOnNull(text);
        char[] editedText = new char[text.length];
        int textOffset = 0;
        int editedTextOffset = 0;
        var optionalNotAlphabeticSequencePositions = findFirstNonAlphabeticSequencePositions(text, textOffset);
        while (optionalNotAlphabeticSequencePositions.isPresent()) {
            var notAlphabeticSequencePositions = optionalNotAlphabeticSequencePositions.get();
            int positionBeforeSequence = notAlphabeticSequencePositions.getFirst() - 1;
            int positionAfterSequence = notAlphabeticSequencePositions.getSecond();
            int beforeSequenceAppendingLength = positionBeforeSequence - textOffset + 1;
            boolean betweenTwoLetterSequences = isValidIndex(positionBeforeSequence, text.length) &&
                                                isValidIndex(positionAfterSequence, text.length) &&
                                                Character.isLetter(text[positionBeforeSequence]) &&
                                                Character.isLetter(text[positionAfterSequence]);
            System.arraycopy(text, textOffset, editedText, editedTextOffset, beforeSequenceAppendingLength);
            textOffset = positionAfterSequence;
            editedTextOffset += beforeSequenceAppendingLength;
            if (betweenTwoLetterSequences) {
                editedText[editedTextOffset] = ' ';
                editedTextOffset++;
            }
            optionalNotAlphabeticSequencePositions = findFirstNonAlphabeticSequencePositions(text, textOffset);
        }
        int tailLength = text.length - textOffset;
        System.arraycopy(text, textOffset, editedText, editedTextOffset, tailLength);
        char[] trimmedEditedText = trimEditedText(editedText);
        return trimmedEditedText;
    }

    private static char[] trimEditedText(char[] editedText) {
        int trimPosition = 0;
        while (trimPosition < editedText.length) {
            if (editedText[trimPosition] == 0) {
                break;
            }
            trimPosition++;
        }
        char[] trimmedEditedText = Arrays.copyOf(editedText, trimPosition);
        return trimmedEditedText;
    }

    private static boolean isValidIndex(int index, int dataLength) {
        return index >= 0 && index < dataLength;
    }

    private static void checkTextOnNull(char[] text) throws TextProcessingException {
        if (text == null) {
            throw new TextProcessingException("Text cannot be null.");
        }
    }
}
