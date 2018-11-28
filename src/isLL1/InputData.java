package isLL1;

import java.io.PrintStream;
import java.util.*;

class InputData extends LinkedHashMap<String, ArrayList<ArrayList<MyToken>>>{
    private InputData alter;

    public void inputAnalysis(MyContext context){
        readFile(context);
        if(context.getErrorCount() == 0){
            updateSymbolType();
            do{
                context.resetSubWarningCount();
                checkInputData(context);
                if(context.getErrorCount() == 0){
                    reCheckInputData(context);
                }
            }while(context.getSubWarningCount() > 0 && context.getErrorCount() == 0);
            if(context.getErrorCount() == 0){
                printInputData(context, false);
                alter = new InputData();
                createAlter(this, alter);
                formatInputData();
                //printInputData(context, true);
            }
        }
    }

    private void createAlter(InputData fromInData, InputData toInData){
        Iterator<ArrayList<ArrayList<MyToken>>> i  = fromInData.values().iterator();
        Iterator<String> i2 = fromInData.keySet().iterator();

        toInData.clear();
        while(i.hasNext()){
            ArrayList<ArrayList<MyToken>> dArray = i.next();
            ArrayList<ArrayList<MyToken>> aDArray = new ArrayList<ArrayList<MyToken>>();
            String symbolName = i2.next();
            Iterator<ArrayList<MyToken>> ii = dArray.iterator();

            while (ii.hasNext()){
                ArrayList<MyToken> array = ii.next();
                ArrayList<MyToken> aArray = new ArrayList<MyToken>();
                Iterator<MyToken> iii = array.iterator();

                while(iii.hasNext()){
                    MyToken tk = iii.next();

                    aArray.add(new MyToken(tk.getName(), tk.getType()));
                }
                aDArray.add(aArray);
            }

            toInData.put(symbolName, aDArray);
        }
    }

    public void changeAlter(){
        InputData tmpInData = new InputData();
        createAlter(this, tmpInData);
        createAlter(alter, this);
        createAlter(tmpInData, alter);
    }

    private void readFile(MyContext context){
        MyTokenizer tokenizer = context.getTokenizer();
        int rowCount = 0;

        MyToken tk = null;
        do{
            rowCount++;
            MyToken tSymbolTk = tokenizer.getNextMyToken();
            if(tSymbolTk.getType() == MyToken.UTSYMBOL){
                MyToken equalTk   = tokenizer.getNextMyToken();
                if(equalTk.getType() == MyToken.EQUAL){
                    ArrayList<ArrayList<MyToken>> dArray = new ArrayList<ArrayList<MyToken>>();
                    do{
                        ArrayList<MyToken> array = new ArrayList<MyToken>();
                        boolean addFlag;
                        int parCount = 0;
                        do{
                            tk = tokenizer.getNextMyToken();
                            if(tk.getType() == MyToken.ILL || tk.getType() == MyToken.EQUAL){
                                context.printError("[" + rowCount + "行目] : " + tk.getName() + "は右辺に使用できません。");
                            }else if(tk.getType() == MyToken.NLPAR || tk.getType() == MyToken.ALPAR || tk.getType() == MyToken.BLPAR){
                                parCount++;
                            }else if(tk.getType() == MyToken.NRPAR || tk.getType() == MyToken.ARPAR || tk.getType() == MyToken.BRPAR){
                                parCount--;
                            }
                            addFlag = (parCount > 0 && tk.getType() != MyToken.NL);
                            addFlag = addFlag || (parCount == 0 && tk.getType() != MyToken.OR && tk.getType() != MyToken.NL);
                            addFlag = addFlag && (tk.getType() != MyToken.EOF);
                            if(addFlag){
                                array.add(tk);
                            }
                        }while(addFlag);
                        dArray.add(array);
                    }while(tk.getType() != MyToken.NL && tk.getType() != MyToken.EOF);
                    put(tSymbolTk.getName(), dArray);
                }else{
                    tk = equalTk;
                    context.printError("[" + rowCount + "行目] : 等式::=がありません。");
                    do{
                        tk = tokenizer.getNextMyToken();
                    }while(tk.getType() != MyToken.NL);
                }
            }else if(tSymbolTk.getType() == MyToken.EOF || tSymbolTk.getType() == MyToken.NL){
                tk = tSymbolTk;
            }else{
                tk = tSymbolTk;
                context.printError("[" + rowCount + "行目] : 行はじめが終端記号ではありません。");
                do{
                    tk = tokenizer.getNextMyToken();
                }while(tk.getType() != MyToken.NL);
            }
        }while(tk.getType() != MyToken.EOF);
    }

