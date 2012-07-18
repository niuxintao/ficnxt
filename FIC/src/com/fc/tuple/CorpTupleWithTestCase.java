package com.fc.tuple;


import java.util.HashSet;
import java.util.Random;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;

public class CorpTupleWithTestCase {
	private TestSuite wrongSuite;
	private TestSuite rightSuite;
	private int[] param;

	public CorpTupleWithTestCase(TestSuite wrongSuite, TestSuite rightSuite , int[] param) {
		this.wrongSuite = wrongSuite;
		this.rightSuite = rightSuite;
		this.param = param;
	}
	
	public TestCase generateTestCaseContainTuple(Tuple tuple){
		TestCase result = new TestCaseImplement(param.length);
		for (int i = 0 ; i < result.getLength(); i ++){
			result.set(i, -1);
		}
		for (int i = 0 ; i < tuple.getDegree() ; i++){
			result.set(tuple.getParamIndex()[i], tuple.getParamValue()[i]);
		}
		for (int i = 0 ; i < result.getLength() ; i++){
			if (result.getAt(i) == -1)
				result.set(i, this.getItheElement(i));
		}
		//if all element in wrong tuple, generate a different testSuite iterate all possible testSuite not in the wrong sutie
		
		//if can't all testCase has used, then the tuple must be wrong
		
		return result;
	}
	
	public int getItheElement(int i){
		int result = -1;
		HashSet<Integer> wrongElements = new HashSet<Integer>();
		for(int j = 0 ; j < wrongSuite.getTestCaseNum() ; j++){
			wrongElements.add(new Integer(wrongSuite.getAt(j).getAt(i)));
		}
		for(int j = 0 ;  j < param[i]; j++){
		  if(!wrongElements.contains(new Integer(j))){
			  result = j;
			  break;
		  }
		}
		if (result == -1){
			result = (new Random()).nextInt(param[i]);
		}
		return result;
	}

	public TestSuite getWrongSuite() {
		return wrongSuite;
	}

	public void setWrongSuite(TestSuite wrongSuite) {
		this.wrongSuite = wrongSuite;
	}

	public TestSuite getRightSuite() {
		return rightSuite;
	}

	public void setRightSuite(TestSuite rightSuite) {
		this.rightSuite = rightSuite;
	}

	public int[] getParam() {
		return param;
	}

	public void setParam(int[] param) {
		this.param = param;
	}
}
