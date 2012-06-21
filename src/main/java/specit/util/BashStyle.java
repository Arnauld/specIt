package specit.util;

/**
 *
 *
 */
public enum BashStyle {
    Bold(1, 22),
    Italic(3, 23),
    Underline(4, 24),
    Black(30, 39),
    Red(31, 39),
    RedHi(91, 39),
    Green(32, 39),
    Yellow(33, 39),
    Blue(34, 39),
    Magenta(35, 39),
    Cyan(36, 39),
    White(37, 39),
    Grey(90, 39),
    GreenHi(92, 32),
    CyanHi(96, 39);

    private final int beg;
    private final int end;

    private BashStyle(int beg, int end) {
        this.beg = beg;
        this.end = end;
    }

    /**
     * Format text with the specified style
     */
    public String stylize(String content) {
        return open() + content + close();
    }

    public String close() {
        return "\u001B[" + end + "m";
    }

    public String open() {
        return "\u001B[" + beg + "m";
    }
}
