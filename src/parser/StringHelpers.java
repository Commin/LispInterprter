package parser;

import java.util.List;
import java.util.Vector;

/**
 * File: StringHelpers.java
 *
 * @author commin
 * @create 2017/10/24
 */
public class StringHelpers {

    public static String join(List s, String glue){
        String newString = "";
        for ( int i = 0; i < s.size() - 1; i++ ){
            newString = newString + s.get(i).toString() + glue;
        }
        return newString + s.get(s.size() - 1).toString();
    }

    public static Vector<String> vectorize(String s){
        Vector <String> v = new Vector <String> ();
        int i;
        for ( i = 0; i < s.length() - 1; i ++ ){
            v.add(s.substring(i,i+1));
        }
        if ( i > 0 ){
            v.add(s.substring(i));
        }
        return v;
    }
}
