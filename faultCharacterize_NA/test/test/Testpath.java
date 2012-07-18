package test;



import org.junit.Test;

import com.fc.exploreAlorgrithm.Path;
import com.fc.testObject.TestSuite;

public class Testpath extends TestCharacterize {
	@Test
	public void testWorkFlow() {
		init();
		Path path = new Path(tree,caseRunner,generate);
		path.process();
		TestSuite extra = path.getExtraCases();
		System.out.println("all:"+extra.getTestCaseNum());
		for(int i = 0 ; i < extra.getTestCaseNum(); i++){
			System.out.println(extra.getAt(i).getStringOfTest());
		}
		
	/*	List<Tuple> bugs = tree.getBugModes();
		for(Tuple bug : bugs){
			System.out.println(bug.toString());
		}*/
	}
}
