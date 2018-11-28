package isLL1;

import java.util.ArrayList;
import java.util.List;

class NParInstruction extends ParInstruction{
    NParInstruction(ArrayList<MyToken> array){
        super(array);
    }

    public ArrayList<ArrayList<MyToken>> makeNoPar(int startIndex, int endIndex){
        ArrayList<ArrayList<MyToken>> dArray = makeOrTokenList(startIndex, endIndex);
        List<MyToken> startList = array.subList(0, startIndex);
        List<MyToken> endList   = array.subList(endIndex + 1, array.size());

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
        return super.nextParIndex(startIndex, MyToken.NLPAR, MyToken.NRPAR);
    }
}
