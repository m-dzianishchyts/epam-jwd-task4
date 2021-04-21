package by.epamtc.text.util.processors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings({"unused", "CodeBlock2Expr"})
class TextProcessingUtilsTest {

    private static String testText = "First(1) word, second(2) word, third(3) word - six(6) words!";

    private static Stream<Arguments> provideArgumentsForReplaceLetterByPositionInWordValidTest() {
        return Stream.of(Arguments.of(testText, 0, '_', "_irst(_) _ord, _econd(_) _ord, _hird(_) _ord - _ix(_) _ords!"),
                         Arguments.of(testText, 1, '_', "F_rst(1) w_rd, s_cond(2) w_rd, t_ird(3) w_rd - s_x(6) w_rds!"),
                         Arguments.of(testText, 2, '_', "Fi_st(1) wo_d, se_ond(2) wo_d, th_rd(3) wo_d - si_(6) wo_ds!"),
                         Arguments.of(testText, 3, '_', "Fir_t(1) wor_, sec_nd(2) wor_, thi_d(3) wor_ - six(6) wor_s!"),
                         Arguments.of(testText, 4, '_', "Firs_(1) word, seco_d(2) word, thir_(3) word - six(6) word_!"),
                         Arguments.of(testText, 5, '_', "First(1) word, secon_(2) word, third(3) word - six(6) words!"),
                         Arguments.of(testText, 6, '_', testText), Arguments.of("", 5, '_', ""));
    }

    private static Stream<Arguments> provideArgumentsForReplaceLetterByPositionInWordAsStringInvalidTest() {
        return Stream.of(Arguments.of(null, 0, '0', TextProcessingException.class),
                         Arguments.of("Any text", -10, '0', TextProcessingException.class));
    }

    private static Stream<Arguments> provideArgumentsForReplaceLetterByPositionInWordAsCharArrayInvalidTest() {
        return Stream.of(Arguments.of(null, 0, '0', TextProcessingException.class),
                         Arguments.of("Any text".toCharArray(), -10, '0', TextProcessingException.class));
    }

    private static Stream<Arguments> provideArgumentsForReplaceEachLetterAfterValidTest() {
        return Stream.of(
                Arguments.of(testText, 'r', 'd', 'k', "First(1) work, second(2) work, thirk(3) work - six(6) works!"),
                Arguments.of(testText, 't', 'd', 'k', testText), Arguments.of("abbbbb", 'a', 'b', 'a', "aabbbb"),
                Arguments.of("", 'a', 'a', 'a', ""));
    }

    private static Stream<Arguments> provideArgumentsForReplaceEachWordAsStringByPredicateValidTest() {
        String replacement = "hello world";
        return Stream.of(
                Arguments.of(testText, WordPredicate.specifyWordAsStringLength(3), replacement,
                             "First(1) word, second(2) word, third(3) word - hello world(6) words!"),
                Arguments.of(testText, WordPredicate.specifyWordAsStringLength(4), replacement,
                             "First(1) hello world, second(2) hello world, third(3) hello world - six(6) words!"),
                Arguments.of(testText, WordPredicate.specifyWordAsStringLength(5), replacement,
                             "hello world(1) word, second(2) word, hello world(3) word - six(6) hello world!"),
                Arguments.of(testText, WordPredicate.specifyWordAsStringLength(6), replacement,
                             "First(1) word, hello world(2) word, third(3) word - six(6) words!"),
                Arguments.of(testText, WordPredicate.specifyWordAsStringLength(7), replacement, testText),
                Arguments.of("", WordPredicate.specifyWordAsStringLength(7), replacement, ""));
    }

    private static Stream<Arguments> provideArgumentsForRemoveEachWordAsStringByPredicateValidTest() {
        return Stream.of(
                Arguments.of(testText, WordPredicate.specifyWordAsStringLength(5)
                                                    .and(WordPredicate.CONSONANT_PREFIX_PREDICATE_STRING_IMPLEMENTATION),
                             "", "(1) word, second(2) word, (3) word - six(6) !"),
                Arguments.of(testText, WordPredicate.specifyWordAsStringLength(4)
                                                    .and(WordPredicate.CONSONANT_PREFIX_PREDICATE_STRING_IMPLEMENTATION),
                             "", "First(1) , second(2) , third(3)  - six(6) words!"),
                Arguments.of(testText, WordPredicate.specifyWordAsStringLength(4)
                                                    .and(WordPredicate.VOWEL_PREFIX_PREDICATE_STRING_IMPLEMENTATION),
                             "", testText),
                Arguments.of(testText, WordPredicate.specifyWordAsStringLength(7)
                                                    .and(WordPredicate.CONSONANT_PREFIX_PREDICATE_STRING_IMPLEMENTATION),
                             "", testText),
                Arguments.of("", WordPredicate.specifyWordAsStringLength(7)
                                              .and(WordPredicate.CONSONANT_PREFIX_PREDICATE_STRING_IMPLEMENTATION),
                             "", ""));
    }

