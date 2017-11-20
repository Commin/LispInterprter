package parser;

import java.util.Vector;
import java.util.Hashtable;

/**
 * File: TreeNode.java
 *
 * This is the central data structure for representing Atoms and S-Expression.
 *
 * @author commin
 * @create 2017/10/22
 */
abstract class TreeNode {
    /**
     * This function determines if the TreeNode is part of a list
     *
     * @return True or False depending on if it is a list
     */
    protected abstract boolean isList();
    protected Vector<String> tokens = new Vector<>();

    /**
     * The evaluation of TreeNodes returns a new TreeNode.
     *
     * @return The result of evaluating the TreeNode
     * @throws ParseException
     */
    abstract TreeNode evaluate() throws ParseException;

    /**
     * The evaluation of TreeNodes returns a new TreeNode.
     *
     * @param numericFlag Whether or not to take numeric literally
     * @return The result of evaluating the TreeNode
     * @throws ParseException
     */
    abstract TreeNode evaluate(boolean numericFlag) throws ParseException;

    /**
     * The evaluation of TreeNodes returns a new TreeNode.
     *
     * @param numericFlag Whether or not to take numeric literally
     * @param env The scoped variables
     * @return The result of evaluating the TreeNode
     * @throws ParseException
     */
    abstract TreeNode evaluate(boolean numericFlag, Hashtable<String, TreeNode> env) throws ParseException;

    /**
     * The evaluation of TreeNodes returns a new TreeNode.
     *
     * @param env The scoped variables
     * @return The result of evaluating the TreeNode
     * @throws ParseException
     */
    abstract TreeNode evaluate(Hashtable <String, TreeNode> env) throws ParseException;

    static TreeNode create(Vector<String> s)throws ParseException{
        if (s.size()>0) {
            if (s.get(0).matches(Patterns.START_PARENTHESIS)) {
                return new SExpression(s);
            } else {
                return new Atom(s.get(0));
            }
        } else {
            throw new ParseException("Tried to create a TreeNode with no data");
        }
    }

    static TreeNode create(boolean b){
        return new Atom(b);
    }

    static TreeNode create(int i){
        return new Atom(i);
    }
}
