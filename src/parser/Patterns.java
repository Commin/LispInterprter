package parser;

/**
 * File: Patterns.java
 *
 * @author commin
 * @create 2017/10/22
 */
public class Patterns {
    static final String LETTER = "[a-zA-Z]";
    static final String LITERAL = "[a-zA-Z0-9]+";
    static final String FUNCTION_NAME_VALID = "[A-Z_a-z][A-Z_a-z0-9]*";
    static final String WHITESPACE = "[\\s]+";
    static final String NEWLINE = "\n";
    static final String NUMERIC_ATOM = "[\\d\\+\\-]?[\\d]*";
    static final String SYMBOL = "\\(|\\)";
    static final String START_PARENTHESIS = "[(]";
    static final String END_PARENTHESIS = "[)]";
    static final String DOT = "[.]";
}
