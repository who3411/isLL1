package isLL1;

import java.util.ArrayList;
import java.util.Iterator;

abstract class MySet {
    protected String symbolName;
    protected ArrayList<MyToken> array;

    MySet(){
        symbolName = null;
        array = new ArrayList<MyToken>();
    }

    MySet(String symbolName){
        this.symbolName = symbolName;
        array = new ArrayList<MyToken>();
    }

    MySet(String symbolName, ArrayList<MyToken> array){
        this.symbolName = symbolName;
        this.array      = array;
    }

    public void setSymbolName(String symbolName){
        this.symbolName = symbolName;
    }

    public String getSymbolName(){
        return symbolName;
    }

    public ArrayList<MyToken> getArray(){
        return array;
    }

    public boolean isSet(int type){
        Iterator i = array.iterator();

        while(i.hasNext()){
            MyToken tk = (MyToken)i.next();
            if(tk.getType() == type){
                return true;
            }
        }

        return false;
    }

    protected String toSetString(String name){
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(name + "(" + symbolName + ") = {");
        for(int i = 0;i < array.size();i++){
            MyToken tk = array.get(i);

            strBuf.append(tk.getName());
            if(i != array.size() - 1){
                strBuf.append(", ");
            }
        }
        strBuf.append("}\n");
        return strBuf.toString();
    }
}
