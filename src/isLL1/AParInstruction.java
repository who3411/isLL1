package isLL1;

import java.util.ArrayList;
import java.util.List;

class AParInstruction extends ParInstruction{
    AParInstruction(ArrayList<MyToken> array){
        super(array);
    }

    public ArrayList<ArrayList<MyToken>> makeNoPar(int startIndex, int endIndex){
        ArrayList<ArrayList<MyToken>> dArray = makeOrTokenList(startIndex, endIndex);
        List<MyToken> startList = array.subList(0, startIndex);
        List<MyToken> endList   = array.subList(endIndex + 1, array.size());
        ArrayList<MyToken> empArray = new ArrayList<MyToken>();

        dArray.add(0, empArray);

        for(int i = 0;i < dArray.size();i++){
            ArrayList<MyToken> retArray = dArray.get(i);

            retArray.addAll(0, startList);
            retArray.addAll(retArray.size(), endList);

            if(retArray.size() == 0){
                dArray.remove(retArray);
            }
        }

        return dArray;
    }

    @Override
    public int nextParIndex(int startIndex){
        return super.nextParIndex(startIndex, MyToken.ALPAR, MyToken.ARPAR);
    }
}
