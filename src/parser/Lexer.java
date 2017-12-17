package parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Vector;

/**
 * File: Lexer.java
 *
 * @author commin
 * @create 2017/12/12
 */
public final class Lexer {

    private int lineNumber = 1;
    private int parenthesisNumber = 0;


    public Vector<ASTree> tokenize() throws IOException, SyntaxException {
        return this.tokenize(System.in);
    }

    public Vector<ASTree> tokenize(InputStream inStream) throws IOException, SyntaxException {
        // only support S-expression and atom
        Vector<ASTree> ret = new Vector<>();
        ASTree t, cur = null;
        //0:
        int state = 0;
        StringBuffer sb = new StringBuffer();
        PushbackInputStream in = new PushbackInputStream(inStream);

        while (true) {
            int ci = in.read();
            if (ci == -1) {
                if (sb.length() > 0) {
                    // if only an atom
                    ret.add(new ASTree(sb.toString()));
                }
//                if(parenthesisNumber<0) {
//                    System.out.println(parenthesisNumber);
//                    throw new SyntaxException("lack of start parenthesis in line "
//                            + lineNumber + ". ");
//                }
//                else if(parenthesisNumber>0){
//                    System.out.println(parenthesisNumber);
//                    throw new SyntaxException("lack of end parenthesis in line "
//                            + lineNumber + ". ");
//                }
                System.out.println("parenthesisNumber: "+ parenthesisNumber);
                return ret;
            }
            char c = (char)ci;
            switch (state) {
                // initial state
                case 0:
                    if (Character.isSpaceChar(c)) {
                        continue;
                    }
                    //判断是否为换行符且不为空格
                    else if(Character.isWhitespace(c)){
                        lineNumber++;
                        continue;
                    }

                    //start parenthesis
                    if (c == '(') {
                        parenthesisNumber++;
                        if (cur == null) {
                            cur = new ASTree();
                        } else {
                            t = new ASTree(cur);
                            cur.add(t);
                            t.begin();
                            cur = t;
                        }
                    }
                    // expression ends
                    else if (c == ')') {
                        parenthesisNumber--;
                        if (cur.end()) {
                            ret.add(cur);
                            cur = null;
                            System.out.println(ret.toString());
                        } else {
                            cur = cur.parent;
                        }
                    } else {
                        // word
                        state = 1;
                        sb.append(c);
                    }
                    break;
                // in an atom
                case 1:
                    if (Character.isWhitespace(c) || c == '(' || c == ')') {
                        ASTree e = new ASTree(sb.toString());
                        cur.add(e);

                        sb = new StringBuffer();
                        state = 0;
                        if (c == '(' ){
                            parenthesisNumber++;
                            in.unread(c);
                        }
                        else if(c == ')') {
                            parenthesisNumber--;
                            in.unread(c);
                        }
                        continue;
                    }
                    sb.append(c);
                    break;
                default: break;
            }
        }

    }
}
