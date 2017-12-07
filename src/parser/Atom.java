package parser;

import java.util.Vector;
import java.util.Hashtable;

/**
 * File: Atom.java
 *
 * @author commin
 * @create 2017/10/22
 */
public class Atom extends TreeNode {
    private String literalString;

    public Atom(String s) throws ParseException {
        //TODO need to test the condition function
        s = Patterns.getInstance().replaceOperator(s);
        if ( ! s.matches(Patterns.LITERAL) && ! s.matches(Patterns.NUMERIC_ATOM) ){
            throw new ParseException("Can't recognize the atom identifier.");
        }
        literalString = s;
        tokens = new Vector<>();
        tokens.add(literalString);
//       ParseLogger.getInstance().println(this.getClass().getName()+".Atom(String)",tokens.get(0));
    }

    public Atom(boolean b){
        literalString = b ? "T" : "NIL";
        tokens = new Vector<> ();
        tokens.add(literalString);
    }

    public Atom(int i){
        literalString = Integer.toString(i);
        tokens = new Vector<> ();
        tokens.add(literalString);
    }

    @Override
    protected boolean isList() {
        return false;
    }

    @Override
    public String toString() {
        if( literalString.matches(Patterns.NUMERIC_ATOM)){
            return literalString.replaceAll("\\A\\+","");
        } else {
            return literalString;
        }
    }

    @Override
    TreeNode evaluate() throws ParseException {
        return null;
    }

    @Override
    TreeNode evaluate(boolean numericFlag) throws ParseException {
        return evaluate();
    }

    @Override
    TreeNode evaluate(boolean numericFlag, Hashtable<String, TreeNode> env) throws ParseException {
        return evaluate();
    }

    @Override
    TreeNode evaluate(Hashtable<String, TreeNode> env) throws ParseException {
        return evaluate();
    }
}
