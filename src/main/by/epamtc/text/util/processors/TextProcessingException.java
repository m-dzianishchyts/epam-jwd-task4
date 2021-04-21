package by.epamtc.text.util.processors;

public class TextProcessingException extends Exception {

    public TextProcessingException() {
    }

    public TextProcessingException(String message) {
        super(message);
    }

    public TextProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public TextProcessingException(Throwable cause) {
        super(cause);
    }
}