    private void updateSymbolType(){
        Iterator<ArrayList<ArrayList<MyToken>>> i = values().iterator();
        while(i.hasNext()){
            ArrayList<ArrayList<MyToken>> dArray = i.next();
            Iterator<ArrayList<MyToken>> ii = dArray.iterator();
            while (ii.hasNext()){
                ArrayList<MyToken> array = ii.next();
                Iterator<MyToken> iii = array.iterator();
                while(iii.hasNext()){
                    MyToken tk = iii.next();
                    if(tk.getType() == MyToken.UTSYMBOL){
                        tk.setType(containsKey(tk.getName()) ? MyToken.TSYMBOL : MyToken.NTSYMBOL);
                    }
                }
            }
        }
    }

    private void checkInputData(MyContext context){
        Iterator<ArrayList<ArrayList<MyToken>>> i  = values().iterator();
        Iterator<String> i2 = keySet().iterator();

        while(i.hasNext()){
            ArrayList<ArrayList<MyToken>> removeDArray = new ArrayList<ArrayList<MyToken>>();
            ArrayList<ArrayList<MyToken>> dArray = i.next();
            String symbolName = i2.next();

            Iterator<ArrayList<MyToken>> ii = dArray.iterator();
            int epsilonCount = 0;
            while (ii.hasNext()){
                MyToken lastLparTk, lastRparTk;
                ArrayList<MyToken> array = ii.next();
                ArrayList<MyToken> removeArray = new ArrayList<MyToken>();
                LinkedList<MyToken> parCheck = new LinkedList<MyToken>();
                Iterator<MyToken> iii = array.iterator();
                String strLine = toLineString(symbolName);
                int nparCount = 0;
                int aparCount = 0;
                int bparCount = 0;

                lastLparTk = null;
                lastRparTk = null;
                while(iii.hasNext()){
                    MyToken tk = iii.next();
                    if(tk.getType() == MyToken.EPSILON){
                        if(array.size() > 1){
                            context.printError(strLine + "空文字は単体でしか使用できません。");
                        }
                        if(++epsilonCount > 1){
                            context.printWarning(strLine + "1行に空文字が2つ以上あります。");
                            removeDArray.add(array);
                            break;
                        }
                    }else if(tk.getType() == MyToken.EQUAL){
                        context.printError(strLine + "左辺では使用できません。");
                    }else if(tk.getType() == MyToken.NLPAR){
                        nparCount++;
                        parCheck.add(tk);
                    }else if(tk.getType() == MyToken.NRPAR){
                        MyToken checkTk = parCheck.getLast();
                        int startPar = array.indexOf(checkTk);
                        int lastPar  = array.indexOf(tk);

                        if(--nparCount < 0){
                            context.printError(strLine + "\")\"が\"(\"よりも多いです。");
                        }
                        if(checkTk.getType() != MyToken.NLPAR){
                            context.printError(strLine + "括弧の順番が不適切です。");
                        }
                        if(lastPar - startPar == 1){
                            context.printWarning(strLine + "括弧内の記述が存在しません。");
                            removeArray.add(checkTk);
                            removeArray.add(tk);
                        }
                        if(lastLparTk != null && lastRparTk != null){
                            if(array.indexOf(lastLparTk) - 1 == startPar && array.indexOf(lastRparTk) + 1 == lastPar){
                                context.printError(strLine + "括弧が重複しています。");
                            }
                        }
                        parCheck.removeLast();
                        lastLparTk = checkTk;
                        lastRparTk = tk;
                    }else if(tk.getType() == MyToken.ALPAR){
                        aparCount++;
                        parCheck.add(tk);
                    }else if(tk.getType() == MyToken.ARPAR){
                        MyToken checkTk = parCheck.getLast();
                        int startPar = array.indexOf(checkTk);
                        int lastPar  = array.indexOf(tk);

                        if(--aparCount < 0){
                            context.printError(strLine + "\"]\"が\"[\"よりも多いです。");
                        }
                        if(checkTk.getType() != MyToken.ALPAR){
                            context.printError(strLine + "括弧の順番が不適切です。");
                        }
                        if(lastPar - startPar == 1){
                            context.printWarning(strLine + "括弧内の記述が存在しません。");
                            removeArray.add(checkTk);
                            removeArray.add(tk);
                        }
                        if(lastLparTk != null && lastRparTk != null){
                            if(array.indexOf(lastLparTk) - 1 == startPar && array.indexOf(lastRparTk) + 1 == lastPar){
                                context.printError(strLine + "括弧が重複しています。");
                            }
                        }
                        parCheck.removeLast();
                        lastLparTk = checkTk;
                        lastRparTk = tk;
                    }else if(tk.getType() == MyToken.BLPAR){
                        bparCount++;
                        parCheck.add(tk);
                    }else if(tk.getType() == MyToken.BRPAR){
                        MyToken checkTk = parCheck.getLast();
                        int startPar = array.indexOf(checkTk);
                        int lastPar  = array.indexOf(tk);

                        if(--bparCount < 0){
                            context.printError(strLine + "\"}\"が\"{\"よりも多いです。");
                        }
                        if(checkTk.getType() != MyToken.BLPAR){
                            context.printError(strLine + "括弧の順番が不適切です。");
                        }
                        if(lastPar - startPar == 1){
                            context.printWarning(strLine + "括弧内の記述が存在しません。");
                            removeArray.add(checkTk);
                            removeArray.add(tk);
                        }
                        if(lastLparTk != null && lastRparTk != null){
                            if(array.indexOf(lastLparTk) - 1 == startPar && array.indexOf(lastRparTk) + 1 == lastPar){
                                context.printError(strLine + "括弧が重複しています。");
                            }
                        }
                        parCheck.removeLast();
                        lastLparTk = checkTk;
                        lastRparTk = tk;
                    }else if(tk.getType() == MyToken.OR){

                    }
                }
                if(nparCount > 0){
                    context.printError(strLine + "\"(\"が\")\"よりも多いです。");
                }
                if(aparCount > 0){
                    context.printError(strLine + "\"[\"が\"]\"よりも多いです。");
                }
                if(bparCount > 0){
                    context.printError(strLine + "\"{\"が\"}\"よりも多いです。");
                }

                iii = removeArray.iterator();
                while(iii.hasNext()){
                    array.remove(iii.next());
                }
            }

            ii = removeDArray.iterator();
            while(ii.hasNext()){
                dArray.remove(ii.next());
            }
        }
    }

