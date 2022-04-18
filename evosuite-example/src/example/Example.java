package example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

import org.evosuite.Properties;
import org.evosuite.Properties.StatisticsBackend;
//import org.evosuite.Properties.StatisticsBackend;
import org.evosuite.result.BranchInfo;
import org.evosuite.utils.LoggingUtils;

import evosuite.shell.EvoTestResult;
import evosuite.shell.experiment.SFConfiguration;

public class Example {

	public static void main(String[] args) throws Exception {
		Example example = new Example();
		example.run();
	}
	
	/**
	 * We need to support JDK1.8 as the Java Home, 
	 * one concern is that the threads will not be fully killed after getting the test results,
	 * the user may need to kill the main process in an explicit way.
	 */
	public void run() throws Exception {
		
		
		/**
		 * The following code is used for debugging
		 */
		Properties.CLIENT_ON_THREAD = true;
		Properties.STATISTICS_BACKEND = StatisticsBackend.DEBUG;

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
		int budget = 5;
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
		
		System.out.println("test done");
		System.currentTimeMillis();

		Thread[] threadArray = new Thread[Thread.activeCount() + 2];
		Thread.enumerate(threadArray);
		
		for (Thread t : threadArray) {
			if(t == null){
				continue;
			}
			
			if(t.getName().equals("main")) {
				continue;
			}

			kill(t);
		}
		
//		SandboxFromJUnitTest.getInstance().
		
	}

	private void kill(Thread t) throws InterruptedException {
		while (t.isAlive()) {
			t.interrupt();
			Object obj = LockSupport.getBlocker(t);
			if(obj != null) {
				LockSupport.unpark(t);		
				t.interrupt();
				
				//TODO get the ThreadPoolExecutor via thread.target..., then shutdown, 
				//TODO it should work
				
			}
			
			Thread.sleep(100);
			
			if(t.isDaemon()) {
				break;
			}
		}
	}

}
