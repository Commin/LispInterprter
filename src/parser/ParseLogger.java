package parser;

/**
 * File: ParseLogger.java
 *
 * @author commin
 * @create 2017/12/5
 */
public class ParseLogger {
    private static volatile ParseLogger parseLogger;
    private ParseLogger(){}
    private volatile boolean enableLog = true;
    public static ParseLogger getInstance(){
        if(parseLogger == null){
            synchronized (ParseLogger.class){
                if(parseLogger == null){
                    parseLogger = new ParseLogger();
                }
            }
        }
        return parseLogger;
    }

    public void disable() {
        this.enableLog = false;
    }

    public void enable(){
        this.enableLog = true;
    }

    public void print(String methodName,String s){
        if(enableLog) {
            System.out.print(methodName + ": " + s);
        }
    }

    public void println(String methodName,String s){
        if(enableLog){
            System.out.println(methodName+": "+s);
        }
    }


}