    private void reCheckInputData(MyContext context){
        Iterator<ArrayList<ArrayList<MyToken>>> i  = values().iterator();
        Iterator<String> i2 = keySet().iterator();

        while(i.hasNext()){
            ArrayList<MyToken> insertArray = null;
            ArrayList<ArrayList<MyToken>> dArray = i.next();
            String symbolName = i2.next();

            Iterator<ArrayList<MyToken>> ii = dArray.iterator();
            while (ii.hasNext()){
                ArrayList<MyToken> array = ii.next();
                LinkedList<MyToken> orParList = new LinkedList<MyToken>();
                String strLine = toLineString(symbolName);
                int parCount = 0;
                int symbolCount = 0;

                for(int j = 0;j < array.size();j++){
                    MyToken tk = array.get(j);
                    MyToken orParTk;
                    int rePar = 0;
                    int startIndex = -1;
                    int lastIndex  = -1;

                    switch(tk.getType()){
                        case MyToken.ALPAR:
                        case MyToken.BLPAR:
                            parCount++;
                        case MyToken.NLPAR:
                        case MyToken.OR:
                            if(tk.getType() == MyToken.OR){
                                MyToken checkTk = orParList.getLast();
                                startIndex  = array.indexOf(checkTk);
                                lastIndex   = array.indexOf(tk);
                            }

                            if(startIndex + 1 == lastIndex){
                                context.printWarning(strLine + "空文字となる\"|\"があったので削除します。");
                                array.remove(tk);
                                j--;
                            }else{
                                orParList.add(tk);
                            }
                            break;
                        case MyToken.ARPAR:
                            rePar = (rePar == 0 ? MyToken.ALPAR : rePar);
                        case MyToken.BRPAR:
                            rePar = (rePar == 0 ? MyToken.BLPAR : rePar);
                            parCount--;
                        case MyToken.NRPAR:
                            rePar = (rePar == 0 ? MyToken.NLPAR : rePar);

                            if(orParList.getLast().getType() == MyToken.OR){
                                MyToken checkTk = orParList.getLast();
                                startIndex  = array.indexOf(checkTk);
                                lastIndex   = array.indexOf(tk);

                                if(startIndex + 1 == lastIndex){
                                    context.printWarning(strLine + "空文字となる\"|\"があったので削除します。");
                                    array.remove(orParList.removeLast());
                                    j--;
                                }
                            }

                            if(orParList.getLast().getType() == rePar){
                                startIndex = array.indexOf(orParList.getLast());
                                lastIndex  = array.indexOf(tk);

                                if(startIndex + 1 == lastIndex){
                                    context.printWarning(strLine + "空文字となる括弧があったので削除します。");
                                    array.remove(orParList.getLast());
                                    array.remove(tk);
                                    j -= 2;
                                }
                            }

                            do{
                                orParTk = orParList.removeLast();
                            }while(orParTk.getType() != rePar);
                            break;
                        default:
                            if(parCount == 0){
                                boolean symbolFlag = true;
                                if(tk.getType() == MyToken.TSYMBOL){
                                    ArrayList<ArrayList<MyToken>> cDArray = get(tk.getName());

                                    for(int k = 0;k < cDArray.size();k++){
                                        if(cDArray.get(k).get(0).getType() == MyToken.EPSILON){
                                            symbolFlag = false;
                                            break;
                                        }
                                    }
                                }
                                symbolCount += (symbolFlag ? 1 : 0);
                            }
                            break;
                    }
                }
                if(symbolCount == 0){
                    Iterator<ArrayList<MyToken>> i_d = dArray.iterator();
                    boolean  epsilonExist = false;

                    while(i_d.hasNext()){
                        ArrayList<MyToken> cArray = i_d.next();

                        if(cArray.get(0).getType() == MyToken.EPSILON){
                            epsilonExist = true;
                            break;
                        }
                    }

                    if(!epsilonExist){
                        context.printWarning(strLine + "空文字が発生する可能性があるので、空文字を追加します。");
                        insertArray = new ArrayList<MyToken>();
                        insertArray.add(new MyToken("~", MyToken.EPSILON));
                    }
                }
            }
            if(insertArray != null){
                dArray.add(insertArray);
            }
        }
    }

