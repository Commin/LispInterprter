package parser;

import lexer.Lexer;

import java.io.IOException;
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
        ParseLogger.getInstance().println(this.getClass().getName()+".ParseTree(Vector)",root.toString());
        ParseLogger.getInstance().println(this.getClass().getName()+"------------------",root.toString());

    }

    protected String evaluate() throws ParseException{
        String rtn = root.evaluate().toString();
        ParseLogger.getInstance().println(this.getClass().getName()+".evaluate()",rtn);
        return rtn;
    }

    @Override
    public String toString() {
        return root.toString();
    }

    public static void main(String[] args) {
        try {
            Lexer l = new Lexer("( (+ . 4) . (5 . 6) )");
            Vector<String> test = l.getTokens();

            ParseTree num = new ParseTree(test);
            System.out.println(num.toString());
            System.out.println(num.evaluate());
        } catch (IOException e) {
        e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
