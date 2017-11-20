package lexer;

import java.io.IOException;

/**
 * File: LexicalException.java
 *
 * @author commin
 * @create 2017/10/26
 */
public class LexicalException extends IOException {
    public LexicalException(String msg){
        super(msg);
    }
}
