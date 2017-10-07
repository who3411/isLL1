package isLL1;

import java.util.ArrayList;
import java.util.List;

class BParInstruction extends ParInstruction{
    BParInstruction(ArrayList<MyToken> array){
        super(array);
    }

    public ArrayList<ArrayList<MyToken>> makeNoPar(int startIndex, int endIndex){
        ArrayList<ArrayList<MyToken>> dArray  = makeOrTokenList(startIndex, endIndex);
        ArrayList<ArrayList<MyToken>> cDArray = (ArrayList<ArrayList<MyToken>>)dArray.clone();
        List startList = array.subList(0, startIndex);
        List endList   = array.subList(endIndex + 1, array.size());
        ArrayList<MyToken> empArray = new ArrayList<MyToken>();

        for(int i = 0;i < cDArray.size();i++){
            ArrayList<MyToken> iArray   = cDArray.get(i);

            for(int j = 0;j < cDArray.size();j++){
                ArrayList<MyToken> retArray = new ArrayList<MyToken>();
                ArrayList<MyToken> jArray   = cDArray.get(j);

                retArray.addAll(iArray);
                retArray.addAll(jArray);

                if(retArray.size() != 0){
                    dArray.add(retArray);
                }
            }
        }

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
        return super.nextParIndex(startIndex, MyToken.BLPAR, MyToken.BRPAR);
    }
}
