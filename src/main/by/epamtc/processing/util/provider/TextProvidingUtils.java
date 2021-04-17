package by.epamtc.processing.util.provider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public final class TextProvidingUtils {

    private TextProvidingUtils() {
    }

    public static String readTextFromConsole() {
        String text = readTextFromStream(System.in);
        return text;
    }

    private static String readTextFromStream(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input stream cannot be null.");
        }
        StringBuilder builder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            builder.append(nextLine).append(System.lineSeparator());
        }
        return builder.toString();
    }

    public static String readTextFromFile(File file) throws FileException {
        if (file == null) {
            throw new FileException("File cannot be null.");
        }
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            String text = readTextFromStream(inputStream);
            return text;
        } catch (FileNotFoundException e) {
            throw new FileException(e);
        } catch (SecurityException e) {
            throw new FileException("Read access is denied.", e);
        } catch (IOException e) {
            throw new FileException("An error occurred while reading file.", e);
        }
    }
}
