package specit.invocation;

/**
 *
 *
 */
public interface Converter {

    /**
     * Indicates whether this converter can converter a string to the suitable type.
     *
     * @param requiredType
     * @return
     */
    boolean canConvertTo(Class<?> requiredType);

    /**
     * Convert the <code>code</code> into a suitable type.
     *
     * @param requiredType
     * @param value
     * @return
     */
    Object fromString(Class<?> requiredType, String value);

    /**
     * Hints method that returns completion suggestions for the given input.
     * <p/>
     * If the input is an acceptable input "as is" it should be also part of the
     * suggestions. If the input is invalid and cannot be parsed "as is" it must not
     * be returned in the suggestions.
     * Thus if the suggestions returns is empty it also means the input is invalid.
     *
     * @param input
     * @return
     */
    String[] suggest(String input);
}
