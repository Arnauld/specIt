package specit.util;

/**
 *
 *
 */
public class Java {
    private Java() {
    }

    public static String filterInvalidMethodNameCharacters(String content) {
        char filteredName[] = new char[content.length()];
        int index = 0;
        for (int i = 0; i < content.length(); i++) {
            char ch = content.charAt(i);
            if (Character.isJavaIdentifierPart(ch) && ch != '$' && ch != 127) {
                filteredName[index++] = ch;
            }
        }
        return new String(filteredName, 0, index);
    }
}
