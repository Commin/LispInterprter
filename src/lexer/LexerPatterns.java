package lexer;

/**
 * File: LexerPatterns.java
 *
 * @author commin
 * @create 2017/10/22
 *
 */
public class LexerPatterns {
    static final String LETTER = "[a-zA-Z]";

    static final String WHITESPACE = "[\\s]+";
    static final String WHITESPACE_NEWLINE = "[\\s]+|[\n]+";
    static final String NEWLINE = "[\n]+";
    static final String COMMENT = "[\\;].";
    static final String SYMBOL = "\\(+|\\)+";
    static final String INCLUDE_SYMBOL = ".(\\(+|\\)+).";
}