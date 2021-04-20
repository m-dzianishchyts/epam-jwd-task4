package by.epamtc.text.util.processors;

import java.util.function.Predicate;

public class WordPredicateFactory {

    public static Predicate<String> specifyWordLength(int wordLength) {
        Predicate<String> predicate = word -> word.length() == wordLength;
        return predicate;
    }

    public static Predicate<String> specifyPrefix(char prefix) {
        Predicate<String> predicate = word -> word.startsWith(String.valueOf(prefix));
        return predicate;
    }
}
