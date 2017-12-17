package parser;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * File: Env.java
 *
 * @author commin
 * @create 2017/12/11
 */
public class Env {
    private Hashtable<String, ASTree> table = new Hashtable<>();
    private Env parent = null;

    public Env(Hashtable<String,ASTree> table){
        this.table = table;
    }

    public Env(String name, ASTree value){
        table.put(name,value);
    }

    public Env(){

    }

    public Env(Vector<String> params, Vector<ASTree> argList) {
        for (int i = 0; i < params.size(); i++) {
            table.put(params.get(0), argList.get(i));
        }
    }

    static Env extend(Env old, Env new_) {
        Env e = new Env(new Hashtable<>(new_.table));
        e.parent = old;
        return e;
    }

    public ASTree get(String name) {
        if (table.containsKey(name)){
            return table.get(name);
        }
        if (parent != null) {
            return parent.get(name);
        }
        return null;
    }

    void add(String name, ASTree value) {
        table.put(name, value);
    }

    @Override
    public String toString() {
        List<String> values = table.entrySet().parallelStream().map(e -> e.getKey() + ": "
                + e.getValue().toSimpleString()).collect(Collectors.toList());
        //toSimpleString -> toString
        return "{ "+ String.join(", ", values);
    }
}
