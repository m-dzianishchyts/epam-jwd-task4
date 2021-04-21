package by.epamtc.text.util.processors;

import java.util.Set;
import java.util.function.Predicate;

public class WordPredicate {

    public static final Predicate<String> VOWEL_PREFIX_PREDICATE_STRING_IMPLEMENTATION = word -> {
        if (word == null || word.isEmpty()) {
            return false;
        }
        return isVowel(word.charAt(0));
    };
    public static final Predicate<String> CONSONANT_PREFIX_PREDICATE_STRING_IMPLEMENTATION = word -> {
        if (word == null || word.isEmpty()) {
            return false;
        }
        return isConsonant(word.charAt(0));
    };
    public static final Predicate<char[]> VOWEL_PREFIX_PREDICATE_CHAR_ARRAY_IMPLEMENTATION = word -> {
        if (word == null || word.length == 0) {
            return false;
        }
        return isVowel(word[0]);
    };
    public static final Predicate<char[]> CONSONANT_PREFIX_PREDICATE_CHAR_ARRAY_IMPLEMENTATION = word -> {
        if (word == null || word.length == 0) {
            return false;
        }
        return isConsonant(word[0]);
    };

    private static final Set<Character> ENGLISH_VOWELS = Set.of('a', 'e', 'i', 'o', 'u');
    private static final Set<Character> RUSSIAN_VOWELS = Set.of('а', 'е', 'ё', 'и', 'о', 'у', 'ы', 'э', 'ю', 'я');
    private static final Set<Character> ENGLISH_CONSONANTS = Set.of('b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm',
                                                                    'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y',
                                                                    'z');
    private static final Set<Character> RUSSIAN_CONSONANTS = Set.of('б', 'в', 'г', 'д', 'ж', 'з', 'й', 'к', 'л', 'м',
                                                                    'н', 'п', 'р', 'с', 'т', 'ф', 'х', 'ц', 'ч', 'ш',
                                                                    'щ', 'ъ', 'ь');

    public static Predicate<String> specifyWordAsStringLength(int wordLength) {
        Predicate<String> predicate = word -> word.length() == wordLength;
        return predicate;
    }

    public static Predicate<char[]> specifyWordAsCharArrayLength(int wordLength) {
        Predicate<char[]> predicate = word -> word.length == wordLength;
        return predicate;
    }

    private static boolean isConsonant(char letter) {
        char lowerCaseLetter = Character.toLowerCase(letter);
        return ENGLISH_CONSONANTS.contains(lowerCaseLetter) || RUSSIAN_CONSONANTS.contains(lowerCaseLetter);
    }

    private static boolean isVowel(char letter) {
        char lowerCaseLetter = Character.toLowerCase(letter);
        return ENGLISH_VOWELS.contains(lowerCaseLetter) || RUSSIAN_VOWELS.contains(lowerCaseLetter);
    }
}
