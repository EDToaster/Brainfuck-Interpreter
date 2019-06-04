package ca.edtoaster.utils;

public class SourceUtils {
    public static final String INVALID_CHARACTERS_REGEX = "[^><+\\-.,\\[\\]]";
    public static final String INVALID_REPLACEMENT = "";

    public static String stripNonConformingCharacters(String input) {
        return input.replaceAll(INVALID_CHARACTERS_REGEX, INVALID_REPLACEMENT);
    }

}
