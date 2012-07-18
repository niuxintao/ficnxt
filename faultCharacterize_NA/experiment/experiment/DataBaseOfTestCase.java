package experiment;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;

public class DataBaseOfTestCase {
	private TestCase wrongCase;
	private TestSuite rightSuite;
	private int[] param;
	public DataBaseOfTestCase(int length,int valueNum){
		init(length,valueNum);
	}
 	private void init(int length,int valueNum) {
 		int[] wrong = new int[length];
 		int[] pass = new int[length];
 		param = new int[length];
 		for(int i = 0; i < length; i++){
 			wrong[i] = 1;
 			pass[i] = 0;
 			param[i] = valueNum;
 		}
		TestCase rightCase = new TestCaseImplement();
		((TestCaseImplement) rightCase).setTestCase(pass);
		 wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		rightSuite = new TestSuiteImplement();
		rightSuite.addTest(rightCase);
	}
 	public TestCase getWrongCase() {
		return wrongCase;
	}
	public TestSuite getRightSuite() {
		return rightSuite;
	}

	public int[] getParam() {
		return param;
	}
}
