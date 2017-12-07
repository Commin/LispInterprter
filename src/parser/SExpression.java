package parser;

import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Vector;

/**
 * File: SExpression.java
 *
 * @author commin
 * @create 2017/10/22
 */
public class SExpression extends TreeNode {
    protected TreeNode address;
    protected TreeNode data;
    protected Vector<String> dataTokens;
    protected Vector<String> addressTokens;

    public SExpression(Vector<String> s) throws ParseException{
        consConstructor(s);
        ParseLogger.getInstance().println(this.getClass().getName()+".SExpression(Vector)",this.toListString());
    }

    public SExpression(TreeNode t) throws ParseException{
        consConstructor(t.tokens);
    }

    /**
     * This function takes two TreeNodes and puts one int the address field
     * and other in the data field
     *
     * @param a The address-to-be TreeNode
     * @param d The data-to-be TreeNode
     */
    public SExpression(TreeNode a, TreeNode d){
        address = a;
        data = d;
        addressTokens = a.tokens;
        dataTokens = d.tokens;
        tokens = new Vector<>();
        tokens.add("(");
        tokens.addAll(a.tokens);
        tokens.add(".");
        tokens.addAll(d.tokens);
        tokens.add(")");
    }

    private void consConstructor(Vector<String> s) throws ParseException{
        if(s.size() >0 && s.get(0).matches(Patterns.START_PARENTHESIS)){
            int i = 1;
            int dataStart = 3;
                int open = 1;
            if(Patterns.START_PARENTHESIS.equals(s.get(i))){
                while (open > 0 && i < s.size()) {
                    i++;
                    if (Patterns.START_PARENTHESIS.equals(s.get(i))){
                        open ++;
                    } else if (Patterns.END_PARENTHESIS.equals(s.get(i))){
                        open --;
                    }
                }
                dataStart = i + 1;
            }
            i = dataStart >3 ? s.indexOf(Patterns.DOT, dataStart) : 2;
//            ParseLogger.getInstance().println(this.getClass().getName()+".consConstructor(Vecotr)",
//                    "this is addressTokens: "+s.subList(1,i).toString());
            addressTokens = new Vector<>(s.subList(1,i));
            address = TreeNode.create(addressTokens);
//            ParseLogger.getInstance().println(this.getClass().getName()+".consConstructor(Vecotr)",
//                    "this is dataTokens: "+s.subList(i+1,s.size() -1).toString());
            dataTokens = new Vector<>(s.subList(i+1,s.size() -1));
            data = TreeNode.create(dataTokens);
            tokens = new Vector<>();
            tokens.add("(");
            tokens.addAll(addressTokens);
            tokens.add(".");
            tokens.addAll(dataTokens);
            tokens.add(")");
        } else {
            throw new ParseException("Error! Invalid S-Expression: " + s.toString());
        }
    }

    @Override
    protected boolean isList() {
        return data.toString().matches("NIL") || data.isList();
    }

    @Override
    public String toString() {
        if( isList()){
            try {
                return toListString();
            } catch (ParseException e) {
                return "(" + address.toString() + " . " + data.toString() + ")";
            }
        } else {
            return "( " + address.toString() + " . " + data.toString() + " )";
        }
    }

    protected String toListString() throws ParseException{
        return "(" + StringHelpers.join(toVector(), " ") + ")";
    }

    private Vector <String> toVector() throws ParseException{
        if ( ! isList() ){
            throw new ParseException("Error!");
        }
        Vector <String> v = new Vector <String> ();
        SExpression tmp = this;
        while ( tmp.isList() ){
            v.add(tmp.address.toString());
            try {
                tmp = new SExpression(tmp.dataTokens);
            } catch (Exception e){
                break;
            }
        }
        return v;
    }

    @Override
    TreeNode evaluate() throws ParseException {
        return this.evaluate(false);
    }

    /**
     * This is the main evaluation function for an SExpression.
     *
     * @param numericFlag Whether or not to interpret numeric literally
     * @return The TreeNode representing the result
     * @throws ParseException If the function name, variable, etc. are undefined
     */
    @Override
    TreeNode evaluate(boolean numericFlag) throws ParseException {
        ParseLogger.getInstance().println(this.getClass().getName()+".evaluate(boolean)", toString());
        String a = address.evaluate().toString();
        SExpression params;
        TreeNode rtn;


        if(numericFlag && a.matches(Patterns.NUMERIC_ATOM)){
            ParseLogger.getInstance().println(this.getClass().getName()+".evaluate(boolean)","numberic");
            return address.evaluate();
        } else if (a.matches(Patterns.NIL)){
            return Primitives.NIL();
        } else if (a.matches(Patterns.T)){
            return Primitives.T();
        } else if (Environment.varIsDefined(a)){
            return Environment.getVarValue(a);
        } else if (Environment.functionIsDefined(a)){
            return Environment.executeFunction(a,TreeNode.create(dataTokens));
        } else if (a.matches(Patterns.DEFUN)){
            return Primitives.DEFUN((SExpression) data);
        } else if (a.matches(Patterns.CAR)|| a.matches(Patterns.CDR)) {
            SExpression s;
            if (data.isList()) {
                s = new SExpression(dataTokens);
                s = new SExpression(s.address.evaluate().tokens);
            } else {
                s = new SExpression(dataTokens);
            }
            params = s;
        }else {
            params = (SExpression) data;
        }


        try{
            rtn = invokePrimitive(a,params);
            return rtn;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    private TreeNode invokePrimitive(String name, SExpression obj) throws ParseException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        java.lang.reflect.Method method = Primitives.class.getDeclaredMethod(name, SExpression.class);
        method.setAccessible(true);
        Object o = method.invoke(null,obj);
        if(o.toString().matches(Patterns.TRUE)){
            return new Atom("T");
        } else if (o.toString().matches(Patterns.FALSE)){
            return new Atom("NIL");
        } else {
            return (TreeNode) o;
        }
    }

    @Override
    TreeNode evaluate(boolean numericFlag, Hashtable<String, TreeNode> env) throws ParseException {
        Hashtable <String, TreeNode> oldVars = Environment.getVarTable();
        Environment.mergeVars(env);
        TreeNode rtn = evaluate(numericFlag);
        Environment.setVars(oldVars);
        return rtn;
    }

    @Override
    TreeNode evaluate(Hashtable<String, TreeNode> env) throws ParseException {
        return evaluate(false, env);
    }



}


