package parser;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

/**
 * File: UserFunction.java
 *
 * @author commin
 * @create 2017/10/24
 */
public class UserFunction {

    protected String name;
    protected Vector<String> formals;
    protected TreeNode body;

    public UserFunction(String n, TreeNode function, TreeNode b) throws ParseException{
        name = n;
        boolean matchedFunctionIsNil = function.toString().matches("NIL");
        boolean matchedBodyIsNil = function.toString().matches("NIL");

        if ( (!function.isList() && !matchedFunctionIsNil ) || ( !b.isList() && !matchedBodyIsNil )){
            throw new ParseException("Invalid function parameters or body.\n"
                    + function.toString() + "\n" + b.toString());
        }

        String formalString = function.toString();
        formals = splitParamList(formalString);
        body = b;
    }

    private Vector<String> splitParamList(String formalString) {
        //TODO
        return null;
    }

    protected TreeNode evaluate(TreeNode s) throws ParseException{
        Hashtable<String, TreeNode> bindings = bind(s);
        return body.evaluate(true, bindings);
    }

    private Hashtable<String, TreeNode> bind(TreeNode s) throws ParseException {
        if ( !s.isList() && !s.toString().matches("NIL") ){
            throw new ParseException("Error! Invalid parameters to function: " + name);
        }

        Hashtable <String, TreeNode> env = new Hashtable <String, TreeNode> ();

        if ( !s.isList() ){
            return env;
        }

        SExpression tmp = new SExpression(s);
        int i;
        for ( i = 0; i < formals.size(); i++ ){
            env.put(formals.get(i), tmp.address.evaluate());
            try{
                tmp = new SExpression(tmp.dataTokens);
            } catch (Exception e){
                break;
            }
        }

        if ( i < formals.size() - 1 ){
            throw new ParseException("Error! Too few arguments for: " + name);
        } else if ( ! tmp.data.evaluate().toString().matches("NIL") ){
            throw new ParseException("Error! Too many arguments for: " + name);
        }

        return env;
    }
}
