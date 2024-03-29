package com.fc.model;

import java.util.ArrayList;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class FIC {
	protected CaseRunner caseRunner;
	protected TestCase testCase;
	protected int[] param;
	protected List<Tuple> bugs;
	protected TestSuite extraCases;

	class Pa {
		public List<Integer> CFree;
		public Tuple fixdOne;
	}

	/**
	 * @return the bugs
	 */
	public List<Tuple> getBugs() {
		return bugs;
	}

	public FIC(TestCase testCase, int[] param, CaseRunner caseRunner) {
		this.testCase = testCase;
		this.param = param;
		bugs = new ArrayList<Tuple>();
		this.caseRunner = caseRunner;
		extraCases = new TestSuiteImplement();
	}

	public Pa LocateFixedParam(List<Integer> CFree, Tuple partBug) {

		// convert the List to a int[]
		int[] free = CovertTntegerToInt(CFree);

		// get a Candidate index
		Tuple freeTuple = new Tuple(free.length, testCase);

		freeTuple.setParamIndex(free);
		Tuple noCand = freeTuple.cat(freeTuple, partBug);

		Tuple cand = noCand.getReverseTuple();
		int[] Ccand = cand.getParamIndex();
		List<Integer> U = new ArrayList<Integer>();
		U.addAll(CFree);

		for (int i = 0; i < Ccand.length; i++) {
			Integer para = new Integer(Ccand[i]);
			List<Integer> temp = new ArrayList<Integer>();
			temp.addAll(U);
			// accroding to sequnce

			if (temp.size() == 0) {
				temp.add(para);
			} else
				for (int j = temp.size() - 1; j >= 0; j--) {
					if (temp.get(j) < para) {
						temp.add(j + 1, para);
						break;
					} else if (temp.get(j) == para) {
						break;
					} else if (j == 0) {
						temp.add(j, para);
						break;
					}
				}
			if (this.runTest(Motivate(temp))) {
				Tuple fixone = new Tuple(1, testCase);
				fixone.setParamIndex(new int[] { para });
				Pa pa = new Pa();
				pa.CFree = U;
				pa.fixdOne = fixone;
				return pa;
			}
			U = temp;
		}
		Tuple fixone = new Tuple(0, testCase);
		Pa pa = new Pa();
		pa.CFree = U;
		pa.fixdOne = fixone;
		return pa;
	}

	public int[] CovertTntegerToInt(List<Integer> CFree) {
		int[] free = new int[CFree.size()];
		for (int i = 0; i < free.length; i++) {
			free[i] = CFree.get(i);
		}
		return free;
	}

	public List<Integer> CovertTntToTnteger(int[] CFree) {
		List<Integer> free = new ArrayList<Integer>();
		for (int i = 0; i < CFree.length; i++) {
			free.add(new Integer(CFree[i]));
		}
		return free;
	}

	protected boolean runTest(TestCase testCase) {
		caseRunner.runTestCase(testCase);
		return testCase.testDescription() == TestCase.PASSED;
	}

	private TestCase Motivate(List<Integer> change) {
		// TODO Auto-generated method stub
		TestCase newCase = testCase.copy();
		newCase.setTestState(TestCase.UNTESTED);
		for (Integer i : change) {
			int index = i;
			newCase.set(index, (testCase.getAt(index) + 1) % param[index]);
		}
		extraCases.addTest(newCase);
		return newCase;
	}

	public Pa LocateFixeedParamBS(List<Integer> CFree, Tuple partBug) {
		// convert the List to a int[]
		int[] free = CovertTntegerToInt(CFree);

		// get a Candidate index
		Tuple freeTuple = new Tuple(free.length, testCase);

		freeTuple.setParamIndex(free);
		Tuple noCand = freeTuple.cat(freeTuple, partBug);

		Tuple cand = noCand.getReverseTuple();
		int[] Ccand = cand.getParamIndex();
		List<Integer> U = new ArrayList<Integer>();
		U.addAll(CFree);

		Tuple determine = cand.cat(cand, freeTuple);
		if (Ccand == null
				|| Ccand.length == 0
				|| !this.runTest(Motivate(CovertTntToTnteger(determine
						.getParamIndex())))) {
			Tuple fixone = new Tuple(0, testCase);
			Pa pa = new Pa();
			pa.CFree = U;
			pa.fixdOne = fixone;
			return pa;
		}

		int low = 0;
		int high = Ccand.length - 1;
		int middle = (low + high) / 2 ;
		List<Integer> candList = this.CovertTntToTnteger(Ccand);
		while (low < high) {
			int[] lower = this
					.CovertTntegerToInt(candList.subList(low, middle+1));
			Tuple Low = new Tuple(lower.length, testCase);
			Low.setParamIndex(lower);

			Tuple Temp = freeTuple.cat(freeTuple, Low);
			if (this.runTest(Motivate(this.CovertTntToTnteger(Temp
					.getParamIndex())))) {
                 high = middle;
                 middle = (low + high) / 2;
			}
			else{
				low = middle + 1;
				 middle = (low + high) / 2;
				 freeTuple = Temp;
			}
		}
		Tuple fixone = new Tuple(1, testCase);
		fixone.set(0, new Integer(candList.get(low)));
		Pa pa = new Pa();
		pa.CFree = this.CovertTntToTnteger(freeTuple.getParamIndex());
		pa.fixdOne = fixone;
		return pa;
	}

	public Tuple Fic(List<Integer> CTabu) {
		Tuple partBug = new Tuple(0, testCase);
		List<Integer> CFree = new ArrayList<Integer>();
		CFree.addAll(CTabu);
		while (true) {
			Pa pa = this.LocateFixeedParamBS(CFree, partBug);
			CFree = pa.CFree;
			Tuple newRelatedPartBug = pa.fixdOne;
			if (newRelatedPartBug.getDegree() == 0) {
				break;
			}
			partBug = partBug.cat(partBug, newRelatedPartBug);
		}
		return partBug;
	}

	public void FicNOP() {
		List<Integer> CTabu = new ArrayList<Integer>();
		while (true) {
			if (this.runTest(Motivate(CTabu))) {
				break;
			}
			
			Tuple bug = Fic(CTabu);
			if (bug.getDegree() == 0)
				break;
			this.bugs.add(bug);

			Tuple tuple = new Tuple(CTabu.size(), testCase);
			int[] tabu = CovertTntegerToInt(CTabu);
			tuple.setParamIndex(tabu);

			Tuple newCTabu = tuple.cat(tuple, bug);
			CTabu.addAll(CovertTntToTnteger(newCTabu.getParamIndex()));
		}
	}

	/**
	 * @return the extraCases
	 */
	public TestSuite getExtraCases() {
		return extraCases;
	}

	public static void main(String[] args) {
		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] param = new int[] { 3, 3, 3, 3, 3, 3, 3, 3 };

		Tuple bugModel1 = new Tuple(2, wrongCase);
		bugModel1.set(0, 2);
		bugModel1.set(1, 5);

		Tuple bugModel2 = new Tuple(1, wrongCase);
		bugModel2.set(0, 3);

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);

		FIC fic = new FIC(wrongCase, param, caseRunner);
		fic.FicNOP();

		for (int i = 0; i < fic.getExtraCases().getTestCaseNum(); i++) {
			System.out.println(fic.getExtraCases().getAt(i).getStringOfTest());
		}
		for (Tuple bug : fic.getBugs()) {
			System.out.println(bug.toString());
		}
	}
}
