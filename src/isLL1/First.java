package isLL1;

import java.util.ArrayList;

class First extends MySet{
    First(){
        super();
    }

    First(String symbolName){
        super(symbolName);
    }

    First(String symbolName, ArrayList<MyToken> array){
        super(symbolName, array);
    }
}
