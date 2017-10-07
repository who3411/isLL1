package isLL1;

import java.io.*;

class MyIO{
	private static final int INPUTSTREAM  = 1;
	private static final int OUTPUTSTREAM = 2;
	private static final int ERRPUTSTREAM = 3;
	
	private String inputFile;
	private String outputFile;
	private String errputFile;
	
	private InputStream input;
	private PrintStream output;
	private PrintStream errput;
	
	MyIO(){
		inputFile  = null;
		outputFile = null;
		errputFile = null;
		input      = null;
		output     = null;
		errput     = System.err;
	}
	
	MyIO(InputStream input, PrintStream output, PrintStream errput){
		inputFile  = null;
		outputFile = null;
		errputFile = null;
		this.input  = input;
		this.output = output;
		this.errput = errput;
	}
	
	MyIO(String inputFile, String outputFile, String errputFile){
		setInputFile(inputFile);
		setOutputFile(outputFile);
		setErrputFile(errputFile);
	}
	
	private void openStream(String filename, int streamType){
		try{
			if(streamType == INPUTSTREAM){
				input = new FileInputStream(filename);
			}else if(streamType == OUTPUTSTREAM) {
				output = new PrintStream(filename);
			}else if (streamType == ERRPUTSTREAM){
				errput = new PrintStream(filename);
			}
		}catch(FileNotFoundException e){
			e.printStackTrace(errput);
		}
	}
	
	public void setInputFile(String inputFile){
		this.inputFile = inputFile;
		openStream(inputFile, INPUTSTREAM);
	}
	
	public void setOutputFile(String outputFile){
		this.outputFile = outputFile;
		openStream(outputFile, OUTPUTSTREAM);
	}
	
	public void setErrputFile(String errputFile){
		this.errputFile = errputFile;
		openStream(errputFile, ERRPUTSTREAM);
	}
	
	public void setInput(InputStream input){
		this.input = input;
	}
	
	public void setOutput(PrintStream output){
		this.output = output;
	}
	
	public void setErrput(PrintStream errput){
		this.errput = errput;
	}
	
	public InputStream getInput(){
		return input;
	}
	
	public PrintStream getOutput(){
		return output;
	}
	
	public PrintStream getErrput(){
		return errput;
	}
	
	public String getInputFile(){
		return inputFile;
	}
	
	public String getOutputFile(){
		return outputFile;
	}
	
	public String getErrputFile(){
		return errputFile;
	}
}