    private String toLineString(String symbolName){
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(symbolName);
        strBuf.append(" ::= ");
        strBuf.append(toRightSideString(symbolName));
        return strBuf.toString();
    }

    private StringBuffer toRightSideString(String symbolName){
        StringBuffer strBuf = new StringBuffer();
        Iterator<ArrayList<MyToken>> i = get(symbolName).iterator();

        while(i.hasNext()){
            Iterator<MyToken> ii = i.next().iterator();

            while(ii.hasNext()){
                MyToken tk = ii.next();

                strBuf.append(tk.getName() + " ");
            }
            if(i.hasNext()){
                strBuf.append("| ");
            }
        }
        strBuf.append("\n");
        return strBuf;
    }

    private void formatInputData(){
        Iterator<ArrayList<ArrayList<MyToken>>> i = values().iterator();

        while(i.hasNext()){
            ArrayList<ArrayList<MyToken>> dArray = i.next();

            for(int j = 0;j < dArray.size();j++){
                @SuppressWarnings("unchecked")
                ArrayList<MyToken> array = (ArrayList<MyToken>)dArray.get(j).clone();
                boolean isInc = false;

                for(int k = 0;k < array.size();k++){
                    ArrayList<ArrayList<MyToken>> incDArray;
                    MyToken tk = array.get(k);
                    ParInstruction pInst = null;

                    switch(tk.getType()){
                        case MyToken.NLPAR:
                            pInst = new NParInstruction(array);
                            break;
                        case MyToken.ALPAR:
                            pInst = new AParInstruction(array);
                            break;
                        case MyToken.BLPAR:
                            pInst = new BParInstruction(array);
                            break;
                    }
                    if(pInst != null){
                        int startIndex = array.indexOf(tk);
                        int endIndex   = pInst.nextParIndex(startIndex);

                        incDArray = pInst.makeNoPar(startIndex, endIndex);
                        dArray.addAll(j + 1, incDArray);
                        /*
                        System.out.print("[formatInputData]startIndex = " + startIndex);
                        System.out.print("(\"" +  array.get(startIndex).getName() + "\")");
                        System.out.print(" endIndex = " + endIndex);
                        System.out.print("(\"" +  array.get(endIndex).getName() + "\")");
                        System.out.print("\n");

                        for(int l = 0;l < incDArray.size();l++){
                            ArrayList<MyToken> lArray = incDArray.get(l);
                            System.out.print("[formatInputData " +  l + "]");
                            for(int m = 0;m < lArray.size();m++){
                                MyToken mTk = lArray.get(m);

                                System.out.print(mTk.getName() + "  ");
                            }
                            System.out.println();
                        }
                        System.out.println();
                        */
                        isInc = true;
                        break;
                    }

                }
                if(isInc){
                    dArray.remove(j--);
                }
            }
            removeDup(dArray);
        }
    }

