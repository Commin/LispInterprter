package parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

/**
 * File: Lisp.java
 *
 * @author commin
 * @create 2017/12/11
 */
public class Lisp {
    public static ASTree eval(ASTree expr, Env env)
            throws SyntaxException, RunTimeException {
        // #t
        if (expr.isAtom() && expr.equals(Patterns.T)) {
            return expr;
        }
        // look up variable
        else if(expr.isAtom()){
            ASTree v = env.get(expr.atom);
            if (v == null){
                throw new RunTimeException("undefined variable "+expr.atom);
            }
            return v;
        }
        // preserve
        else if(expr.isInt()){
            return expr;
        }
        // only the root of lisp
        else if(expr.isList()){
            if (expr.hasHead()){
                ASTree head = expr.getHead();
                ASTree tail = expr.getTail();
                return Lisp.evalList(head, tail, env);
            } else {
                return expr;
            }
        }
        else {
            return new ASTree();
        }
    }

     public static ASTree evalList(ASTree head, ASTree tail, Env env)
            throws SyntaxException, RunTimeException {
        if(head.isAtom()){
            // Quote
            if (head.equals(Patterns.QUOTE[0]) || head.equals(Patterns.QUOTE[1])) {
                return tail.get(0);
            }
            // Atom
            else if (head.equals(Patterns.ATOM)) {
                return ASTree.NewBool(Lisp.eval(tail.get(0), env).isAtom());
            }
            // Eq
            else if (head.equals(Patterns.EQ[0]) || head.equals(Patterns.EQ[1])) {
                ASTree v1 = Lisp.eval(tail.get(0), env);
                ASTree v2 = Lisp.eval(tail.get(1), env);
                if (v1.isAtom() && v2.isAtom()){
                    return ASTree.NewBool(v1.equals(v2));
                }
                else if (v1.isInt() && v2.isInt()){
                    return ASTree.NewBool(v1.ival == v2.ival);
                }
                else {
                    return ASTree.NewBool(v1.isNil() && v2.isNil());
                }
            }
            //GTR LSS
            else if (head.equals(Patterns.GTR[0]) || head.equals(Patterns.GTR[1])) {
                ASTree v1 = Lisp.eval(tail.get(0), env);
                ASTree v2 = Lisp.eval(tail.get(1), env);
                if (v1.isInt() && v2.isInt()){
                    return ASTree.NewBool(v1.ival > v2.ival);
                }else {
                    throw new SyntaxException("can't compare "+ v1.toString() + " and " + v2.toString() + ". ");
                }
            }
            else if (head.equals(Patterns.GEQ[0]) || head.equals(Patterns.GEQ[1])) {
                ASTree v1 = Lisp.eval(tail.get(0), env);
                ASTree v2 = Lisp.eval(tail.get(1), env);
                if (v1.isInt() && v2.isInt()){
                    return ASTree.NewBool(v1.ival >= v2.ival);
                }else {
                    throw new SyntaxException("can't compare "+ v1.toString() + " and " + v2.toString() + ". ");
                }
            }
            else if (head.equals(Patterns.LSS[0]) || head.equals(Patterns.LSS[1])) {
                ASTree v1 = Lisp.eval(tail.get(0), env);
                ASTree v2 = Lisp.eval(tail.get(1), env);
                if (v1.isInt() && v2.isInt()){
                    return ASTree.NewBool(v1.ival < v2.ival);
                }else {
                    throw new SyntaxException("can't compare "+ v1.toString() + " and " + v2.toString() + ". ");
                }
            }
            else if (head.equals(Patterns.LEQ[0]) || head.equals(Patterns.LEQ[1])) {
                ASTree v1 = Lisp.eval(tail.get(0), env);
                ASTree v2 = Lisp.eval(tail.get(1), env);
                if (v1.isInt() && v2.isInt()){
                    return ASTree.NewBool(v1.ival <= v2.ival);
                }else {
                    throw new SyntaxException("can't compare "+ v1.toString() + " and " + v2.toString() + ". ");
                }
            }

            // Car
            else if (head.equals(Patterns.CAR)) {
                return Lisp.eval(tail.get(0), env).getHead();
            }
            // Cdr
            else if (head.equals(Patterns.CDR)) {
                return Lisp.eval(tail.get(0), env).getTail();
            }
            // Cons
            else if (head.equals(Patterns.CONS)) {
                ASTree v1 = Lisp.eval(tail.get(0), env);
                ASTree v2 = Lisp.eval(tail.get(1), env);
                return ASTree.cons(v1, v2);
            }
            // Cond
            else if (head.equals(Patterns.COND)) {
                return ASTree.cond(tail, env);
            }
            //defun
            else if (head.equals(Patterns.DEFUN)){
                env = Lisp.def(tail, env);
                return tail;
            }
            //lambda
            else if (head.equals(Patterns.LAMBDA)){
                ASTree params_ = tail.get(0);
                ASTree body_ = tail.get(1);
                return new ASTree(new Closure(params_.toStringVector(),body_,env));
            }
            // + - * /
            else if(Arrays.asList(Patterns.arithmeticArray).contains(head.atom)){
                ASTree v1 = Lisp.eval(tail.get(0), env);
                ASTree v2 = Lisp.eval(tail.get(1), env);
                if (head.equals(Patterns.arithmeticArray[0])) {
                    return ASTree.add(v1, v2);
                } else if (head.equals(Patterns.arithmeticArray[1])) {
                    return ASTree.sub(v1, v2);
                } else if (head.equals(Patterns.arithmeticArray[2])) {
                    return ASTree.mul(v1, v2);
                } else if (head.equals(Patterns.arithmeticArray[3])) {
                    return ASTree.div(v1, v2);
                }else if (head.equals(Patterns.arithmeticArray[4])) {
                    return ASTree.remainder(v1, v2);
                }
            }
            //list
            else if (head.equals(Patterns.LIST)) {
                Vector<ASTree> ls = new Vector<>();
                for (ASTree e : tail.lst) {
                    ls.add(Lisp.eval(e, env));
                }
                return new ASTree(ls);
            }
        }

        // apply user defined function
        ASTree hval = Lisp.eval(head, env);
        if (hval.isLambda()) {
            return Lisp.apply(hval, tail.lst, env);
        }
        else {
            throw new RunTimeException("not a function");
        }
    }

