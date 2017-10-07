package isLL1;

import java.util.ArrayList;
import java.util.Iterator;

class LL1Check{
	private MyContext   context;
	private InputData   inData;
	private FirstSet    firstSet;
	private FollowSet   followSet;
	private DirectorSet directorSet;

	public static void main(String[] args) {
		LL1Check check = new LL1Check(args[0]);
		check.doLL1Check();
	}

	private LL1Check(String filePath){
		context = new MyContext(filePath);
		inData = new InputData();
	}

	private void doLL1Check(){
		String strLine;

		inData.inputAnalysis(context);

		if(context.getErrorCount() == 0) {

			firstSet = new FirstSet(inData);
			strLine  = firstSet.makeAllSet();
			if(strLine != null){
				context.printError(strLine);
				return;
			}
			context.println(firstSet.toAllSetString());

			followSet = new FollowSet(inData, firstSet);
			strLine   = followSet.makeAllSet();
			if(strLine != null){
				context.printError(strLine);
				return;
			}
			context.println(followSet.toAllSetString());

			inData.changeAlter();
			directorSet = new DirectorSet(inData, firstSet, followSet);
			strLine     = directorSet.makeAllSet();
			if(strLine != null){
				context.printError(strLine);
				return;
			}
			context.println(directorSet.toAllSetString());

			strLine = directorSet.isLL1();
			if(strLine == null){
				context.println("この拡張BNF記法はLL1です。");
			}else{
				context.println(strLine + "この拡張BNF記法はLL1ではありません。");
			}
			/*
			inData.changeAlter();
			for(Iterator i = inData.values().iterator();i.hasNext();){
				ArrayList<MyToken> test = (ArrayList<MyToken>)i.next();
				System.out.println(test.size());
			}
			System.out.println();
			*/
		}

		context.printReport();
	}
}