    //for debug
    public void printInputData(MyContext context, boolean isTypeDebug){
        PrintStream out = context.getMyIO().getOutput();
        Iterator<ArrayList<ArrayList<MyToken>>> i  = values().iterator();
        Iterator<String> i2 = keySet().iterator();

        while(i.hasNext()){
            ArrayList<ArrayList<MyToken>> dArray = i.next();
            String symbolName = i2.next();
            Iterator<ArrayList<MyToken>> ii = dArray.iterator();
            out.print(symbolName + " ::= ");
            while (ii.hasNext()){
                ArrayList<MyToken> array = ii.next();
                Iterator<MyToken> iii = array.iterator();
                while(iii.hasNext()){
                    MyToken tk = iii.next();

                    out.print(tk.getName());
                    out.print((isTypeDebug ? "(" + tk.getType() + ") " : " "));
                }
                if(ii.hasNext()){
                    out.print("| ");
                }
            }
            out.println();
            //out.println(dArray.size());
        }
        out.println();
    }

    private void removeDup(ArrayList<ArrayList<MyToken>> dArray){
        HashSet<ArrayList<MyToken>> hSet = new HashSet<ArrayList<MyToken>>();
        ArrayList<ArrayList<MyToken>> retDArray = new ArrayList<ArrayList<MyToken>>();
        Iterator<ArrayList<MyToken>> i = dArray.iterator();

        while(i.hasNext()){
            ArrayList<MyToken> array = i.next();

            if(!hSet.contains(array)){
                hSet.add(array);
                retDArray.add(array);
            }
        }

        dArray.clear();
        dArray.addAll(retDArray);
    }
}
