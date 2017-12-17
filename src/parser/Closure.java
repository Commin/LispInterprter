package parser;

import java.util.Vector;

/**
 * File: Closure.java
 *
 * @author commin
 * @create 2017/12/11
 */
public class Closure {
    Env env;
    Vector<String> params;
    ASTree body;
    // help debug
    public String name;

    public  Closure(Vector<String> params, ASTree body, Env env){
        super();
        this.params = params;
        this.body = body;
        this.env = env;
    }

    @Override
    public String toString() {
        return "Closure [env=" + env + ", param=" + params + ", body=" + body + "]";
    }

    public String toSimpleString() {
        return "Closure[" + params + "...]";
    }
}
