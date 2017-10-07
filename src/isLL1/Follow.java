package isLL1;

import java.util.ArrayList;

class Follow extends MySet{
    Follow(){
        super();
    }

    Follow(String symbolName){
        super(symbolName);
    }

    Follow(String symbolName, ArrayList<MyToken> array){
        super(symbolName, array);
    }
}
