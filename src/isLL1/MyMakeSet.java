package isLL1;

import java.util.*;

abstract class MyMakeSet extends LinkedHashMap<String, ArrayList<MySet>>{
    protected InputData inData;
    protected String    strLine;
    MyMakeSet(InputData inData) {
        this.inData = inData;
    }

    public String makeAllSet(){
        Iterator<String> i = inData.keySet().iterator();

        strLine = null;

        while(i.hasNext()){
            String symbolName = i.next();

            put(symbolName, null);
        }

        i = inData.keySet().iterator();
        while(i.hasNext()){
            ArrayList<MySet> array;
            String symbolName = i.next();

            if(get(symbolName) == null){
                array = makeOneSet(symbolName);
                if(strLine != null && array == null){
                    return strLine;
                }
                put(symbolName, array);
            }
        }
        return null;
    }

    protected abstract ArrayList<MySet> makeOneSet(String makeSymbolName);

    public String toAllSetString(String methodName){
        StringBuffer strBuf = new StringBuffer();
        Iterator<ArrayList<MySet>> i = values().iterator();

        while(i.hasNext()){
            ArrayList<MySet> setArray = i.next();
            Iterator<MySet> ii = setArray.iterator();
            while (ii.hasNext()){
                MySet set  = ii.next();
                String str = set.toSetString(methodName);

                strBuf.append(str);
            }
        }

        return strBuf.toString();
    }

    public void removeDup(ArrayList<MyToken> array){
        HashSet<String> hSet = new HashSet<String>();
        ArrayList<MyToken> retArray = new ArrayList<MyToken>();
        Iterator<MyToken> i = array.iterator();

        while(i.hasNext()){
            MyToken tk = i.next();

            if(!hSet.contains(tk.getName())){
                hSet.add(tk.getName());
                retArray.add(tk);
            }
        }

        array.clear();
        array.addAll(retArray);
    }
}