    private static Stream<Arguments> provideArgumentsForReplaceEachWordAsCharArrayByPredicateValidTest() {
        char[] replacement = "hello world".toCharArray();
        return Stream.of(
                Arguments.of(testText, WordPredicate.specifyWordAsCharArrayLength(3), replacement,
                             "First(1) word, second(2) word, third(3) word - hello world(6) words!"),
                Arguments.of(testText, WordPredicate.specifyWordAsCharArrayLength(4), replacement,
                             "First(1) hello world, second(2) hello world, third(3) hello world - six(6) words!"),
                Arguments.of(testText, WordPredicate.specifyWordAsCharArrayLength(5), replacement,
                             "hello world(1) word, second(2) word, hello world(3) word - six(6) hello world!"),
                Arguments.of(testText, WordPredicate.specifyWordAsCharArrayLength(6), replacement,
                             "First(1) word, hello world(2) word, third(3) word - six(6) words!"),
                Arguments.of(testText, WordPredicate.specifyWordAsCharArrayLength(7), replacement, testText),
                Arguments.of("", WordPredicate.specifyWordAsCharArrayLength(7), replacement, ""));
    }

    private static Stream<Arguments> provideArgumentsForRemoveEachWordAsCharArrayByPredicateValidTest() {
        char[] emptyReplacement = new char[0];
        return Stream.of(
                Arguments.of(testText, WordPredicate.specifyWordAsCharArrayLength(5)
                                                    .and(WordPredicate.CONSONANT_PREFIX_PREDICATE_CHAR_ARRAY_IMPLEMENTATION),
                             emptyReplacement, "(1) word, second(2) word, (3) word - six(6) !"),
                Arguments.of(testText, WordPredicate.specifyWordAsCharArrayLength(4)
                                                    .and(WordPredicate.CONSONANT_PREFIX_PREDICATE_CHAR_ARRAY_IMPLEMENTATION),
                             emptyReplacement, "First(1) , second(2) , third(3)  - six(6) words!"),
                Arguments.of(testText, WordPredicate.specifyWordAsCharArrayLength(4)
                                                    .and(WordPredicate.VOWEL_PREFIX_PREDICATE_CHAR_ARRAY_IMPLEMENTATION),
                             emptyReplacement, testText),
                Arguments.of(testText, WordPredicate.specifyWordAsCharArrayLength(7)
                                                    .and(WordPredicate.CONSONANT_PREFIX_PREDICATE_CHAR_ARRAY_IMPLEMENTATION),
                             emptyReplacement, testText),
                Arguments.of("", WordPredicate.specifyWordAsCharArrayLength(7)
                                              .and(WordPredicate.CONSONANT_PREFIX_PREDICATE_CHAR_ARRAY_IMPLEMENTATION),
                             emptyReplacement, ""));
    }

    private static Stream<Arguments> provideArgumentsForReplaceEachWordAsStringByPredicateInvalidTest() {
        return Stream.of(
                Arguments.of(null, (Predicate<String>) s -> true, "replacement", TextProcessingException.class),
                Arguments.of("null", null, "replacement", TextProcessingException.class),
                Arguments.of("null", (Predicate<String>) s -> true, null, TextProcessingException.class));
    }

    private static Stream<Arguments> provideArgumentsForReplaceEachWordAsCharArrayByPredicateInvalidTest() {
        return Stream.of(
                Arguments.of(null, (Predicate<char[]>) s -> true, "replacement".toCharArray(),
                             TextProcessingException.class),
                Arguments.of("null".toCharArray(), null, "replacement".toCharArray(), TextProcessingException.class),
                Arguments.of("null".toCharArray(), (Predicate<char[]>) s -> true, null, TextProcessingException.class));
    }

