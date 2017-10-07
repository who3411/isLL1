package isLL1;

class MyToken{
	public static final int UTSYMBOL  = 1;
	public static final int TSYMBOL   = 2;
	public static final int NTSYMBOL  = 3;
	public static final int NONE      = 0;
	public static final int EOF       = -1;
	public static final int ILL       = -2;

	public static final int EPSILON   = 4;
	public static final int EQUAL     = 5;
	public static final int NRPAR     = 6;
	public static final int NLPAR     = 7;
	public static final int ARPAR     = 8;
	public static final int ALPAR     = 9;
	public static final int BRPAR     = 10;
	public static final int BLPAR     = 11;
	public static final int OR        = 12;
	public static final int NL        = 13;
	public static final int FIN       = 14;
	
	private String name;
	private int    type;
	
	MyToken(){
		this.name = null;
		this.type = NONE;
	}
	
	MyToken(String name, int type){
		this.name = name;
		this.type = type;
	}
	
	public String getName(){
		return name;
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public int getType(){
		return type;
	}
}