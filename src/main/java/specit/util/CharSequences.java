package specit.util;

import java.util.regex.Pattern;

public final class CharSequences {

    private CharSequences() {
    }

    private static final Pattern ENDS_WITH_BLANKLINES = Pattern.compile(".*(?:\n|\r|\r\n)+\\s*(?:\n|\r|\r\n)");

    public static boolean endsWithBlankLine(CharSequence sequence) {
        return ENDS_WITH_BLANKLINES.matcher(sequence).matches();
    }

    public static boolean startsWith(CharSequence sequence, String prefix) {
        return startsWith(sequence, 0, prefix);
    }

    public static boolean startsWith(CharSequence sequence, int offset, String prefix) {
        if (sequence.length() - offset >= prefix.length()) {
            for (int i = 0, n = prefix.length(); i < n; i++) {
                if (sequence.charAt(offset + i) != prefix.charAt(i)) {
                    return false;
                }
            }
            // still there!
            return true;
        }
        return false;
    }

    public static boolean startsWithIgnoringChars(CharSequence sequence, String prefix, String ignoredChars) {
        int offset = 0;
        offsetLoop:
        for (; offset < sequence.length(); offset++) {
            char c = sequence.charAt(offset);
            for (int k = 0; k < ignoredChars.length(); k++) {
                if (ignoredChars.charAt(k) == c) {
                    continue offsetLoop;
                }
            }
            break;
        }
        return startsWith(sequence, offset, prefix);
    }
}
