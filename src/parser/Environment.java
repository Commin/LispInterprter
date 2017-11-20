package parser;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * File: Environment.java
 *
 * It manages the bindings of functions and variables within
 * the context of the running Lisp program.
 *
 * @author commin
 * @create 2017/10/24
 */
public class Environment {
    public static Hashtable <String, UserFunction> funcs = new Hashtable <String, UserFunction>();
    public static Hashtable<String, TreeNode> vars = new Hashtable <String, TreeNode>();

    public static TreeNode executeFunction(String name, TreeNode params) throws ParseException{
        if( !funcs.containsKey(name)){
            throw new ParseException("Error! Undefined function: " + name);
        }

        UserFunction f = funcs.get(name);
        return f.evaluate(params);
    }

    public static void registerFunction(String name, TreeNode params, TreeNode body)
            throws ParseException {
        UserFunction f = new UserFunction(name, params, body);
        funcs.put(name, f);
    }

    public static boolean functionIsDefined(String name){
        return funcs.containsKey(name);
    }

    public static void print(){
        System.out.println(vars.toString());
    }

    public static String stringify(){
        return vars.toString();
    }

    public static boolean varIsDefined(String name){
        return vars.containsKey(name);
    }

    public static void unbind(String name){
        vars.remove(name);
    }

    public static void unbindAll(Hashtable <String, TreeNode> tbl){
        Iterator it = tbl.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            if ( vars.get(pairs.getKey()) == pairs.getValue() ){
                vars.remove(pairs.getKey());
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public static TreeNode getVarValue(String name) throws ParseException{
        if ( vars.containsKey(name) ){
            return vars.get(name);
        } else {
            throw new ParseException("Error! No such variable.");
        }
    }

    public static Hashtable <String, TreeNode> getVarTable(){
        return new Hashtable <String, TreeNode> (vars);
    }
    /**
     * This substitutes in new variable bindings to the current environment
     *
     * @param newVars A Hashtable of the new variable bindings
     */
    public static void mergeVars(Hashtable <String, TreeNode> newVars){
        Iterator it = newVars.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            if ( vars.contains(pairs.getKey()) ){
                // Do not let it store multiple things in one bucket
                vars.remove(pairs.getKey());
            }
            vars.put( (String) pairs.getKey(), (TreeNode) pairs.getValue() );
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public static void setVars(Hashtable <String, TreeNode> v){
        vars = new Hashtable <String, TreeNode> (v);
    }


}
