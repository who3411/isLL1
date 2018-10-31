package isLL1;

import java.util.ArrayList;
import java.util.Iterator;

class FirstSet extends MyMakeSet {
    private String lastMakeSymbolName;
    private String lastSymbolName;
    FirstSet(InputData inData){
        super(inData);
    }

    public String toAllSetString(){
        return super.toAllSetString("First");
    }

    @Override
    public String makeAllSet(){
        lastMakeSymbolName = null;
        lastSymbolName     = null;
        return super.makeAllSet();
    }

    protected ArrayList<MySet> makeOneSet(String makeSymbolName){
        ArrayList<MySet> retArray = new ArrayList<MySet>();

        First   first = new First(makeSymbolName);
        ArrayList<MyToken> array = first.getArray();
        Iterator<ArrayList<MyToken>> i = inData.get(makeSymbolName).iterator();

        while(i.hasNext()){
            ArrayList<MyToken> iArray = i.next();
            MyToken tk = iArray.get(0);

            if(tk.getType() == MyToken.TSYMBOL) {
                String symbolName = tk.getName();
                ArrayList<MySet> subArray;

                if(makeSymbolName.equals(symbolName)){
                    continue;
                }else if(get(symbolName) == null){
                    if(lastMakeSymbolName != null && lastSymbolName != null){
                        if(lastMakeSymbolName.equals(symbolName) && lastSymbolName.equals(makeSymbolName)){
                            StringBuffer strBuf = new StringBuffer();

                            strBuf.append(lastMakeSymbolName);
                            strBuf.append("と");
                            strBuf.append(makeSymbolName);
                            strBuf.append("の間で循環が発生しています。");
                            strLine = strBuf.toString();
                            return null;
                        }
                    }
                    lastMakeSymbolName = makeSymbolName;
                    lastSymbolName     = symbolName;
                    subArray = makeOneSet(symbolName);
                    if(strLine != null && subArray == null){
                        return null;
                    }
                    put(symbolName, subArray);
                }else{
                    subArray = get(symbolName);
                }
                for(int j = 0;j < subArray.size();j++){
                    MySet set = subArray.get(j);
                    ArrayList<MyToken> oneArray = set.getArray();

                    array.addAll(oneArray);
                }
            }else if(tk.getType() == MyToken.NTSYMBOL || tk.getType() == MyToken.EPSILON){
                array.add(tk);
            }
        }
        removeDup(array);
        retArray.add(first);

        return retArray;
    }
}
