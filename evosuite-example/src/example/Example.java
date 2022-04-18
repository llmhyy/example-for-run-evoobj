package example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.evosuite.Properties;
//import org.evosuite.Properties.StatisticsBackend;
import org.evosuite.result.BranchInfo;

import evosuite.shell.EvoTestResult;
import evosuite.shell.experiment.SFConfiguration;

public class Example {

	/**
	 * We need to support JDK1.8 as the Java Home, 
	 * one concern is that the threads will not be fully killed after getting the test results,
	 * the user may need to kill the main process in an explicit way.
	 */
	public static void main(String[] args) throws Exception {
		
		
		/**
		 * The following code is used for debugging
		 */
//		Properties.CLIENT_ON_THREAD = true;
//		Properties.STATISTICS_BACKEND = StatisticsBackend.DEBUG;

		Properties.ENABLE_BRANCH_ENHANCEMENT = false;
		Properties.ADOPT_SMART_MUTATION = false;
		Properties.INSTRUMENT_CONTEXT = true;
		
		/**
		 * your jar file, which should conform to Evosuite SF100 format.
		 */
		SFConfiguration.sfBenchmarkFolder = "D:\\sf100";
		
		/**
		 * setting up a project with the following name, pay attention to the JVM signature
		 */
		String projectId = "0_commons";
		String[] targetMethods = new String[]{
//			"org.example.custom.recursivecase.LinkedListNode#method()V", // Tested
			"org.apache.commons.lang3.builder.CompareToBuilder#append([S[S)Lorg/apache/commons/lang3/builder/CompareToBuilder;", // Tested
		};
		
		/**
		 * run the results.
		 */
		List<EvoTestResult> results = new ArrayList<EvoTestResult>();
		int repeatTime = 1;
		int budget = 10;
		Long seed = null;
		
		String fitnessApproach = "branch";
		
		boolean aor = false;
		
		results = CommonTestUtil.evoTestSingleMethod(projectId,  
				targetMethods, fitnessApproach, repeatTime, budget, true, 
				seed, aor, "generateMOSuite", "MOSUITE", "DynaMOSA");
		
		
		for(EvoTestResult result: results) {
			Map<BranchInfo, String> map = result.getCoveredBranchWithTest();
			for(BranchInfo info: map.keySet()) {
				//TODO
				System.out.print(info.getLineNo());
				
			}
		}

		
		System.currentTimeMillis();
	}

}
