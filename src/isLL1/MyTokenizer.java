package isLL1;

import java.io.*;

class MyTokenizer{
	private static final byte NORMALCHAR  = 0;
	private static final byte EQUALCHAR   = 1;
	private static final byte SPACECHAR   = 2;
	private static final byte COMMENTCHAR = 3;
	private static final byte ILLCHAR     = 4;

	private static final String EQUALCHARS   = ":=";
	private static final String SPACECHARS   = " \t\r";
	private static final String COMMENTCHARS = ";";

	private InputStream input;
	private PrintStream errput;
	private MyRule      rule;

    private byte[]  characters = new byte[256];
    private char    lastChar;
    private boolean existlastChar;

    private MyToken currentMyToken;

    MyTokenizer(){
        initCharacters();

        existlastChar = false;
        rule = new MyRule();
    }

    MyTokenizer(MyIO io) {
        this.input = io.getInput();
        this.errput = io.getErrput();

        initCharacters();

        existlastChar = false;
        rule = new MyRule();
    }

    public void setInput(InputStream input){
        this.input = input;
    }

    public void setErrput(PrintStream errput){
        this.errput = errput;
    }

    private void initCharacters(){
        for(int i = 0;i < characters.length;i++){
            characters[i] = ILLCHAR;
        }
        for(char c = '0';'0' <= c && c <= '9';c++){
            characters[c] = NORMALCHAR;
        }
        for(char c = 'A';'A' <= c && c <= 'Z';c++){
            characters[c] = NORMALCHAR;
        }
        for(char c = 'a';'a' <= c && c <= 'z';c++){
            characters[c] = NORMALCHAR;
        }
        characters['_'] = NORMALCHAR;
        for(int i = 0;i < EQUALCHARS.length();i++){
            char c = EQUALCHARS.charAt(i);
            characters[c] = EQUALCHAR;
        }
        for(int i = 0;i < SPACECHARS.length();i++){
            char c = SPACECHARS.charAt(i);
            characters[c] = SPACECHAR;
        }
        for(int i = 0;i < COMMENTCHARS.length();i++){
            char c = COMMENTCHARS.charAt(i);
            characters[c] = COMMENTCHAR;
        }
    }

    private char wrapRead() {
        char retChar;
        if(existlastChar){
            retChar = lastChar;
            existlastChar = false;
        }else{
            try {
                retChar = (char)input.read();
            }catch(IOException e){
                e.printStackTrace(errput);
                retChar = (char)-1; // ret EOF
            }
        }
        return retChar;
    }

    private void setLastChar(char lastChar){
        this.lastChar = lastChar;
        existlastChar = true;
    }

    public MyToken getCurrentMyToken(){
        return currentMyToken;
    }

    public MyToken getNextMyToken(){
        currentMyToken = readMyToken();
        if(rule.containsKey(currentMyToken.getName())){
            Integer ruleType = rule.get(currentMyToken.getName());
            currentMyToken.setType(ruleType.intValue());
        }

        return currentMyToken;
    }

    private MyToken readMyToken(){
        MyToken retToken = null;
        char currentChar;
        boolean eofFlag = false;
        StringBuffer strBuf = new StringBuffer();

        do{
            currentChar = wrapRead();
            if(currentChar == (char)-1){
                break;
            }else if(characters[currentChar] == COMMENTCHAR){
                while(currentChar != '\n'){
                    currentChar = wrapRead();
                }
            }
        }while(characters[currentChar] == SPACECHAR);
        if(currentChar == (char)-1){
            eofFlag = true;
        }else{
            switch(characters[currentChar]){
                case NORMALCHAR:
                    do{
                        strBuf.append(currentChar);
                        currentChar = wrapRead();
                        if(currentChar == (char)-1){
                            eofFlag = true;
                            break;
                        }
                    }while(characters[currentChar] == NORMALCHAR);
                    retToken = new MyToken(strBuf.toString(), MyToken.UTSYMBOL);
                    setLastChar(currentChar);
                    break;
                case EQUALCHAR:
                    do{
                        strBuf.append(currentChar);
                        currentChar = wrapRead();
                        if(currentChar == (char)-1){
                            eofFlag = true;
                            break;
                        }
                    }while(characters[currentChar] == EQUALCHAR);
                    retToken = new MyToken(strBuf.toString(), MyToken.ILL);
                    setLastChar(currentChar);
                    break;
                case ILLCHAR:
                    strBuf.append(currentChar);
                    retToken = new MyToken(strBuf.toString(), MyToken.ILL);
                    break;
            }
        }
        return (eofFlag && strBuf.length() == 0 ? new MyToken("ENDOFFILE", MyToken.EOF) : retToken);
    }
}