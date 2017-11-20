package parser;

import java.util.Vector; /**
 * File: ParseTree.java
 *
 * This files contains the main tree structure of the Lisp program.
 *
 * @author commin
 * @create 2017/10/24
 */
public class ParseTree{
        private TreeNode root;

    public ParseTree(Vector<String> s) throws ParseException{
        root = TreeNode.create(s);
    }

    protected String evaluate() throws ParseException{
        String rtn = root.evaluate().toString();
        return rtn;
    }

    protected String print(){
        String result = root.toString();
        return result;
    }
}
