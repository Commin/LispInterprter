import lexer.Lexer;
import parser.Parser;

import java.io.IOException;
import java.util.Vector;

/**
 * File: LexerInterpreter.java
 *
 * @author commin
 * @create 2017/10/22
 */
public class LexerInterpreter {
    public static void main(String[] args) {
        try {
            Lexer l = new Lexer("(- 40 45 (CAR (43 54)\n" +
                    "))))))");
            Vector<String> tokens = l.getTokens();
            System.out.println("size= " +tokens.size());
            int i =1;
            for (String token: tokens) {
                System.out.println(i+ ": " + token + ": " +token.length());
                i++;
            }
            //Parser p = new Parser(l.getTokens());
            //p.evaluate();
        } catch (Exception e){
            e.printStackTrace();
            if ( args.length > 0 && args[0].matches("-d") ){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            System.exit(3);
        }
    }
}
