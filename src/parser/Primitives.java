package parser;

/**
 * File: Primitives.java
 *
 * @author commin
 * @create 2017/10/24
 */
public class Primitives {

    // 	PLUS,
    // 	MINUS,
    // 	T,
    // 	NIL,
    // 	CONS,
    // 	CAR,
    // 	CDR,
    // 	ATOM,
    // 	EQ,
    //  NEQ,
    // 	NULL,
    // 	INT,
    // 	DIVIDE,
    // 	TIMES,
    // 	REMAINDER,
    // 	LSS,
    //  LEQ,
    // 	GTR,
    //  GEQ,
    // 	COND,
    // 	QUOTE,
    // 	DEFUN

    public static TreeNode T(){
        return new Atom(true);
    }

    public static TreeNode NIL(){
        return new Atom(false);
    }

    public static SExpression CONS( SExpression s) throws ParseException{
        SExpression tmp = new SExpression(s.dataTokens);
        return new SExpression(s.address.evaluate(), tmp.address.evaluate());
    }

    public static TreeNode CAR (SExpression s) throws ParseException{
        return s.address;
    }

    public static TreeNode CDR (SExpression s) throws ParseException{
        return s.data;
    }

    /**
     *
     * Determines if an S-Expression is semantically an Atom
     *
     * @param s The S-Expression in question
     * @return True if it is an atom literal. False otherwise.
     * @throws ParseException
     */
    public static TreeNode ATOM (SExpression s) throws ParseException{
        return TreeNode.create(s.address.evaluate().toString().
                matches(Patterns.LITERAL));
    }

    public static TreeNode NULL (SExpression s) throws ParseException{
        return TreeNode.create(s.data.evaluate().toString().
                matches("NIL"));
    }

    public static TreeNode INT (SExpression s) throws ParseException{
        return TreeNode.create(s.address.evaluate(true).toString().
                matches(Patterns.NUMERIC_ATOM));
    }

    public static TreeNode PLUS (SExpression s) throws ParseException{
        return TreeNode.create(Integer.parseInt(s.address.evaluate(true).toString())
                + Integer.parseInt(s.data.evaluate(true).toString()));
    }

    public static TreeNode MINUS (SExpression s) throws ParseException{
        return TreeNode.create(Integer.parseInt(s.address.evaluate(true).toString())
                - Integer.parseInt(s.data.evaluate(true).toString()));
    }

    public static TreeNode TIMES (SExpression s) throws ParseException{
        return TreeNode.create(Integer.parseInt(s.address.evaluate(true).toString())
                * Integer.parseInt(s.data.evaluate(true).toString()));
    }

    public static TreeNode DIVIDE (SExpression s) throws ParseException{
        return TreeNode.create(Integer.parseInt(s.address.evaluate(true).toString())
                / Integer.parseInt(s.data.evaluate(true).toString()));
    }

    public static TreeNode REMAINDER (SExpression s) throws ParseException{
        return TreeNode.create(Integer.parseInt(s.address.evaluate(true).toString())
                % Integer.parseInt(s.data.evaluate(true).toString()));
    }


    /**
     *
     * @param s The S-Expression describing the conditions
     * @return The result of evaluating the expression in the list with the first boolean component
     * @throws ParseException
     */
    public static TreeNode COND (SExpression s) throws ParseException{
        SExpression sAddr = new SExpression(s.addressTokens);
        if(sAddr.address.evaluate().toString().matches("T")){
            SExpression tmp = new SExpression(sAddr.dataTokens);
            return tmp.address.evaluate(true);
        }else {
            SExpression b = new SExpression(s.dataTokens);
            return COND(b);
        }
    }

    public static TreeNode EQ (SExpression s) throws ParseException{
        return TreeNode.create(s.address.evaluate().toString().
                matches(s.data.evaluate(true).toString()));
    }

    public static TreeNode NEQ (SExpression s) throws ParseException{
        return TreeNode.create(!s.address.evaluate().toString().
                matches(s.data.evaluate(true).toString()));
    }

    public static TreeNode LSS (SExpression s) throws ParseException{
        return TreeNode.create(Integer.parseInt(s.address.evaluate(true).toString())
                < Integer.parseInt(s.data.evaluate(true).toString()));
    }

    public static TreeNode LEQ (SExpression s) throws ParseException{
        return TreeNode.create(Integer.parseInt(s.address.evaluate(true).toString())
                <= Integer.parseInt(s.data.evaluate(true).toString()));
    }

    public static TreeNode GTR (SExpression s) throws ParseException{
        return TreeNode.create(Integer.parseInt(s.address.evaluate(true).toString())
                > Integer.parseInt(s.data.evaluate(true).toString()));
    }

    public static TreeNode GEQ (SExpression s) throws ParseException{
        return TreeNode.create(Integer.parseInt(s.address.evaluate(true).toString())
                >= Integer.parseInt(s.data.evaluate(true).toString()));
    }

    public static TreeNode QUOTE (SExpression s) throws ParseException{
        return s.address;
    }

    public static TreeNode DEFUN(SExpression s) throws ParseException{
        String name = s.address.toString();

        if ( ! name.matches(Patterns.FUNCTION_NAME_VALID) ){
            throw new ParseException("Error! Function names must be character literals only");
        }

        if ( Primitives.primitiveExists(name) ){
            throw new ParseException("Error! Cannot override a primitive function.");
        }

        SExpression sData = new SExpression(s.dataTokens);
        TreeNode params = TreeNode.create(sData.addressTokens);
        SExpression sDataData = new SExpression(sData.dataTokens);
        TreeNode body = TreeNode.create(sDataData.addressTokens);

        Environment.registerFunction(name, params, body);

        return new Atom(name);
    }

    private static boolean primitiveExists(String name){
        java.lang.reflect.Method method;
        try {
            method = Primitives.class.getDeclaredMethod(name, SExpression.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }


}
