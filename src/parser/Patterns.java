package parser;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * File: Patterns.java
 *
 * @author commin
 * @create 2017/12/12
 */
public class Patterns {

    public static final String NIL = "nil";
    public static final String T = "#t";
    public static final String[] QUOTE = {"quote","'"};
    public static final String DEFUN = "defun";
    public static final String LIST = "list";
    public static final String LAMBDA = "lambda";
    public static final String ATOM = "atom";
    public static final String COND = "cond";
    public static final String[] EQ = {"eq","=="};
    public static final String[] GTR = {"gtr",">"};
    public static final String[] GEQ = {"geq",">="};
    public static final String[] LSS = {"lss","<"};
    public static final String[] LEQ = {"leq","<="};

    public static final String CONS = "cons";
    public static final String CAR = "car";
    public static final String CDR = "cdr";

    public static String [] arithmeticArray = {"+", "-", "*", "/", "%"};
}
