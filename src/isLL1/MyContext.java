package isLL1;

class MyContext {
	private MyIO io;
	private MyTokenizer tokenizer;
	private int errorCount;
	private int warningCount;
	private int subWarningCount;

	MyContext(String filePath){
		io = new MyIO();
		io.setInputFile(filePath);
		io.setOutput(System.out);
		io.setErrput(System.err);

		tokenizer = new MyTokenizer(io);

		errorCount      = 0;
		warningCount    = 0;
		subWarningCount = 0;
	}

	public MyIO getMyIO(){
		return io;
	}

	public MyTokenizer getTokenizer() {
		return tokenizer;
	}

	public void print(String str){
		io.getOutput().print(str);
	}

	public void println(String str){
		io.getOutput().println(str);
	}

	public void printError(String str){
		errorCount++;
		io.getErrput().println("[error]\n" + str);
	}

	public void printWarning(String str){
		warningCount++;
		subWarningCount++;
		io.getErrput().println("[warning]\n" + str);
	}

	public int getErrorCount(){
		return errorCount;
	}

	public int getWarningCount(){
		return warningCount;
	}

	public void resetSubWarningCount(){
		subWarningCount = 0;
	}

	public int getSubWarningCount(){
		return subWarningCount;
	}

	public void printReport(){
		io.getErrput().printf("[error] %d [warning] %d\n", errorCount, warningCount);
	}
}