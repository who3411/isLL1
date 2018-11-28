package isLL1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

class FollowSet extends MyMakeSet {
    private static final MyToken FINMARKER = new MyToken("$", MyToken.FIN);
    private FirstSet firstSet;
    FollowSet(InputData inData, FirstSet firstSet){
        super(inData);
        this.firstSet = firstSet;
    }

    public String toAllSetString(){
        return super.toAllSetString("Follow");
    }

    protected ArrayList<MySet> makeOneSet(String makeSymbolName){
        ArrayList<MySet> retArray = new ArrayList<MySet>();

        int refCount = 0;
        Follow  follow = new Follow(makeSymbolName);
        ArrayList<MyToken> array = follow.getArray();
        Iterator<ArrayList<ArrayList<MyToken>>> i  = inData.values().iterator();
        Iterator<String> i2 = inData.keySet().iterator();

        while(i.hasNext()){
            ArrayList<ArrayList<MyToken>> dArray = i.next();
            String symbolName = i2.next();
            Iterator<ArrayList<MyToken>> ii = dArray.iterator();

            while(ii.hasNext()){
                ArrayList<MyToken> iiArray = ii.next();
                Iterator<MyToken> iii = iiArray.iterator();

                while(iii.hasNext()){
                    MyToken tk = iii.next();

                    if(makeSymbolName.equals(tk.getName())){
                        int nextNum  = iiArray.indexOf(tk) + 1;
                        MyToken myTk;

                        if(makeSymbolName.equals(symbolName)){
                            continue;
                        }else if(nextNum == iiArray.size()){
                            myTk = new MyToken("~", MyToken.EPSILON);
                        }else{
                            myTk = iiArray.get(nextNum);
                        }
                        if(makeSymbolName.equals(myTk.getName())){
                            continue;
                        }
                        switch(myTk.getType()){
                            case MyToken.TSYMBOL:
                                if(firstSet.get(myTk.getName()).get(0).isSet(MyToken.EPSILON)){
                                    setSymbolMySet(myTk.getName(), array, this);
                                }else{
                                    setSymbolMySet(myTk.getName(), array, firstSet);
                                }
                                break;
                            case MyToken.NTSYMBOL:
                                array.add(myTk);
                                break;
                            case MyToken.EPSILON:
                                //System.out.println("makeSymbolName = " + makeSymbolName + " symbolName = " + symbolName);
                                setSymbolMySet(symbolName, array, this);
                                break;
                        }
                        refCount++;
                    }
                }
            }
        }

        if(refCount == 0){
            array.add(FINMARKER);
        }else{
            removeDup(array);
        }
        retArray.add(follow);
        //System.out.print(follow.toSetString("Follow"));
        return retArray;
    }

    private void setSymbolMySet(String symbolName, ArrayList<MyToken> array, MyMakeSet mmset){
        ArrayList<MySet> subArray;

        if(mmset.get(symbolName) == null){
            subArray = makeOneSet(symbolName);
            mmset.put(symbolName, subArray);
        }else{
            subArray = mmset.get(symbolName);
        }
        for(int j = 0;j < subArray.size();j++){
            MySet set = subArray.get(j);
            ArrayList<MyToken> oneArray = set.getArray();

            array.addAll(oneArray);
        }
    }
}
