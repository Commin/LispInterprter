package parser;

import java.util.Arrays;
import java.util.Vector;

/**
 * File: Parser.java
 *
 * This is the main Parser class for the program.
 * It handles the tokens from the lexical analysis,
 * converts them to dot-notation,
 * and provides access to program evaluation
 *
 * @author commin
 * @create 2017/10/24
 */
public class Parser {

    Vector <ParseTree> statements = new Vector<ParseTree>();

    /**
     *
     * This function initializes the parser for the lexical tokens,
     * splitting them into statements, and storing them.
     *
     * @param tokens The vector of lexical tokens from analysis
     * @throws ParseException If conversion to dot-notation fails
     */
    public Parser(Vector<String> tokens) throws ParseException{
        Vector <String> t;
        int i = 0;
        int indexBegin, indexEnd;

        while ( i < tokens.size() && i >= 0 ){
            // The variable l must always hold the last location of the current statement!!!
            indexBegin = tokens.indexOf("(", i);
            if ( indexBegin == i ){
                // There is a new statement to add - starting at i
                indexEnd = endOfExpression(new Vector <String> (tokens.subList(i, tokens.size()))) + i;
            } else if ( indexBegin > i ){
                // The next statement is after some non-parenthetical stuff
                indexEnd = indexBegin - 1;
            } else {
                // There are no more parenthetical statements. Parse the rest of the program
                indexEnd = tokens.size() - 1;
            }

            Vector<String> tmp = new Vector<>(tokens.subList(i, indexEnd+1));
            ParseLogger.getInstance().println(getClass().getName()+".Parser()",
                        i +" " + indexBegin + " " + indexEnd);

//            for (String str: tmp) {
//                System.out.print(str + " ~ ");
//            }
//            System.out.println();

            t = convertToDotNotation(new Vector <String> (tokens.subList(i, indexEnd+1)));
            ParseLogger.getInstance().println(getClass().getName()+".Parser()",i+"次: "+statements.toString());
            statements.add(new ParseTree(t));
            i = indexEnd + 1;
        }



        ParseLogger.getInstance().println(getClass().getName()+".Parser()","over--------------");
        ParseLogger.getInstance().println(getClass().getName()+".Parser()","statements size: "+statements.size());
    }

    public void evaluate() throws ParseException{
        for (int i = 0; i < statements.size(); i++) {
            ParseLogger.getInstance().println(getClass().getName()+".evaluate()",statements.get(i).evaluate());
        }
    }

    private Vector<String> convertToDotNotation(Vector<String> s) {
        Vector<String> r = new Vector<String>();
        Vector<String> tmp;
        int nextInnerToken;
        if (s.get(0).matches(Patterns.START_PARENTHESIS)) {
            // We have a list or S-Expression

            int closeParen = endOfExpression(s);
            if (closeParen > 1) {
                // The expression is not ()

                r.add("(");
                if (closeParen > 2) {
                    // There is more than one token - so not ( a )

                    // Is the first one a nested expression?
                    if (s.get(1).matches(Patterns.START_PARENTHESIS)) {
                        nextInnerToken = endOfExpression(new Vector<String>(s.subList(1, closeParen))) + 2;
                    } else {
                        nextInnerToken = 2;
                    }

                    if (!s.get(nextInnerToken).matches(Patterns.DOT)) {
                        // The expression must be a list because it is not in dot-notation
                        r.addAll(convertToDotNotation(new Vector <>(s.subList(1,nextInnerToken))));
                        r.add(".");
                        tmp = new Vector <>();
                        tmp.add("(");
                        tmp.addAll(s.subList(nextInnerToken, closeParen));
                        tmp.add(")");
                        r.addAll(convertToDotNotation(tmp));
                    } else {
                        // Since it is in the form of ( [stuff] . [stuff] ), we pass [stuff] to be converted
                        r.addAll(convertToDotNotation(new Vector <>(s.subList(1,nextInnerToken))));
                        r.add(".");
                        r.addAll(convertToDotNotation(new Vector <>(s.subList(nextInnerToken+1, closeParen))));
                    }
                } else {
                    // The statement is in the form ( a )
                    r.add(s.get(1));
                    r.add(".");
                    r.add("NIL");
                }
                r.add(")");
            } else {
                // We have ()
                r.add("NIL");
            }
        } else {
            if (s.indexOf(Patterns.START_PARENTHESIS) > 0) {
                r.addAll(s.subList(0, s.indexOf("(")));
                r.addAll(convertToDotNotation(new Vector<>
                        (s.subList(s.indexOf("("), s.size()))));
            } else {
                r = s;
            }
        }
//        ParseLogger.getInstance().println(getClass().getName()+".convertToDotNotation()",r.toString());
        return r;
    }

    /**
     * This function finds the closing parenthesis of a given Lisp segment
     *
     *
     * @param s The tokens (in dot-notation) of a segment of Lisp code
     * @return the index of the closing parenthesis
     * @throws IllegalArgumentException If the vector is not a parenthetical expression
     * @throws ArrayIndexOutOfBoundsException If the statement does not have a closing parenthesis
     */
    private int endOfExpression(Vector<String> s) throws IllegalArgumentException, ArrayIndexOutOfBoundsException{
        if(!s.get(0).matches(Patterns.START_PARENTHESIS)){
            throw new IllegalArgumentException("ERROR: Cannot find the expression begin with '('.");
        }
        int openPairs = 1;
        int end = 1;
        while ( openPairs > 0){
            if (end >= s.size()){
                throw new ArrayIndexOutOfBoundsException("ERROR: Unbalanced parentheses.");
            }

            if ( s.get(end).matches(Patterns.END_PARENTHESIS)){
                openPairs--;
            } else if (s.get(end).matches(Patterns.START_PARENTHESIS)){
                openPairs++;
            }

            if( openPairs > 0){
                end++;
            }
        }
        return end;
    }
}
