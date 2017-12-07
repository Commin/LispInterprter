package parser;


import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * File: Patterns.java
 *
 * @author commin
 * @create 2017/10/22
 */
public class Patterns {
    private static volatile Patterns patterns;
    private Patterns(){
        initTable();
    }
    public static Patterns getInstance(){
        if(patterns == null){
            synchronized (ParseLogger.class){
                if(patterns == null){
                    patterns = new Patterns();
                }
            }
        }
        return patterns;
    }



    static final String LITERAL = "[a-zA-Z0-9]+";
    static final String FUNCTION_NAME_VALID = "[A-Z_a-z][A-Z_a-z0-9]*";
    static final String NUMERIC_ATOM = "[\\d\\+\\-]?[\\d]*";
    static final String SYMBOL = "\\+|\\-|\\*|\\/|\\%";
    static final String T = "T";
    static final String NIL = "NIL";
    static final String TRUE = "true";
    static final String FALSE = "false";
    static final String DEFUN = "DEFUN";
    static final String CAR = "CAR";
    static final String CDR = "CDR";
    static final String START_PARENTHESIS = "[(]";
    static final String END_PARENTHESIS = "[)]";
    static final String DOT = "[.]";

    static Hashtable<String,String> operatorTable = new Hashtable<>();


    private void initTable(){
        operatorTable.put(".","DOT");
        operatorTable.put("+","PLUS");
        operatorTable.put("-","MINUS");
        operatorTable.put("*","TIMES");
        operatorTable.put("/","DIVIDE");
        operatorTable.put("%","REMAINDER");
        operatorTable.put("'","QUOTE");
        operatorTable.put("==","EQ");
        operatorTable.put("!=","NEQ");
        operatorTable.put(">","LSS");
        operatorTable.put(">=","LEQ");
        operatorTable.put("<","GTR");
        operatorTable.put("<=","GEQ");
    }
    
    public String replaceOperator(String operator){
        Iterator<Map.Entry<String, String>> iterator = operatorTable.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String,String> entry = iterator.next();
            if(entry.getKey().equals(operator)){
                return entry.getValue();
            }
        }
        return operator;
    }


}