    private static ASTree apply(ASTree func, Vector<ASTree> lst, Env env) throws SyntaxException, RunTimeException {
        Closure c = func.closure;
        Env e = new Env();
        for (int i = 0; i < c.params.size(); i++) {
            String name = c.params.get(i);
            ASTree v = Lisp.eval(lst.get(i), env);
            e.add(name, v);
        }
        Env newEnv = Env.extend(c.env, e);
        ASTree r = Lisp.eval(c.body, newEnv);
        return r;
    }

    private static Env def(ASTree tail, Env env) throws SyntaxException, RunTimeException {
        ASTree name = tail.get(0);
        ASTree value;
        if (tail.len() == 2) {
            // variable
            value = Lisp.eval(tail.get(1), env);
        } else {
            // function
            ASTree param_ = tail.get(1);
            ASTree body_ = tail.get(2);
            Closure closure = new Closure(param_.toStringVector(), body_, env);
            closure.name = name.atom;
            value = new ASTree(closure);
        }
        env.add(name.atom, value);
        return env;
    }

    public static void main(String[] args) {
        Lexer l = new Lexer();
        try {
            String str = "(atom (3))";
            Vector<ASTree> tree = l.tokenize(new ByteArrayInputStream(str.getBytes()));
            System.out.println(tree.toString());
            Env env = new Env();
            ASTree r = null;
            for (ASTree t : tree) {
                r = Lisp.eval(t, env);
                if (r.isDef()) {
                    env = Lisp.def(r, env);
                }
            }
            System.out.println(r);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SyntaxException e) {
            e.printStackTrace();
        } catch (RunTimeException e) {
            e.printStackTrace();
        }
    }


}
