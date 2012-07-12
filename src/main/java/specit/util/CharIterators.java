package specit.util;

public final class CharIterators {

    private CharIterators() {
    }

    public static CharIterator createFrom(final CharSequence text) {
        if(text==null) {
            throw new IllegalArgumentException("Text cannot be null");
        }

        return new CharIterator() {
            private int pos = 0;

            @Override
            public int read() {
                if (pos < text.length()) {
                    return text.charAt(pos++);
                }
                return CharIterator.EOF;
            }
        };
    }
}
