package lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Vector;

/**
 * File: Lexer.java
 *
 * This is the main Lisp Lexical Analyzer.
 * @author commin
 * @create 2017/10/22
 */
public class Lexer {
    private String programString ="";
    private Vector<String> tokenVector = new Vector<>();

    public Lexer(String programString) throws IOException {
        this.programString = programString;
        init();
    }

    public Lexer(Reader reader) throws IOException{
        BufferedReader br = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        String str;
        while((str=br.readLine())!=null) {
            sb.append(str);
            sb.append("\n");
        }
        programString = sb.toString();
        init();
    }

    public Lexer(InputStream stream) throws IOException {
        byte[] bytes = new byte[1024];
        while (stream.available() > 0){
            stream.read(bytes, 0, 1024);
            programString = programString.concat(new String(bytes)).trim();
        }
        init();
    }

    public Vector<String> getTokens() {
        return tokenVector;
    }

    private void init() throws LexicalException{
        tokenVector = tokenize(programString);
    }

    private Vector<String> tokenize(String s) throws LexicalException{
        String[] strings = s.split(LexerPatterns.NEWLINE);
        Vector<String> tokens = new Vector<>();
        for (int i = 0; i < strings.length; i++) {
            //trim comment
            if(strings[i].matches(LexerPatterns.COMMENT)){
                continue;
            }

            strings[i] = strings[i].replaceAll(LexerPatterns.START_SYMBOL,LexerPatterns.START_SYMBOL_WITH_WHITESPACE);
            strings[i] = strings[i].replaceAll(LexerPatterns.END_SYMBOL,LexerPatterns.END_SYMBOL_WITH_WHITESPACE);


            String[] tokenStrings = strings[i].split(LexerPatterns.WHITESPACE);
            for (int j = 0; j < tokenStrings.length; j++) {
                if(!"".equals(tokenStrings[j])) {
                    tokens.add(tokenStrings[j]);
                }
            }
        }
        return tokens;
    }

//    private String[] splitBySpace(String s) throws LexicalException{
//        //trim comment
//
//
//        String[] tokens = s.split(LexerPatterns.WHITESPACE_NEWLINE);
//        if (tokens.length == 0){
//            throw new LexicalException("The program code is invalid.");
//        }
//        return tokens;
//    }

//    private Vector<String> tokenize (String[] tokens) throws LexicalException{
//        Vector<String> tokenVector = new Vector<>();
//        if (tokens.length == 1){
//            if ( tokens[0].matches(LexerPatterns.SYMBOL)){
//                throw new LexicalException("The program code only includes unclosed parenthesis.");
//            }
//            tokenVector.add(tokens[0]);
//            return tokenVector;
//        }
//
//        for (int i = 0; i < tokens.length; i++) {
//            String tmpToken = tokens[i];
//            if(tmpToken.matches(LexerPatterns.NEWLINE)){
//                tmpToken.replaceAll(LexerPatterns.NEWLINE," ");
//            }
//
//            if(tmpToken.matches(LexerPatterns.INCLUDE_SYMBOL)){
//
//
//            } else {
//                tokenVector.add(tmpToken);
//            }
//        }
//        return tokenVector;
//    }
}
