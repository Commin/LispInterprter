import lexer.Lexer;

import java.io.IOException;
import java.util.Vector;

/**
 * File: LexerRunner.java
 *
 * @author commin
 * @create 2017/10/22
 */
public class LexerRunner {
    public static void main(String[] args) {

        try {
            Lexer l = new Lexer(new CodeDialog());
            long time = System.currentTimeMillis();
            Vector<String> tokens = l.getTokens();
            for (String token : tokens) {
                System.out.println(token);
            }
            System.out.println(System.currentTimeMillis() - time);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
