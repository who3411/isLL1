package isLL1;

import java.util.ArrayList;

abstract class ParInstruction {
    protected ArrayList<MyToken> array;

    ParInstruction(ArrayList<MyToken> array){
        this.array = array;
    }

    public abstract ArrayList<ArrayList<MyToken>> makeNoPar(int startIndex, int endIndex);

    public abstract int nextParIndex(int startIndex);

    protected int nextParIndex(int startIndex, int lparType, int rparType){
        int parCount = 1;
        for(int i = startIndex + 1;i < array.size();i++){
            MyToken tk = array.get(i);

            if(tk.getType() == lparType){
                parCount++;
            }else if(tk.getType() == rparType){
                parCount--;
                if(parCount == 0){
                    return i;
                }
            }
        }
        return -1;
    }

    protected ArrayList<Integer> makeOrIndexList(int startIndex, int endIndex){
        ArrayList<Integer> retArray = new ArrayList<Integer>();
        int parCount = 0;
        for(int i = startIndex + 1;i < endIndex;i++){
            MyToken tk = array.get(i);

            switch(tk.getType()){
                case MyToken.NLPAR:
                case MyToken.ALPAR:
                case MyToken.BLPAR:
                    parCount++;
                    break;
                case MyToken.NRPAR:
                case MyToken.ARPAR:
                case MyToken.BRPAR:
                    parCount--;
                    break;
            }
            if(parCount == 0 && tk.getType() == MyToken.OR){
                retArray.add(i);
            }
        }

        retArray.add(endIndex);
        return retArray;
    }

    public ArrayList<ArrayList<MyToken>> makeOrTokenList(int startIndex, int endIndex){
        ArrayList<ArrayList<MyToken>> retDArray = new ArrayList<ArrayList<MyToken>>();
        ArrayList<Integer> orIndexArray;
        int startOrIndex = startIndex + 1;

        orIndexArray = makeOrIndexList(startIndex, endIndex);
        for(int i = 0;i < orIndexArray.size();i++){
            int endOrIndex = orIndexArray.get(i);
            ArrayList<MyToken> retArray = new ArrayList<MyToken>();

            for(int j = startOrIndex;j < endOrIndex;j++){
                retArray.add(array.get(j));
            }

            retDArray.add(retArray);
            startOrIndex = orIndexArray.get(i) + 1;
        }

        return retDArray;
    }

    /*
    protected boolean compareTokenArray(ArrayList<MyToken> array1, ArrayList<MyToken> array2){
        if(array1.size() != array2.size()){
            return false;
        }
        for(int i = 0;i < array1.size();i++){
            System.out.print("[compare]array1(" + i + ") = " + array1.get(i).getName() + ", ");
            System.out.println("array2(" + i + ") = " + array2.get(i).getName());
            if(!array1.get(i).getName().equals(array2.get(i).getName())){
                System.out.println();
                return false;
            }
        }
        System.out.println();
        return true;
    }
    */
}