    private static Stream<Arguments> provideArgumentsForRemoveAllNonAlphabeticalExceptSpacesValidTest() {
        return Stream.of(Arguments.of(testText, "First word second word third word  six words"),
                         Arguments.of("First(1)word,second(2)word,third(3)word-six(6)words!",
                                      "First word second word third word six words"),
                         Arguments.of(" ", " "),
                         Arguments.of("", ""));
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForReplaceLetterByPositionInWordValidTest")
    void replaceLetterByPositionInWordValidTest(String testText, int replacementPosition, char replacement,
                                                String expectedResult) throws TextProcessingException {
        String processedTextRegex = TextProcessingRegexUtils.replaceLetterByPositionInWord(testText,
                                                                                           replacementPosition,
                                                                                           replacement);
        String processedTextString = TextProcessingStringUtils.replaceLetterByPositionInWord(testText,
                                                                                             replacementPosition,
                                                                                             replacement);
        char[] testTextArray = testText.toCharArray();
        TextProcessingCharArrayUtils.replaceLetterByPositionInWord(testTextArray, replacementPosition, replacement);
        String processedTextCharArray = new String(testTextArray);
        assertEquals(expectedResult, processedTextRegex);
        assertEquals(expectedResult, processedTextString);
        assertEquals(expectedResult, processedTextCharArray);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForReplaceLetterByPositionInWordAsStringInvalidTest")
    void replaceLetterByPositionInWordAsStringInvalidTest(String testText, int replacementPosition, char replacement,
                                                          Class<? extends Throwable> exceptionClass) {
        assertThrows(exceptionClass, () -> {
            TextProcessingRegexUtils.replaceLetterByPositionInWord(testText, replacementPosition, replacement);
        });
        assertThrows(exceptionClass, () -> {
            TextProcessingStringUtils.replaceLetterByPositionInWord(testText, replacementPosition, replacement);
        });
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForReplaceLetterByPositionInWordAsCharArrayInvalidTest")
    void replaceLetterByPositionInWordAsCharArray(char[] testTextArray, int replacementPosition, char replacement,
                                                  Class<? extends Throwable> exceptionClass) {
        assertThrows(exceptionClass, () -> {
            TextProcessingCharArrayUtils.replaceLetterByPositionInWord(testTextArray, replacementPosition, replacement);
        });
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForReplaceEachLetterAfterValidTest")
    void replaceEachLetterAfterValidTest(String testText, char charBefore, char charToReplace, char replacement,
                                         String expectedResult) throws TextProcessingException {
        String processedTextRegex = TextProcessingRegexUtils.replaceEachLetterAfter(testText, charBefore, charToReplace,
                                                                                    replacement);
        String processedTextString = TextProcessingStringUtils.replaceEachLetterAfter(testText, charBefore,
                                                                                      charToReplace, replacement);
        char[] testTextArray = testText.toCharArray();
        TextProcessingCharArrayUtils.replaceEachLetterAfter(testTextArray, charBefore, charToReplace, replacement);
        String processedTextCharArray = new String(testTextArray);
        assertEquals(expectedResult, processedTextRegex);
        assertEquals(expectedResult, processedTextString);
        assertEquals(expectedResult, processedTextCharArray);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void replaceEachLetterAfterInvalidTestTextAsString() {
        String testText = null;
        char charBefore = '0';
        char charToReplace = '0';
        char replacement = '0';
        assertThrows(TextProcessingException.class, () -> {
            TextProcessingRegexUtils.replaceEachLetterAfter(testText, charBefore, charToReplace, replacement);
        });
        assertThrows(TextProcessingException.class, () -> {
            TextProcessingStringUtils.replaceEachLetterAfter(testText, charBefore, charToReplace, replacement);
        });
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void replaceEachLetterAfterInvalidTestTextAsCharArray() {
        char[] testTextArray = null;
        char charBefore = '0';
        char charToReplace = '0';
        char replacement = '0';
        assertThrows(TextProcessingException.class, () -> {
            TextProcessingCharArrayUtils.replaceEachLetterAfter(testTextArray, charBefore, charToReplace, replacement);
        });
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForReplaceEachWordAsStringByPredicateValidTest")
    void replaceEachWordAsStringByPredicateValidTest(String testText, Predicate<String> predicate, String replacement,
                                                     String expectedResult) throws TextProcessingException {
        String processedTextRegex = TextProcessingRegexUtils.replaceEachWordByPredicate(testText, predicate,
                                                                                        replacement);
        String processedTextString = TextProcessingStringUtils.replaceEachWordByPredicate(testText, predicate,
                                                                                          replacement);
        assertEquals(expectedResult, processedTextRegex);
        assertEquals(expectedResult, processedTextString);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForRemoveEachWordAsStringByPredicateValidTest")
    void removeEachWordAsStringByPredicateValidTest(String testText, Predicate<String> predicate, String replacement,
                                                    String expectedResult) throws TextProcessingException {
        String processedTextRegex = TextProcessingRegexUtils.replaceEachWordByPredicate(testText, predicate,
                                                                                        replacement);
        String processedTextString = TextProcessingStringUtils.replaceEachWordByPredicate(testText, predicate,
                                                                                          replacement);
        assertEquals(expectedResult, processedTextRegex);
        assertEquals(expectedResult, processedTextString);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForReplaceEachWordAsCharArrayByPredicateValidTest")
    void replaceEachWordAsCharArrayByPredicateValidTest(String testText, Predicate<char[]> predicate,
                                                        char[] replacement,
                                                        String expectedResult) throws TextProcessingException {
        char[] testTextArray = testText.toCharArray();
        char[] processedTextArray = TextProcessingCharArrayUtils.replaceEachWordByPredicate(testTextArray, predicate,
                                                                                            replacement);
        String processedTextString = new String(processedTextArray);
        assertEquals(expectedResult, processedTextString);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForRemoveEachWordAsCharArrayByPredicateValidTest")
    void removeEachWordAsCharArrayByPredicateValidTest(String testText, Predicate<char[]> predicate, char[] replacement,
                                                       String expectedResult) throws TextProcessingException {
        char[] testTextArray = testText.toCharArray();
        char[] processedTextArray = TextProcessingCharArrayUtils.replaceEachWordByPredicate(testTextArray, predicate,
                                                                                            replacement);
        String processedTextString = new String(processedTextArray);
        assertEquals(expectedResult, processedTextString);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForReplaceEachWordAsStringByPredicateInvalidTest")
    void replaceEachWordAsStringByPredicateInvalidTest(String testText, Predicate<String> predicate, String replacement,
                                               Class<? extends Throwable> exceptionClass) {
        assertThrows(exceptionClass, () -> {
            TextProcessingRegexUtils.replaceEachWordByPredicate(testText, predicate, replacement);
        });
        assertThrows(exceptionClass, () -> {
            TextProcessingStringUtils.replaceEachWordByPredicate(testText, predicate, replacement);
        });
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForReplaceEachWordAsCharArrayByPredicateInvalidTest")
    void replaceEachWordByPredicateInvalidTest(char[] testText, Predicate<char[]> predicate, char[] replacement,
                                               Class<? extends Throwable> exceptionClass) {
        assertThrows(exceptionClass, () -> {
            TextProcessingCharArrayUtils.replaceEachWordByPredicate(testText, predicate, replacement);
        });
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForRemoveAllNonAlphabeticalExceptSpacesValidTest")
    void removeAllNonAlphabeticExceptSpacesValidTest(String testText, String expectedResult)
            throws TextProcessingException {
        String processedTextRegex = TextProcessingRegexUtils.removeAllNotAlphabeticExceptSpaces(testText);
        String processedTextString = TextProcessingStringUtils.removeAllNotAlphabeticExceptSpaces(testText);
        char[] testTextArray = testText.toCharArray();
        char[] processedTextArray = TextProcessingCharArrayUtils.removeAllNotAlphabeticExceptSpaces(testTextArray);
        String processedTextCharArray = new String(processedTextArray);
        assertEquals(expectedResult, processedTextRegex);
        assertEquals(expectedResult, processedTextString);
        assertEquals(expectedResult, processedTextCharArray);
    }

    @Test
    void removeAllNonAlphabeticExceptSpacesInvalidTest() {
        assertThrows(TextProcessingException.class, () -> {
            TextProcessingRegexUtils.removeAllNotAlphabeticExceptSpaces(null);
            TextProcessingStringUtils.removeAllNotAlphabeticExceptSpaces(null);
            TextProcessingCharArrayUtils.removeAllNotAlphabeticExceptSpaces(null);
        });
    }
}