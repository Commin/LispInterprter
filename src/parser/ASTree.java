package parser;

import java.util.Vector;
import java.util.stream.Collectors;

/**
 * File: ASTree.java
 *
 * @author commin
 * @create 2017/12/11
 */
public class ASTree {

    private static final int ATOM = 1;
    private static final int SEXPR = 2;
    private static final int LMD = 3;
    private static final int INT = 4;

    ASTree parent;
    private int type;
    Vector<ASTree> lst = new Vector<>();
    String atom;
    int ival;
    // when lambda
    Closure closure;

    public ASTree(String atom){
        super();
        if (atom.equals(Patterns.NIL)) {
            setType(SEXPR);
        } else if (Character.isDigit(atom.charAt(0))) {
            this.ival = Integer.parseInt(atom);
            setType(INT);
        } else if (atom.startsWith("'") && atom.length() >= 2) {
            // 'a is short for (' a)
            lst.add(new ASTree(Patterns.QUOTE[0]));
            lst.add(new ASTree(atom.substring(1)));
            setType(SEXPR);
        } else {
            this.atom = atom;
            setType(ATOM);
        }
        System.out.println("ASTree: " + getType() + " " + toString());
    }

    public ASTree() {
        super();
        this.parent = this;
        setType(SEXPR);
        System.out.println("ASTree: " + getType() + " " + toString());
    }

    public ASTree(Vector<ASTree> lst) {
        this.lst = lst;
        this.parent = this;
        setType(SEXPR);
        System.out.println("ASTree: " + getType() + " " + toString());
    }

    public ASTree(ASTree parent) {
        super();
        this.parent = parent;
        setType(SEXPR);
        System.out.println("ASTree: " + getType() + " " + toString());
    }

    public ASTree(Closure closure) {
        // lambda
        this.closure = closure;
        setType(LMD);
        System.out.println("ASTree: " + getTail() + " " + toString());
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        if (type == ATOM){
            return atom;
        }
        else if (type == SEXPR && this.len() == 0 ){
            return "nil";
        }
        else if(type == INT){
            return String.valueOf(ival);
        }
        else if(type == SEXPR){
            return "( " + String.join(" ", lst.stream().map(
                    e -> e.toString()).collect(Collectors.toList())) + " )";
        }
        else if(type == LMD){
            return closure.toString();
        }
        else {
            return "";
        }
    }

    public String toSimpleString(){
        if(type == SEXPR){
            return "( " + String.join(" ", lst.stream().map(
                    e -> e.toSimpleString()).collect(Collectors.toList())) + " )";
        }
        else if(type == LMD){
            return closure.toSimpleString();
        }
        else {
            return toString();
        }
    }

    public void begin() {}

    public void add(ASTree e) {
        e.parent = this;
        lst.add(e);
    }

    public boolean end() {
        return this == this.parent;
    }

    public int len() {
        return lst.size();
    }

    public boolean isDef() {
        if (this.hasHead() && this.getHead().isAtom() && this.getHead().equals(Patterns.DEFUN)) {
            return true;
        }
        return false;
    }

    public boolean isLambda() {
        return type == LMD;
    }

    public int getType() {
        return type;
    }

    boolean isList() {
        return type == SEXPR;
    }

    boolean isAtom() {
        return type == ATOM;
    }

    boolean isInt() {
        return type == INT;
    }

    boolean hasHead() {
        return this.len() > 0;
    }

    public boolean isRoot() {
        return this == this.parent;
    }

    private boolean isTrue() {
        if (type == ATOM) {
            return this.equals(Patterns.T);
        }
        else {
            return this.len() != 0;
        }
    }

    boolean isNil() {
        return this.type == SEXPR && this.len() == 0;
    }


    public boolean equals(String str) {
        return this.atom.equals(str);
    }
    public boolean equals(ASTree atom) {
        return this.atom.equals(atom.atom);
    }

    public ASTree getHead() {
        return this.get(0);
    }

    public ASTree getTail() {
        Vector<ASTree> lst = new Vector<>(this.lst);
        lst.remove(0);
        return new ASTree(lst);
    }

    public ASTree get(int i) {
        return this.lst.get(i);
    }

    static ASTree NewBool(boolean b) {
        return b ? new ASTree("#t") : new ASTree();
    }

    static ASTree cond(ASTree tree, Env env) throws SyntaxException, RunTimeException {
        while (tree.len() > 0) {
            ASTree entry = tree.getHead();
            ASTree cond = entry.get(0);
            ASTree cond_res = Lisp.eval(cond, env);
            if (cond_res.isTrue()) {
                return Lisp.eval(entry.get(1), env);
            }
            tree = tree.getTail();
        }
        return new ASTree();
    }

    public static ASTree add(ASTree v1, ASTree v2) {
        int i = v1.ival + v2.ival;
        return new ASTree(String.valueOf(i));
    }
    public static ASTree mul(ASTree v1, ASTree v2) {
        int i = v1.ival * v2.ival;
        return new ASTree(String.valueOf(i));
    }
    public static ASTree sub(ASTree v1, ASTree v2) {
        int i = v1.ival - v2.ival;
        return new ASTree(String.valueOf(i));
    }
    public static ASTree div(ASTree v1, ASTree v2) {
        int i = v1.ival / v2.ival;
        //TODO expection
        return new ASTree(String.valueOf(i));
    }
    public static ASTree remainder(ASTree v1, ASTree v2) {
        int i = v1.ival % v2.ival;
        //TODO expection
        return new ASTree(String.valueOf(i));
    }

    public static ASTree cons(ASTree t1, ASTree t2) {
        t2.lst.add(0, t1);
        return t2;
    }

    public Vector<String> toStringVector() {
        Vector<String> ls = new Vector<>();
        for (ASTree t : lst) {
            ls.add(t.atom);
        }
        return ls;
    }

}


