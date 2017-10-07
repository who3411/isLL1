package isLL1;

import java.util.ArrayList;

class Director  extends MySet{
    private MySet   firstOrFollow;
    private boolean isFirst;

    Director(){
        symbolName    = null;
        array         = null;
        firstOrFollow = null;
        isFirst       = false;
    }

    Director(String symbolName, ArrayList<MyToken> array, MySet firstOfFollow, boolean isFirst){
        this.symbolName    = symbolName;
        this.array         = array;
        this.firstOrFollow = firstOfFollow;
        this.isFirst       = isFirst;
    }

    public void setFirst(First first){
        this.firstOrFollow = first;
        isFirst = true;
    }

    public First getFirst(){
        return (First)(isFirst ? firstOrFollow : null);
    }

    public void setFollow(Follow follow){
        this.firstOrFollow = follow;
        isFirst = false;
    }

    public Follow getFollow() {
        return (Follow)(isFirst ? null : firstOrFollow);
    }

    public MySet getDirector(){
        return firstOrFollow;
    }

    @Override
    protected String toSetString(String name){
        StringBuffer strBuf = new StringBuffer();
        ArrayList<MyToken> setArray = firstOrFollow.getArray();

        strBuf.append(name + "(" + symbolName + ", ");
        for(int i = 0;i < array.size();i++){
            MyToken tk = array.get(i);

            strBuf.append(tk.getName());
            if(i != array.size() - 1){
                strBuf.append(" ");
            }
        }
        strBuf.append(") = {");
        for(int i = 0;i < setArray.size();i++){
            MyToken tk = setArray.get(i);

            strBuf.append(tk.getName());
            if(i != setArray.size() - 1){
                strBuf.append(", ");
            }
        }
        strBuf.append("}\n");
        return strBuf.toString();
    }
}
