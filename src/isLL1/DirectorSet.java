package isLL1;

import java.util.ArrayList;
import java.util.Iterator;

class DirectorSet extends MyMakeSet {
    private FirstSet  firstSet;
    private FollowSet followSet;
    DirectorSet(InputData inData, FirstSet firstSet, FollowSet followSet){
        super(inData);
        this.firstSet  = firstSet;
        this.followSet = followSet;
    }

    public String toAllSetString(){
        return super.toAllSetString("Director");
    }

    protected ArrayList<MySet> makeOneSet(String makeSymbolName){
        ArrayList<MySet> retArray = new ArrayList<MySet>();
        ArrayList<ArrayList<MyToken>> dArray = inData.get(makeSymbolName);
        Iterator i = dArray.iterator();

        while(i.hasNext()){
            ArrayList<MyToken> iArray = (ArrayList<MyToken>)i.next();
            MyToken tk = iArray.get(0);
            Director director;
            MySet    firstOrFollow = null;
            boolean isFirst;

            //System.out.println("[DIRECTOR]" + tk.getName() + "(" + tk.getType() + ")");
            isFirst = !(tk.getType() == MyToken.EPSILON);
            if(isFirst){
                ArrayList<MyToken> fArray = new ArrayList<MyToken>();
                boolean parFlag = true, nextFlag = false;
                int parCount  = 0;
                int nextTkNum = 0;

                while(parFlag || nextFlag || parCount > 0){
                    MyToken myTk = iArray.get(nextTkNum++);

                    //System.out.println(myTk.getName() + " " + parFlag + " " + nextFlag + " " + parCount);
                    if(parFlag){
                        switch(myTk.getType()){
                            case MyToken.NLPAR:
                            case MyToken.ALPAR:
                            case MyToken.BLPAR:
                                parCount++;
                                continue;
                            default:
                                parFlag = false;
                                nextFlag = true;
                                break;
                        }
                    }
                    if(nextFlag){
                        switch(myTk.getType()){
                            case MyToken.TSYMBOL:
                                First fFirst = (First)firstSet.get(myTk.getName()).get(0);

                                fArray.addAll(fFirst.getArray());
                                break;
                            case MyToken.NTSYMBOL:
                            case MyToken.EPSILON:
                                fArray.add(myTk);
                                break;
                        }
                        nextFlag = false;
                    }
                    if(parCount > 0){
                        switch(myTk.getType()){
                            case MyToken.NRPAR:
                            case MyToken.ARPAR:
                            case MyToken.BRPAR:
                                parCount--;
                                if(iArray.size() != nextTkNum){
                                    nextFlag = true;
                                }
                                if(myTk.getType() == MyToken.NRPAR && parCount == 0){
                                    nextFlag = false;
                                }
                                break;
                            case MyToken.OR:
                                nextFlag = true;
                                break;
                        }
                    }
                }

                firstSet.removeDup(fArray);
                firstOrFollow = new First(tk.getName(), fArray);
            }else{
                firstOrFollow = followSet.get(makeSymbolName).get(0);
            }
            director = new Director(makeSymbolName, iArray, firstOrFollow, isFirst);

            retArray.add(director);
        }

        return retArray;
    }

    public String isLL1(){
        Iterator i  = values().iterator();
        Iterator i2 = keySet().iterator();

        while(i.hasNext()){
            ArrayList<MySet> array = (ArrayList<MySet>)i.next();
            String symbolName = (String)i2.next();

            for(int j = 0;j < array.size();j++){
                for(int k = j + 1;k < array.size();k++){
                    ArrayList<MyToken> interArray;
                    MySet set1 = ((Director)(array.get(j))).getDirector();
                    MySet set2 = ((Director)(array.get(k))).getDirector();
                    ArrayList<MyToken> array1 = set1.getArray();
                    ArrayList<MyToken> array2 = set2.getArray();

                    interArray = calcIntersection(array1, array2);
                    if(interArray != null){
                        StringBuffer strBuf = new StringBuffer();

                        strBuf.append("Director(" + symbolName + ", ");
                        for(int l = 0;l < array1.size();l++){
                            MyToken tk = array1.get(l);

                            strBuf.append(tk.getName());
                            if(l != array.size() - 1){
                                strBuf.append(" ");
                            }
                        }

                        strBuf.append(") ∩ Director(" + symbolName + ", ");
                        for(int l = 0;l < array2.size();l++){
                            MyToken tk = array2.get(l);

                            strBuf.append(tk.getName());
                            if(l != array.size() - 1){
                                strBuf.append(" ");
                            }
                        }
                        strBuf.append(") = {");

                        for(int l = 0;l < interArray.size();l++){
                            MyToken tk = interArray.get(l);

                            strBuf.append(tk.getName());
                            if(l != interArray.size() - 1){
                                strBuf.append(", ");
                            }
                        }
                        strBuf.append("}\n");

                        strLine = strBuf.toString();
                        return strLine;
                    }
                }
            }
        }
        return null;
    }

    private ArrayList<MyToken> calcIntersection(ArrayList<MyToken> tks1, ArrayList<MyToken> tks2){
        ArrayList<MyToken> tks = new ArrayList<MyToken>();

        for(int i = 0;i < tks1.size();i++){
            for(int j = 0;j < tks2.size();j++){
                MyToken tk1 = tks1.get(i);
                MyToken tk2 = tks2.get(j);

                if(tk1.getName().equals(tk2.getName())){
                    //System.out.println("[calcIntersection]tk1 = " + tk1.getName() + " tk2 = " + tk2.getName());
                    tks.add(tk1);
                }
            }
        }

        return (tks.size() == 0 ? null : tks);
    }
}
