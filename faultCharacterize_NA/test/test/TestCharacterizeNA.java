package test;

import java.util.List;

import org.junit.Test;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.exploreAlorgrithm.PathNA;
import com.fc.model.TreeStruct;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.CorpTupleWithTestCase;
import com.fc.tuple.Tuple;

public class TestCharacterizeNA {
	protected CaseRunner caseRunner;
	protected CorpTupleWithTestCase generate;
	protected TreeStruct tree;
	
	@Test
	public void testWorkFlow(){
		init();
		PathNA workMachine = new PathNA(tree,caseRunner,generate);	
		workMachine.process();	
		
		List<Tuple> bugs = tree.getBugModes();
		for(Tuple bug : bugs){
			System.out.println(bug.toString());
		}
		
		TestSuite extra = workMachine.getExtraCases();
		for(int i = 0 ; i < extra.getTestCaseNum(); i++){
			System.out.println(extra.getAt(i).getStringOfTest());
		}
		
	}

	protected void init() {
		int[] wrong = new int[]{1,1,1};
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement)wrongCase).setTestCase(wrong);
		
		TestSuite rightSuite = new TestSuiteImplement();
		TestSuite wrongSuite = new TestSuiteImplement();
		wrongSuite.addTest(wrongCase);
		
		
		int[] param = new int[]{10,10,10};
		
		Tuple bugModel = new Tuple(2,wrongCase);
		bugModel.set(0, 0);
		bugModel.set(1, 1);

		
		caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject)caseRunner).inject(bugModel);
		
		tree = new TreeStruct(wrongCase,rightSuite);
		tree.constructTree();
		tree.init();
		
		generate = new CorpTupleWithTestCase(wrongCase,param);
	}
}
